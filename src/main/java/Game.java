import java.util.HashMap;
import java.util.Map;

public class Game {
    private final Map<Integer, Player> players;
    private Cell[][] board;
    private Board betterBoard;

    public Game(int width, int height, Player player1, Player player2) {
        this(new Board(BoardUtils.getDefaultBoard(width, height)), player1, player2);
    }

    public Game(Board board, Player player1, Player player2) {
        this.players = new HashMap<Integer, Player>(2);
        players.put(1, player1);
        players.put(2, player2);
        betterBoard = board;
        this.board = betterBoard.getConfiguration();
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
            betterBoard.setCell(move.getFromCol(), move.getFromRow(), new Cell(playerId, board[move.getFromCol()][move.getFromRow()].getNumSoldiers() - move.getAmount()));
            betterBoard.setCell(move.getToCol(), move.getToRow(), new Cell(playerId, board[move.getToCol()][move.getToRow()].getNumSoldiers() + move.getAmount()));
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
        return board;
    }

    public Board getBetterBoard() {
        return betterBoard;
    }
}
