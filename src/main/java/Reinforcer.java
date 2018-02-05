public class Reinforcer {
    void apply(int playerId, int quota, Iterable<ReinforcementMove> moves, Board board) {
        if (moves == null)
            return;
        for (ReinforcementMove move : moves) {
            try {
                Cell cell = board.cellAt(move.getCol(), move.getRow());
                int amount = move.getAmount();
                if (cell.isControlledBy(playerId) && !exceedingQuota(quota, amount)) {
                    cell.updateNumSoldiers(cell.getNumSoldiers() + amount);
                    quota -= amount;
                }
            } catch (Exception ignored) {
            }
        }
    }

    private boolean exceedingQuota(int quota, int amount) {
        return amount > quota;
    }
}