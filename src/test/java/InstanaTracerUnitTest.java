import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class InstanaTracerUnitTest {

    InstanaTracer tracer;

    Graph<String, DefaultWeightedEdge> referenceGraph;

    @BeforeEach
    void setUp() {
        tracer = new InstanaTracer();
    }

    @ParameterizedTest
    @CsvSource({
            "testInput1.txt",
            "testInput2.txt",
            "testInput3.txt",
            "testInput4.txt",
            "testInput5.txt",
            "testInput6.txt",
            "testInput7.txt",
            "testInput8.txt",
            "testInput9.txt",
            "testInput10.txt",
            "testInput11.txt"
    })
    void shouldReturnAverageLatencyABC_whenTask1(String fileName) {
        tracer.readGraphFromFile("src/test/resources/" + fileName);
        referenceGraph = buildJGraphTGraph();
        String[] nodeNames = {"A", "B", "C"};
        assertThat(tracer.calculateTraceLatency(nodeNames)).isEqualTo(calculateDistance(nodeNames));
    }

    @ParameterizedTest
    @CsvSource({
            "testInput1.txt",
            "testInput2.txt",
            "testInput3.txt",
            "testInput4.txt",
            "testInput5.txt",
            "testInput6.txt",
            "testInput7.txt",
            "testInput8.txt",
            "testInput9.txt",
            "testInput10.txt",
            "testInput11.txt"
    })
    void shouldReturnAverageLatencyAD_whenTask2(String fileName) {
        tracer.readGraphFromFile("src/test/resources/" + fileName);
        String[] nodeNames = {"A", "D"};
        referenceGraph = buildJGraphTGraph();
        assertThat(tracer.calculateTraceLatency(nodeNames)).isEqualTo(calculateDistance(nodeNames));
    }


    @ParameterizedTest
    @CsvSource({
            "testInput1.txt",
            "testInput2.txt",
            "testInput3.txt",
            "testInput4.txt",
            "testInput5.txt",
            "testInput6.txt",
            "testInput7.txt",
            "testInput8.txt",
            "testInput9.txt",
            "testInput10.txt",
            "testInput11.txt"
    })
    void shouldReturnAverageLatencyADC_whenTask3(String fileName) {
        tracer.readGraphFromFile("src/test/resources/" + fileName);
        String[] nodeNames = {"A", "D", "C"};
        referenceGraph = buildJGraphTGraph();
        assertThat(tracer.calculateTraceLatency(nodeNames)).isEqualTo(calculateDistance(nodeNames));
    }


    @ParameterizedTest
    @CsvSource({
            "testInput11.txt",
            "testInput1.txt",
            "testInput2.txt",
            "testInput3.txt",
            "testInput4.txt",
            "testInput5.txt",
            "testInput6.txt",
            "testInput7.txt",
            "testInput8.txt",
            "testInput9.txt",
            "testInput10.txt",
            "testInput11.txt"
    })
    void shouldReturnAverageLatencyAEBCD_whenTask4(String fileName) {
        tracer.readGraphFromFile("src/test/resources/" + fileName);
        String[] nodeNames = {"A", "E", "B", "C", "D"};
        referenceGraph = buildJGraphTGraph();
        assertThat(tracer.calculateTraceLatency(nodeNames)).isEqualTo(calculateDistance(nodeNames));
    }

    @ParameterizedTest
    @CsvSource({
            "testInput11.txt",
            "testInput1.txt",
            "testInput2.txt",
            "testInput3.txt",
            "testInput4.txt",
            "testInput5.txt",
            "testInput6.txt",
            "testInput7.txt",
            "testInput8.txt",
            "testInput9.txt",
            "testInput10.txt",
            "testInput11.txt"
    })
    void shouldReturnAverageLatencyAED_whenTask5(String fileName) {
        tracer.readGraphFromFile("src/test/resources/" + fileName);
        String[] nodeNames = {"A", "E", "D"};
        referenceGraph = buildJGraphTGraph();
        assertThat(tracer.calculateTraceLatency(nodeNames)).isEqualTo(calculateDistance(nodeNames));
    }

    @ParameterizedTest
    @CsvSource({
            "testInput1.txt",
            "testInput2.txt",
            "testInput3.txt",
            "testInput4.txt",
            "testInput5.txt",
            "testInput6.txt",
            "testInput7.txt",
            "testInput8.txt",
            "testInput9.txt",
            "testInput10.txt",
            "testInput11.txt"
    })
    void shouldReturnNumOfTracesWithUpToMax3Hops_whenTask6(String fileName) {
        tracer.readGraphFromFile("src/test/resources/" + fileName);

        referenceGraph = buildJGraphTGraph();
        AllDirectedPaths<String, DefaultWeightedEdge> allDirectedPaths = new AllDirectedPaths<>(referenceGraph);
        long expected = allDirectedPaths.getAllPaths("C", "C", false, 3).stream().filter(path -> path.getWeight() > 0).count();
        int actual = tracer.countTracesMaxHops("C", "C", 3);
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "testInput1.txt",
            "testInput2.txt",
            "testInput3.txt",
            "testInput4.txt",
            "testInput5.txt",
            "testInput6.txt",
            "testInput7.txt",
            "testInput8.txt",
            "testInput9.txt",
            "testInput10.txt",
            "testInput11.txt"
    })
    void shouldReturnNumOfTracesWith4Hops_whenTask7(String fileName) {
        tracer.readGraphFromFile("src/test/resources/" + fileName);
        referenceGraph = buildJGraphTGraph();
        AllDirectedPaths<String, DefaultWeightedEdge> allDirectedPaths = new AllDirectedPaths<>(referenceGraph);
        int hops = 4;
        long expected = allDirectedPaths.getAllPaths("A", "C", false, hops).stream().filter(path -> path.getEdgeList().size() == hops).count();
        int actual = tracer.countTracesExactHops("A", "C", hops);
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "testInput1.txt",
            "testInput2.txt",
            "testInput3.txt",
            "testInput4.txt",
            "testInput5.txt",
            "testInput6.txt",
            "testInput7.txt",
            "testInput8.txt",
            "testInput9.txt",
            "testInput10.txt",
            "testInput11.txt"
    })
    void shouldReturnLengthOfTheShortestTraceAC_whenTask8(String fileName) {
        tracer.readGraphFromFile("src/test/resources/" + fileName);
        referenceGraph = buildJGraphTGraph();
        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(referenceGraph);
        double pathLength = dijkstraShortestPath.getPathWeight("A", "C");
        String expected = pathLength == Double.POSITIVE_INFINITY ? "NO SUCH TRACE" : Integer.toString((int) pathLength);
        String actual = tracer.shortestLatency("A", "C");
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "testInput1.txt",
            "testInput2.txt",
            "testInput3.txt",
            "testInput4.txt",
            "testInput5.txt",
            "testInput6.txt",
            "testInput7.txt",
            "testInput8.txt",
            "testInput9.txt",
            "testInput10.txt",
            "testInput11.txt"
    })
    void shouldReturnLengthOfTheShortestTraceBB_whenTask9(String fileName) {
        tracer.readGraphFromFile("src/test/resources/" + fileName);
        String expected = shortestCycleLength();
        String actual = tracer.shortestLatency("B", "B");
        System.out.println("Expected: " + expected + " Actual: " + actual);
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "testInput6.txt,4",
            "testInput7.txt,3",
            "testInput11.txt,7"
    })
    void shouldReturnNumberOfTracesWithMaxLatency_whenTask10(String fileName, Integer expected) {
        tracer.readGraphFromFile("src/test/resources/" + fileName);
        int actual = tracer.countTracesWithMaxLatency("C", 30);
        assertThat(actual).isEqualTo(expected);
    }

    private String shortestCycleLength() {
        Graph<String, DefaultWeightedEdge> graph = new DirectedWeightedPseudograph<>(DefaultWeightedEdge.class);

        for (InstanaTracer.Node node : tracer.nodes.values()) {
            graph.addVertex(node.name);
        }

        for (InstanaTracer.Node node : tracer.nodes.values()) {
            for (InstanaTracer.Edge edge : node.edges) {
                DefaultWeightedEdge graphEdge = graph.addEdge(node.name, edge.destination.name);
                if (graphEdge != null) {
                    graph.setEdgeWeight(graphEdge, edge.latency);
                }
            }
        }

        double shortestCycleLength = Double.POSITIVE_INFINITY;

        Set<DefaultWeightedEdge> outgoingEdges = graph.outgoingEdgesOf("B");

        for (DefaultWeightedEdge edge : outgoingEdges) {
            String neighbor = graph.getEdgeTarget(edge);
            double edgeWeight = graph.getEdgeWeight(edge);

            // Find the shortest path from neighbor back to B
            DijkstraShortestPath<String, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(graph);
            GraphPath<String, DefaultWeightedEdge> path = dijkstra.getPath(neighbor, "B");

            if (path != null && !path.getEdgeList().isEmpty()) {
                double totalLength = edgeWeight + path.getWeight();
                if (totalLength < shortestCycleLength) {
                    shortestCycleLength = totalLength;
                }
            }
        }

        // Output the result
        if (Double.isInfinite(shortestCycleLength)) {
            return "NO SUCH TRACE";
        } else {
            return Integer.toString((int) shortestCycleLength);
        }
    }

    public Graph<String, DefaultWeightedEdge> buildJGraphTGraph() {
        Graph<String, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        // Add vertices
        for (InstanaTracer.Node node : tracer.nodes.values()) {
            graph.addVertex(node.name);
        }

        // Add edges with weights
        for (InstanaTracer.Node node : tracer.nodes.values()) {
            for (InstanaTracer.Edge edge : node.edges) {
                DefaultWeightedEdge graphEdge = graph.addEdge(node.name, edge.destination.name);
                if (graphEdge != null) {
                    graph.setEdgeWeight(graphEdge, edge.latency);
                }
            }
        }

        return graph;
    }

    public String calculateDistance(String[] nodes) {
        double totalDistance = 0.0;

        for (int i = 0; i < nodes.length - 1; i++) {
            DefaultWeightedEdge edge = referenceGraph.getEdge(nodes[i], nodes[i + 1]);
            if (edge != null) {
                totalDistance += referenceGraph.getEdgeWeight(edge);
            } else {
                return "NO SUCH TRACE";
            }
        }

        return Integer.toString((int) totalDistance);
    }
}