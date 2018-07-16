package main;

public class Board {
    private int size;
    private Button[][] board;

    public Board(int size) {
        this.size = size;
        board = new Button[size][size];

        initBoard();
    }

    private void initBoard() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                board[i][j] = new Button(i, j);
            }
    }

    public Button getButton(int row, int column) { return board[row][column]; }

}
