public class Cell {
    private int controllingPlayer;
    private int numSoldiers;

    public Cell() {
        controllingPlayer = 0;
        numSoldiers = 0;
    }

    public Cell(int controllingPlayer, int numSoldiers) {
        this.controllingPlayer = controllingPlayer;
        this.numSoldiers = numSoldiers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        if (controllingPlayer != cell.controllingPlayer) return false;
        return numSoldiers == cell.numSoldiers;
    }

    @Override
    public int hashCode() {
        int result = controllingPlayer;
        result = 31 * result + numSoldiers;
        return result;
    }

    public int getControllingPlayer() {
        return controllingPlayer;
    }

    public int getNumSoldiers() {
        return numSoldiers;
    }
}
