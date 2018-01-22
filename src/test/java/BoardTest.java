import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BoardTest {

    private Board board;
    private int dim;

    @Before
    public void setUp() throws Exception {
        dim = 7;
        board = new Board(dim);
    }

    @Test
    public void retrievesDimension() throws Exception {
        assertEquals(dim, board.getDim());
    }

    @Test
    public void boardInitializesEmpty() {
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                assertTrue(board.getCell(i, j).isNeutral());
            }
        }
    }

    @Test
    public void retrievesHome1Cell() throws Exception {
        board.setCell(0, 0, new Cell(1, 7));

        TestUtils.assertCellContents(board.getHome1Cell(), 1, 7);
    }

    @Test
    public void retrievesHome2Cell() throws Exception {
        board.setCell(dim - 1, dim - 1, new Cell(78, 11));

        TestUtils.assertCellContents(board.getHome2Cell(), 78, 11);
    }

    @Test
    public void populatesHomeBases() {
        int soldiers = 30;

        board.populateHomeBases(soldiers);

        TestUtils.assertCellContents(board.getHome1Cell(), 1, soldiers);
        TestUtils.assertCellContents(board.getHome2Cell(), 2, soldiers);
    }

    @Test(expected = RuntimeException.class)
    public void cannotSetCellsToNull() {
        board.setCell(3, 2, null);
    }

    @Test
    public void getPlayerCellCount() throws Exception {
        board.setCell(1, 3, new Cell(7, 12));
        board.setCell(5, 0, new Cell(7, 57));
        board.setCell(2, 6, new Cell(7, 7));

        // Should be neutral according to Cell's contract
        board.setCell(2, 1, new Cell(7, 0));

        assertEquals(3, board.getPlayerCellCount(7));
    }
}