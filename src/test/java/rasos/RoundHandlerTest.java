package rasos;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class RoundHandlerTest {

    private static final int PLAYER_A_ID = 1;
    private static final int PLAYER_B_ID = 2;

    private Board board;
    private Player playerA;
    private Player playerB;
    private Reinforcer reinforcer;
    private Attacker attacker;
    private RoundHandler roundHandler;
    private RiskLogger logger;
    private ExecutorService executor;

    @Before
    public void setUp() {
        board = new Board(7);
        playerA = mock(Player.class);
        playerB = mock(Player.class);
        when(playerA.getPlayerId()).thenReturn(PLAYER_A_ID);
        when(playerB.getPlayerId()).thenReturn(PLAYER_B_ID);
        reinforcer = mock(Reinforcer.class);
        attacker = mock(Attacker.class);
        executor = Executors.newSingleThreadExecutor();
        logger = mock(RiskLogger.class);
        roundHandler = getRoundHandler();
    }

    @Test
    public void assignPlayerIds() {
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(playerA).setPlayerId(captor.capture());
        verify(playerB).setPlayerId(captor.capture());

        List<Integer> values = captor.getAllValues();
        assertNotEquals("Players should have different ids", values.get(0), values.get(1));
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

    @Test(timeout = 100)
    public void preventTooLongReinforcementComputation() throws InterruptedException, ExecutionException, TimeoutException {
        executor = stubExecutorWithImmediateThrowingFuture();
        playerB = stubPlayerWithInfiniteLoopOnReinforcement();
        roundHandler = getRoundHandler();

        roundHandler.playOneRound(board);

        verify(reinforcer, times(2)).apply(any(Board.class), eq(Collections.emptyList()), anyInt(), anyInt());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void whenPlayerReturnsNullInOnReinforcementUseEmptyListInstead() {
        roundHandler.playOneRound(board);

        verify(reinforcer, times(2)).apply(any(Board.class), eq(Collections.emptyList()), anyInt(), anyInt());
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

        verify(reinforcer, times(2)).apply(any(Board.class), eq(Collections.emptyList()), anyInt(), anyInt());
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

        verify(reinforcer).apply(board, movesA, 3, PLAYER_A_ID);
        verify(reinforcer).apply(board, movesB, 2, PLAYER_B_ID);
    }

    @Test
    public void callsPlayerOnAttackWithGameBoard() {
        roundHandler.playOneRound(board);

        verify(playerA).onAttack(board);
        verify(playerB).onAttack(board);
    }

    @Test(timeout = 100)
    public void preventTooLongAttackComputation() throws InterruptedException, ExecutionException, TimeoutException {
        executor = stubExecutorWithImmediateThrowingFuture();
        playerB = stubPlayerWithInfiniteLoopOnAttack();
        roundHandler = getRoundHandler();

        roundHandler.playOneRound(board);

        //noinspection unchecked
        verify(attacker).apply(any(Board.class), eq(Collections.emptyList()), eq(Collections.emptyList()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void whenPlayerReturnsNullInOnAttackUseEmptyListInstead() {
        roundHandler.playOneRound(board);

        verify(attacker).apply(any(Board.class), eq(Collections.emptyList()), eq(Collections.emptyList()));
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
    public void reinforcementHappensBeforeAttack() {
        InOrder inOrder = inOrder(reinforcer, attacker);

        roundHandler.playOneRound(board);

        inOrder.verify(reinforcer, atLeastOnce()).apply(any(Board.class), any(Iterable.class), anyInt(), anyInt());
        inOrder.verify(attacker, atLeastOnce()).apply(any(Board.class), anyVararg());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void loggerLogsOnRoundStart() {
        InOrder inOrder = inOrder(logger, reinforcer);

        roundHandler.playOneRound(board);

        inOrder.verify(logger).logRoundStart();
        inOrder.verify(reinforcer, atLeastOnce()).apply(any(Board.class), any(Iterable.class), anyInt(), anyInt());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void loggerLogsOnRoundEnd() {
        InOrder inOrder = inOrder(logger, attacker);

        roundHandler.playOneRound(board);

        inOrder.verify(attacker, atLeastOnce()).apply(any(Board.class), anyVararg());
        inOrder.verify(logger).logRoundEnd(board);
    }

    private RoundHandler getRoundHandler() {
        return new RoundHandler(playerA, playerB, reinforcer, attacker, executor, logger);
    }

    private void makePlayerAControlTotalOf_3_Cells() {
        board.cellAt(1, 1).setValues(PLAYER_A_ID, 4);
        board.cellAt(1, 3).setValues(PLAYER_A_ID, 4);
        board.cellAt(3, 1).setValues(PLAYER_A_ID, 19);
    }

    private void makePlayerBControlTotalOf_2_Cells() {
        board.cellAt(2, 2).setValues(PLAYER_B_ID, 2);
        board.cellAt(3, 2).setValues(PLAYER_B_ID, 2);
    }

    private ExecutorService stubExecutorWithImmediateThrowingFuture() throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executor = mock(ExecutorService.class);
        Future future = mock(Future.class);
        when(future.get(anyLong(), any(TimeUnit.class))).thenThrow(new TimeoutException());
        //noinspection unchecked
        when(executor.submit(any(Runnable.class))).thenReturn(future);
        return executor;
    }

    private Player stubPlayerWithInfiniteLoopOnReinforcement() {
        Player player = mock(Player.class);
        when(player.onReinforcement(any(Board.class), anyInt())).then(invocation -> {
            //noinspection InfiniteLoopStatement,StatementWithEmptyBody
            while (true) ;
        });
        return player;
    }

    private Player stubPlayerWithInfiniteLoopOnAttack() {
        Player player = mock(Player.class);
        when(player.onAttack(any(Board.class))).then(invocation -> {
            //noinspection InfiniteLoopStatement,StatementWithEmptyBody
            while (true) ;
        });
        return player;
    }
}
