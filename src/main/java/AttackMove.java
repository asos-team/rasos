public class AttackMove {
    private int fromCol;
    private int fromRow;
    private int toCol;
    private int toRow;
    private int amount;

    public AttackMove(int fromCol, int fromRow, int toCol, int toRow, int amount) {
        this.fromCol = fromCol;
        this.fromRow = fromRow;
        this.toCol = toCol;
        this.toRow = toRow;
        this.amount = amount;
    }

    public int getFromCol() {
        return fromCol;
    }

    public int getFromRow() {
        return fromRow;
    }

    public int getToCol() {
        return toCol;
    }

    public int getToRow() {
        return toRow;
    }

    public int getAmount() {
        return amount;
    }
}
