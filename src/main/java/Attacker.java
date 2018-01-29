public class Attacker {

    public void apply(Iterable<AttackMove> attackMoves, Board board) {
        for (AttackMove move : attackMoves) {
            if (move != null) {
                Cell originCell = board.cellAt(move.getOriginCol(), move.getOriginRow());
                Cell destCell = board.cellAt(move.getDestCol(), move.getDestRow());
                int amount = move.getAmount();
                originCell.setNumSoldiers(originCell.getNumSoldiers() - amount);
                destCell.setNumSoldiers(destCell.getNumSoldiers() + amount);
            }
        }
    }
}