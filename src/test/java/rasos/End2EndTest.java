package rasos;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Collections;
import java.util.concurrent.Executors;

import static rasos.Config.ID_A;
import static rasos.Config.ID_B;

public class End2EndTest {
    @Test
    public void theGame() {
        RiskLogger logger = new StdoutRiskLogger(s -> {
        });
        Player playerA = new RabakPlayer();
        Player playerB = new BunkerPlayer();
        RoundHandler handler = new RoundHandler(
                ID_A,
                ID_B,
                playerA,
                playerB,
                new Reinforcer(logger),
                new Attacker(logger), Executors.newSingleThreadExecutor(), logger);
        Game g = new Game(5, 20, 50, playerA, playerB, handler, new GameEndChecker(ID_A, ID_B), logger);

        g.start();
    }

    private class BunkerPlayer extends Player {

        private int turn = 0;

        @Override
        public Iterable<ReinforcementMove> onReinforcement(Board board, int reinforcement) {
            switch (++turn) {
                case 1:
                    return Lists.newArrayList(new ReinforcementMove(1, 1, 1));
                case 2:
                    return Lists.newArrayList(
                            new ReinforcementMove(1, 1, 1),
                            new ReinforcementMove(2, 2, reinforcement - 1));
                case 3:
                    return Lists.newArrayList(
                            new ReinforcementMove(1, 1, 1),
                            new ReinforcementMove(2, 2, 1),
                            new ReinforcementMove(3, 3, reinforcement - 1));
                case 4:
                    return Lists.newArrayList(
                            new ReinforcementMove(1, 1, 1),
                            new ReinforcementMove(2, 2, 1),
                            new ReinforcementMove(3, 3, 1),
                            new ReinforcementMove(4, 4, reinforcement - 1));
            }
            throw new RuntimeException("Exceeded the expected turns number");
        }

        @Override
        public Iterable<AttackMove> onAttack(Board board) {
            int soldiers = board.cellAt(turn, turn).getNumSoldiers();
            AttackMove move = new AttackMove(turn, turn, turn + 1, turn + 1, soldiers - 1);
            return Collections.singleton(move);
        }
    }

    private class RabakPlayer extends Player {

        @Override
        public Iterable<ReinforcementMove> onReinforcement(Board board, int reinforcement) {
            return null;
        }

        @Override
        public Iterable<AttackMove> onAttack(Board board) {
            return null;
        }
    }
}
