import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BoardTest {

    @Test
    public void retrieveDimension() throws Exception {
        assertEquals(6, new Board(6).getDim());
    }

    @Test
    public void boardInitializesEmpty() {
        int dim = 5;
        Board board = new Board(dim);
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                assertTrue(board.getCell(i, j).isEmpty());
            }
        }
    }

    @Test
    public void populateHomeBases() {
        int dim = 5;
        int soldiers = 30;
        Board board = new Board(dim);

        board.populateHomeBases(soldiers);

        TestUtils.assertCellContents(board.getCell(0, 0), 1, soldiers);
        TestUtils.assertCellContents(board.getCell(dim - 1, dim - 1), 2, soldiers);
    }
}