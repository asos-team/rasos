class Game {
    private final RoundHandler handler;
    private Board board;
    private RiskLogger logger;

    Game(int dim, int numSoldiers, RoundHandler handler, RiskLogger logger) {
        this.board = new Board(dim);
        this.handler = handler;
        this.board.populateHomeBases(numSoldiers);
        this.logger = logger;
    }

    void start() {
        logger.logStart();
        handler.playOneRound(board);
    }

    Board getBoard() {
        return board;
    }
}
