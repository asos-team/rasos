import java.util.stream.StreamSupport;

public class Reinforcer {
    void apply(int playerId, int requiredNumberOfSoldiers, Iterable<ReinforcementMove> moves, Board board) {
        if (moves == null)
            return;
        if (StreamSupport.stream(moves.spliterator(), false)
                .mapToInt(ReinforcementMove::getAmount)
                .sum() > requiredNumberOfSoldiers) {
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
