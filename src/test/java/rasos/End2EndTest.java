package rasos;

import org.junit.Test;
import rasos.players.AttackPlayer;

import java.util.concurrent.Executors;

public class End2EndTest {
    @Test
    public void theGame() {
        RiskLogger logger = new StdoutRiskLogger(s -> {});
        RoundHandler handler = new RoundHandler(
                new AttackPlayer(),
                new AttackPlayer(),
                new Reinforcer(logger),
                new Attacker(logger),
                Executors.newSingleThreadExecutor(),
                logger);
        Game g = new Game(5, 20, 50, handler, new GameEndChecker(), logger);

        g.start();
    }
}
