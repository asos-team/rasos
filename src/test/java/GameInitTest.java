import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class GameInitTest {

    @Test
    public void initializeBySpecifyingDimension() {
        int boardDim = 7;
        Game game = new Game(boardDim, mock(Player.class), mock(Player.class));
        assertThat(game.getBoard().getDim(), is(boardDim));
    }

    @Test
    public void initializeWithExistingBoard() {
        Board board = new Board(7);
        board.populateHomeBases(15);
        Game game = createGame(board);
        assertThat(game.getBoard(), is(board));
    }

    @Test(expected = RuntimeException.class)
    public void throwsWhenInitializedWithNullBoard() throws Exception {
        createGame(null);
    }

    private Game createGame(Board board) {
        return new Game(board, mock(Player.class), mock(Player.class));
    }
}