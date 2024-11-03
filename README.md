# InstanaTracer Project

## Overview

The InstanaTracer project is a Java application designed to calculate trace latencies and perform various graph-related
tasks. This project includes a Maven wrapper to ensure it can be built without requiring Maven to be installed on the
user's system.

## Prerequisites

- Java (JDK 8 or higher)

## Setup

1. **Clone the repository**:
   ```sh
   git clone https://github.com/your-repo/instana-tracer.git
   cd instana-master

2. **Build the project (or use maven wrapper)**:
   ```sh
   mvn clean install

3. **Running the Project**

   To run the project, put input files in the project's source directory (comma-separated values related to the
   project's directory), use the following command:
   ```sh
   java -cp target/instana.jar InstanaTracer input1.txt,input2.txt

4. **(OPTIONAL) Running the Project in Intellij IDEA**

   To run the project in Intellij IDEA, open the project in the IDE and run the `InstanaTracer` class with the
   appropriate input arguments.
   

   