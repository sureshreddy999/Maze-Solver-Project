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

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

class Dijkstra {

    // Dijkstra's pathfinding method
    public static boolean searchpath(int[][] maze, int sx, int sy, List<Integer> path) {
        int rows = maze.length, cols = maze[0].length;

        boolean[][] visited = new boolean[rows][cols];        // To track visited nodes
        PriorityQueue<Node> open = new PriorityQueue<>();     // Min-heap based on distance
        int[][] parent = new int[rows][cols];                 // To reconstruct the path
        int[][] distance = new int[rows][cols];               // Distance from start to each cell

        // Initialize parent and distance arrays
        for (int i = 0; i < rows; i++) {
            Arrays.fill(parent[i], -1);
            Arrays.fill(distance[i], Integer.MAX_VALUE); // Set all distances to infinity
        }

        distance[sy][sx] = 0; // Distance to the start cell is 0
        open.add(new Node(sx, sy, 0)); // Add start node to priority queue
        visited[sy][sx] = true;

        // Directions: left, right, up, down
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        // Main loop
        while (!open.isEmpty()) {
            Node curr = open.poll(); // Get node with smallest distance

            // Goal check
            if (maze[curr.y][curr.x] == 9) {
                // Reconstruct the path from goal to start
                int x = curr.x, y = curr.y;
                while (parent[y][x] != -1) {
                    path.add(x);
                    path.add(y);
                    int parentIndex = parent[y][x];
                    x = parentIndex % cols;
                    y = parentIndex / cols;
                }
                path.add(sx);
                path.add(sy);
                return true;
            }

            // Explore 4 neighbors
            for (int d = 0; d < 4; d++) {
                int nx = curr.x + dx[d];
                int ny = curr.y + dy[d];

                // Valid move check
                if (nx >= 0 && ny >= 0 && nx < cols && ny < rows &&
                    !visited[ny][nx] && maze[ny][nx] != 1) {

                    int newDist = distance[curr.y][curr.x] + 1; // Each move costs 1
                    if (newDist < distance[ny][nx]) {
                        distance[ny][nx] = newDist; // Update to smaller distance
                        parent[ny][nx] = curr.y * cols + curr.x; // Save parent
                        open.add(new Node(nx, ny, newDist)); // Add neighbor to queue
                        visited[ny][nx] = true; // Mark as visited
                    }
                }
            }
        }

        // No path found
        return false;
    }

    // Node class to represent each cell with its coordinates and current distance
    static class Node implements Comparable<Node> {
        int x, y, distance;

        Node(int x, int y, int distance) {
            this.x = x;
            this.y = y;
            this.distance = distance;
        }

        // Comparison for priority queue (based on distance)
        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }

}
