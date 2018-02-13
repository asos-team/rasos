package rasos;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class BoardTest {

    private Board board;
    private int dim;

    @Before
    public void setUp() {
        dim = 7;
        board = new Board(dim);
    }

    @Test
    public void retrievesDimension() {
        assertEquals(dim, board.getDim());
    }

    @Test
    public void retrievesCellsByCoordinates() {
        board.cellAt(2, 4).setValues(777, 777);

        TestUtils.assertCellContents(board.cellAt(2, 4), 777, 777);
    }

    @Test
    public void boardInitializesNeutral() {
        for (int i = 1; i <= dim; i++) {
            for (int j = 1; j <= dim; j++) {
                assertTrue(board.cellAt(i, j).isNeutral());
            }
        }
    }

    @Test
    public void retrievesHome1Cell() {
        board.cellAt(1, 1).setValues(1, 7);

        TestUtils.assertCellContents(board.getHome1Cell(), 1, 7);
    }

    @Test
    public void retrievesHome2Cell() {
        board.cellAt(dim, dim).setValues(78, 11);

        TestUtils.assertCellContents(board.getHome2Cell(), 78, 11);
    }

    @Test
    public void populatesHomeBases() {
        int soldiers = 30;

        board.populateHomeBases(soldiers);

        TestUtils.assertCellContents(board.getHome1Cell(), 1, soldiers);
        TestUtils.assertCellContents(board.getHome2Cell(), 2, soldiers);
    }

    @Test
    public void populateHomeBasesReturnsBoardWithNeutralHomes() {
        board.populateHomeBases(17);
        board.populateHomeBases(0);

        assertTrue("Home base A should be neutral", board.getHome1Cell().isNeutral());
        assertTrue("Home base B should be neutral", board.getHome2Cell().isNeutral());
    }

    @Test
    public void getPlayerCellCount() {
        board.cellAt(2, 4).setValues(7, 12);
        board.cellAt(6, 1).setValues(7, 57);
        board.cellAt(3, 7).setValues(7, 7);

        assertEquals(3, board.getPlayerCellCount(7));
    }

    @Test
    public void toStringHumanReadable() {
        Board b = new Board(2);
        b.populateHomeBases(10);

        String toString = b.toString();

        assertThat(toString, is("[10,1][0,0]" + System.lineSeparator() + "[0,0][10,2]"));
    }
}