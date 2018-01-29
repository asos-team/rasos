import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class Game {
    private final Map<Integer, Player> players;
    private final Reinforcer reinforcer;
    private final Attacker attacker;
    private Board board;

    Game(int dim, int numSoldiers, Player playerA, Player playerB) {
        this(new Board(dim), playerA, playerB, new Reinforcer(), new Attacker());
        this.board.populateHomeBases(numSoldiers);
    }

    Game(Board board, Player playerA, Player playerB, Reinforcer reinforcer, Attacker attacker) {
        this.players = new HashMap<>(2);
        players.put(1, playerA);
        players.put(2, playerB);
        if (board == null) {
            throw new RuntimeException("Game cannot initialize with null configuration.");
        }
        this.board = board;
        this.reinforcer = reinforcer;
        this.attacker = attacker;
    }

    void start() {
        for (int playerId = 1; playerId <= 2; playerId++) {
            reinforce(playerId);
            attack(playerId);
        }
    }

    Board getBoard() {
        return board;
    }

    private void reinforce(int playerId) {
        int requiredNumberOfSoldiers = board.getPlayerCellCount(playerId);
        Iterable<ReinforcementMove> moves = players.get(playerId).onReinforcement(board, requiredNumberOfSoldiers);
        Board board = this.board;
        reinforcer.apply(playerId, requiredNumberOfSoldiers, moves, board);
    }

    private void attack(int playerId) {
        Player player = players.get(playerId);
        Iterable<AttackMove> attackMoves = getAttackMoves(player);
        attacker.apply(attackMoves, getBoard());
    }

    private Iterable<AttackMove> getAttackMoves(Player player) {
        Iterable<AttackMove> attackMoves = player.onAttack(getBoard());
        attackMoves = attackMoves != null ? attackMoves : Collections.emptyList();
        return attackMoves;
    }
}
