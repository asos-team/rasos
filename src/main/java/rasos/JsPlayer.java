package rasos;

import java.util.Collections;

public class JsPlayer extends Player {
    public JsPlayer(String script) {

    }

    @Override
    public Iterable<ReinforcementMove> onReinforcement(Board board, int reinforcement) {
        return Collections.emptyList();
    }

    @Override
    public Iterable<AttackMove> onAttack(Board board) {
        return null;
    }
}
