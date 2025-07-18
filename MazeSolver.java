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

        algoBox.addActionListener(e -> currentAlgorithm = (String) algoBox.getSelectedItem());

        generateButton.addActionListener(e -> {
            int difficulty = difficultySlider.getValue();
            if(difficulty==2){
                difficulty = 3; // Adjust difficulty level for better maze generation
            }
            else if(difficulty==3){
                difficulty = 4; // Adjust difficulty level for better maze generation
            }
            rows = 9 + difficulty * 5;
            cols = 20 + difficulty * 5;
            // if(rows>=23 && cols>=23){
            //     rows = 21; // Limit size to prevent performance issues
            //     cols = 21;
            // }
            generateRandomMaze();
            mazePanel.repaint();
        });

        solveButton.addActionListener(e -> {
            path.clear();
            pathIndex = 0;
            
            int[][] mazeCopy = new int[rows][cols];
            for (int i = 0; i < rows; i++) {
                System.arraycopy(maze[i], 0, mazeCopy[i], 0, cols);
            }
            
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
            
            if (!path.isEmpty()) {
                printPathCoordinates(); // Debug output
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

    private void printPathCoordinates() {
        System.out.println("Path Coordinates:");
        for (int i = 0; i < path.size(); i += 2) {
            int x = path.get(i);
            int y = path.get(i + 1);
            System.out.println("(" + x + "," + y + ") - Value: " + maze[y][x]);
        }
    }

private void generateRandomMaze() {
    path.clear();
    maze = new int[rows][cols];
    for (int y = 0; y < rows; y++) {
        Arrays.fill(maze[y], 1); // Start with all walls
    }
    generateMazeDFS(1, 1);
    maze[rows - 2][cols - 2] = 9; // Goal
    Random rand1 = new Random();
    int ourwish= rand1.nextInt(10) + 1; // Ensure goal is walkable
    if(ourwish>5){
    for(int j=1 ;j<cols-2;j++){
        maze[rows - 2][j] = 0; // Create a path to the goal
    }
}
    maze[1][1] = 0; // Ensure start is walkable

    // ✅ Add extra passages for loops
    Random rand = new Random();
    int extraPassages = (rows * cols) / 10; // Adjust % of extra passages here

    for (int i = 0; i < extraPassages; i++) {
        int x = rand.nextInt(cols - 2) + 1;
        int y = rand.nextInt(rows - 2) + 1;
        if (maze[y][x] == 1) {
            // Only break walls that have at least 2 adjacent passages
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

    private class MazePanel extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(rows> 23 && cols > 23) {
                g.translate(190, 0);
            }
           
           
            else{
            g.translate(295, 145);
            }
            // Draw maze grid
            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < cols; x++) {
                    Color cellColor = switch (maze[y][x]) {
                        case 1 -> Color.BLACK;   // Wall
                        case 9 -> Color.RED;     // Goal
                        case 2 -> new Color(220, 220, 220); // Visited
                        default -> Color.WHITE;  // Path
                    };
                    g.setColor(cellColor);
                    g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                    g.setColor(Color.DARK_GRAY);
                    g.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
                }
            }
            
            // Draw start position
            g.setColor(Color.BLUE);
            g.fillRect(1 * cellSize, 1 * cellSize, cellSize, cellSize);
            
            // Draw solution path with validation
            g.setColor(new Color(50, 150, 50)); // Dark green
            for (int i = 0; i < Math.min(pathIndex, path.size() / 2); i++) {
                int pathX = path.get(i * 2);
                int pathY = path.get(i * 2 + 1);
                
                // Strict validation
                if (pathX >= 0 && pathX < cols && pathY >= 0 && pathY < rows && 
                    maze[pathY][pathX] != 1) {
                    g.fillRect(pathX * cellSize + 2, pathY * cellSize + 2, 
                              cellSize - 4, cellSize - 4);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MazeSolver().setVisible(true);
        });
    }
}