/*
 * MazeSolver GUI Application in Java
 *
 * Aim:
 * This program creates a graphical maze-solving application using Java Swing. It generates a random maze 
 * and solves it using one of the selected algorithms: DFS, BFS, A*, or Dijkstra. The GUI allows users to:
 * - Generate mazes of varying difficulty.
 * - Choose the algorithm to visualize.
 * - View the maze-solving process animated in real-time.
 *
 * Features:
 * - Dynamic maze generation using randomized DFS.
 * - Supports multiple algorithms for comparison.
 * - Timer-based path visualization.
 * - Adjustable difficulty via slider.
 * - Interactive GUI with buttons and dropdowns.
 *
 * Maze Cell Encoding:
 * - 0 = Walkable path
 * - 1 = Wall
 * - 2 = Visited path during solving
 * - 9 = Goal
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

public class MazeSolver extends JFrame {

    private int rows = 14;
    private int cols = 25;
    private int[][] maze;
    private List<Integer> path = new ArrayList<>();
    private MazePanel mazePanel;
    private int cellSize = 30;
    private int pathIndex = 0;
    private String currentAlgorithm = "DFS";

    public MazeSolver() {
        setTitle("Maze Solver");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        generateRandomMaze();
        mazePanel = new MazePanel();
        add(mazePanel, BorderLayout.CENTER);

        // Control panel components
        JPanel controlPanel = new JPanel();
        JButton generateButton = new JButton("Generate Maze");
        JButton solveButton = new JButton("Solve");
        String[] algorithms = {"DFS", "BFS", "A*", "Dijkstra"};
        JComboBox<String> algoBox = new JComboBox<>(algorithms);
        JSlider difficultySlider = new JSlider(1, 2, 1);

        controlPanel.add(new JLabel("Algorithm:"));
        controlPanel.add(algoBox);
        controlPanel.add(generateButton);
        controlPanel.add(solveButton);
        controlPanel.add(new JLabel("Difficulty:"));
        controlPanel.add(difficultySlider);

        add(controlPanel, BorderLayout.SOUTH);

        // Event listeners
        algoBox.addActionListener(e -> currentAlgorithm = (String) algoBox.getSelectedItem());

        generateButton.addActionListener(e -> {
            int difficulty = difficultySlider.getValue();
            if (difficulty == 2) difficulty = 3;
            else if (difficulty == 3) difficulty = 4;
            rows = 9 + difficulty * 5;
            cols = 20 + difficulty * 5;
            generateRandomMaze();
            mazePanel.repaint();
        });

        solveButton.addActionListener(e -> {
            path.clear();
            pathIndex = 0;

            // Copy maze before solving
            int[][] mazeCopy = new int[rows][cols];
            for (int i = 0; i < rows; i++) {
                System.arraycopy(maze[i], 0, mazeCopy[i], 0, cols);
            }

            // Run selected algorithm
            switch (currentAlgorithm) {
                case "DFS":
                    DepthFirst.searchpath(mazeCopy, 1, 1, path);
                    break;
                case "BFS":
                    BFS.searchpath(mazeCopy, 1, 1, path);
                    break;
                case "A*":
                    AStar.searchpath(mazeCopy, 1, 1, path);
                    break;
                case "Dijkstra":
                    Dijkstra.searchpath(mazeCopy, 1, 1, path);
                    break;
            }

            // Animate solution path
            if (!path.isEmpty()) {
                printPathCoordinates();
                Timer timer = new Timer(100, new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        pathIndex++;
                        if (pathIndex >= path.size() / 2) {
                            ((Timer) evt.getSource()).stop();
                        }
                        mazePanel.repaint();
                    }
                });
                timer.start();
            }
        });
    }

    // Debugging: Print coordinates of the found path
    private void printPathCoordinates() {
        System.out.println("Path Coordinates:");
        for (int i = 0; i < path.size(); i += 2) {
            int x = path.get(i);
            int y = path.get(i + 1);
            System.out.println("(" + x + "," + y + ") - Value: " + maze[y][x]);
        }
    }

    // Generate maze using randomized DFS and optionally add loops
    private void generateRandomMaze() {
        path.clear();
        maze = new int[rows][cols];
        for (int y = 0; y < rows; y++) {
            Arrays.fill(maze[y], 1); // Fill all with walls
        }
        generateMazeDFS(1, 1); // Start DFS from (1,1)
        maze[rows - 2][cols - 2] = 9; // Set goal

        // Create guaranteed path to goal (sometimes)
        Random rand1 = new Random();
        if (rand1.nextInt(10) + 1 > 5) {
            for (int j = 1; j < cols - 2; j++) {
                maze[rows - 2][j] = 0;
            }
        }

        maze[1][1] = 0; // Ensure start is walkable

        // Add random passages to create loops
        Random rand = new Random();
        int extraPassages = (rows * cols) / 10;
        for (int i = 0; i < extraPassages; i++) {
            int x = rand.nextInt(cols - 2) + 1;
            int y = rand.nextInt(rows - 2) + 1;
            if (maze[y][x] == 1) {
                int passages = 0;
                if (maze[y - 1][x] == 0) passages++;
                if (maze[y + 1][x] == 0) passages++;
                if (maze[y][x - 1] == 0) passages++;
                if (maze[y][x + 1] == 0) passages++;
                if (passages >= 2) {
                    maze[y][x] = 0;
                }
            }
        }
    }

    // Depth-first recursive maze generation
    private void generateMazeDFS(int x, int y) {
        int[] dx = {2, -2, 0, 0};
        int[] dy = {0, 0, 2, -2};
        maze[y][x] = 0;

        List<Integer> dirs = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(dirs);

        for (int dir : dirs) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            if (nx > 0 && ny > 0 && nx < cols - 1 && ny < rows - 1 && maze[ny][nx] == 1) {
                maze[y + dy[dir] / 2][x + dx[dir] / 2] = 0;
                generateMazeDFS(nx, ny);
            }
        }
    }

    // Maze panel with real-time drawing
    private class MazePanel extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // Adjust drawing offset for larger mazes
            if (rows > 23 && cols > 23) g.translate(190, 0);
            else g.translate(295, 145);

            // Draw maze cells
            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < cols; x++) {
                    Color cellColor = switch (maze[y][x]) {
                        case 1 -> Color.BLACK;
                        case 9 -> Color.RED;
                        case 2 -> new Color(220, 220, 220);
                        default -> Color.WHITE;
                    };
                    g.setColor(cellColor);
                    g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                    g.setColor(Color.DARK_GRAY);
                    g.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
                }
            }

            // Draw starting point
            g.setColor(Color.BLUE);
            g.fillRect(1 * cellSize, 1 * cellSize, cellSize, cellSize);

            // Animate path
            g.setColor(new Color(50, 150, 50)); // Green
            for (int i = 0; i < Math.min(pathIndex, path.size() / 2); i++) {
                int pathX = path.get(i * 2);
                int pathY = path.get(i * 2 + 1);
                if (pathX >= 0 && pathX < cols && pathY >= 0 && pathY < rows && maze[pathY][pathX] != 1) {
                    g.fillRect(pathX * cellSize + 2, pathY * cellSize + 2, cellSize - 4, cellSize - 4);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MazeSolver().setVisible(true));
    }
}
