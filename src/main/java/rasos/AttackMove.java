package rasos;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttackMove that = (AttackMove) o;
        return originCol == that.originCol &&
                originRow == that.originRow &&
                destCol == that.destCol &&
                destRow == that.destRow &&
                amount == that.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(originCol, originRow, destCol, destRow, amount);
    }

    @Override
    public String toString() {
        return "AttackMove{" +
                "originCol=" + originCol +
                ", originRow=" + originRow +
                ", destCol=" + destCol +
                ", destRow=" + destRow +
                ", amount=" + amount +
                '}';
    }
}
