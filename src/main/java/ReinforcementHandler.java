import java.util.stream.StreamSupport;

public class ReinforcementHandler {
    void reinforce(int playerId, int requiredNumberOfReinforcements, Iterable<ReinforcementMove> moves, Board board) {
        if (moves == null)
            return;
        if (StreamSupport.stream(moves.spliterator(), false)
                .mapToInt(ReinforcementMove::getAmount)
                .sum() > requiredNumberOfReinforcements) {
            throw new RuntimeException("Too many soldiers in reinforcement");
        }
        for (ReinforcementMove move : moves) {
            Cell cell = board.cellAt(move.getCol(), move.getRow());
            if (!cell.isControlledBy(playerId))
                throw new RuntimeException("You cannot reinforce a cell that you don't control");
            cell.setNumSoldiers(cell.getNumSoldiers() + move.getAmount());
        }
    }
}
