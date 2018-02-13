package rasos;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameEndCheckerTest {

    @Test
    public void emptyBoardIsEndingTheGame() {
        assertTrue("Game should end when the board is empty.", new GameEndChecker().isEndOfGame(new Board(5)));
    }

    @Test
    @Ignore
    public void gameIsContinuedWhenBoardIsNotEmpty() {
        assertFalse("Game should continue when the board is not empty.", new GameEndChecker().isEndOfGame(new Board(5)));
    }
}