/*
 * Depth-First Search (DFS) Pathfinding Algorithm in Java
 *
 * Aim:
 * This program implements the Depth-First Search (DFS) algorithm to find a path in a 2D grid/maze 
 * from a given starting point to the goal (represented by the number '9'). 
 * DFS explores paths by going as deep as possible before backtracking. 
 * Obstacles in the grid are represented by '1'. 
 * This method marks visited paths with '2' to avoid cycles and repeated visits.
 *
 * Output:
 * If a valid path to the goal is found, the function returns true and stores the sequence of 
 * (x, y) positions in the 'path' list. Otherwise, it returns false if no path is found.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;
/*
 * Algorithm: Depth-First Search (DFS)
 *
 * 1. Start from the given starting cell (sx, sy).
 * 2. Mark the current cell as visited.
 * 3. If the current cell is the goal (value 9), add to path and return true.
 * 4. Recursively explore each of the 4 directions: up, down, left, right.
 *    - For each unvisited and valid neighbor (not a wall), recursively call DFS.
 *    - If any recursive call returns true, current cell is part of the valid path.
 * 5. If none of the neighbors lead to the goal, backtrack by returning false.
 * 
 * DFS uses recursion and explores a path as deep as possible before backtracking.
 */


public class DepthFirst {

    // DFS pathfinding method
    public static boolean searchpath(int[][] maze, int x, int y, List<Integer> path) {
        // Check if current cell is out of bounds or is a wall (1)
        if (x < 0 || y < 0 || x >= maze[0].length || y >= maze.length || maze[y][x] == 1) {
            return false;
        }

        // Check if goal is reached
        if (maze[y][x] == 9) {
            path.add(x); // Add goal x
            path.add(y); // Add goal y
            return true;
        }

        // Visit unvisited cell (represented by 0)
        if (maze[y][x] == 0) {
            maze[y][x] = 2; // Mark cell as visited

            // Directions: left, right, up, down
            int[] dx = {-1, 1, 0, 0};
            int[] dy = {0, 0, -1, 1};

            // Explore each direction recursively
            for (int i = 0; i < 4; i++) {
                if (searchpath(maze, x + dx[i], y + dy[i], path)) {
                    path.add(x); // Add x on the path
                    path.add(y); // Add y on the path
                    return true;
                }
            }
        }

        // Return false if no path is found from this cell
        return false;
    }

}
