package rasos;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class RoundHandlerTest {
    private Board board;
    private Player playerA;
    private Player playerB;
    private Reinforcer reinforcer;
    private Attacker attacker;
    private RoundHandler roundHandler;
    private RiskLogger logger;

    @Before
    public void setUp() {
        logger = mock(RiskLogger.class);
        board = new Board(7);
        playerA = mock(Player.class);
        playerB = mock(Player.class);
        reinforcer = mock(Reinforcer.class);
        attacker = mock(Attacker.class);
        roundHandler = new RoundHandler(playerA, playerB, attacker, reinforcer, logger);
    }

    @Test
    public void assignPlayerIds() {
        verify(playerA).setPlayerId(1);
        verify(playerB).setPlayerId(2);
    }

    @Test
    public void callsPlayerOnReinforcementWithGameBoard() {
        roundHandler.playOneRound(board);

        verify(playerA).onReinforcement(eq(board), any(int.class));
        verify(playerB).onReinforcement(eq(board), any(int.class));
    }

    @Test
    public void callsPlayerOnReinforcementWithNumberOfSoldiers() {
        makePlayerAControlTotalOf_3_Cells();
        makePlayerBControlTotalOf_2_Cells();

        roundHandler.playOneRound(board);

        verify(playerA).onReinforcement(any(Board.class), eq(3));
        verify(playerB).onReinforcement(any(Board.class), eq(2));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void whenPlayerThrowsInOnReinforcementUseEmptyListInstead() {
        when(playerA.onReinforcement(any(Board.class), any(int.class)))
                .thenThrow(new RuntimeException("Bukchin is a shitty programmer"));

        try {
            roundHandler.playOneRound(board);
        } catch (Exception ignored) {
        }

        verify(reinforcer).apply(any(Board.class), eq(Collections.emptyList()), anyInt(), anyInt());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void callsReinforcerWithReinforcementMoves() {
        Iterable<ReinforcementMove> movesA = mock(Iterable.class);
        Iterable<ReinforcementMove> movesB = mock(Iterable.class);
        when(playerA.onReinforcement(any(Board.class), any(int.class))).thenReturn(movesA);
        when(playerB.onReinforcement(any(Board.class), any(int.class))).thenReturn(movesB);

        makePlayerAControlTotalOf_3_Cells();
        makePlayerBControlTotalOf_2_Cells();

        roundHandler.playOneRound(board);

        verify(reinforcer).apply(board, movesA, 3, 1);
        verify(reinforcer).apply(board, movesB, 2, 2);
    }

    @Test
    public void callsPlayerOnAttackWithGameBoard() {
        roundHandler.playOneRound(board);

        verify(playerA).onAttack(board);
        verify(playerB).onAttack(board);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void whenPlayerThrowsInOnAttackUseEmptyListInstead() {
        when(playerA.onAttack(any(Board.class))).thenReturn(Collections.emptyList());
        when(playerB.onAttack(any(Board.class))).thenThrow(new RuntimeException("Weiss is a shitty programmer"));

        try {
            roundHandler.playOneRound(board);
        } catch (Exception ignored) {
        }

        verify(attacker).apply(any(Board.class), eq(Collections.emptyList()), eq(Collections.emptyList()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void callsAttackerWithAttackMoves() {
        Iterable<AttackMove> movesA = mock(Iterable.class);
        Iterable<AttackMove> movesB = mock(Iterable.class);
        when(playerA.onAttack(any(Board.class))).thenReturn(movesA);
        when(playerB.onAttack(any(Board.class))).thenReturn(movesB);

        roundHandler.playOneRound(board);

        verify(attacker).apply(board, movesA, movesB);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void loggerLogsOnRoundStart() {
        roundHandler.playOneRound(board);
        InOrder inOrder = inOrder(logger, reinforcer);
        inOrder.verify(logger).logRoundStart();
        inOrder.verify(reinforcer, atLeastOnce()).apply(any(Board.class), any(Iterable.class), anyInt(), anyInt());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void loggerLogsOnRoundEnd() {
        roundHandler.playOneRound(board);
        InOrder inOrder = inOrder(logger, attacker);
        inOrder.verify(attacker, atLeastOnce()).apply(any(Board.class), anyVararg());
        inOrder.verify(logger).logRoundEnd(board);
    }

    private void makePlayerAControlTotalOf_3_Cells() {
        board.cellAt(1, 1).setValues(1, 4);
        board.cellAt(1, 3).setValues(1, 4);
        board.cellAt(3, 1).setValues(1, 19);
    }

    private void makePlayerBControlTotalOf_2_Cells() {
        board.cellAt(2, 2).setValues(2, 2);
        board.cellAt(3, 2).setValues(2, 2);
    }
}
