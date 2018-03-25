package rasos;

class Game {

    static final int ID_A = 1;
    static final int ID_B = 2;

    private final Board board;
    private final int rounds;
    private final RoundHandler handler;
    private final GameEndChecker checker;
    private final RiskLogger logger;

    Game(int dim, int soldiers, int rounds, Player playerA, Player playerB, RoundHandler handler, GameEndChecker checker, RiskLogger logger) {
        playerA.setPlayerId(ID_A);
        playerB.setPlayerId(ID_B);
        this.board = new Board(dim);
        this.checker = checker;
        this.board.populateHomeBases(soldiers, 1, 2);
        this.rounds = rounds;
        this.handler = handler;
        this.logger = logger;
    }

    void start() {
        logger.logGameStart();
        for (int i = 0; i < rounds && !checker.isEndOfGame(board); i++) {
            handler.playOneRound(board);
        }
        logger.logGameEnd(checker.getWinnerId(board));
    }

    Board getBoard() {
        return board;
    }
}
