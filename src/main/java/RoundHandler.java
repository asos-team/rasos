import java.util.Collections;

public class RoundHandler {
    private final Player playerA;
    private final Player playerB;
    private final Attacker attacker;
    private final Reinforcer reinforcer;

    public RoundHandler(Player playerA, Player playerB, Attacker attacker, Reinforcer reinforcer) {
        this.playerA = playerA;
        this.playerB = playerB;
        this.attacker = attacker;
        this.reinforcer = reinforcer;
    }

    public void playOneRound(Board board) {
        int quotaA = board.getPlayerCellCount(1);
        int quotaB = board.getPlayerCellCount(2);
        Iterable<ReinforcementMove> movesA = playerA.onReinforcement(board, quotaA);
        Iterable<ReinforcementMove> movesB = playerB.onReinforcement(board, quotaB);
        reinforcer.apply(board, movesA, quotaA, 1);
        reinforcer.apply(board, movesB, quotaB, 2);

        //noinspection unchecked
        attacker.apply(board, getAttackMoves(playerA, board), getAttackMoves(playerB, board));
    }

    private Iterable<AttackMove> getAttackMoves(Player player, Board board) {
        Iterable<AttackMove> attackMoves = player.onAttack(board);
        attackMoves = attackMoves != null ? attackMoves : Collections.emptyList();
        return attackMoves;
    }

}
