public class AttackMove {
    private int originCol;
    private int originRow;
    private int destCol;
    private int destRow;
    private int amount;

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
