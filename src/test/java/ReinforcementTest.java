import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ReinforcementTest {

    private static final int NO_SOLDIERS = 0;
    private Player player1;
    private Player player2;
    private Game game;

    @Before
    public void setUp() {
        int boardDim = 3;
        player1 = mock(Player.class);
        player2 = mock(Player.class);
        game = new Game(boardDim, NO_SOLDIERS, player1, player2);
    }

    @Test
    public void callsPlayerOnReinforcementWithGameBoard() {
        game.start();

        verify(player1).onReinforcement(eq(game.getBoard()), any(int.class));
        verify(player2).onReinforcement(eq(game.getBoard()), any(int.class));
    }

    @Test
    public void callsPlayerOnReinforcementWithNumberOfSoldiers() {
        makePlayer1ControlTotalOf_3_Cells();
        makePlayer2ControlTotalOf_2_Cells();

        game.start();

        verify(player1).onReinforcement(any(Board.class), eq(3));
        verify(player2).onReinforcement(any(Board.class), eq(2));
    }

    @Test
    public void appliesReinforcementSimplestCase_player1() {
        testAppliesReinforcementSimplestCase(player1, 1);
    }

    @Test
    public void appliesReinforcementSimplestCase_player2() {
        testAppliesReinforcementSimplestCase(player2, 2);
    }

    @Test
    public void impossibleToReinforceACellThatYouDoNotControl() throws Exception {
        Board board = game.getBoard();
        board.setCell(1, 1, new Cell(1, 4));
        when(player1.onReinforcement(any(Board.class), any(int.class)))
                .thenReturn(Collections.singleton(new ReinforcementMove(3, 1, 1)));

        game.start();

        assertTrue("cell should stay neutral but was reinforced", board.getCell(3, 1).isNeutral());
    }

    // doesNotAllowReinforcementOfNonEligibleSoldiers (but does reinforce the eligible amount as long as valid)
    // allowsMultipleReinforcementMoves

    private void makePlayer1ControlTotalOf_3_Cells() {
        game.getBoard().setCell(1, 1, new Cell(1, 4));
        game.getBoard().setCell(1, 3, new Cell(1, 4));
        game.getBoard().setCell(3, 1, new Cell(1, 19));
    }

    private void makePlayer2ControlTotalOf_2_Cells() {
        game.getBoard().setCell(2, 2, new Cell(2, 2));
        game.getBoard().setCell(3, 2, new Cell(2, 2));
    }

    private void testAppliesReinforcementSimplestCase(Player player, int playerId) {
        int soldiers = 13;
        game.getBoard().setCell(2, 3, new Cell(playerId, soldiers));
        when(player.onReinforcement(any(Board.class), any(int.class)))
                .thenReturn(Collections.singleton(new ReinforcementMove(2, 3, 1)));

        game.start();

        Board board = game.getBoard();
        TestUtils.assertCellContents(board.getCell(2, 3), playerId, soldiers + 1);
    }
}