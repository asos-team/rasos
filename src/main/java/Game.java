import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class Game {
    private final Map<Integer, Player> players;
    private final Reinforcer reinforcer;
    private final Attacker attacker;
    private Board board;
    private RiskLogger logger;

    Game(int dim, int numSoldiers, Player playerA, Player playerB, Attacker attacker, Reinforcer reinforcer,RiskLogger logger) {
        Board board = new Board(dim);
        this.players = new HashMap<>(2);
        players.put(1, playerA);
        players.put(2, playerB);
        this.board = board;
        this.reinforcer = reinforcer;
        this.attacker = attacker;
        this.board.populateHomeBases(numSoldiers);
        this.logger=logger;
    }

    void start() {
        logger.logStart();
        reinforce();
        attack();
    }

    private void reinforce() {
        for (int playerId = 1; playerId <= 2; playerId++) {
            reinforce(playerId);
        }
    }

    private void attack() {
        attacker.apply(getBoard(), getAttackMoves(players.get(1)), getAttackMoves(players.get(2)));
    }

    Board getBoard() {
        return board;
    }

    private void reinforce(int playerId) {
        int quota = board.getPlayerCellCount(playerId);
        Iterable<ReinforcementMove> moves = players.get(playerId).onReinforcement(board, quota);
        reinforcer.apply(board, moves, quota, playerId);
    }

    private Iterable<AttackMove> getAttackMoves(Player player) {
        Iterable<AttackMove> attackMoves = player.onAttack(getBoard());
        attackMoves = attackMoves != null ? attackMoves : Collections.emptyList();
        return attackMoves;
    }
}
