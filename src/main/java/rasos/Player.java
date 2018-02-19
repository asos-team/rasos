package rasos;

public abstract class Player {

    private int playerId;

    public abstract Iterable<ReinforcementMove> onReinforcement(Board board, int reinforcement);

    public abstract Iterable<AttackMove> onAttack(Board board);

    void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    protected Iterable<CellCoordinates> getMyCellsCoordinates(Board board){
        return board.getControlledCoordinates(playerId);
    }
}
