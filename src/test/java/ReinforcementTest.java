import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ReinforcementTest {

    private static final int boardDim = 2;
    private Game game;
    private Player player1;
    private Player player2;

    @Before
    public void setUp() {
        player1 = mock(Player.class);
        player2 = mock(Player.class);
        when(player1.onReinforcement(any(Board.class), any(int.class))).thenReturn(Lists.<ReinforcementMove>newArrayList());
        when(player2.onReinforcement(any(Board.class), any(int.class))).thenReturn(Lists.<ReinforcementMove>newArrayList());
        when(player1.onAttack(any(Board.class))).thenReturn(Lists.<AttackMove>newArrayList());
        when(player2.onAttack(any(Board.class))).thenReturn(Lists.<AttackMove>newArrayList());
        game = new Game(boardDim, player1, player2);
    }

    @Test
    public void callsPlayerOnReinforcementWithGameBoard() {
        game.tick();

        verify(player1).onReinforcement(eq(game.getBoard()), any(int.class));
        verify(player2).onReinforcement(eq(game.getBoard()), any(int.class));
    }

    @Test
    public void callsPlayerOnReinforcementWithSuitableNumberOfSoldiers() {
        game.getBoard().setCell(0, 1, new Cell(1, 4));

        game.tick();

        verify(player1).onReinforcement(any(Board.class), eq(2));
        verify(player2).onReinforcement(any(Board.class), eq(1));
    }

    @Test
    public void appliesReinforcement() {
        when(player1.onReinforcement(any(Board.class), any(int.class)))
                .thenReturn(Lists.newArrayList(new ReinforcementMove(0, 0, 1)));
        when(player2.onReinforcement(any(Board.class), any(int.class)))
                .thenReturn(Lists.newArrayList(new ReinforcementMove(boardDim - 1, boardDim - 1, 1)));

        game.tick();

        Board board = game.getBoard();
        TestUtils.assertCellContents(board.getHome1Cell(), 1, 21);
        TestUtils.assertCellContents(board.getHome2Cell(), 2, 21);
    }
}