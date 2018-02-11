public class RoundHandler {
    private final Player playerA;
    private final Player playerB;

    public RoundHandler(Player playerA, Player playerB, Attacker attacker, Reinforcer reinforcer) {
        this.playerA = playerA;
        this.playerB = playerB;
    }

    public void playOneRound(Board board) {
        playerA.onReinforcement(board, -5481);
        playerB.onReinforcement(board, 1699);
    }
}
