package rasos;

public class ReinforcementMove {
    private final int col;
    private final int row;
    private final int amount;

    public ReinforcementMove(int col, int row, int amount) {
        this.col = col;
        this.row = row;
        this.amount = amount;
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
