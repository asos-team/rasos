import org.junit.Test;

public class BoardTest {

    @Test
    public void boardInitializesEmptyWithSoldiersOnStartPoints() {
        int boardDim = 5;
        Board board = new Board(boardDim);
        int initialNumSoldiers = 20;
        board.populateHomeBases(initialNumSoldiers);
        for (int i = 0; i < boardDim; i++) {
            for (int j = 0; j < boardDim; j++) {
                Cell cell = board.getCell(i, j);
                if (i == 0 && j == 0) {
                    TestUtils.assertCellContents(cell, 1, initialNumSoldiers);
                } else if (i == boardDim - 1 && j == boardDim - 1) {
                    TestUtils.assertCellContents(cell, 2, initialNumSoldiers);
                } else {
                    TestUtils.assertCellContents(cell, 0, 0);
                }
            }
        }
    }

}