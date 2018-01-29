import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class GameInitTest {

    @Test
    public void initializesWithSpecifiedDimension() {
        int boardDim = 7;
        Game game = new Game(boardDim, 20, mock(Player.class), mock(Player.class));
        assertThat(game.getBoard().getDim(), is(boardDim));
    }

    @Test
    public void initializeWithExistingBoard() {
        Board board = new Board(7);
        board.populateHomeBases(15);
        Game game = createGame(board);
        assertThat(game.getBoard(), is(board));
    }

    @Test
    public void populatesHomeBasesWithSpecifiedNumberOfSoldiers() throws Exception {
        int soldiers = 99;
        Game game = new Game(7, soldiers, mock(Player.class), mock(Player.class));

        TestUtils.assertCellContents(game.getBoard().getHome1Cell(), 1, soldiers);
        TestUtils.assertCellContents(game.getBoard().getHome2Cell(), 2, soldiers);
    }

    @Test(expected = RuntimeException.class)
    public void throwsWhenInitializedWithNullBoard() throws Exception {
        createGame(null);
    }

    private Game createGame(Board board) {
        return new Game(board, mock(Player.class), mock(Player.class), new ReinforcementHandler(), new Attacker());
    }
}