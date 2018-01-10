public class AttackMove {
    private int x;
    private int y;
    private int amount;

    public AttackMove(int x, int y, int amount) {
        this.x = x;
        this.y = y;
        this.amount = amount;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getAmount() {
        return amount;
    }
}
