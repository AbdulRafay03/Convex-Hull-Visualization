# Convex Hull Algorithms

This project implements and contrasts various convex hull algorithms in the context of a two-dimensional plane. The implemented algorithms include Graham Scan, Jarvis March, Brute Force, Quick Elimination, Andrew’s Monotone Chain, and Line Intersection Algorithms. The primary focus is on assessing their performance, execution duration, and efficiency in constructing convex hulls using a given set of points.

## Implemented Modules

### 1. Graham Scan
   - Finds the convex hull by sorting points based on polar angles and traversing them in order.

### 2. Jarvis March
   - Also known as the Gift-wrapping algorithm, Jarvis March iteratively selects the point with the smallest polar angle concerning the current point.

### 3. Brute Force
   - A straightforward approach that checks all possible combinations of points to find the convex hull. Serves as a baseline for performance comparison.

### 4. Quick Elimination
   - Efficiently eliminates points that cannot be part of the convex hull based on their relative positions to the current candidate hull.

### 5. Andrew’s Monotone Chain
   - A divide-and-conquer algorithm that sorts points based on their x-coordinates, constructs upper and lower hulls separately and then merges them.

### 6. Line Intersection
   - Detects and handles cases where lines intersect during the convex hull construction process.
   - Utilizes Bounding Box, CCW (Counter-Clockwise), and Line-by-Line Intersection modules.

### Bounding Box
   - Computes the bounding box of a set of points.

### CCW (Counter-Clockwise)
   - Determines whether three points are listed in a counter-clockwise order.

## How to Use

### Prerequisites
- Ensure you have a compatible programming environment for the chosen programming language (Python & Java).
- Install any required dependencies as specified in the individual algorithm implementations.

### Running the Algorithms

#### Running Convex Hull Visualization

To visualize the Convex Hull algorithms, follow these steps:

1. Open the "ConvexHullVisualization.java" file.
2. Run the file using your preferred Java development environment or from the command line.

This will execute the visualization program, allowing you to observe the Convex Hull construction process.

#### Running Line Intersection Algorithms

For the Line Intersection part of the project, algorithms are implemented in separate cells in the ".ipynb" (Jupyter Notebook) file. Follow these steps:

1. Open the provided ".ipynb" file in a Jupyter Notebook environment.
2. Each cell corresponds to a separate Line Intersection algorithm. Execute the cells individually to observe the results and analysis for each algorithm.

### Note
Make sure you have the necessary dependencies and libraries installed, as specified in the project documentation.

## Contributing

Contributions are welcome! If you have suggestions, enhancements, or bug fixes, please submit a pull request.



```bash
