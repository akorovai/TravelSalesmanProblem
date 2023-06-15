# TSP (Traveling Salesman Problem) Algorithm

This repository contains a Java implementation of the Traveling Salesman Problem (TSP) algorithm using simulated annealing.

## Overview

The TSP algorithm aims to find the shortest possible route that a traveling salesman can take to visit a set of cities and return to the starting city. The algorithm uses the simulated annealing technique to iteratively improve the route by swapping cities and accepting moves that decrease the total distance or follow a probabilistic rule.

## Getting Started

To run the TSP algorithm, follow these steps:

1. Clone the repository to your local machine.
2. Ensure that you have Java JDK 16 or higher installed.
3. Compile the source code using the following command:
    ```
    javac -cp . src/akorovai/climbingalgorithm/*.java src/akorovai/climbingalgorithm/entities/*.java
    ```
4. Run the program using the following command:
    ```
    java -cp . src.akorovai.climbingalgorithm.App [file_path]
    ```
   Replace `[file_path]` with the path to the input file containing the city distances.

## Input File Format

The input file should follow the format below:

```
[number_of_cities]
[from_city] [to_city] [distance]
[from_city] [to_city] [distance]
...
```

Here's an example input file (`travelsalesman.txt`):

```
7
0 1 4
0 2 9
0 3 8
0 4 2
0 5 10
0 6 8
1 2 8
1 3 6
1 4 3
1 5 15
1 6 11
2 3 9
2 4 7
2 5 5
2 6 10
3 4 12
3 5 8
3 6 2
4 5 7
4 6 12
5 6 6
```

## Dependencies

The project uses the following dependencies managed by Maven:

- Lombok (version 1.18.26) - for reducing boilerplate code

## Algorithm Details

The TSP algorithm implemented in this project utilizes the simulated annealing technique. Here are some important details:

- Initial temperature: 100.0
- Minimum temperature: 0.001
- Cooling rate: 0.85

The algorithm works as follows:

1. Read the input file and prepare the data for analysis.
2. Generate an initial route by shuffling the city indices.
3. Perform the simulated annealing process:
    - Iterate until the temperature reaches the minimum temperature:
        - Generate a new route by swapping two random cities in the current route.
        - Calculate the distance of the new route.
        - Accept the new route if it improves the distance or based on a probabilistic rule.
        - Update the current route and distance if necessary.
        - Decrease the temperature based on the cooling rate.
4. Output the best route and its distance.

