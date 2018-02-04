public class Reinforcer {
    void apply(int playerId, int quota, Iterable<ReinforcementMove> moves, Board board) {
        if (moves == null)
            return;
        for (ReinforcementMove move : moves) {
            Cell cell = board.cellAt(move.getCol(), move.getRow());
            if (!cell.isControlledBy(playerId))
                return;
            int amount = move.getAmount();
            if (amount <= quota) {
                cell.setNumSoldiers(cell.getNumSoldiers() + amount);
                quota -= amount;
            }
        }
    }
}
