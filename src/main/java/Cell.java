public class Cell {
    static final String NEUTRAL_CELL_CONTAINING_SOLDIERS_ERROR = "A neutral cell must not contain any soldiers.";
    static final String NEGATIVE_CONTROLLING_PLAYER_ID_ERROR = "Negative controlling player ID is not allowed.";
    static final String NEGATIVE_AMOUNT_OF_SOLDIERS_ERROR = "Negative amount of soldiers is not allowed.";
    private int controllingPlayerId;
    private int numSoldiers;

    Cell(int controllingPlayerId, int numSoldiers) {
        validateParams(controllingPlayerId, numSoldiers);
        this.numSoldiers = numSoldiers;
        this.controllingPlayerId = decideControllingPlayerId(controllingPlayerId, numSoldiers);
    }

    static Cell neutral() {
        return new Cell(0, 0);
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        if (controllingPlayerId != cell.controllingPlayerId) return false;
        return numSoldiers == cell.numSoldiers;
    }

    @Override
    public int hashCode() {
        int result = controllingPlayerId;
        result = 31 * result + numSoldiers;
        return result;
    }

    @Override
    public String toString() {
        return String.format("[%d,%d]", numSoldiers, controllingPlayerId);
    }

    void setControllingPlayerId(int controllingPlayerId) {
        this.controllingPlayerId = controllingPlayerId;
    }

    int getNumSoldiers() {
        return numSoldiers;
    }

    void setNumSoldiers(int numSoldiers) {
        validateNumSoldiers(numSoldiers);
        this.numSoldiers = numSoldiers;
    }

    private void validateNumSoldiers(int numSoldiers) {
        if (numSoldiers < 0)
            throw new RuntimeException(NEGATIVE_AMOUNT_OF_SOLDIERS_ERROR);
    }

    boolean isNeutral() {
        return controllingPlayerId == 0;
    }

    boolean isControlledBy(int playerId) {
        return controllingPlayerId == playerId;
    }

    private int decideControllingPlayerId(int controllingPlayerId, int numSoldiers) {
        if (numSoldiers == 0) {
            return 0;
        } else {
            return controllingPlayerId;
        }
    }

    private void validateParams(int controllingPlayerId, int numSoldiers) {
        if (controllingPlayerId == 0 && numSoldiers != 0)
            throw new RuntimeException(NEUTRAL_CELL_CONTAINING_SOLDIERS_ERROR);
        else if (controllingPlayerId < 0)
            throw new IllegalArgumentException(NEGATIVE_CONTROLLING_PLAYER_ID_ERROR);
        else validateNumSoldiers(numSoldiers);
    }
}
