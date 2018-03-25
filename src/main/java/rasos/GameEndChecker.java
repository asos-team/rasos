package rasos;

public class GameEndChecker {
    private final int idA;
    private final int idB;

    public GameEndChecker(int idA, int idB) {
        this.idA = idA;
        this.idB = idB;
    }

    public boolean isEndOfGame(Board board) {
        return playerHaveNoSoldiers(board, idA) || playerHaveNoSoldiers(board, idB);
    }

    public int getWinnerId(Board board) {
        int aCellCount = board.getPlayerCellCount(idA);
        int bCellCount = board.getPlayerCellCount(idB);
        if (aCellCount == bCellCount)
            return 0;
        return aCellCount > bCellCount ? idA : idB;
    }

    private boolean playerHaveNoSoldiers(Board board, int id) {
        return board.getPlayerCellCount(id) == 0;
    }
}
