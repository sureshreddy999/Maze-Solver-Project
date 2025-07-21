/*
 * Dijkstra's Pathfinding Algorithm in Java
 *
 * Aim:
 * This program implements Dijkstra's algorithm to find the shortest path from a given starting 
 * point (sx, sy) to a goal point (represented by '9') in a 2D grid/maze. The maze uses:
 * - 0 for open paths
 * - 1 for walls/obstacles
 * - 9 for the goal cell
 *
 * Dijkstra's algorithm is a weighted graph search that guarantees the shortest path in graphs 
 * with non-negative weights. In this grid, all edge weights are treated as 1.
 *
 * Output:
 * If a path to the goal exists, it returns true and populates the 'path' list with (x, y) 
 * coordinates in reverse order from goal to start. Otherwise, it returns false.
 */

import javax.swing.*; // For GUI (not used here directly)
import java.awt.*;    // For GUI (not used here directly)
import java.awt.event.*; // For GUI events (not used here directly)
import java.util.*;   // For collections like List, PriorityQueue, etc.
import java.util.List;
import javax.swing.Timer; // For animation timing (used in visual part, not here)

// Dijkstra class containing the search algorithm
class Dijkstra {

    /**
     * Performs Dijkstra's algorithm on a 2D maze.
     * @param maze The 2D grid containing 0 (open), 1 (walls), 9 (goal)
     * @param sx Start X-coordinate
     * @param sy Start Y-coordinate
     * @param path List to store the result path if found
     * @return true if path to goal (9) is found, else false
     */
    public static boolean searchpath(int[][] maze, int sx, int sy, List<Integer> path) {
        int rows = maze.length;        // Total number of rows in the maze
        int cols = maze[0].length;     // Total number of columns in the maze

        // 2D boolean array to track visited cells
        boolean[][] visited = new boolean[rows][cols];

        // Priority queue (min-heap) to get the next closest node based on distance
        PriorityQueue<Node> open = new PriorityQueue<>();

        // Parent matrix to reconstruct the path (stores parent index of each cell)
        int[][] parent = new int[rows][cols];

        // Distance matrix to store shortest distance from source to each cell
        int[][] distance = new int[rows][cols];

        // Initialize parent and distance matrices
        for (int i = 0; i < rows; i++) {
            Arrays.fill(parent[i], -1);                     // -1 means no parent yet
            Arrays.fill(distance[i], Integer.MAX_VALUE);    // Infinite distance initially
        }

        // Distance from source cell to itself is 0
        distance[sy][sx] = 0;

        // Add the source node to priority queue
        open.add(new Node(sx, sy, 0));

        // Mark the starting cell as visited
        visited[sy][sx] = true;

        // Movement directions: left, right, up, down
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        // Main loop: run until all possible paths are explored
        while (!open.isEmpty()) {
            // Poll the node with the smallest distance
            Node curr = open.poll();

            // If the goal is reached
            if (maze[curr.y][curr.x] == 9) {
                // Reconstruct the path from goal to start using parent matrix
                int x = curr.x;
                int y = curr.y;
                while (parent[y][x] != -1) {
                    path.add(x); // Add x-coordinate to path
                    path.add(y); // Add y-coordinate to path

                    // Get parent index and convert it back to (x, y)
                    int parentIndex = parent[y][x];
                    x = parentIndex % cols;
                    y = parentIndex / cols;
                }
                // Add the start node
                path.add(sx);
                path.add(sy);
                return true; // Path found
            }

            // Explore all 4 possible directions
            for (int d = 0; d < 4; d++) {
                int nx = curr.x + dx[d]; // Neighbor X
                int ny = curr.y + dy[d]; // Neighbor Y

                // Check if neighbor is within bounds and not a wall or visited
                if (nx >= 0 && ny >= 0 && nx < cols && ny < rows &&
                    !visited[ny][nx] && maze[ny][nx] != 1) {

                    int newDist = distance[curr.y][curr.x] + 1; // Each step = cost 1

                    // If a shorter path to neighbor is found
                    if (newDist < distance[ny][nx]) {
                        distance[ny][nx] = newDist; // Update distance
                        parent[ny][nx] = curr.y * cols + curr.x; // Store parent index
                        open.add(new Node(nx, ny, newDist)); // Add neighbor to queue
                        visited[ny][nx] = true; // Mark neighbor as visited
                    }
                }
            }
        }

        // If while loop exits and goal wasn't found, return false
        return false;
    }

    // Node class to represent each cell in the maze
    static class Node implements Comparable<Node> {
        int x, y;         // Cell's X and Y coordinates
        int distance;     // Distance from the start cell

        Node(int x, int y, int distance) {
            this.x = x;
            this.y = y;
            this.distance = distance;
        }

        // Comparison based on distance for priority queue ordering
        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }
}
