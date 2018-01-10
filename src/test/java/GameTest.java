import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class GameTest {

    private static final int boardDim = 2;
    private Game game;
    private Player player1;
    private Player player2;

    @Before
    public void setUp() {
        player1 = mock(Player.class);
        player2 = mock(Player.class);
        game = new Game(boardDim, boardDim, player1, player2);
    }

    @Test
    public void getsBoardDimensions() {
        Pair<Integer, Integer>[][] board = game.getBoard();

        assertThat(board.length, is(boardDim));
        assertThat(board[0].length, is(boardDim));
    }

    @Test
    public void boardInitializesEmptyWithSoldiersOnStartPoints() {
        for (int i = 0; i < game.getBoard().length; i++) {
            for (int j = 0; j < game.getBoard()[i].length; j++) {
                Pair<Integer, Integer> cell = game.getBoard()[i][j];
                if (i == 0 && j == 0) {
                    assertThat(cell, is(new Pair<Integer, Integer>(1, 20)));
                } else if (i == boardDim - 1 && j == boardDim - 1) {
                    assertThat(cell, is(new Pair<Integer, Integer>(2, 20)));
                } else {
                    assertThat(cell, is(new Pair<Integer, Integer>(0, 0)));
                }
            }
        }
    }

    @Test
    public void callsPlayerOnReinforcementWithSuitableNumberOfSoldiers() {
        game.tick();
        verify(player1).onReinforcement(game.getBoard(), 1);
        verify(player2).onReinforcement(game.getBoard(), 1);
    }

    @Test
    public void appliesReinforcement() {
        when(player1.onReinforcement(any(Pair[][].class), any(int.class))).thenReturn(new ReinforcementMove(0, 0, 1));
        when(player2.onReinforcement(any(Pair[][].class), any(int.class))).thenReturn(new ReinforcementMove(4, 4, 1));
        game.tick();

        Pair<Integer, Integer>[][] board = game.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Pair<Integer, Integer> cell = board[i][j];
                if (i == 0 && j == 0) {
                    assertThat(cell, is(new Pair<Integer, Integer>(1, 21)));
                } else if (i == boardDim - 1 && j == boardDim - 1) {
                    assertThat(cell, is(new Pair<Integer, Integer>(2, 21)));
                } else {
                    assertThat(cell, is(new Pair<Integer, Integer>(0, 0)));
                }
            }
        }
    }

    @Test
    public void initializeWithSpecialBoardConfiguration() {
        Pair<Integer, Integer>[][] configuration = BoardUtils.getBlankBoard(2,2);
        Game g = new Game(configuration, player1, player2);
        assertThat(g.getBoard(), is(configuration));
    }

    @Test(expected = RuntimeException.class)
    public void throwOnUninitializedBoardConfiguration() {
        Pair<Integer, Integer>[][] configuration = new Pair[2][2];
        new Game(configuration, player1, player2);
    }

    @Test
    public void callsPlayerReinforcementOnSpecialBoardConfiguration() {
        Pair<Integer, Integer>[][] configuration = BoardUtils.getBlankBoard(2,2);
        configuration[0][0] = new Pair<Integer, Integer>(1, 10);
        configuration[0][1] = new Pair<Integer, Integer>(1, 10);
        configuration[1][1] = new Pair<Integer, Integer>(2, 10);
        Game g = new Game(configuration, player1, player2);

        g.tick();

        verify(player1).onReinforcement(configuration, 2);
        verify(player2).onReinforcement(configuration, 1);
    }
}