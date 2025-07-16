package com.sakib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;

public class TicTacToeGUI extends JFrame {
    private TicTacToeGame game;
    private JButton[][] buttons;
    private JLabel statusLabel;
    private JLabel xScoreLabel, oScoreLabel;
    private int xScore = 0, oScore = 0;

    public TicTacToeGUI() {
        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 500);
        setLayout(new BorderLayout());

        // Set game icon from resources/icon.png
        try {
            setIconImage(new ImageIcon(getClass().getResource("/icon.png")).getImage());
        } catch (Exception e) {
            System.out.println("Icon not found");
        }

        game = new TicTacToeGame();
        buttons = new JButton[3][3];

        // Board panel with grid buttons
        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        boardPanel.setBackground(Color.WHITE);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 48));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setBackground(Color.WHITE);
                int row = i, col = j;

                buttons[i][j].addActionListener(e -> handleMove(row, col));

                buttons[i][j].addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        if (buttons[row][col].isEnabled() && buttons[row][col].getText().equals(""))
                            buttons[row][col].setBackground(new Color(220, 240, 255));
                    }
                    public void mouseExited(MouseEvent e) {
                        if (buttons[row][col].isEnabled() && buttons[row][col].getText().equals(""))
                            buttons[row][col].setBackground(Color.WHITE);
                    }
                });

                boardPanel.add(buttons[i][j]);
            }
        }

        // Status label (shows turns, winner, draw)
        statusLabel = new JLabel("Player " + game.getCurrentPlayer() + "'s Turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Score labels for X and O
        xScoreLabel = new JLabel("X: 0");
        xScoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        oScoreLabel = new JLabel("O: 0");
        oScoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        // Score panel with BorderLayout to place X left and O right
        JPanel scorePanel = new JPanel(new BorderLayout());
        scorePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        scorePanel.add(xScoreLabel, BorderLayout.WEST);
        scorePanel.add(oScoreLabel, BorderLayout.EAST);

        // Control panel with only Reset Score button (no Restart Game button)
        JPanel controlPanel = new JPanel();
        JButton resetScoreButton = new JButton("Reset Score");
        resetScoreButton.addActionListener(e -> {
            xScore = 0;
            oScore = 0;
            updateScoreLabels();
        });
        controlPanel.add(resetScoreButton);

        add(scorePanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        add(controlPanel, BorderLayout.PAGE_END);

        setVisible(true);
    }

    private void handleMove(int row, int col) {
        if (game.makeMove(row, col)) {
            buttons[row][col].setText(String.valueOf(game.getCurrentPlayer()));
            playSound("click.wav");

            if (game.checkWinner()) {
                statusLabel.setText("Player " + game.getCurrentPlayer() + " wins! 🎉");
                playSound("win.wav");
                highlightWin();
                updateScore();
                disableBoard();
                autoResetAfterDelay(1000); // 1 second delay
            } else if (game.isGameOver()) {
                statusLabel.setText("It's a draw!");
                disableBoard();
                autoResetAfterDelay(1000); // 1 second delay
            } else {
                game.switchPlayer();
                statusLabel.setText("Player " + game.getCurrentPlayer() + "'s Turn");
            }
        }
    }

    private void autoResetAfterDelay(int delayMillis) {
        Timer timer = new Timer(delayMillis, e -> resetGame());
        timer.setRepeats(false);
        timer.start();
    }

    private void disableBoard() {
        for (JButton[] row : buttons)
            for (JButton btn : row)
                btn.setEnabled(false);
    }

    private void resetGame() {
        game.reset();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
                buttons[i][j].setBackground(Color.WHITE);
            }
        statusLabel.setText("Player " + game.getCurrentPlayer() + "'s Turn");
    }

    private void updateScore() {
        if (game.getCurrentPlayer() == 'X') xScore++;
        else oScore++;
        updateScoreLabels();
    }

    private void updateScoreLabels() {
        xScoreLabel.setText("X: " + xScore);
        oScoreLabel.setText("O: " + oScore);
    }

    private void highlightWin() {
        char[][] board = game.getBoard();
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != '\0' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                for (int j = 0; j < 3; j++) buttons[i][j].setBackground(Color.GREEN);
                return;
            }
            if (board[0][i] != '\0' && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                for (int j = 0; j < 3; j++) buttons[j][i].setBackground(Color.GREEN);
                return;
            }
        }
        if (board[0][0] != '\0' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            for (int i = 0; i < 3; i++) buttons[i][i].setBackground(Color.GREEN);
        } else if (board[0][2] != '\0' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            buttons[0][2].setBackground(Color.GREEN);
            buttons[1][1].setBackground(Color.GREEN);
            buttons[2][0].setBackground(Color.GREEN);
        }
    }

    private void playSound(String soundFile) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource("/" + soundFile));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            System.err.println("Could not play sound: " + soundFile);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeGUI::new);
    }
}
