class Game {
    private final Board board;
    private final int rounds;
    private final RoundHandler handler;
    private final GameEndChecker checker;
    private final RiskLogger logger;

    Game(int dim, int soldiers, int rounds, RoundHandler handler, GameEndChecker checker, RiskLogger logger) {
        this.board = new Board(dim);
        this.checker = checker;
        this.board.populateHomeBases(soldiers);
        this.rounds = rounds;
        this.handler = handler;
        this.logger = logger;
    }

    void start() {
        logger.logStart();
        for (int i = 0; i < rounds; i++) {
            handler.playOneRound(board);
            checker.isEndOfGame(board);
        }
    }

    Board getBoard() {
        return board;
    }
}
