package rasos;

public abstract class Player {

    private int playerId;

    public abstract Iterable<ReinforcementMove> onReinforcement(Board board, int reinforcement);

    public abstract Iterable<AttackMove> onAttack(Board board);

    int getPlayerId() {
        return playerId;
    }

    void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
