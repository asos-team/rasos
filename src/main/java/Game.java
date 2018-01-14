import javafx.util.Pair;

public class Game {
    private final int width;
    private final int height;
    private final Player player1;
    private final Player player2;
    private Pair<Integer, Integer>[][] board;

    public Game(int width, int height, Player player1, Player player2) {
        this(BoardUtils.getBlankBoard(width, height), player1, player2);
    }

    public Game(Pair<Integer, Integer>[][] configuration, Player player1, Player player2) {
        this.width = configuration.length;
        this.height = configuration[0].length;
        this.player1 = player1;
        this.player2 = player2;
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
        applyReinforcements(1, player1);
        applyReinforcements(2, player2);

        applyAttackMoves(1, player1);
        applyAttackMoves(2, player2);
    }

    private void applyAttackMoves(int playerId, Player player) {
        for (AttackMove move : player.onAttack(getBoard())) {
            board[move.getFromCol()][move.getFromRow()] = new Pair<Integer, Integer>(playerId, board[move.getFromCol()][move.getFromRow()].getValue() - move.getAmount());
            board[move.getToCol()][move.getToRow()] = new Pair<Integer, Integer>(playerId, board[move.getToCol()][move.getToRow()].getValue() + move.getAmount());
        }
    }

    private void applyReinforcements(int playerId, Player player) {
        for (ReinforcementMove move :
                player.onReinforcement(getBoard(), getPlayerCellCount(playerId))) {
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
