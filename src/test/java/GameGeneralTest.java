import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class GameGeneralTest {

    @Test
    public void getsBoardDimensions() {
        int boardDim = 7;
        Game game = new Game(boardDim, mock(Player.class), mock(Player.class));
        assertThat(game.getBoard().getDim(), is(boardDim));
    }

    @Test
    public void initializeWithExistingBoard() {
        Board board = createSomeInitializedBoard();
        Game game = createGame(board);
        assertThat(game.getBoard(), is(board));
    }

    @Test(expected = RuntimeException.class)
    public void throwsOnBoardContainingNulls() {
        Board board = createSomeInitializedBoard();
        board.setCell(3, 5, null);
        createGame(board);
    }

    private Board createSomeInitializedBoard() {
        Board board = new Board(7);
        board.populateHomeBases(15);
        return board;
    }

    private Game createGame(Board board) {
        return new Game(board, mock(Player.class), mock(Player.class));
    }
}