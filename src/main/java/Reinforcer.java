public class Reinforcer {
    void apply(Board board, Iterable<ReinforcementMove> moves, int quota, int playerId) {
        if (moves == null) return;
        for (ReinforcementMove move : moves) {
            try {
                Cell cell = board.cellAt(move.getCol(), move.getRow());
                int amount = move.getAmount();
                if (cell.isControlledBy(playerId) && !(amount > quota)) {
                    cell.updateNumSoldiers(cell.getNumSoldiers() + amount);
                    quota -= amount;
                }
            } catch (Exception ignored) {
            }
        }
    }
}