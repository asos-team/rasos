import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ReinforcementTest {

    private static final int NO_SOLDIERS = 0;
    @Rule
    public final ExpectedException expectedEx = ExpectedException.none();
    private Player playerA;
    private Player playerB;
    private Game game;

    @Before
    public void setUp() {
        int boardDim = 3;
        playerA = mock(Player.class);
        playerB = mock(Player.class);
        game = new Game(boardDim, NO_SOLDIERS, playerA, playerB, new Attacker(), new Reinforcer());
    }

    @Test
    public void callsPlayerOnReinforcementWithGameBoard() {
        game.start();

        verify(playerA).onReinforcement(eq(game.getBoard()), any(int.class));
        verify(playerB).onReinforcement(eq(game.getBoard()), any(int.class));
    }

    @Test
    public void callsPlayerOnReinforcementWithNumberOfSoldiers() {
        makePlayerAControlTotalOf_3_Cells();
        makePlayerBControlTotalOf_2_Cells();

        game.start();

        verify(playerA).onReinforcement(any(Board.class), eq(3));
        verify(playerB).onReinforcement(any(Board.class), eq(2));
    }

    private void makePlayerAControlTotalOf_3_Cells() {
        game.getBoard().setCell(1, 1, new Cell(1, 4));
        game.getBoard().setCell(1, 3, new Cell(1, 4));
        game.getBoard().setCell(3, 1, new Cell(1, 19));
    }

    private void makePlayerBControlTotalOf_2_Cells() {
        game.getBoard().setCell(2, 2, new Cell(2, 2));
        game.getBoard().setCell(3, 2, new Cell(2, 2));
    }
}