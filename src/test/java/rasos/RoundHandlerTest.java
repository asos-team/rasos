package rasos;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.Collections;
import java.util.concurrent.*;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static rasos.Config.ID_A;
import static rasos.Config.ID_B;

public class RoundHandlerTest {

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
        when(playerA.getPlayerId()).thenReturn(ID_A);
        when(playerB.getPlayerId()).thenReturn(ID_B);
        reinforcer = mock(Reinforcer.class);
        attacker = mock(Attacker.class);
        executor = Executors.newSingleThreadExecutor();
        logger = mock(RiskLogger.class);
        roundHandler = getRoundHandler();
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

        verify(reinforcer, times(2)).apply(any(Board.class), anyInt(), eq(Collections.emptyList()), anyInt());
    }

    @Test
    public void whenPlayerReturnsNullInOnReinforcementUseEmptyListInstead() {
        roundHandler.playOneRound(board);

        verify(reinforcer, times(2)).apply(any(Board.class), anyInt(), eq(Collections.emptyList()), anyInt());
    }

    @Test
    public void whenPlayerThrowsInOnReinforcementUseEmptyListInstead() {
        when(playerA.onReinforcement(any(Board.class), any(int.class)))
                .thenThrow(new RuntimeException("Bukchin is a shitty programmer"));

        try {
            roundHandler.playOneRound(board);
        } catch (Exception ignored) {
        }

        verify(reinforcer, times(2)).apply(any(Board.class), anyInt(), eq(Collections.emptyList()), anyInt());
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

        verify(reinforcer).apply(board, ID_A, movesA, 3);
        verify(reinforcer).apply(board, ID_B, movesB, 2);
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
        verify(attacker).apply(any(Board.class), any(), any(), anyInt(), anyInt());
    }

    @Test
    public void whenPlayerReturnsNullInOnAttackUseEmptyListInstead() {
        roundHandler.playOneRound(board);

        verify(attacker).apply(any(Board.class), eq(Collections.emptyList()), eq(Collections.emptyList()), anyInt(), anyInt());
    }

    @Test
    public void whenPlayerThrowsInOnAttackUseEmptyListInstead() {
        when(playerA.onAttack(any(Board.class))).thenReturn(Collections.emptyList());
        when(playerB.onAttack(any(Board.class))).thenThrow(new RuntimeException("Weiss is a shitty programmer"));

        try {
            roundHandler.playOneRound(board);
        } catch (Exception ignored) {
        }

        verify(attacker).apply(any(Board.class), eq(Collections.emptyList()), eq(Collections.emptyList()), anyInt(), anyInt());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void callsAttackerWithAttackMovesAndIds() {
        Iterable<AttackMove> movesA = mock(Iterable.class);
        Iterable<AttackMove> movesB = mock(Iterable.class);
        when(playerA.onAttack(any(Board.class))).thenReturn(movesA);
        when(playerB.onAttack(any(Board.class))).thenReturn(movesB);

        roundHandler.playOneRound(board);

        verify(attacker).apply(board, movesA, movesB, ID_A, ID_B);
    }

    @Test
    public void reinforcementHappensBeforeAttack() {
        InOrder inOrder = inOrder(reinforcer, attacker);

        roundHandler.playOneRound(board);

        inOrder.verify(reinforcer, atLeastOnce()).apply(any(Board.class), anyInt(), any(), anyInt());
        inOrder.verify(attacker, atLeastOnce()).apply(any(Board.class), any(), any(), anyInt(), anyInt());
    }

    @Test
    public void loggerLogsOnRoundStart() {
        InOrder inOrder = inOrder(logger, reinforcer);

        roundHandler.playOneRound(board);

        inOrder.verify(logger).logRoundStart();
        inOrder.verify(reinforcer, atLeastOnce()).apply(any(Board.class), anyInt(), any(), anyInt());
    }

    @Test
    public void loggerLogsOnRoundEnd() {
        InOrder inOrder = inOrder(logger, attacker);

        roundHandler.playOneRound(board);

        inOrder.verify(attacker, atLeastOnce()).apply(any(Board.class), any(), any(), anyInt(), anyInt());
        inOrder.verify(logger).logRoundEnd(board);
    }

    private RoundHandler getRoundHandler() {
        return new RoundHandler(playerA, playerB, reinforcer, attacker, executor, logger);
    }

    private void makePlayerAControlTotalOf_3_Cells() {
        board.cellAt(1, 1).setValues(ID_A, 4);
        board.cellAt(1, 3).setValues(ID_A, 4);
        board.cellAt(3, 1).setValues(ID_A, 19);
    }

    private void makePlayerBControlTotalOf_2_Cells() {
        board.cellAt(2, 2).setValues(ID_B, 2);
        board.cellAt(3, 2).setValues(ID_B, 2);
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
