package rasos;

public class Reinforcer {
    private RiskLogger logger;

    Reinforcer(RiskLogger logger) {
        this.logger = logger;
    }

    void apply(Board board, int playerId, Iterable<ReinforcementMove> moves, int quota) {
        for (ReinforcementMove move : moves) {
            try {
                Cell cell = board.cellAt(move.getCol(), move.getRow());
                int amount = move.getAmount();
                if (cell.isControlledBy(playerId) && !(amount > quota)) {
                    cell.updateNumSoldiers(cell.getNumSoldiers() + amount);
                    quota -= amount;
                    logger.logSuccessfulReinforcement(playerId, move);
                } else {
                    logger.logFailedReinforcement(playerId, move);
                }
            } catch (Exception ignored) {
            }
        }
    }
}