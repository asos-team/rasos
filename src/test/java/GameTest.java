import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GameTest {

    private static final int NO_SOLDIERS = 0;
    private int boardDim;
    private RiskLogger logger;
    private Game game;
    private RoundHandler handler;

    @Before
    public void setUp() {
        boardDim = 7;
        handler = mock(RoundHandler.class);
        logger = mock(RiskLogger.class);
        game = createSimpleGame(NO_SOLDIERS);
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
    public void callsRoundHandlerWithBoard() {
        game.start();
        verify(handler).playOneRound(game.getBoard());
    }

    @Test
    public void gameCallsLogStartOnMatchStart() {
        game.start();
        verify(logger).logStart();
    }

    private Game createSimpleGame(int soldiers) {
        return new Game(boardDim, soldiers, handler, logger);
    }
}