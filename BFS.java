import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.Queue;
import javax.swing.Timer;

public class BFS {
    public static boolean searchpath(int[][] maze, int startX, int startY, List<Integer> path) {
        int rows = maze.length;
        int cols = maze[0].length;
        boolean[][] visited = new boolean[rows][cols];
        Queue<int[]> queue = new LinkedList<>();
        int[][] parent = new int[rows][cols];

        // Initialize parent array
        for (int i = 0; i < rows; i++) {
            Arrays.fill(parent[i], -1);
        }

        queue.add(new int[]{startX, startY});
        visited[startY][startX] = true;

        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        int goalX = -1, goalY = -1;

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int x = curr[0], y = curr[1];

            if (maze[y][x] == 9) {
                goalX = x;
                goalY = y;
                break;
            }

            for (int d = 0; d < 4; d++) {
                int nx = x + dx[d];
                int ny = y + dy[d];

                // Strict validation for BFS moves
                if (nx >= 0 && ny >= 0 && nx < cols && ny < rows &&
                    !visited[ny][nx] && maze[ny][nx] != 1) {  // Explicit wall check
                    
                    queue.add(new int[]{nx, ny});
                    visited[ny][nx] = true;
                    parent[ny][nx] = y * cols + x;
                }
            }
        }

        // Reconstruct path if goal found
        if (goalX != -1 && goalY != -1) {
            int x = goalX, y = goalY;
            while (parent[y][x] != -1) {
                path.add(x);
                path.add(y);
                int parentIndex = parent[y][x];
                x = parentIndex % cols;
                y = parentIndex / cols;
            }
            path.add(startX);
            path.add(startY);
            return true;
        }

        return false;
    }
}