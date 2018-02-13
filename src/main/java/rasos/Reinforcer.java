package rasos;

public class Reinforcer {
    private RiskLogger logger;

    Reinforcer(RiskLogger logger) {
        this.logger = logger;
    }

    void apply(Board board, Iterable<ReinforcementMove> moves, int quota, int playerId) {
        if (moves == null) return;
        for (ReinforcementMove move : moves) {
            try {
                Cell cell = board.cellAt(move.getCol(), move.getRow());
                int amount = move.getAmount();
                if (cell.isControlledBy(playerId) && !(amount > quota)) {
                    cell.updateNumSoldiers(cell.getNumSoldiers() + amount);
                    quota -= amount;
                    logger.logSuccessfulReinforcement(playerId, move);
                } else{
                    logger.logFailedReinforcement(playerId, move);
                }
            } catch (Exception ignored) {
            }
        }
    }
}