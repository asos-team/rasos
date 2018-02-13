package rasos;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class GameEndCheckerTest {
    @Test
    public void emptyBoardIsEndingTheGame() {
        assertTrue("Game should end when the board is empty.", new GameEndChecker().isEndOfGame(new Board(5)));
    }
}