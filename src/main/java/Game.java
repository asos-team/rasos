import java.util.HashMap;
import java.util.Map;

public class Game {
    private final Map<Integer, Player> players;
    private Board board;

    public Game(int width, Player player1, Player player2) {
        this(new Board(width), player1, player2);
        this.board.populateHomeBases(20);
    }

    public Game(Board board, Player player1, Player player2) {
        this.players = new HashMap<Integer, Player>(2);
        players.put(1, player1);
        players.put(2, player2);
        this.board = board;
        this.board.validateBoardInitialized();
    }

    public void tick() {
        for (int playerId = 1; playerId <= 2; playerId++) {
            applyReinforcements(playerId);
            applyAttackMoves(playerId);
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

    private void applyReinforcements(int playerId) {
        Iterable<ReinforcementMove> moves = players.get(playerId).onReinforcement(board, board.getPlayerCellCount(playerId));
        for (ReinforcementMove move : moves) {
            Cell currentCell = board.getCell(move.getCol(), move.getRow());
            Cell newCell = new Cell(playerId, currentCell.getNumSoldiers() + move.getAmount());
            board.setCell(move.getCol(), move.getRow(), newCell);
        }
    }

    public Board getBoard() {
        return board;
    }
}
