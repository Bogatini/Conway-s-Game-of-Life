import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameOfLife extends JFrame {

    private static int[][] GRID = new int[50][50];
    private boolean isRunning = false;
    private JButton[][] buttons;
    private int STEPNUM = 0;

    private Timer TIMER;
    private int timerDelay = 500;
    private JLabel stepCountLabel;

    public GameOfLife() {
        setTitle("Game of Life");
        setLayout(new BorderLayout());  
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buttons = new JButton[GRID.length][GRID[0].length];
        initializeGrid();
        addControlPanel();

        setVisible(true);
    }

    private void addControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());
        
        stepCountLabel = new JLabel("Steps: " + STEPNUM);
        stepCountLabel.setHorizontalAlignment(SwingConstants.LEFT);
        controlPanel.add(stepCountLabel, BorderLayout.WEST);


        JPanel startPanel = new JPanel(new FlowLayout());
        JButton startStopButton = new JButton("Start/Stop Simulation");
        
        startStopButton.addActionListener(e -> toggleSimulation());
        startPanel.add(startStopButton);

        controlPanel.add(startPanel, BorderLayout.CENTER);

        
        JPanel resetPanel = new JPanel(new FlowLayout());
        JButton resetButton = new JButton("Reset");

        resetButton.addActionListener(e -> wipeGrid());
        resetPanel.add(resetButton);

        controlPanel.add(resetPanel, BorderLayout.EAST);


        add(controlPanel, BorderLayout.SOUTH);
    }

    private void wipeGrid() {
        for (int y = 0; y < GRID.length; y++) {
            for (int x = 0; x < GRID[0].length; x++) {
                GRID[y][x] = 0;
            }
        }
        repaintGrid();
        toggleSimulation();
        STEPNUM = 0;
    }

    private void toggleSimulation() {
        isRunning = !isRunning;
        if (isRunning) {
            startSimulation();
        } else {
            stopSimulation();
        }
    }

    private void startSimulation() {
        TIMER = new Timer(timerDelay, e -> {
            STEPNUM += 1;
            stepCountLabel.setText("Steps: " + STEPNUM);
            updateGrid();
            repaintGrid();
        });
        TIMER.start();
    }

    private void stopSimulation() {
        if (TIMER != null) {
            TIMER.stop();
        }
    }

    private void updateGrid() {
        int[][] newGrid = new int[GRID.length][GRID[0].length];

        for (int y = 0; y < GRID.length; y++) {
            for (int x = 0; x < GRID[0].length; x++) {
                newGrid[y][x] = checkCell(x, y, GRID);
            }
        }
        GRID = newGrid; // Update the original grid to the new state
    }

    private void repaintGrid() {
        for (int y = 0; y < GRID.length; y++) {
            for (int x = 0; x < GRID[0].length; x++) {
                if (GRID[y][x] == 1) {
                    buttons[y][x].setBackground(Color.BLACK);
                }
                if (GRID[y][x] == 0) {
                    buttons[y][x].setBackground(Color.WHITE);
                }
            }
        }
    }

    private int checkCell(int x, int y, int[][] currentGrid) {
        int liveNeighbours = 0;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if ((dx == 0) && (dy == 0)) continue;

                int neighbourx = x + dx;
                int neighboury = y + dy;

                if (neighbourx >= 0 && neighbourx < GRID[0].length && neighboury >= 0 && neighboury < GRID.length) {
                    if (currentGrid[neighboury][neighbourx] == 1) liveNeighbours += 1;
                }
            }
        }

        if (liveNeighbours < 2) return 0; // kill
        if (liveNeighbours > 3) return 0; // kill
        if (liveNeighbours == 3) return 1; // alive
        return currentGrid[y][x]; // stays the same
    }

    private void initializeGrid() {
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(GRID.length, GRID[0].length));

        for (int y = 0; y < GRID.length; y++) {
            for (int x = 0; x < GRID[0].length; x++) {
                JButton button = new JButton();
                button.setBackground(Color.WHITE);
                button.setOpaque(true);
                button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                buttons[y][x] = button;

                final int finalY = y;
                final int finalX = x;

                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            GRID[finalY][finalX] = 1;
                            button.setBackground(Color.BLACK);
                        } 
                        else if (e.getButton() == MouseEvent.BUTTON3) {
                            GRID[finalY][finalX] = 0;
                            button.setBackground(Color.WHITE);
                        }
                    }
                });
                gridPanel.add(button);
            }
        }
        add(gridPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        GameOfLife gameOfLifeWindow = new GameOfLife();
    }
}