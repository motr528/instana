import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * The InstanaTracer class represents a graph of nodes and edges, and provides methods to calculate trace latencies,
 * count traces with specific hop counts, and find the shortest latency between nodes.
 */
public class InstanaTracer {

    Map<String, Node> nodes = new HashMap<>();

    /**
     * The main method to execute the program.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java -cp target/instana.jar InstanaTracer <input-file-paths>");
            return;
        }

        String[] inputFilePaths = args[0].split(",");

        for (String inputFilePath : inputFilePaths) {
            InstanaTracer tracer = new InstanaTracer();
            System.out.println("Processing file: " + inputFilePath);
            tracer.readGraphFromFile(inputFilePath.trim());

            // Task 1
            System.out.println(tracer.calculateTraceLatency(new String[]{"A", "B", "C"}));

            // Task 2
            System.out.println(tracer.calculateTraceLatency(new String[]{"A", "D"}));

            // Task 3
            System.out.println(tracer.calculateTraceLatency(new String[]{"A", "D", "C"}));

            // Task 4
            System.out.println(tracer.calculateTraceLatency(new String[]{"A", "E", "B", "C", "D"}));

            // Task 5
            System.out.println(tracer.calculateTraceLatency(new String[]{"A", "E", "D"}));

            // Task 6
            System.out.println(tracer.countTracesMaxHops("C", "C", 3));

            // Task 7
            System.out.println(tracer.countTracesExactHops("A", "C", 4));

            // Task 8
            System.out.println(tracer.shortestLatency("A", "C"));

            // Task 9
            System.out.println(tracer.shortestLatency("B", "B"));

            // Task 10
            System.out.println(tracer.countTracesWithMaxLatency("C", 30));
        }
    }

    /**
     * Reads the graph from the input file and constructs nodes and edges.
     *
     * @param filename the name of the input file
     */
    public void readGraphFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            String[] edgesInput = line.split(",\\s*");
            for (String edgeStr : edgesInput) {
                String startName = edgeStr.substring(0, 1);
                String endName = edgeStr.substring(1, 2);
                int latency = Integer.parseInt(edgeStr.substring(2));

                Node startNode = nodes.computeIfAbsent(startName, Node::new);
                Node endNode = nodes.computeIfAbsent(endName, Node::new);

                Edge edge = new Edge(endNode, latency);
                startNode.addEdge(edge);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculates the total latency of a trace defined by an array of node names.
     *
     * @param nodeNames the array of node names defining the trace
     * @return the total latency of the trace, or "NO SUCH TRACE" if the trace is invalid
     */
    public String calculateTraceLatency(String[] nodeNames) {
        int latency = 0;
        for (int i = 0; i < nodeNames.length - 1; i++) {
            Node currentNode = nodes.get(nodeNames[i]);
            Edge edge = getEdgeTo(currentNode, nodeNames[i + 1]);
            if (edge != null) {
                latency += edge.latency;
            } else {
                return "NO SUCH TRACE";
            }
        }
        return "" + latency;
    }

    /**
     * Counts the number of traces from the start node to the end node with a maximum number of hops.
     *
     * @param startName the name of the start node
     * @param endName   the name of the end node
     * @param maxHops   the maximum number of hops
     * @return the number of traces with a maximum number of hops
     */
    public int countTracesMaxHops(String startName, String endName, int maxHops) {
        Queue<PathNode> queue = new LinkedList<>();
        Node start = nodes.get(startName);
        Node end = nodes.get(endName);

        int count = 0;
        queue.offer(new PathNode(start, 0));
        while (!queue.isEmpty()) {
            PathNode current = queue.poll();
            int depth = current.depth;

            if (depth > maxHops) {
                continue;
            }

            if (depth > 0 && current.node == end) {
                count++;
            }

            if (depth < maxHops) {
                for (Edge edge : current.node.edges) {
                    queue.offer(new PathNode(edge.destination, depth + 1));
                }
            }
        }
        return count;
    }

    /**
     * Counts the number of traces from the start node to the end node with an exact number of hops.
     *
     * @param startName the name of the start node
     * @param endName   the name of the end node
     * @param exactHops the exact number of hops
     * @return the number of traces with an exact number of hops
     */
    public int countTracesExactHops(String startName, String endName, int exactHops) {
        Queue<PathNode> queue = new LinkedList<>();
        Node start = nodes.get(startName);
        Node end = nodes.get(endName);

        int count = 0;
        queue.offer(new PathNode(start, 0));
        while (!queue.isEmpty()) {
            PathNode current = queue.poll();
            int depth = current.depth;

            if (depth > exactHops) {
                continue;
            }

            if (depth == exactHops && current.node == end) {
                count++;
            }

            if (depth < exactHops) {
                for (Edge edge : current.node.edges) {
                    queue.offer(new PathNode(edge.destination, depth + 1));
                }
            }
        }
        return count;
    }

    /**
     * Finds the shortest latency from the start node to the end node.
     *
     * @param startName the name of the start node
     * @param endName   the name of the end node
     * @return the shortest latency as a string, or "NO SUCH TRACE" if no trace exists
     */
    public String shortestLatency(String startName, String endName) {
        Node startNode = nodes.get(startName);
        Node endNode = nodes.get(endName);

        Map<Node, Integer> distances = new HashMap<>();
        PriorityQueue<NodeLatency> queue = new PriorityQueue<>(Comparator.comparingInt(nl -> nl.latency));

        queue.add(new NodeLatency(startNode, 0));

        while (!queue.isEmpty()) {
            NodeLatency current = queue.poll();
            Node currentNode = current.node;

            if (currentNode == endNode && current.latency != 0) {
                return Integer.toString(current.latency);
            }

            if (distances.containsKey(currentNode) && distances.get(currentNode) <= current.latency) {
                continue;
            }

            distances.put(currentNode, current.latency);

            for (Edge edge : currentNode.edges) {
                queue.add(new NodeLatency(edge.destination, current.latency + edge.latency));
            }
        }
        return "NO SUCH TRACE";
    }

    /**
     * Counts the number of traces from the start node with a maximum latency.
     *
     * @param startName  the name of the start node
     * @param maxLatency the maximum latency
     * @return the number of traces with a maximum latency
     */
    public int countTracesWithMaxLatency(String startName, int maxLatency) {
        Node startNode = nodes.get(startName);
        if (startNode == null) {
            return 0;
        }

        return findTraces(startNode, startNode, 0, maxLatency);
    }

    /**
     * Recursively finds traces from the current node to the start node with a maximum latency.
     *
     * @param currentNode    the current node
     * @param startNode      the start node
     * @param currentLatency the current latency
     * @param maxLatency     the maximum latency
     * @return the number of traces with a maximum latency
     */
    private int findTraces(Node currentNode, Node startNode, int currentLatency, int maxLatency) {
        int traceCount = 0;

        if (currentLatency >= maxLatency) {
            return 0;
        }

        if (currentNode == startNode && currentLatency > 0) {
            traceCount++;
        }

        for (Edge edge : currentNode.edges) {
            int newLatency = currentLatency + edge.latency;
            if (newLatency < maxLatency) {
                traceCount += findTraces(edge.destination, startNode, newLatency, maxLatency);
            }
        }

        return traceCount;
    }

    /**
     * Gets the edge from the specified node to the destination node with the given name.
     *
     * @param node            the node
     * @param destinationName the name of the destination node
     * @return the edge to the destination node, or null if no such edge exists
     */
    private Edge getEdgeTo(Node node, String destinationName) {
        for (Edge edge : node.edges) {
            if (edge.destination.name.equals(destinationName)) {
                return edge;
            }
        }
        return null;
    }

    /**
     * Represents an edge in the graph with a destination node and latency.
     */
    static class Edge {
        Node destination;
        int latency;

        /**
         * Constructs an Edge with the specified destination node and latency.
         *
         * @param destination the destination node
         * @param latency     the latency of the edge
         */
        public Edge(Node destination, int latency) {
            this.destination = destination;
            this.latency = latency;
        }
    }

    /**
     * Represents a node in the graph with a name and a list of edges.
     */
    static class Node {
        String name;
        List<Edge> edges = new ArrayList<>();

        /**
         * Constructs a Node with the specified name.
         *
         * @param name the name of the node
         */
        public Node(String name) {
            this.name = name;
        }

        /**
         * Adds an edge to the node's list of edges.
         *
         * @param edge the edge to add
         */
        public void addEdge(Edge edge) {
            edges.add(edge);
        }
    }

    /**
     * Represents a node in a path with a depth.
     */
    static class PathNode {
        Node node;
        int depth;

        /**
         * Constructs a PathNode with the specified node and depth.
         *
         * @param node  the node
         * @param depth the depth of the node in the path
         */
        public PathNode(Node node, int depth) {
            this.node = node;
            this.depth = depth;
        }
    }

    /**
     * Represents a node with an associated latency.
     */
    static class NodeLatency {
        Node node;
        int latency;

        /**
         * Constructs a NodeLatency with the specified node and latency.
         *
         * @param node    the node
         * @param latency the latency associated with the node
         */
        public NodeLatency(Node node, int latency) {
            this.node = node;
            this.latency = latency;
        }
    }
}
