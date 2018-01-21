import java.util.HashMap;
import java.util.Map;

public class Game {
    private final Map<Integer, Player> players;
    private Board betterBoard;

    public Game(int width, int height, Player player1, Player player2) {
        this(new Board(Board.getDefaultBoard(width, height)), player1, player2);
    }

    public Game(Board board, Player player1, Player player2) {
        this.players = new HashMap<Integer, Player>(2);
        players.put(1, player1);
        players.put(2, player2);
        betterBoard = board;
        betterBoard.validateBoardInitialized();
    }

    public void tick() {
        for (int playerId = 1; playerId <= 2; playerId++) {
            applyReinforcements(playerId);
            applyAttackMoves(playerId);
        }
    }

    private void applyAttackMoves(int playerId) {
        for (AttackMove move : players.get(playerId).onAttack(betterBoard)) {
            Cell newOriginCell = new Cell(playerId, betterBoard.getCell(move.getOriginCol(), move.getOriginRow()).getNumSoldiers() - move.getAmount());
            Cell newDestinationCell = new Cell(playerId, betterBoard.getCell(move.getDestCol(), move.getDestRow()).getNumSoldiers() + move.getAmount());
            betterBoard.setCell(move.getOriginCol(), move.getOriginRow(), newOriginCell);
            betterBoard.setCell(move.getDestCol(), move.getDestRow(), newDestinationCell);
        }
    }

    private void applyReinforcements(int playerId) {
        Iterable<ReinforcementMove> moves = players.get(playerId).onReinforcement(betterBoard, betterBoard.getPlayerCellCount(playerId));
        for (ReinforcementMove move :
                moves) {
            Cell currentCell = betterBoard.getCell(move.getCol(), move.getRow());
            Cell newCell = new Cell(playerId, currentCell.getNumSoldiers() + move.getAmount());

            betterBoard.setCell(move.getCol(), move.getRow(), newCell);
        }
    }

    public Cell[][] getBoard() {
        return betterBoard.getConfiguration();
    }

    public Board getBetterBoard() {
        return betterBoard;
    }
}
