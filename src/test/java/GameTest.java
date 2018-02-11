import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class GameTest {

    private static final int NO_SOLDIERS = 0;
    private int boardDim;
    private Player playerA;
    private Player playerB;
    private RiskLogger logger;

    @Before
    public void setUp() {
        boardDim = 7;
        playerA = mock(Player.class);
        playerB = mock(Player.class);
        logger = mock(RiskLogger.class);
    }

    @Test
    public void initializesWithSpecifiedDimension() {
        Game game = createSimpleGame();
        assertThat(game.getBoard().getDim(), is(boardDim));
    }

    @Test
    public void populatesHomeBasesWithSpecifiedNumberOfSoldiers() {
        int soldiers = 99;
        Game game = createSimpleGame(soldiers);

        TestUtils.assertCellContents(game.getBoard().getHome1Cell(), 1, soldiers);
        TestUtils.assertCellContents(game.getBoard().getHome2Cell(), 2, soldiers);
    }

    @Test
    public void callsPlayerOnReinforcementWithGameBoard() {
        Game game = createSimpleGame();

        game.start();

        verify(playerA).onReinforcement(eq(game.getBoard()), any(int.class));
        verify(playerB).onReinforcement(eq(game.getBoard()), any(int.class));
    }

    @Test
    public void callsPlayerOnReinforcementWithNumberOfSoldiers() {
        Game game = createSimpleGame();
        makePlayerAControlTotalOf_3_Cells(game);
        makePlayerBControlTotalOf_2_Cells(game);

        game.start();

        verify(playerA).onReinforcement(any(Board.class), eq(3));
        verify(playerB).onReinforcement(any(Board.class), eq(2));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void callsReinforcerWithReinforcementMoves() {
        Reinforcer reinforcer = mock(Reinforcer.class);

        Iterable<ReinforcementMove> movesA = mock(Iterable.class);
        Iterable<ReinforcementMove> movesB = mock(Iterable.class);
        when(playerA.onReinforcement(any(Board.class), any(int.class))).thenReturn(movesA);
        when(playerB.onReinforcement(any(Board.class), any(int.class))).thenReturn(movesB);

        Game game = createSimpleGame(reinforcer, new Attacker());
        makePlayerAControlTotalOf_3_Cells(game);
        makePlayerBControlTotalOf_2_Cells(game);

        game.start();

        verify(reinforcer).apply(game.getBoard(), movesA, 3, 1);
        verify(reinforcer).apply(game.getBoard(), movesB, 2, 2);
    }

    @Test
    public void callsPlayerOnAttackWithGameBoard() {
        Game game = createSimpleGame();

        game.start();

        verify(playerA).onAttack(game.getBoard());
        verify(playerB).onAttack(game.getBoard());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void callsAttackerWithAttackMoves() {
        Attacker attacker = mock(Attacker.class);

        Iterable<AttackMove> movesA = mock(Iterable.class);
        Iterable<AttackMove> movesB = mock(Iterable.class);
        when(playerA.onAttack(any(Board.class))).thenReturn(movesA);
        when(playerB.onAttack(any(Board.class))).thenReturn(movesB);

        Game game = createSimpleGame(new Reinforcer(), attacker);

        game.start();

        verify(attacker).apply(game.getBoard(), movesA, movesB);
    }

    @Test
    public void gameCallsLogStartOnMatchStart(){
        Game game = createSimpleGame();
        game.start();
        verify(logger).logStart();
    }

    private Game createSimpleGame() {
        return createSimpleGame(new Reinforcer(), new Attacker());
    }

    private Game createSimpleGame(int soldiers) {
        return createSimpleGame(new Reinforcer(), new Attacker(), soldiers);
    }

    private Game createSimpleGame(Reinforcer reinforcer, Attacker attacker) {
        return createSimpleGame(reinforcer, attacker, NO_SOLDIERS);
    }

    private Game createSimpleGame(Reinforcer reinforcer, Attacker attacker, int soldiers) {
        return new Game(boardDim, soldiers, playerA, playerB, attacker, reinforcer,logger);
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