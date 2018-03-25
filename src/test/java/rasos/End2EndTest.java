package rasos;

import org.junit.Test;
import rasos.players.AttackPlayer;

import java.util.concurrent.Executors;

public class End2EndTest {
    @Test
    public void theGame() {
        RiskLogger logger = new StdoutRiskLogger(s -> {});
        AttackPlayer playerA = new AttackPlayer();
        AttackPlayer playerB = new AttackPlayer();
        RoundHandler handler = new RoundHandler(
                playerA,
                playerB,
                new Reinforcer(logger),
                new Attacker(logger),
                Executors.newSingleThreadExecutor(),
                logger);
        Game g = new Game(5, 20, 50, playerA, playerB, handler, new GameEndChecker(), logger);

        g.start();
    }
}
