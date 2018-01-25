import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

public class Game {
    private final Map<Integer, Player> players;
    private Board board;

    public Game(int dim, int numSoldiers, Player player1, Player player2) {
        this(new Board(dim), player1, player2);
        this.board.populateHomeBases(numSoldiers);
    }

    public Game(Board board, Player player1, Player player2) {
        this.players = new HashMap<>(2);
        players.put(1, player1);
        players.put(2, player2);
        if (board == null) {
            throw new RuntimeException("Game cannot initialize with null configuration.");
        }
        this.board = board;
    }

    public void start() {
        for (int playerId = 1; playerId <= 2; playerId++) {
            reinforce(playerId);
//            applyAttackMoves(playerId);
        }
    }

    private void applyAttackMoves(int playerId) {
        for (AttackMove move : players.get(playerId).onAttack(board)) {
            Cell newOriginCell = new Cell(playerId, board.getCell(move.getOriginCol(), move.getOriginRow()).getNumSoldiers() - move.getAmount());
            Cell newDestinationCell = new Cell(playerId, board.getCell(move.getDestCol(), move.getDestRow()).getNumSoldiers() + move.getAmount());
            board.setCell(move.getOriginCol(), move.getOriginRow(), newOriginCell);
            board.setCell(move.getDestCol(), move.getDestRow(), newDestinationCell);
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
        Cell currentCell = board.getCell(move.getCol(), move.getRow());
        if (!currentCell.isControlledBy(playerId))
            throw new RuntimeException("You cannot reinforce a cell that you don't control");
        Cell newCell = new Cell(playerId, currentCell.getNumSoldiers() + move.getAmount());
        board.setCell(move.getCol(), move.getRow(), newCell);
    }

    public Board getBoard() {
        return board;
    }
}
