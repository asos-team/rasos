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
        Iterable<ReinforcementMove> player1ReinforcementMoves = player1.onReinforcement(getBoard(), getPlayerCellCount(1));
        Iterable<ReinforcementMove> player2ReinforcementMoves = player2.onReinforcement(getBoard(), getPlayerCellCount(2));

        applyReinforcements(player1ReinforcementMoves, 1);
        applyReinforcements(player2ReinforcementMoves, 2);
    }

    private void applyReinforcements(Iterable<ReinforcementMove> reinforcementMoves, int playerId) {
        for (ReinforcementMove move :
                reinforcementMoves) {
            Pair<Integer, Integer> currentCell = board[move.getCol()][move.getRow()];
            Pair<Integer, Integer> newCell = new Pair<Integer, Integer>(playerId, currentCell.getValue() + move.getAmount());

            board[move.getCol()][move.getRow()]=newCell;
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
