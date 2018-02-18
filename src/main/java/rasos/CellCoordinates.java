package rasos;

import java.util.Objects;

public class CellCoordinates {
    private int rowIdx;
    private int colIdx;

    public CellCoordinates(int colIdx, int rowIdx) {
        this.rowIdx = rowIdx;
        this.colIdx = colIdx;
    }

    public int getRowIdx() {
        return rowIdx;
    }

    public int getColIdx() {
        return colIdx;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellCoordinates that = (CellCoordinates) o;
        return rowIdx == that.rowIdx &&
                colIdx == that.colIdx;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowIdx, colIdx);
    }
}
