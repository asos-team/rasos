public class AttackMove {
    private final int originCol;
    private final int originRow;
    private final int destCol;
    private final int destRow;
    private final int amount;

    public AttackMove(int originCol, int originRow, int destCol, int destRow, int amount) {
        this.originCol = originCol;
        this.originRow = originRow;
        this.destCol = destCol;
        this.destRow = destRow;
        this.amount = amount;
    }

    public int getOriginCol() {
        return originCol;
    }

    public int getOriginRow() {
        return originRow;
    }

    public int getDestCol() {
        return destCol;
    }

    public int getDestRow() {
        return destRow;
    }

    public int getAmount() {
        return amount;
    }
}
