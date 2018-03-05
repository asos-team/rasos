package rasos;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class GameEndCheckerTest {
    @Rule
    public final ExpectedException expectedEx = ExpectedException.none();
    private GameEndChecker gameEndChecker;
    private Board board;

    @Before
    public void setUp() {
        gameEndChecker = new GameEndChecker();
        board = new Board(5);
    }

    @Test
    public void emptyBoardIsEndingTheGame() {
        assertTrue("Game should end when the board is empty.", isEndOfGame());
    }

    @Test
    public void gameIsContinuedWhenBoardIsNotEmpty() {
        board.populateHomeBases(11, 1, 2);
        assertFalse("Game should continue when the board is not empty.", isEndOfGame());
    }

    @Test
    public void gameIsEndedWhenOnePlayerDoesNotHaveSoldiersLeft() {
        board.cellAt(2, 4).setValues(1, 9);
        assertTrue("Game should end when a player does not have soldiers.", isEndOfGame());
    }

    @Test
    public void emptyBoardHasNoWinner() {
        assertEquals("WinnerId should be zero for an empty board.", 0, winner());
    }

    @Test
    public void whenAPlayerDoesNotHaveSoldiersLeftHeLoses() {
        int id = 2;
        board.cellAt(3, 1).setValues(id, 12);
        assertEquals(String.format("WinnerId should be %d.", id), id, winner());
    }

    @Test
    public void getWinnerIdReturnsZeroForATie() {
        board.populateHomeBases(676, 1, 2);
        assertEquals("WinnerId should be zero for a tie.", 0, winner());
    }

    @Test
    public void getWinnerIdReturnsTheLeadingPlayerId() {
        int id = 2;
        board.populateHomeBases(43, 1, 2);
        board.cellAt(4, 5).setValues(id, 60);
        assertEquals(String.format("WinnerId should be %d.", id), id, winner());
    }

    private boolean isEndOfGame() {
        return gameEndChecker.isEndOfGame(board);
    }

    private int winner() {
        return gameEndChecker.getWinnerId(board);
    }
}