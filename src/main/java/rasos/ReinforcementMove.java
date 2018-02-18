package rasos;

import java.util.Objects;

public class ReinforcementMove {
    private final int col;
    private final int row;
    private final int amount;

    public ReinforcementMove(int col, int row, int amount) {
        this.col = col;
        this.row = row;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReinforcementMove that = (ReinforcementMove) o;
        return col == that.col &&
                row == that.row &&
                amount == that.amount;
    }

    @Override
    public int hashCode() {

        return Objects.hash(col, row, amount);
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public int getAmount() {
        return amount;
    }
}
