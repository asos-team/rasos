package rasos;

import com.google.common.collect.Iterables;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItems;
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
    public void isEmpty() {
        assertTrue("Board should be empty.", new Board(12).isEmpty());
    }

    @Test
    public void isNotEmpty() {
        Board board = new Board(12);
        board.populateHomeBases(33);
        assertFalse("Board shouldn't be empty.", board.isEmpty());
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
        b.cellAt(2, 1).setValues(1, 5);

        String toString = b.toString();

        String line1 = String.format("%s\t%s\t", b.getHome1Cell(), b.cellAt(2, 1));
        String line2 = String.format("%s\t%s", b.cellAt(1, 2), b.getHome2Cell());
        assertThat(toString, is(line1 + System.lineSeparator() + line2));
    }

    @Test
    public void retrievesPlayerControlledCellCoordinates() {
        board.populateHomeBases(20);
        board.cellAt(1, 2).setValues(1, 10);

        Iterable<CellCoordinates> player1Cells = board.getControlledCoordinates(1);

        CellCoordinates cc1 = new CellCoordinates(1, 1);
        CellCoordinates cc2 = new CellCoordinates(1, 2);

        assertThat(player1Cells, hasItems(cc1, cc2));
    }

    @Test
    public void retrievesEmptyCoordinatesListForNonExistingPlayer() {
        assertThat(Iterables.size(board.getControlledCoordinates(1)), is(0));
    }
}