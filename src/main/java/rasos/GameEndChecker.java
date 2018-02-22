package rasos;

public class GameEndChecker {
    public boolean isEndOfGame(Board board) {
        return playerHaveNoSoldiers(board, 1) || playerHaveNoSoldiers(board, 2);
    }

    public int getWinnerId(Board board) {
        if (!board.isEmpty())
            return board.getPlayerCellCount(1) > 0 ? 1 : 2;
        return 0;
    }

    private boolean playerHaveNoSoldiers(Board board, int id) {
        return board.getPlayerCellCount(id) == 0;
    }
}
