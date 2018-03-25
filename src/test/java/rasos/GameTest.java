package rasos;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import rasos.players.AttackPlayer;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.concurrent.Executors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static rasos.Config.ID_A;
import static rasos.Config.ID_B;

public class GameTest {

    private static final int NO_SOLDIERS = 0;
    private int boardDim;
    private Player playerA;
    private Player playerB;
    private Game game;
    private RoundHandler handler;
    private GameEndChecker checker;
    private RiskLogger logger;

    @Before
    public void setUp() {
        boardDim = 7;
        playerA = mock(Player.class);
        playerB = mock(Player.class);
        handler = mock(RoundHandler.class);
        checker = mock(GameEndChecker.class);
        logger = mock(RiskLogger.class);
        game = createSimpleGame(NO_SOLDIERS);
    }

    @Test
    public void gameWithShittyJsPlayer() {
        RiskLogger logger = new StdoutRiskLogger(s -> {});
        String script = "function onGameStart(playerId){};" +
                "function onReinforcement(board, reinforcement){" +
                "return [{col:1,row:1,amount:board.configuration[0][0].numSoldiers}];" +
                "}; function onAttack(board){" +
                "throw 'fucking mega lol';};";

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        JsonParser parser = new JsonParser();
        JsPlayer playerA = new JsPlayer(script, engine, parser, logger);
        AttackPlayer playerB = new AttackPlayer();
        RoundHandler handler = new RoundHandler(
                playerA,
                playerB,
                new Reinforcer(logger),
                new Attacker(logger),
                Executors.newSingleThreadExecutor(),
                logger);
        Game g = new Game(5, 20, 50, playerA, playerB, handler, new GameEndChecker(ID_A, ID_B), logger);

        g.start();
    }

    @Test
    public void initializesWithSpecifiedDimension() {
        assertThat(game.getBoard().getDim(), is(boardDim));
    }

    @Test
    public void populatesHomeBasesWithSpecifiedNumberOfSoldiers() {
        int soldiers = 99;
        Game game = createSimpleGame(soldiers);

        TestUtils.assertCellContents(game.getBoard().getHome1Cell(), 1, soldiers);
        TestUtils.assertCellContents(game.getBoard().getHome2Cell(), 2, soldiers);
    }

    @Test
    public void assignPlayerIds() {
        verify(playerA).setPlayerId(ID_A);
        verify(playerB).setPlayerId(ID_B);
    }

    @Test
    public void differentPlayerIds() {
        assertNotEquals("Players should have different ids", ID_A, ID_B);
    }

    @Test
    public void callsRoundHandlerWithBoard() {
        game.start();
        verify(handler).playOneRound(game.getBoard());
    }

    @Test
    public void gameEndsAfterDesiredNumberOfRounds() {
        int rounds = 13;
        Game game = createLongGame(rounds);
        game.start();
        verify(handler, times(rounds)).playOneRound(game.getBoard());
    }

    @Test
    public void gameChecksForGameEndingEachRound() {
        int rounds = 13;
        Game game = createLongGame(rounds);
        game.start();
        verify(checker, times(rounds)).isEndOfGame(game.getBoard());
    }

    @Test
    public void whenGameEndsItNoLongerPlayRounds() {
        int rounds = 8;
        when(checker.isEndOfGame(any(Board.class))).thenReturn(false, false, false, true);
        Game game = createLongGame(rounds);
        game.start();
        verify(handler, times(3)).playOneRound(game.getBoard());
    }

    @Test
    public void gameCallsLogStartOnMatchStart() {
        InOrder inOrder = inOrder(logger, handler);
        game.start();

        inOrder.verify(logger).logGameStart();
        inOrder.verify(handler, atLeastOnce()).playOneRound(any(Board.class));
    }

    @Test
    public void gameCallsLogEndOnMatchEnd() {
        InOrder inOrder = inOrder(logger, handler);
        game.start();

        inOrder.verify(handler, atLeastOnce()).playOneRound(any(Board.class));
        inOrder.verify(logger).logGameEnd(checker.getWinnerId(any(Board.class)));
    }

    private Game createSimpleGame(int soldiers) {
        return createConfigurableGame(soldiers, 1);
    }

    private Game createLongGame(int rounds) {
        return createConfigurableGame(NO_SOLDIERS, rounds);
    }

    private Game createConfigurableGame(int soldiers, int rounds) {
        return new Game(boardDim, soldiers, rounds, playerA, playerB, handler, checker, logger);
    }
}