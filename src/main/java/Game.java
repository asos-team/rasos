import javafx.util.Pair;

public class Game {
    private final int width;
    private final int height;
    private final Player player1;
    private final Player player2;
    private Pair<Integer, Integer>[][] board;

    public Game(int width, int height, Player player1, Player player2) {
        this.width = width;
        this.height = height;
        this.player1 = player1;
        this.player2 = player2;
        initializeBoard();
    }

    public Game(Pair<Integer, Integer>[][] configuration, Player player1, Player player2) {
        width = configuration.length;
        height = configuration[0].length;
        this.player1 = player1;
        this.player2 = player2;
    }

    public void tick() {
        player1.onReinforcement(getBoard(), 1);
        player2.onReinforcement(getBoard(), 1);
        board[0][0] = new Pair<Integer, Integer>(1, 21);
        board[width - 1][height - 1] = new Pair<Integer, Integer>(2, 21);
    }

    public Pair<Integer, Integer>[][] getBoard() {
        return board;
    }

    private void initializeBoard() {
        board = new Pair[width][height];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Pair<Integer, Integer>(0, 0);
            }
        }
        board[0][0] = new Pair<Integer, Integer>(1, 20);
        board[width - 1][height - 1] = new Pair<Integer, Integer>(2, 20);
    }
}
