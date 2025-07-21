/*
 * A* Pathfinding Algorithm Implementation in Java
 *
 * Aim:
 * This program implements the A* (A-Star) pathfinding algorithm to find the shortest path 
 * in a 2D grid/maze from a given start point to a goal point. The goal cell is represented by '9' 
 * in the grid and the obstacles/walls are represented by '1'. The algorithm uses a heuristic 
 * (Manhattan distance) to efficiently explore the path while avoiding obstacles.
 *
 * Output:
 * If a path is found, it returns the sequence of (x, y) positions from start to goal.
 * Otherwise, it returns false indicating no path is available.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;
/*
 * Algorithm: A* (A-Star) Pathfinding Algorithm
 *
 * 1. Initialize a priority queue to store nodes with priority = cost + heuristic (f = g + h).
 * 2. Set g(start) = 0 and h(start) = heuristic from start to goal.
 * 3. While the priority queue is not empty:
 *    a. Poll the node with the lowest f-score.
 *    b. If it's the goal (value 9), reconstruct the path using parent tracking.
 *    c. For each neighbor (up/down/left/right):
 *       - If it's valid and unvisited:
 *         - Compute tentative g = current.g + 1.
 *         - Compute h = Manhattan distance to goal.
 *         - Compute f = g + h.
 *         - If this f is better, update and add to queue.
 *         - Mark as visited and update parent.
 * 4. If the goal is found, build the path. Else return false.
 *
 * A* improves Dijkstra by adding a heuristic to guide the search towards the goal.
 */


class AStar {

    // Node class to represent each cell in the grid
    static class Node implements Comparable<Node> {
        int x, y; // Coordinates of the cell
        int g;    // Cost from start to this node
        int f;    // Estimated total cost (g + heuristic)

        Node(int x, int y, int g, int f) {
            this.x = x;
            this.y = y;
            this.g = g;
            this.f = f;
        }

        // For priority queue sorting based on lowest f cost
        public int compareTo(Node other) {
            return Integer.compare(this.f, other.f);
        }
    }

    // A* search algorithm to find path from (sx, sy) to the goal
    public static boolean searchpath(int[][] maze, int sx, int sy, List<Integer> path) {
        int rows = maze.length, cols = maze[0].length;

        boolean[][] visited = new boolean[rows][cols]; // Track visited cells
        PriorityQueue<Node> open = new PriorityQueue<>(); // Min-heap for open set

        int[][] parent = new int[rows][cols]; // To reconstruct the path later
        for (int i = 0; i < rows; i++) {
            Arrays.fill(parent[i], -1); // -1 indicates no parent yet
        }

        // Locate goal cell (where value is 9 in the maze)
        int gx = -1, gy = -1;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (maze[y][x] == 9) {
                    gx = x;
                    gy = y;
                    break;
                }
            }
        }

        // If goal not found, return false
        if (gx == -1) return false;

        // Start node added to open set with f = g + h
        open.add(new Node(sx, sy, 0, manhattan(sx, sy, gx, gy)));
        visited[sy][sx] = true;

        // Directions: left, right, up, down
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        // Main A* loop
        while (!open.isEmpty()) {
            Node curr = open.poll(); // Get node with lowest f

            // If goal is reached
            if (maze[curr.y][curr.x] == 9) {
                // Reconstruct the path using parent array
                int x = curr.x, y = curr.y;
                while (parent[y][x] != -1) {
                    path.add(x); // Add x
                    path.add(y); // Add y
                    int parentIndex = parent[y][x];
                    x = parentIndex % cols;
                    y = parentIndex / cols;
                }
                // Add starting position
                path.add(sx);
                path.add(sy);
                return true;
            }

            // Explore all 4 neighbors
            for (int d = 0; d < 4; d++) {
                int nx = curr.x + dx[d];
                int ny = curr.y + dy[d];

                // Valid move check: within bounds, not visited, and not a wall (1)
                if (nx >= 0 && ny >= 0 && nx < cols && ny < rows &&
                    !visited[ny][nx] && maze[ny][nx] != 1) {
                    
                    int g = curr.g + 1; // Cost so far
                    int f = g + manhattan(nx, ny, gx, gy); // Total estimated cost
                    open.add(new Node(nx, ny, g, f)); // Add neighbor to open set
                    visited[ny][nx] = true; // Mark as visited
                    parent[ny][nx] = curr.y * cols + curr.x; // Save parent for backtracking
                }
            }
        }

        // No path found
        return false;
    }

    // Heuristic function: Manhattan distance
    private static int manhattan(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
}
