import java.util.HashMap;
import java.util.Map;

public class Game {
    private final Map<Integer, Player> players;
    private Cell[][] board;

    public Game(int width, int height, Player player1, Player player2) {
        this(BoardUtils.getDefaultBoard(width, height), player1, player2);
    }

    public Game(Cell[][] configuration, Player player1, Player player2) {
        this.players = new HashMap<Integer, Player>(2);
        players.put(1,player1);
        players.put(2,player2);
        this.board = configuration;
        validateBoardInitialized(configuration);
    }

    private void validateBoardInitialized(Cell[][] board) {
        for (Cell[] column : board) {
            for (Cell cell : column) {
                if (cell == null) {
                    throw new RuntimeException("Board isn't fully initialized!");
                }
            }
        }
    }

    public void tick() {
        for (int playerId = 1; playerId <= 2; playerId++) {
            applyReinforcements(playerId);
            applyAttackMoves(playerId);
        }
    }

    private void applyAttackMoves(int playerId) {
        for (AttackMove move : players.get(playerId).onAttack(getBoard())) {
            board[move.getFromCol()][move.getFromRow()] = new Cell(playerId, board[move.getFromCol()][move.getFromRow()].getNumSoldiers() - move.getAmount());
            board[move.getToCol()][move.getToRow()] = new Cell(playerId, board[move.getToCol()][move.getToRow()].getNumSoldiers() + move.getAmount());
        }
    }

    private void applyReinforcements(int playerId) {
        Iterable<ReinforcementMove> moves = players.get(playerId).onReinforcement(getBoard(), getPlayerCellCount(playerId));
        for (ReinforcementMove move :
                moves) {
            Cell currentCell = board[move.getCol()][move.getRow()];
            Cell newCell = new Cell(playerId, currentCell.getNumSoldiers() + move.getAmount());

            board[move.getCol()][move.getRow()] = newCell;
        }
    }

    private int getPlayerCellCount(int playerId) {
        int playerCellCount = 0;
        for (Cell[] column : board) {
            for (Cell cell : column) {
                if (cell.getControllingPlayer() == playerId) {
                    playerCellCount++;
                }
            }
        }
        return playerCellCount;
    }

    public Cell[][] getBoard() {
        return board;
    }
}
