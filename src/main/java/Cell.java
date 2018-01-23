public class Cell {
    public static final String NEUTRAL_CELL_CONTAINING_SOLDIERS_ERROR = "A neutral cell must not contain any soldiers.";
    public static final String NEGATIVE_CONTROLLING_PLAYER_ID_ERROR = "Negative controlling player ID is not allowed.";
    public static final String NEGATIVE_AMOUNT_OF_SOLDIERS_ERROR = "Negative amount of soldiers is not allowed.";
    private final int controllingPlayerId;
    private final int numSoldiers;

    public Cell() {
        this(0, 0);
    }

    public Cell(int controllingPlayerId, int numSoldiers) {
        validateParams(controllingPlayerId, numSoldiers);
        this.numSoldiers = numSoldiers;
        this.controllingPlayerId = decideControllingPlayerId(controllingPlayerId, numSoldiers);
    }

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

    public boolean isControlledBy(int playerId) {
        return controllingPlayerId == playerId;
    }

    public int getNumSoldiers() {
        return numSoldiers;
    }

    public boolean isNeutral() {
        return controllingPlayerId == 0;
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
        else if (numSoldiers < 0)
            throw new RuntimeException(NEGATIVE_AMOUNT_OF_SOLDIERS_ERROR);
    }
}
