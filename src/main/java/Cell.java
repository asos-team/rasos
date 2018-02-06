import org.omg.SendingContext.RunTime;

public class Cell {
    static final String NEUTRAL_CELL_CONTAINING_SOLDIERS_ERROR = "A neutral cell must not contain any soldiers.";
    static final String NEGATIVE_CONTROLLING_PLAYER_ID_ERROR = "Negative controlling player ID is not allowed.";
    static final String NEGATIVE_AMOUNT_OF_SOLDIERS_ERROR = "Negative amount of soldiers is not allowed.";
    static final String NON_NEUTRAL_CELL_CONTAINING_SOLDIERS = "A cell cannot be created with a controlling player but without soldiers";
    private int controllingPlayerId;
    private int numSoldiers;

    Cell(int controllingPlayerId, int numSoldiers) {
        throwOnInvalidConstructorParams(controllingPlayerId, numSoldiers);

        if (isZeroUpdate(numSoldiers)) {
            makeNeutral();
        } else {
            this.controllingPlayerId = controllingPlayerId;
            this.numSoldiers = numSoldiers;
        }

    }

    private void throwOnInvalidConstructorParams(int controllingPlayerId, int numSoldiers) {
        if (isZeroUpdate(controllingPlayerId) && !isZeroUpdate(numSoldiers))
            throw new RuntimeException(NEUTRAL_CELL_CONTAINING_SOLDIERS_ERROR);
        if (isNegativeUpdate(controllingPlayerId))
            throw new RuntimeException(NEGATIVE_CONTROLLING_PLAYER_ID_ERROR);
        if (isNegativeUpdate(numSoldiers))
            throw new RuntimeException(NEGATIVE_AMOUNT_OF_SOLDIERS_ERROR);
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
        boolean isValidUpdate = isPositiveNumSoldiersUpdate(numSoldiers);
        if (isValidUpdate)
            this.numSoldiers = numSoldiers;
    }

    private boolean isPositiveNumSoldiersUpdate(int numSoldiers) {
        if (numSoldiers < 0)
            throw new RuntimeException(NEGATIVE_AMOUNT_OF_SOLDIERS_ERROR);
        if (numSoldiers == 0) {
            makeNeutral();
            return false;
        }
        return true;
    }

    void updateControllingPlayerId(int controllingPlayerId) {
        boolean isValidUpdate = isNonPlayerSpecificControllingPlayerUpdate(controllingPlayerId);
        if (isValidUpdate)
            this.controllingPlayerId = controllingPlayerId;
    }

    private boolean isNonPlayerSpecificControllingPlayerUpdate(int controllingPlayerId) {
        if (isNegativeUpdate(controllingPlayerId))
            throw new IllegalArgumentException(NEGATIVE_CONTROLLING_PLAYER_ID_ERROR);
        if (isZeroUpdate(controllingPlayerId)) {
            makeNeutral();
            return false;
        }
        return true;
    }

    private boolean isNegativeUpdate(int newValue) {
        return newValue < 0;
    }

    private boolean isZeroUpdate(int newValue) {
        return newValue == 0;
    }

    private void makeNeutral() {
        numSoldiers = 0;
        controllingPlayerId = 0;
    }
}
