import java.util.HashMap;
import java.util.Map;

class Game {
    private final Map<Integer, Player> players;
    private final ReinforcementHandler reinforcementHandler;
    private Board board;

    Game(int dim, int numSoldiers, Player playerA, Player playerB) {
        this(new Board(dim), playerA, playerB, new ReinforcementHandler());
        this.board.populateHomeBases(numSoldiers);
    }

    Game(Board board, Player playerA, Player playerB, ReinforcementHandler reinforcementHandler) {
        this.players = new HashMap<>(2);
        players.put(1, playerA);
        players.put(2, playerB);
        if (board == null) {
            throw new RuntimeException("Game cannot initialize with null configuration.");
        }
        this.board = board;
        this.reinforcementHandler = reinforcementHandler;
    }

    void start() {
        for (int playerId = 1; playerId <= 2; playerId++) {
            reinforce(playerId);
//            applyAttackMoves(playerId);
        }
    }

    private void applyAttackMoves(int playerId) {
        for (AttackMove move : players.get(playerId).onAttack(board)) {
            Cell originCell = board.cellAt(move.getOriginCol(), move.getOriginRow());
            Cell destCell = board.cellAt(move.getDestCol(), move.getDestRow());
            int amount = move.getAmount();
            originCell.setNumSoldiers(originCell.getNumSoldiers() - amount);
            destCell.setNumSoldiers(destCell.getNumSoldiers() + amount);
        }
    }

    private void reinforce(int playerId) {
        int requiredNumberOfReinforcements = board.getPlayerCellCount(playerId);
        Iterable<ReinforcementMove> moves = players.get(playerId).onReinforcement(board, requiredNumberOfReinforcements);
        Board board = this.board;
        reinforcementHandler.reinforce(playerId, requiredNumberOfReinforcements, moves, board);
    }

    Board getBoard() {
        return board;
    }
}
