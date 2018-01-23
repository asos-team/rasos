import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ReinforcementTest {

    private static final int NO_SOLDIERS = 0;
    private int boardDim;
    private Player player1;
    private Player player2;

    @Before
    public void setUp() {
        boardDim = 3;
        player1 = mock(Player.class);
        player2 = mock(Player.class);
    }

    @Test
    public void callsPlayerOnReinforcementWithGameBoard() {
        Game game = new Game(boardDim, NO_SOLDIERS, player1, player2);

        game.start();

        verify(player1).onReinforcement(eq(game.getBoard()), any(int.class));
        verify(player2).onReinforcement(eq(game.getBoard()), any(int.class));
    }

    @Test
    public void callsPlayerOnReinforcementWithNumberOfSoldiers() {
        Game game = new Game(boardDim, NO_SOLDIERS, player1, player2);

        makePlayer1ControlTotalOf_3_Cells(game);
        makePlayer2ControlTotalOf_2_Cells(game);

        game.start();

        verify(player1).onReinforcement(any(Board.class), eq(3));
        verify(player2).onReinforcement(any(Board.class), eq(2));
    }

    @Test
    public void appliesReinforcementSimplestCase() {
        int soldiers = 13;
        Game game = new Game(boardDim, NO_SOLDIERS, player1, player2);
        game.getBoard().setCell(2, 3, new Cell(2, soldiers));
        when(player2.onReinforcement(any(Board.class), any(int.class)))
                .thenReturn(Lists.newArrayList(new ReinforcementMove(2, 3, 1)));

        game.start();

        Board board = game.getBoard();
        TestUtils.assertCellContents(board.getCell(2, 3), 2, soldiers + 1);
    }

    private void makePlayer1ControlTotalOf_3_Cells(Game game) {
        game.getBoard().setCell(1, 1, new Cell(1, 4));
        game.getBoard().setCell(1, 3, new Cell(1, 4));
        game.getBoard().setCell(3, 1, new Cell(1, 19));
    }

    private void makePlayer2ControlTotalOf_2_Cells(Game game) {
        game.getBoard().setCell(2, 2, new Cell(2, 2));
        game.getBoard().setCell(3, 2, new Cell(2, 2));
    }
}