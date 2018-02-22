package rasos;

public class GameEndChecker {
    public boolean isEndOfGame(Board board) {
        return playerHaveNoSoldiers(board, 1) || playerHaveNoSoldiers(board, 2);
    }

    public int getWinnerId(Board board) {
        int aCellCount = board.getPlayerCellCount(1);
        int bCellCount = board.getPlayerCellCount(2);
        if (aCellCount == bCellCount)
            return 0;
        return aCellCount > bCellCount ? 1 : 2;
    }

    private boolean playerHaveNoSoldiers(Board board, int id) {
        return board.getPlayerCellCount(id) == 0;
    }
}
