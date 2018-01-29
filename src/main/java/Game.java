import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class Game {
    private final Map<Integer, Player> players;
    private final ReinforcementHandler reinforcementHandler;
    private final Attacker attacker;
    private Board board;

    Game(int dim, int numSoldiers, Player playerA, Player playerB) {
        this(new Board(dim), playerA, playerB, new ReinforcementHandler(), new Attacker());
        this.board.populateHomeBases(numSoldiers);
    }

    Game(Board board, Player playerA, Player playerB, ReinforcementHandler reinforcementHandler, Attacker attacker) {
        this.players = new HashMap<>(2);
        players.put(1, playerA);
        players.put(2, playerB);
        if (board == null) {
            throw new RuntimeException("Game cannot initialize with null configuration.");
        }
        this.board = board;
        this.reinforcementHandler = reinforcementHandler;
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
        int requiredNumberOfReinforcements = board.getPlayerCellCount(playerId);
        Iterable<ReinforcementMove> moves = players.get(playerId).onReinforcement(board, requiredNumberOfReinforcements);
        Board board = this.board;
        reinforcementHandler.reinforce(playerId, requiredNumberOfReinforcements, moves, board);
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
