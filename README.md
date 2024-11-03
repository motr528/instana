# InstanaTracer Project

## Overview

The InstanaTracer project is a Java application designed to calculate trace latencies and perform various graph-related tasks. This project includes a Maven wrapper to ensure it can be built without requiring Maven to be installed on the user's system.

## Prerequisites

- Java (JDK 8 or higher)

## Setup

1. **Clone the repository**:
   ```sh
   git clone https://github.com/your-repo/instana-tracer.git
   cd instana-tracer
   
2. **Build the project**:
   ```sh
   ./mvnw clean install

2. **Running the Project**

   To run the project with a list of input files (comma-separated), use the following command:
   ```sh
   java -cp target/instana.jar InstanaTracer input1.txt,input2.txt
   

   