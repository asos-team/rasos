package rasos;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class GameEndCheckerTest {

    private static final int ID_A = 117;
    private static final int ID_B = 66;

    @Rule
    public final ExpectedException expectedEx = ExpectedException.none();

    private GameEndChecker gameEndChecker;
    private Board board;

    @Before
    public void setUp() {
        gameEndChecker = new GameEndChecker(ID_A, ID_B);
        board = new Board(5);
    }

    @Test
    public void emptyBoardIsEndingTheGame() {
        assertTrue("Game should end when the board is empty.", isEndOfGame());
    }

    @Test
    public void gameIsContinuedWhenBoardIsNotEmpty() {
        board.populateHomeBases(11, ID_A, ID_B);
        assertFalse("Game should continue when the board is not empty.", isEndOfGame());
    }

    @Test
    public void gameIsEndedWhenOnePlayerDoesNotHaveSoldiersLeft() {
        board.cellAt(2, 4).setValues(ID_A, 9);
        assertTrue("Game should end when a player does not have soldiers.", isEndOfGame());
    }

    @Test
    public void emptyBoardHasNoWinner() {
        assertEquals("WinnerId should be zero for an empty board.", 0, winner());
    }

    @Test
    public void whenAPlayerDoesNotHaveSoldiersLeftHeLoses() {
        board.cellAt(3, 1).setValues(ID_B, 12);
        assertEquals(String.format("WinnerId should be %d.", ID_B), ID_B, winner());
    }

    @Test
    public void getWinnerIdReturnsZeroForATie() {
        board.populateHomeBases(676, ID_A, ID_B);
        assertEquals("WinnerId should be zero for a tie.", 0, winner());
    }

    @Test
    public void getWinnerIdReturnsTheLeadingPlayerId() {
        board.populateHomeBases(43, ID_A, ID_B);
        board.cellAt(4, 5).setValues(ID_B, 60);
        assertEquals(String.format("WinnerId should be %d.", ID_B), ID_B, winner());
    }

    private boolean isEndOfGame() {
        return gameEndChecker.isEndOfGame(board);
    }

    private int winner() {
        return gameEndChecker.getWinnerId(board);
    }
}