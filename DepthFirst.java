import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.Timer;


public class DepthFirst {
    public static boolean searchpath(int[][] maze, int x, int y, List<Integer> path) {
        if (x < 0 || y < 0 || x >= maze[0].length || y >= maze.length || maze[y][x] == 1) {
            return false;
        }
        
        if (maze[y][x] == 9) {
            path.add(x);
            path.add(y);
            return true;
        }
        
        if (maze[y][x] == 0) {
            maze[y][x] = 2; // Mark as visited
            
            int[] dx = {-1, 1, 0, 0};
            int[] dy = {0, 0, -1, 1};
            
            for (int i = 0; i < 4; i++) {
                if (searchpath(maze, x + dx[i], y + dy[i], path)) {
                    path.add(x);
                    path.add(y);
                    return true;
                }
            }
        }
        return false;
    }
}