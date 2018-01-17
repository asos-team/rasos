public class BoardUtils {
    static Cell[][] getDefaultBoard(int width, int height) {
        Cell[][] board = new Cell[width][height];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Cell(0, 0);
            }
        }
        board[0][0] = new Cell(1, 20);
        board[width - 1][height - 1] = new Cell(2, 20);
        return board;
    }
}
