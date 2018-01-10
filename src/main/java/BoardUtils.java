import javafx.util.Pair;

public class BoardUtils {
    static Pair<Integer, Integer>[][] getBlankBoard(int width, int height) {
        Pair[][] board = new Pair[width][height];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Pair<Integer, Integer>(0, 0);
            }
        }
        board[0][0] = new Pair<Integer, Integer>(1, 20);
        board[width - 1][height - 1] = new Pair<Integer, Integer>(2, 20);
        return board;
    }
}
