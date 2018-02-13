package rasos;

public class GameEndChecker {
    public boolean isEndOfGame(Board board) {
        return playerHaveNoSoldiers(board, 1) || playerHaveNoSoldiers(board, 2);
    }

    private boolean playerHaveNoSoldiers(Board board, int id) {
        return board.getPlayerCellCount(id) == 0;
    }
}
