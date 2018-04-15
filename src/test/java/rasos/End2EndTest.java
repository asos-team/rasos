package rasos;

import org.junit.Before;
import org.junit.Test;
import rasos.players.AttackPlayer;

import java.util.Collections;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static rasos.Config.ID_A;
import static rasos.Config.ID_B;

public class End2EndTest {

    private RiskLogger logger;

    @Before
    public void setUp() {
        logger = spy(StdoutRiskLogger.class);
    }

    @Test
    public void simpleGame() {
        Player playerA = new AttackPlayer();
        Player playerB = new AttackPlayer();
        RoundHandler handler = new RoundHandler(
                ID_A,
                ID_B,
                playerA,
                playerB,
                new Reinforcer(logger),
                new Attacker(logger), Executors.newSingleThreadExecutor(), logger);
        Game g = new Game(5, 20, 50, playerA, ID_A, playerB, ID_B, handler, new GameEndChecker(ID_A, ID_B), logger);

        g.start();
    }

    @Test
    public void complexGame() {
        Player rabakist = new RabakPlayer();
        Player bunker = new BunkerPlayer();
        RoundHandler handler = new RoundHandler(
                ID_A,
                ID_B,
                rabakist,
                bunker,
                new Reinforcer(logger),
                new Attacker(logger), Executors.newSingleThreadExecutor(), logger);
        Game g = new Game(5, 20, 50, rabakist, ID_A, bunker, ID_B, handler, new GameEndChecker(ID_A, ID_B), logger);

        g.start();

        verify(logger).logGameEnd(ID_A);
    }

    private class RabakPlayer extends Player {

        private int turn = 0;

        @Override
        public Iterable<ReinforcementMove> onReinforcement(Board board, int reinforcement) {
            turn++;
            return Collections.singleton(new ReinforcementMove(turn, turn, reinforcement));
        }

        @Override
        public Iterable<AttackMove> onAttack(Board board) {
            int soldiers = board.cellAt(turn, turn).getNumSoldiers();
            AttackMove move = new AttackMove(turn, turn, turn + 1, turn + 1, soldiers - 1);
            return Collections.singleton(move);
        }
    }

    private class BunkerPlayer extends Player {

        @Override
        public Iterable<ReinforcementMove> onReinforcement(Board board, int reinforcement) {
            return Collections.singleton(new ReinforcementMove(board.getDim(), board.getDim(), reinforcement));
        }

        @Override
        public Iterable<AttackMove> onAttack(Board board) {
            return null;
        }
    }
}
