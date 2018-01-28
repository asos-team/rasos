import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

class Game {
    private final Map<Integer, Player> players;
    private Board board;

    Game(int dim, int numSoldiers, Player playerA, Player playerB) {
        this(new Board(dim), playerA, playerB);
        this.board.populateHomeBases(numSoldiers);
    }

    Game(Board board, Player playerA, Player playerB) {
        this.players = new HashMap<>(2);
        players.put(1, playerA);
        players.put(2, playerB);
        if (board == null) {
            throw new RuntimeException("Game cannot initialize with null configuration.");
        }
        this.board = board;
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
        if (moves == null)
//            throw new RuntimeException("Null reinforcement is not allowed");
            return;
        if (StreamSupport.stream(moves.spliterator(), false)
                .mapToInt(ReinforcementMove::getAmount)
                .sum() > requiredNumberOfReinforcements) {
            throw new RuntimeException("Too many soldiers in reinforcement");
        }
        for (ReinforcementMove move : moves) {
            applyMove(playerId, move);
        }
    }

    private void applyMove(int playerId, ReinforcementMove move) {
        Cell cell = board.cellAt(move.getCol(), move.getRow());
        if (!cell.isControlledBy(playerId))
            throw new RuntimeException("You cannot reinforce a cell that you don't control");
        cell.setNumSoldiers(cell.getNumSoldiers() + move.getAmount());
    }

    Board getBoard() {
        return board;
    }
}
