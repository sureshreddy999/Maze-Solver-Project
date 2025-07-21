/*
 * Breadth-First Search (BFS) Pathfinding Algorithm in Java
 *
 * Aim:
 * This program implements the Breadth-First Search (BFS) algorithm to find the shortest path 
 * in a 2D grid/maze from a given starting point to a goal point (cell with value '9'). 
 * BFS explores all neighboring nodes level by level and guarantees the shortest path 
 * in an unweighted grid. Walls/obstacles are represented by the value '1' in the grid.
 *
 * Output:
 * If a path to the goal exists, the function returns true and fills the 'path' list 
 * with the sequence of (x, y) positions from start to goal. If no path is found, it returns false.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.Queue;
import javax.swing.Timer;
/*
 * Algorithm: Breadth-First Search (BFS)
 *
 * 1. Initialize a queue and add the starting cell (sx, sy) to it.
 * 2. Mark the starting cell as visited.
 * 3. While the queue is not empty:
 *    a. Remove the front cell from the queue.
 *    b. If it's the goal (value 9), reconstruct the path using parent tracking.
 *    c. Otherwise, add all unvisited and valid neighbors (up/down/left/right) to the queue.
 *       - Update their parent as the current cell.
 *       - Mark them as visited.
 * 4. If the goal is reached, reconstruct the path using the parent matrix.
 * 5. If the queue becomes empty without reaching the goal, return false.
 *
 * BFS guarantees the shortest path in an unweighted grid by exploring level by level.
 */


public class BFS {

    // BFS pathfinding method
    public static boolean searchpath(int[][] maze, int startX, int startY, List<Integer> path) {
        int rows = maze.length;
        int cols = maze[0].length;

        boolean[][] visited = new boolean[rows][cols]; // Track visited cells
        Queue<int[]> queue = new LinkedList<>();       // BFS queue for exploration
        int[][] parent = new int[rows][cols];          // Store parent cell for path backtracking

        // Initialize parent matrix with -1 (no parent)
        for (int i = 0; i < rows; i++) {
            Arrays.fill(parent[i], -1);
        }

        // Start BFS from the starting position
        queue.add(new int[]{startX, startY});
        visited[startY][startX] = true;

        // Directions: left, right, up, down
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        int goalX = -1, goalY = -1; // To store goal coordinates

        // BFS loop
        while (!queue.isEmpty()) {
            int[] curr = queue.poll(); // Dequeue next cell to process
            int x = curr[0];
            int y = curr[1];

            // Check if goal is reached
            if (maze[y][x] == 9) {
                goalX = x;
                goalY = y;
                break;
            }

            // Explore neighbors in all 4 directions
            for (int d = 0; d < 4; d++) {
                int nx = x + dx[d];
                int ny = y + dy[d];

                // Check if neighbor is within bounds, not visited, and not a wall
                if (nx >= 0 && ny >= 0 && nx < cols && ny < rows &&
                    !visited[ny][nx] && maze[ny][nx] != 1) {

                    queue.add(new int[]{nx, ny}); // Enqueue valid neighbor
                    visited[ny][nx] = true;       // Mark as visited
                    parent[ny][nx] = y * cols + x; // Store current cell as parent
                }
            }
        }

        // Reconstruct path if goal was found
        if (goalX != -1 && goalY != -1) {
            int x = goalX, y = goalY;
            while (parent[y][x] != -1) {
                path.add(x); // Add x coordinate
                path.add(y); // Add y coordinate
                int parentIndex = parent[y][x];
                x = parentIndex % cols;
                y = parentIndex / cols;
            }
            // Add starting point at the end
            path.add(startX);
            path.add(startY);
            return true;
        }

        // No path found
        return false;
    }

}
