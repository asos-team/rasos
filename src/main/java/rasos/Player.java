package rasos;

public interface Player {
    Iterable<ReinforcementMove> onReinforcement(Board board, int reinforcement);

    Iterable<AttackMove> onAttack(Board board);
}
