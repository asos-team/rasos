public class Reinforcer {
    void apply(int playerId, int requiredNumberOfSoldiers, Iterable<ReinforcementMove> moves, Board board) {
        if (moves == null)
            return;
        for (ReinforcementMove move : moves) {
            Cell cell = board.cellAt(move.getCol(), move.getRow());
            if (!cell.isControlledBy(playerId))
                return;
            int amount = move.getAmount();
            if (amount <= requiredNumberOfSoldiers) {
                cell.setNumSoldiers(cell.getNumSoldiers() + amount);
                requiredNumberOfSoldiers -= amount;
            }
        }
    }
}
