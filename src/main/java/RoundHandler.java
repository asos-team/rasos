public class RoundHandler {
    private final Player playerA;
    private final Player playerB;

    public RoundHandler(Player playerA, Player playerB, Attacker attacker, Reinforcer reinforcer) {
        this.playerA = playerA;
        this.playerB = playerB;
    }

    public void playOneRound(Board board) {
        int quotaA = board.getPlayerCellCount(1);
        int quotaB = board.getPlayerCellCount(2);
        playerA.onReinforcement(board, quotaA);
        playerB.onReinforcement(board, quotaB);
    }
}
