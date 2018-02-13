package rasos;

public class GameEndChecker {
    public boolean isEndOfGame(Board board) {
        return board.getPlayerCellCount(1) == 0 || board.getPlayerCellCount(2) == 0 || board.isEmpty();
    }
}
