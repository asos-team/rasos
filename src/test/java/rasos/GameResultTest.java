package rasos;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GameResultTest {

    public static final int BOARD_DIM = 2;
    private Board board;
    private GameResult result;
    private int idA;
    private int idB;

    @Before
    public void setUp() throws Exception {
        board = new Board(BOARD_DIM);
        idA = 1;
        idB = 2;
        result = new GameResult(board, idA, idB);
    }

    @Test
    public void zeroScoreForEmptyBoard() {
        assertThat(result.getScore(idA), is(0));
        assertThat(result.getScore(idB), is(0));
    }

    @Test(expected = RuntimeException.class)
    public void throwsOnUnknownPlayerId() {
        result.getScore(10);
    }

    @Test
    public void totalDemolitionIsWorth11DimSquarePlusSoldiersPoints() {
        int soldiersCount = 1;
        board.cellAt(1, 1).setValues(idA, soldiersCount);
        assertThat(result.getScore(idA), is(11 * BOARD_DIM * BOARD_DIM + soldiersCount));
        assertThat(result.getScore(idB), is(0));
    }

    @Test
    public void cellAndSoldiersTieIsWorth0ToBoth() {
        board.populateHomeBases(10, idA, idB);

        assertThat(result.getScore(idA), is(0));
        assertThat(result.getScore(idB), is(0));
    }

    @Test
    public void playerWithMoreCellsGets10TimesCells_otherGetsNothing() {
        board.populateHomeBases(10, idA, idB);
        board.cellAt(1, 2).setValues(idB, 10);

        assertThat(result.getScore(idA), is(0));
        assertThat(result.getScore(idB), is(20));
    }

    @Test
    public void tieIsBrokenBySoldiersCount() {
        board.cellAt(1, 1).setValues(idA, 10);
        board.cellAt(2, 2).setValues(idB, 11);

        assertThat(result.getScore(idA), is(0));
        assertThat(result.getScore(idB), is(10));
    }
}