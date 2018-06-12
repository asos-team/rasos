package rasos;

import io.reactivex.Observable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class GameResult {

    private static final int DEMOLITION_FACTOR = 11;

    private final Board board;
    private final int idA;
    private final int idB;
    private Map<Integer, Integer> idToScore;
    private boolean isAlreadyEvaluated;

    GameResult(Board board, int idA, int idB) {
        this.board = board;
        this.idA = idA;
        this.idB = idB;
        this.idToScore = new HashMap<>();
        idToScore.put(idA, 0);
        idToScore.put(idB, 0);
        this.isAlreadyEvaluated = false;
    }

    public int getScore(int playerId) {
        throwOnUnknownId(playerId);

        if (!isAlreadyEvaluated) {
            evaluateScore();
            isAlreadyEvaluated = true;
        }

        return idToScore.get(playerId);
    }

    private void evaluateScore() {
        int aCellCount = board.getPlayerCellCount(idA);
        int bCellCount = board.getPlayerCellCount(idB);
        if (isTotalDemolition(aCellCount, bCellCount)) {
            assignDemolitionScores(aCellCount, bCellCount);
        } else if (aCellCount != bCellCount) {
            assignScores(aCellCount, bCellCount, this::scoreWinner);
        } else {
            assignScores(getSoldiersCount(idA), getSoldiersCount(idB), this::scoreWinner);
        }
    }

    private void throwOnUnknownId(int playerId) {
        if (!idToScore.containsKey(playerId)) {
            throw new RuntimeException(
                    String.format("Unknown player id %d. Known ids are %d and %d", playerId, idA, idB));
        }
    }

    private void assignDemolitionScores(int aCellCount, int bCellCount) {
        int totalDemolitionScore = DEMOLITION_FACTOR * board.getDim() * board.getDim();
        assignScores(aCellCount, bCellCount, winnerId -> totalDemolitionScore + getSoldiersCount(winnerId));
    }

    private void assignScores(int aPower, int bPower, Function<Integer, Integer> winnerScore) {
        if (aPower > bPower) {
            idToScore.put(idA, winnerScore.apply(idA));
            idToScore.put(idB, 0);
        } else if (bPower > aPower) {
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
                .reduce((x, y) -> x + y)
                .blockingGet(0);
    }

    private Integer scoreWinner(Integer winnerId) {
        return board.getPlayerCellCount(winnerId) * 10;
    }
}
