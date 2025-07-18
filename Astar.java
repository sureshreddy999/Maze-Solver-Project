import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

class AStar {
    static class Node implements Comparable<Node> {
        int x, y, g, f;

        Node(int x, int y, int g, int f) {
            this.x = x;
            this.y = y;
            this.g = g;
            this.f = f;
        }

        public int compareTo(Node other) {
            return Integer.compare(this.f, other.f);
        }
    }

    public static boolean searchpath(int[][] maze, int sx, int sy, List<Integer> path) {
        int rows = maze.length, cols = maze[0].length;
        boolean[][] visited = new boolean[rows][cols];
        PriorityQueue<Node> open = new PriorityQueue<>();
        int[][] parent = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            Arrays.fill(parent[i], -1);
        }

        // Find goal position
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
        if (gx == -1) return false;

        open.add(new Node(sx, sy, 0, manhattan(sx, sy, gx, gy)));
        visited[sy][sx] = true;

        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        while (!open.isEmpty()) {
            Node curr = open.poll();

            if (maze[curr.y][curr.x] == 9) {
                // Reconstruct path
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

            for (int d = 0; d < 4; d++) {
                int nx = curr.x + dx[d];
                int ny = curr.y + dy[d];

                // Strict validation for A* moves
                if (nx >= 0 && ny >= 0 && nx < cols && ny < rows &&
                    !visited[ny][nx] && maze[ny][nx] != 1) {  // Explicit wall check
                    
                    int g = curr.g + 1;
                    int f = g + manhattan(nx, ny, gx, gy);
                    open.add(new Node(nx, ny, g, f));
                    visited[ny][nx] = true;
                    parent[ny][nx] = curr.y * cols + curr.x;
                }
            }
        }
        return false;
    }

    private static int manhattan(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
}