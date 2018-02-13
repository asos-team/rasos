package rasos;

public class Cell {
    static final String NEUTRAL_CELL_CONTAINING_SOLDIERS_ERROR = "A neutral cell must not contain any soldiers.";
    static final String CONTROLLED_CELL_WITH_ZERO_SOLDIERS_ERROR = "A cell can't be controlled by a player without having any soldiers in it";
    static final String NEGATIVE_CONTROLLING_PLAYER_ID_ERROR = "Non-positive controlling player ID is not allowed.";
    static final String NEGATIVE_AMOUNT_OF_SOLDIERS_ERROR = "Negative amount of soldiers is not allowed.";

    private int controllingPlayerId;
    private int numSoldiers;

    Cell(int controllingPlayerId, int numSoldiers) {
        setValues(controllingPlayerId, numSoldiers);
    }

    static Cell neutral() {
        return new Cell(0, 0);
    }

    private void throwOnInvalidSetParams(int controllingPlayerId, int numSoldiers) {
        if (controllingPlayerId == 0 && numSoldiers != 0)
            throw new RuntimeException(NEUTRAL_CELL_CONTAINING_SOLDIERS_ERROR);
        if (controllingPlayerId != 0 && numSoldiers == 0)
            throw new RuntimeException(CONTROLLED_CELL_WITH_ZERO_SOLDIERS_ERROR);
        if (controllingPlayerId < 0)
            throw new RuntimeException(NEGATIVE_CONTROLLING_PLAYER_ID_ERROR);
        checkNotNegative(numSoldiers);
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

    int getNumSoldiers() {
        return numSoldiers;
    }

    public int getControllingPlayerId() {
        return controllingPlayerId;
    }

    boolean isControlledBy(int playerId) {
        return controllingPlayerId == playerId;
    }

    boolean isNeutral() {
        return controllingPlayerId == 0;
    }

    void updateNumSoldiers(int numSoldiers) {
        checkNotNegative(numSoldiers);
        setNumSoldiersAccordingToValue(numSoldiers);
    }

    public void makeNeutral() {
        numSoldiers = 0;
        controllingPlayerId = 0;
    }

    public void setValues(int controllingPlayerId, int numSoldiers) {
        throwOnInvalidSetParams(controllingPlayerId, numSoldiers);
        this.controllingPlayerId = controllingPlayerId;
        this.numSoldiers = numSoldiers;
    }

    private void checkNotNegative(int numSoldiers) {
        if (numSoldiers < 0)
            throw new RuntimeException(NEGATIVE_AMOUNT_OF_SOLDIERS_ERROR);
    }

    private void setNumSoldiersAccordingToValue(int numSoldiers) {
        if (numSoldiers == 0) {
            makeNeutral();
        } else {
            this.numSoldiers = numSoldiers;
        }
    }
}
