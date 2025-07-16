package com.sakib;

public class TicTacToeGame {
    private char[][] board;
    private char currentPlayer;
    private boolean gameOver;
    private char startingPlayer = 'X';

    public TicTacToeGame() {
        board = new char[3][3];
        currentPlayer = startingPlayer;
        gameOver = false;
    }

    public boolean makeMove(int row, int col) {
        if (gameOver || board[row][col] != '\0') return false;
        board[row][col] = currentPlayer;
        if (checkWinner() || isBoardFull()) gameOver = true;
        return true;
    }

    public void switchPlayer() {
        currentPlayer = currentPlayer == 'X' ? 'O' : 'X';
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public char[][] getBoard() {
        return board;
    }

    public void reset() {
        board = new char[3][3];
        startingPlayer = (startingPlayer == 'X') ? 'O' : 'X';
        currentPlayer = startingPlayer;
        gameOver = false;
    }

    public boolean checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (checkRow(i) || checkCol(i)) return true;
        }
        return checkDiagonals();
    }

    private boolean checkRow(int row) {
        return board[row][0] != '\0' &&
               board[row][0] == board[row][1] &&
               board[row][1] == board[row][2];
    }

    private boolean checkCol(int col) {
        return board[0][col] != '\0' &&
               board[0][col] == board[1][col] &&
               board[1][col] == board[2][col];
    }

    private boolean checkDiagonals() {
        return (board[0][0] != '\0' &&
                board[0][0] == board[1][1] &&
                board[1][1] == board[2][2]) ||
               (board[0][2] != '\0' &&
                board[0][2] == board[1][1] &&
                board[1][1] == board[2][0]);
    }

    private boolean isBoardFull() {
        for (char[] row : board)
            for (char cell : row)
                if (cell == '\0') return false;
        return true;
    }
}
