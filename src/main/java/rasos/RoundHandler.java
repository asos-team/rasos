package rasos;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Objects.requireNonNull;

public class RoundHandler {

    private static final int COMPUTATION_TIMEOUT_MILLIS = 500;
    private final int idA;
    private final int idB;
    private final Player playerA;
    private final Player playerB;
    private final Reinforcer reinforcer;
    private final Attacker attacker;
    private final ExecutorService executor;
    private final RiskLogger logger;

    RoundHandler(int idA, int idB, Player playerA, Player playerB, Reinforcer reinforcer, Attacker attacker, ExecutorService executor, RiskLogger logger) {
        this.idA = idA;
        this.idB = idB;
        this.playerA = playerA;
        this.playerB = playerB;
        this.reinforcer = reinforcer;
        this.attacker = attacker;
        this.executor = executor;
        this.logger = logger;
    }

    public void playOneRound(Board board) {
        logger.logRoundStart();
        reinforce(board);
        attack(board);
        logger.logRoundEnd(board);
    }

    private void reinforce(Board board) {
        for (Player player : Lists.newArrayList(playerA, playerB)) {
            int id = player.getPlayerId();
            int quota = board.getPlayerCellCount(id);
            Iterable<ReinforcementMove> moves = getReinforcementMoves(player, board, quota);
            reinforcer.apply(board, id, moves, quota);
        }
    }

    private void attack(Board board) {
        Iterable<AttackMove> movesA = getAttackMoves(playerA, board);
        Iterable<AttackMove> movesB = getAttackMoves(playerB, board);
        attacker.apply(board, movesA, movesB, idA, idB);
    }

    private Iterable<ReinforcementMove> getReinforcementMoves(Player player, Board board, int quota) {
        try {
            AtomicReference<Iterable<ReinforcementMove>> moves = new AtomicReference<>();
            executor.submit(() -> moves.set(player.onReinforcement(board, quota)))
                    .get(COMPUTATION_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
            requireNonNull(moves.get());
            return moves.get();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private Iterable<AttackMove> getAttackMoves(Player player, Board board) {
        try {
            AtomicReference<Iterable<AttackMove>> moves = new AtomicReference<>();
            executor.submit(() -> moves.set(player.onAttack(board)))
                    .get(COMPUTATION_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
            requireNonNull(moves.get());
            return moves.get();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
