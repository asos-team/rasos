import javafx.util.Pair;

public class Game {
    private final int width;
    private final int height;
    private final Player[] players;
    private Pair<Integer, Integer>[][] board;

    public Game(int width, int height, Player player1, Player player2) {
        this(BoardUtils.getBlankBoard(width, height), player1, player2);
    }

    public Game(Pair<Integer, Integer>[][] configuration, Player player1, Player player2) {
        this.width = configuration.length;
        this.height = configuration[0].length;
        this.players = new Player[]{player1, player2};
        this.board = configuration;
        validateBoardInitialized(configuration);
    }

    private void validateBoardInitialized(Pair<Integer, Integer>[][] board) {
        for (Pair<Integer, Integer>[] column : board) {
            for (Pair<Integer, Integer> cell : column) {
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
        for (AttackMove move : players[playerId - 1].onAttack(getBoard())) {
            board[move.getFromCol()][move.getFromRow()] = new Pair<Integer, Integer>(playerId, board[move.getFromCol()][move.getFromRow()].getValue() - move.getAmount());
            board[move.getToCol()][move.getToRow()] = new Pair<Integer, Integer>(playerId, board[move.getToCol()][move.getToRow()].getValue() + move.getAmount());
        }
    }

    private void applyReinforcements(int playerId) {
        for (ReinforcementMove move :
                players[playerId - 1].onReinforcement(getBoard(), getPlayerCellCount(playerId))) {
            Pair<Integer, Integer> currentCell = board[move.getCol()][move.getRow()];
            Pair<Integer, Integer> newCell = new Pair<Integer, Integer>(playerId, currentCell.getValue() + move.getAmount());

            board[move.getCol()][move.getRow()] = newCell;
        }
    }


    private int getPlayerCellCount(int playerId) {
        int playerCellCount = 0;
        for (Pair<Integer, Integer>[] column : board) {
            for (Pair<Integer, Integer> cell : column) {
                if (cell.getKey() == playerId) {
                    playerCellCount++;
                }
            }
        }
        return playerCellCount;
    }

    public Pair<Integer, Integer>[][] getBoard() {
        return board;
    }

}
