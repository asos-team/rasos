package rasos;

import java.util.Collections;

public class StubPlayer implements Player {
    @Override
    public Iterable<ReinforcementMove> onReinforcement(Board board, int reinforcement) {
        return Collections.emptyList();
    }

    @Override
    public Iterable<AttackMove> onAttack(Board board) {
        return Collections.emptyList();
    }
}
