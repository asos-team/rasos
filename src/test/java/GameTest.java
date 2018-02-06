import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class GameTest {

    private static final int NO_SOLDIERS = 0;
    private int boardDim;

    @Before
    public void setUp() throws Exception {
        boardDim = 7;
    }

    @Test
    public void initializesWithSpecifiedDimension() {
        int boardDim = 7;
        Game game = new Game(boardDim, 20, mock(Player.class), mock(Player.class), new Attacker(), new Reinforcer());
        assertThat(game.getBoard().getDim(), is(boardDim));
    }

    @Test
    public void populatesHomeBasesWithSpecifiedNumberOfSoldiers() throws Exception {
        int soldiers = 99;
        Game game = new Game(7, soldiers, mock(Player.class), mock(Player.class), new Attacker(), new Reinforcer());

        TestUtils.assertCellContents(game.getBoard().getHome1Cell(), 1, soldiers);
        TestUtils.assertCellContents(game.getBoard().getHome2Cell(), 2, soldiers);
    }

    @Test
    public void callsPlayerOnReinforcementWithGameBoard() {

        Player playerA = mock(Player.class);
        Player playerB = mock(Player.class);
        Game game = new Game(boardDim, NO_SOLDIERS, playerA, playerB, new Attacker(), new Reinforcer());

        game.start();

        verify(playerA).onReinforcement(eq(game.getBoard()), any(int.class));
        verify(playerB).onReinforcement(eq(game.getBoard()), any(int.class));
    }

    @Test
    public void callsPlayerOnReinforcementWithNumberOfSoldiers() {
        Player playerA = mock(Player.class);
        Player playerB = mock(Player.class);
        Game game = new Game(boardDim, NO_SOLDIERS, playerA, playerB, new Attacker(), new Reinforcer());
        makePlayerAControlTotalOf_3_Cells(game);
        makePlayerBControlTotalOf_2_Cells(game);

        game.start();

        verify(playerA).onReinforcement(any(Board.class), eq(3));
        verify(playerB).onReinforcement(any(Board.class), eq(2));
    }

    @Test
    public void callsPlayerOnAttackWithGameBoard() {
        Player playerA = mock(Player.class);
        Player playerB = mock(Player.class);
        Game game = new Game(2, 20, playerA, playerB, new Attacker(), new Reinforcer());

        game.start();

        verify(playerA).onAttack(game.getBoard());
        verify(playerB).onAttack(game.getBoard());
    }

    @Test
    public void callsAttackerWithAttackMoves() {
        Player playerA = mock(Player.class);
        Player playerB = mock(Player.class);
        Attacker attacker = mock(Attacker.class);

        AttackMove am1 = mock(AttackMove.class);
        AttackMove am2 = mock(AttackMove.class);
        AttackMove am3 = mock(AttackMove.class);
        AttackMove am4 = mock(AttackMove.class);
        ArrayList<AttackMove> movesA = Lists.newArrayList(am1, am2);
        when(playerA.onAttack(any(Board.class))).thenReturn(movesA);
        ArrayList<AttackMove> movesB = Lists.newArrayList(am3, am4);
        when(playerB.onAttack(any(Board.class))).thenReturn(movesB);
        Game game = new Game(2, 20, playerA, playerB, attacker, new Reinforcer());

        game.start();

        verify(attacker).apply(game.getBoard(), movesA, movesB);
    }

    private void makePlayerAControlTotalOf_3_Cells(Game game) {
        game.getBoard().setCell(1, 1, new Cell(1, 4));
        game.getBoard().setCell(1, 3, new Cell(1, 4));
        game.getBoard().setCell(3, 1, new Cell(1, 19));
    }

    private void makePlayerBControlTotalOf_2_Cells(Game game) {
        game.getBoard().setCell(2, 2, new Cell(2, 2));
        game.getBoard().setCell(3, 2, new Cell(2, 2));
    }
}