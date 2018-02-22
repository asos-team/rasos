package rasos;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RoundHandler {
    private final Map<Integer, Player> players;
    private final Attacker attacker;
    private final Reinforcer reinforcer;
    private final RiskLogger logger;

    RoundHandler(Player playerA, Player playerB, Attacker attacker, Reinforcer reinforcer, RiskLogger logger) {
        this.players = new HashMap<>(2);
        playerA.setPlayerId(1);
        playerB.setPlayerId(2);
        players.put(1, playerA);
        players.put(2, playerB);
        this.attacker = attacker;
        this.reinforcer = reinforcer;
        this.logger = logger;
    }

    public void playOneRound(Board board) {
        logger.logRoundStart();
        reinforce(board);
        attack(board);
        logger.logRoundEnd(board);
    }

    private void reinforce(Board board) {
        for (int id = 1; id <= 2; id++) {
            int quota = board.getPlayerCellCount(id);

            Iterable<ReinforcementMove> moves;
            try {
                moves = players.get(id).onReinforcement(board, quota);
            } catch (Exception e) {
                moves = Collections.emptyList();
            }
            reinforcer.apply(board, moves, quota, id);
        }
    }

    private void attack(Board board) {
        Iterable<AttackMove> movesA = getAttackMoves(players.get(1), board);
        Iterable<AttackMove> movesB = getAttackMoves(players.get(2), board);
        //noinspection unchecked
        attacker.apply(board, movesA, movesB);
    }

    private Iterable<AttackMove> getAttackMoves(Player player, Board board) {
        try {
            Iterable<AttackMove> moves = player.onAttack(board);
            Objects.requireNonNull(moves);
            return moves;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
