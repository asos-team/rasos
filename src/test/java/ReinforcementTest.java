import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ReinforcementTest {

    private static final int NO_SOLDIERS = 0;
    @Rule
    public final ExpectedException expectedEx = ExpectedException.none();
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
    @Ignore
    public void throwsOnNullReinforcementMoves(){
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Null reinforcement is not allowed");

        game.start();
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
    public void throwsOnTryToReinforceACellThatYouDoNotControl() throws Exception {
        Board board = game.getBoard();
        board.setCell(1, 1, new Cell(1, 4));
        when(player1.onReinforcement(any(Board.class), any(int.class)))
                .thenReturn(Collections.singleton(new ReinforcementMove(3, 1, 1)));

        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("You cannot reinforce a cell that you don't control");

        game.start();
    }

    @Test(expected = RuntimeException.class)
    public void reinforceOnlyTheAmountOfSoldiersYouAreAllowed() throws Exception {
        Board board = game.getBoard();
        board.setCell(3, 1, new Cell(1, 4));
        when(player1.onReinforcement(any(Board.class), eq(1)))
                .thenReturn(Collections.singleton(new ReinforcementMove(3, 1, 5)));

        game.start();
    }

    @Test(expected = RuntimeException.class)
    public void sumOfReinforcementsPerTurnIsCoherentWithPredefinedAmount() throws Exception {
        Board board = game.getBoard();
        board.setCell(3, 2, new Cell(2, 7));
        board.setCell(3, 1, new Cell(2, 3));
        ArrayList<ReinforcementMove> moves = new ArrayList<>();
        moves.add(new ReinforcementMove(3, 2, 1));
        moves.add(new ReinforcementMove(3, 1, 2));

        when(player2.onReinforcement(any(Board.class), eq(2))).thenReturn(moves);

        game.start();
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