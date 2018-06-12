package rasos;

import io.reactivex.Observable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class GameResult {

    public static final int DEMOLITION_FACTOR = 11;
    private final Board board;
    private final int idA;
    private final int idB;
    private Map<Integer, Integer> idToScore;

    public GameResult(Board board, int idA, int idB) {
        this.board = board;
        this.idA = idA;
        this.idB = idB;
        this.idToScore = new HashMap<>();
        idToScore.put(idA, 0);
        idToScore.put(idB, 0);
    }

    public int getScore(int playerId) {
        if (!idToScore.containsKey(playerId)) {
            throw new RuntimeException(
                    String.format("Unknown player id %d. Known ids are %d and %d", playerId, idA, idB));
        }

        int aCellCount = board.getPlayerCellCount(idA);
        int bCellCount = board.getPlayerCellCount(idB);
        if (isTotalDemolition(aCellCount, bCellCount)) {
            calcDemolition(aCellCount, bCellCount);
        } else {
            assignScores(aCellCount, bCellCount, winnerId -> board.getPlayerCellCount(winnerId) * 10);
        }


        return idToScore.get(playerId);
    }

    private void calcDemolition(int aCellCount, int bCellCount) {
        int totalDemolitionScore = DEMOLITION_FACTOR * board.getDim() * board.getDim();
        assignScores(aCellCount, bCellCount, winnerId -> totalDemolitionScore + getSoldiersCount(winnerId));
    }

    private void assignScores(int aCellCount, int bCellCount, Function<Integer, Integer> winnerScore) {
        if (aCellCount > bCellCount) {
            idToScore.put(idA, winnerScore.apply(idA));
            idToScore.put(idB, 0);
        } else if (bCellCount > aCellCount) {
            idToScore.put(idA, 0);
            idToScore.put(idB, winnerScore.apply(idB));
        }
    }

    private boolean isTotalDemolition(int aCount, int bCount) {
        return aCount * bCount == 0 && aCount + bCount > 0;
    }

    private int getSoldiersCount(int id) {
        return Observable.fromIterable(board.getControlledCoordinates(id))
                .map(cors -> board.cellAt(cors.getColIdx(), cors.getRowIdx()))
                .map(Cell::getNumSoldiers)
                .reduce((x, y) -> x + y).blockingGet();
    }
}
