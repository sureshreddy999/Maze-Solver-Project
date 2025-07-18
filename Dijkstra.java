import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

class Dijkstra {
    public static boolean searchpath(int[][] maze, int sx, int sy, List<Integer> path) {
        int rows = maze.length, cols = maze[0].length;
        boolean[][] visited = new boolean[rows][cols];
        PriorityQueue<Node> open = new PriorityQueue<>();
        int[][] parent = new int[rows][cols];
        int[][] distance = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            Arrays.fill(parent[i], -1);
            Arrays.fill(distance[i], Integer.MAX_VALUE);
        }
        distance[sy][sx] = 0;

        open.add(new Node(sx, sy, 0));
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

                // Strict validation for Dijkstra moves
                if (nx >= 0 && ny >= 0 && nx < cols && ny < rows &&
                    !visited[ny][nx] && maze[ny][nx] != 1) {  // Explicit wall check
                    
                    int newDist = distance[curr.y][curr.x] + 1;
                    if (newDist < distance[ny][nx]) {
                        distance[ny][nx] = newDist;
                        parent[ny][nx] = curr.y * cols + curr.x;
                        open.add(new Node(nx, ny, newDist));
                        visited[ny][nx] = true;
                    }
                }
            }
        }
        return false;
    }

    static class Node implements Comparable<Node> {
        int x, y, distance;

        Node(int x, int y, int distance) {
            this.x = x;
            this.y = y;
            this.distance = distance;
        }

        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }
}