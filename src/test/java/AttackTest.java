import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@Ignore
public class AttackTest {

    private static final int boardDim = 2;
    private Game game;
    private Player player1;
    private Player player2;

    @Before
    public void setUp() {
        player1 = mock(Player.class);
        player2 = mock(Player.class);
        when(player1.onReinforcement(any(Board.class), any(int.class))).thenReturn(Lists.newArrayList());
        when(player2.onReinforcement(any(Board.class), any(int.class))).thenReturn(Lists.newArrayList());
        when(player1.onAttack(any(Board.class))).thenReturn(Lists.newArrayList());
        when(player2.onAttack(any(Board.class))).thenReturn(Lists.newArrayList());
        game = new Game(boardDim, 20, player1, player2);
    }

    @Test
    public void callsPlayerOnAttackWithGameBoard() {
        game.start();

        verify(player1).onAttack(game.getBoard());
        verify(player2).onAttack(game.getBoard());
    }

    @Test
    public void appliesAttackMoves() {
        when(player1.onAttack(any(Board.class))).thenReturn(Collections.singleton(new AttackMove(1, 1, 2, 1, 1)));
        when(player2.onAttack(any(Board.class))).thenReturn(Collections.singleton(new AttackMove(2, 2, 1, 2, 1)));
        game.start();
        Board board = game.getBoard();
        TestUtils.assertCellContents(board.getHome1Cell(), 1, 19);
        TestUtils.assertCellContents(board.cellAt(2, 1), 1, 1);
        TestUtils.assertCellContents(board.cellAt(1, 2), 2, 1);
        TestUtils.assertCellContents(board.getHome2Cell(), 2, 19);
    }

    @Test
    public void appliesAttackMovesAmount() {
        when(player1.onAttack(any(Board.class))).thenReturn(Collections.singleton(new AttackMove(1, 1, 2, 1, 5)));
        when(player2.onAttack(any(Board.class))).thenReturn(Collections.singleton(new AttackMove(2, 2, 1, 2, 2)));
        game.start();
        Board board = game.getBoard();
        TestUtils.assertCellContents(board.getHome1Cell(), 1, 15);
        TestUtils.assertCellContents(board.cellAt(2, 1), 1, 5);
        TestUtils.assertCellContents(board.cellAt(1, 2), 2, 2);
        TestUtils.assertCellContents(board.getHome2Cell(), 2, 18);
    }

    @Test
    public void appliesManyAttackMovesPlayer1() {
        testAppliesManyAttackMoves(player1, 1, new AttackMove(1, 1, 1, 2, 4), new AttackMove(1, 1, 2, 1, 2));
    }

    @Test
    public void appliesManyAttackMovesPlayer2() {
        testAppliesManyAttackMoves(player2, 2, new AttackMove(2, 2, 1, 2, 4), new AttackMove(2, 2, 2, 1, 2));
    }

    private void testAppliesManyAttackMoves(Player player, int playerId, AttackMove move1, AttackMove move2) {
        when(player.onAttack(any(Board.class))).thenReturn(Arrays.asList(move1, move2));
        game.start();
        Board board = game.getBoard();
        Cell playerHome = playerId == 1 ? board.getHome1Cell() : board.getHome2Cell();
        Cell otherPlayerHome = playerId == 1 ? board.getHome2Cell() : board.getHome1Cell();
        TestUtils.assertCellContents(playerHome, playerId, 14);
        TestUtils.assertCellContents(board.cellAt(1, 2), playerId, 4);
        TestUtils.assertCellContents(board.cellAt(2, 1), playerId, 2);
        int otherPlayerId = 3 - playerId;
        TestUtils.assertCellContents(otherPlayerHome, otherPlayerId, 20);
    }
}