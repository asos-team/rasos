class Game {
    private final int rounds;
    private final RoundHandler handler;
    private Board board;
    private RiskLogger logger;

    Game(int dim, int soldiers, int rounds, RoundHandler handler, RiskLogger logger) {
        this.board = new Board(dim);
        this.board.populateHomeBases(soldiers);
        this.rounds = rounds;
        this.handler = handler;
        this.logger = logger;
    }

    void start() {
        logger.logStart();
        for (int i = 0; i < rounds; i++) {
            handler.playOneRound(board);
        }
    }

    Board getBoard() {
        return board;
    }
}
