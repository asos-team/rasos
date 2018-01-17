import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class GameTest {

    private static final int boardDim = 2;
    private Game game;
    private Player player1;
    private Player player2;

    @Before
    public void setUp() {
        player1 = mock(Player.class);
        player2 = mock(Player.class);
        when(player1.onReinforcement(any(Cell[][].class), any(int.class))).thenReturn(Lists.<ReinforcementMove>newArrayList());
        when(player2.onReinforcement(any(Cell[][].class), any(int.class))).thenReturn(Lists.<ReinforcementMove>newArrayList());
        when(player1.onAttack(any(Cell[][].class))).thenReturn(Lists.<AttackMove>newArrayList());
        when(player2.onAttack(any(Cell[][].class))).thenReturn(Lists.<AttackMove>newArrayList());
        game = new Game(boardDim, boardDim, player1, player2);
    }

    @Test
    public void getsBoardDimensions() {
        Cell[][] board = game.getBoard();

        assertThat(board.length, is(boardDim));
        assertThat(board[0].length, is(boardDim));
    }

    @Test
    public void initializeWithSpecialBoardConfiguration() {
        Cell[][] configuration = BoardUtils.getDefaultBoard(2, 2);
        Game g = new Game(configuration, player1, player2);
        assertThat(g.getBoard(), is(configuration));
    }

    @Test(expected = RuntimeException.class)
    public void throwsOnUninitializedBoardConfiguration() {
        Cell[][] configuration = new Cell[2][2];
        new Game(configuration, player1, player2);
    }

    @Test
    public void callsPlayerOnReinforcementWithSuitableNumberOfSoldiers() {
        game.tick();
        verify(player1).onReinforcement(game.getBoard(), 1);
        verify(player2).onReinforcement(game.getBoard(), 1);
    }

    @Test
    public void callsPlayerReinforcementOnSpecialBoardConfiguration() {
        Cell[][] configuration = BoardUtils.getDefaultBoard(2, 2);
        configuration[0][1] = new Cell(1, 4);
        Game g = new Game(configuration, player1, player2);

        g.tick();

        verify(player1).onReinforcement(configuration, 2);
        verify(player2).onReinforcement(configuration, 1);
    }

    @Test
    public void appliesReinforcement() {
        when(player1.onReinforcement(any(Cell[][].class), any(int.class)))
                .thenReturn(Lists.newArrayList(new ReinforcementMove(0, 0, 1)));
        when(player2.onReinforcement(any(Cell[][].class), any(int.class)))
                .thenReturn(Lists.newArrayList(new ReinforcementMove(boardDim - 1, boardDim - 1, 1)));

        game.tick();

        Cell[][] board = game.getBoard();
        TestUtils.assertCellContents(board[0][0], 1, 21);
        TestUtils.assertCellContents(board[boardDim - 1][boardDim - 1], 2, 21);
    }

    @Test
    public void callsPlayerOnAttackWithSuitableArguments() {
        game.tick();
        verify(player1).onAttack(game.getBoard());
        verify(player2).onAttack(game.getBoard());
    }

    @Test
    public void appliesAttackMoves() {
        when(player1.onAttack(any(Cell[][].class))).thenReturn(Collections.singleton(new AttackMove(0, 0, 1, 0, 1)));
        when(player2.onAttack(any(Cell[][].class))).thenReturn(Collections.singleton(new AttackMove(1, 1, boardDim - 2, boardDim - 1, 1)));
        game.tick();
        Cell[][] board = game.getBoard();
        TestUtils.assertCellContents(board[0][0], 1, 19);
        TestUtils.assertCellContents(board[1][0], 1, 1);
        TestUtils.assertCellContents(board[boardDim - 2][boardDim - 1], 2, 1);
        TestUtils.assertCellContents(board[boardDim - 1][boardDim - 1], 2, 19);
    }

    @Test
    public void appliesAttackMovesAmount() {
        when(player1.onAttack(any(Cell[][].class))).thenReturn(Collections.singleton(new AttackMove(0, 0, 1, 0, 5)));
        when(player2.onAttack(any(Cell[][].class))).thenReturn(Collections.singleton(new AttackMove(1, 1, boardDim - 2, boardDim - 1, 2)));
        game.tick();
        Cell[][] board = game.getBoard();
        TestUtils.assertCellContents(board[0][0], 1, 15);
        TestUtils.assertCellContents(board[1][0], 1, 5);
        TestUtils.assertCellContents(board[boardDim - 2][boardDim - 1], 2, 2);
        TestUtils.assertCellContents(board[boardDim - 1][boardDim - 1], 2, 18);
    }

    @Test
    public void appliesManyAttackMovesPlayer1() {
        testAppliesManyAttackMoves(player1, 1, new AttackMove(0, 0, 0, 1, 4), new AttackMove(0, 0, 1, 0, 2));
    }

    @Test
    public void appliesManyAttackMovesPlayer2() {
        testAppliesManyAttackMoves(player2, 2, new AttackMove(1, 1, 0, 1, 4), new AttackMove(1, 1, 1, 0, 2));
    }

    private void testAppliesManyAttackMoves(Player player, int playerId, AttackMove move1, AttackMove move2) {
        when(player.onAttack(any(Cell[][].class))).thenReturn(Arrays.asList(move1, move2));
        game.tick();
        Cell[][] board = game.getBoard();
        Cell playerHome = playerId == 1 ? board[0][0] : board[boardDim - 1][boardDim - 1];
        Cell otherPlayerHome = playerId == 1 ? board[boardDim - 1][boardDim - 1] : board[0][0];
        TestUtils.assertCellContents(playerHome, playerId, 14);
        TestUtils.assertCellContents(board[0][1], playerId, 4);
        TestUtils.assertCellContents(board[1][0], playerId, 2);
        int otherPlayerId = 3 - playerId;
        TestUtils.assertCellContents(otherPlayerHome, otherPlayerId, 20);
    }
}