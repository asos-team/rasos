import org.junit.Test;

public class BoardUtilsTest {

    @Test
    public void boardInitializesEmptyWithSoldiersOnStartPoints() {
        int boardDim = 5;
        Cell[][] board = BoardUtils.getDefaultBoard(boardDim, boardDim);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Cell cell = board[i][j];
                if (i == 0 && j == 0) {
                    TestUtils.assertCellContents(cell, 1, 20);
                } else if (i == boardDim - 1 && j == boardDim - 1) {
                    TestUtils.assertCellContents(cell, 2, 20);
                } else {
                    TestUtils.assertCellContents(cell, 0, 0);
                }
            }
        }
    }

}