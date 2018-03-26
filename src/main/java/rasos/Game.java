package rasos;

class Game {
    private final Board board;
    private final int rounds;
    private final RoundHandler handler;
    private final GameEndChecker checker;
    private final RiskLogger logger;

    Game(int dim, int soldiers, int rounds, Player playerA, int idA, Player playerB, int idB, RoundHandler handler, GameEndChecker checker, RiskLogger logger) {
        playerA.setPlayerId(idA);
        playerB.setPlayerId(idB);
        this.board = new Board(dim);
        this.checker = checker;
        this.board.populateHomeBases(soldiers, idA, idB);
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
