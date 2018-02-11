public class RoundHandler {
    private final Player playerA;
    private final Player playerB;
    private final Reinforcer reinforcer;

    public RoundHandler(Player playerA, Player playerB, Attacker attacker, Reinforcer reinforcer) {
        this.playerA = playerA;
        this.playerB = playerB;
        this.reinforcer = reinforcer;
    }

    public void playOneRound(Board board) {
        int quotaA = board.getPlayerCellCount(1);
        int quotaB = board.getPlayerCellCount(2);
        Iterable<ReinforcementMove> movesA = playerA.onReinforcement(board, quotaA);
        Iterable<ReinforcementMove> movesB = playerB.onReinforcement(board, quotaB);
        reinforcer.apply(board, movesA, quotaA, 1);
        reinforcer.apply(board, movesB, quotaB, 2);
    }
}
