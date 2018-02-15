package rasos;

public abstract class Player {
    abstract Iterable<ReinforcementMove> onReinforcement(Board board, int reinforcement);

    abstract Iterable<AttackMove> onAttack(Board board);
}
