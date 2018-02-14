package rasos;

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
        logger.logGameStart();
        for (int i = 0; i < rounds && !checker.isEndOfGame(board); i++) {
            handler.playOneRound(board);
        }
    }

    Board getBoard() {
        return board;
    }
}
