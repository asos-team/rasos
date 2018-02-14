package rasos;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameEndCheckerTest {

    @Test
    public void emptyBoardIsEndingTheGame() {
        assertTrue("Game should end when the board is empty.", new GameEndChecker().isEndOfGame(new Board(5)));
    }

    @Test
    public void gameIsContinuedWhenBoardIsNotEmpty() {
        Board board = new Board(5);
        board.populateHomeBases(11);
        assertFalse("Game should continue when the board is not empty.", new GameEndChecker().isEndOfGame(board));
    }

    @Test
    public void gameIsEndedWhenOnePlayerDoesNotHaveSoldiersLeft() {
        Board board = new Board(5);
        board.cellAt(2, 4).setValues(1, 9);
        assertTrue("Game should end when a player does not have soldiers.", new GameEndChecker().isEndOfGame(board));
    }

    @Test
    public void emptyBoardHasNoWinner() {
        assertEquals("WinnerId should be zero for an empty board.", 0, new GameEndChecker().getWinnerId(new Board(5)));
    }
}