import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ReinforcementTest {

    private int boardDim;
    private Player player1;
    private Player player2;
    private Game game;

    @Before
    public void setUp() {
        boardDim = 3;
        player1 = mock(Player.class);
        player2 = mock(Player.class);
        game = new Game(boardDim, player1, player2);
    }

    @Test
    public void callsPlayerOnReinforcementWithGameBoard() {
        game.start();

        verify(player1).onReinforcement(eq(game.getBoard()), any(int.class));
        verify(player2).onReinforcement(eq(game.getBoard()), any(int.class));
    }

    @Test
    public void callsPlayerOnReinforcementWithNumberOfSoldiers() {
        makePlayer1ControlTotalOf_3_Cells();
        makePlayer2ControlTotalOf_2_Cells();

        game.start();

        verify(player1).onReinforcement(any(Board.class), eq(3));
        verify(player2).onReinforcement(any(Board.class), eq(2));
    }

    @Test
    public void appliesReinforcementSimplestCase() {
        when(player1.onReinforcement(any(Board.class), any(int.class)))
                .thenReturn(Lists.newArrayList(new ReinforcementMove(1, 1, 1)));
        when(player2.onReinforcement(any(Board.class), any(int.class)))
                .thenReturn(Lists.newArrayList(new ReinforcementMove(boardDim, boardDim, 1)));

        game.start();

        Board board = game.getBoard();
        TestUtils.assertCellContents(board.getHome1Cell(), 1, 21);
        TestUtils.assertCellContents(board.getHome2Cell(), 2, 21);
    }

    private void makePlayer2ControlTotalOf_2_Cells() {
        game.getBoard().setCell(3, 2, new Cell(2, 2));
    }

    private void makePlayer1ControlTotalOf_3_Cells() {
        game.getBoard().setCell(1, 2, new Cell(1, 4));
        game.getBoard().setCell(3, 1, new Cell(1, 19));
    }
}