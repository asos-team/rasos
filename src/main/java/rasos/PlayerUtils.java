package rasos;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtils {
    private final Player player;

    public PlayerUtils(Player player) {
        this.player = player;
    }

    public Iterable<CellCoordinates> getControlledCells(Board board) {
        return board.getControlledCoordinates(player.getPlayerId());
    }

    public List<CellCoordinates> getNeighbours(Board b, CellCoordinates cc) {
        List<CellCoordinates> neighbours = new ArrayList<>();
        for (int cIdx = -1; cIdx < 2; cIdx++) {
            for (int rIdx = -1; rIdx < 2; rIdx++) {
                int col = cc.getColIdx() + cIdx;
                int row = cc.getRowIdx() + rIdx;
                if (isValidNeighbour(b, cIdx, rIdx, col, row)) {
                    neighbours.add(new CellCoordinates(col, row));
                }
            }
        }
        return neighbours;
    }

    private boolean isValidNeighbour(Board b, int cIdx, int rIdx, int col, int row) {
        return notBothZero(cIdx, rIdx) && isValidBoardCoordinate(b, col, row);
    }

    private boolean isValidBoardCoordinate(Board b, int col, int row) {
        return isValidBoardIdx(b, col) && isValidBoardIdx(b, row);
    }

    private boolean isValidBoardIdx(Board b, int idx) {
        return idx > 0 && idx <= b.getDim();
    }

    private boolean notBothZero(int n1, int n2) {
        return n1 != 0 || n2 != 0;
    }
}