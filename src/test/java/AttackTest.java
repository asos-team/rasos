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
    private Player playerA;
    private Player playerB;

    @Before
    public void setUp() {
        playerA = mock(Player.class);
        playerB = mock(Player.class);
        when(playerA.onReinforcement(any(Board.class), any(int.class))).thenReturn(Lists.newArrayList());
        when(playerB.onReinforcement(any(Board.class), any(int.class))).thenReturn(Lists.newArrayList());
        when(playerA.onAttack(any(Board.class))).thenReturn(Lists.newArrayList());
        when(playerB.onAttack(any(Board.class))).thenReturn(Lists.newArrayList());
        game = new Game(boardDim, 20, playerA, playerB);
    }

    @Test
    public void callsPlayerOnAttackWithGameBoard() {
        game.start();

        verify(playerA).onAttack(game.getBoard());
        verify(playerB).onAttack(game.getBoard());
    }

    @Test
    public void appliesAttackMoves() {
        when(playerA.onAttack(any(Board.class))).thenReturn(Collections.singleton(new AttackMove(1, 1, 2, 1, 1)));
        when(playerB.onAttack(any(Board.class))).thenReturn(Collections.singleton(new AttackMove(2, 2, 1, 2, 1)));
        game.start();
        Board board = game.getBoard();
        TestUtils.assertCellContents(board.getHome1Cell(), 1, 19);
        TestUtils.assertCellContents(board.cellAt(2, 1), 1, 1);
        TestUtils.assertCellContents(board.cellAt(1, 2), 2, 1);
        TestUtils.assertCellContents(board.getHome2Cell(), 2, 19);
    }

    @Test
    public void appliesAttackMovesAmount() {
        when(playerA.onAttack(any(Board.class))).thenReturn(Collections.singleton(new AttackMove(1, 1, 2, 1, 5)));
        when(playerB.onAttack(any(Board.class))).thenReturn(Collections.singleton(new AttackMove(2, 2, 1, 2, 2)));
        game.start();
        Board board = game.getBoard();
        TestUtils.assertCellContents(board.getHome1Cell(), 1, 15);
        TestUtils.assertCellContents(board.cellAt(2, 1), 1, 5);
        TestUtils.assertCellContents(board.cellAt(1, 2), 2, 2);
        TestUtils.assertCellContents(board.getHome2Cell(), 2, 18);
    }

    @Test
    public void appliesManyAttackMovesPlayerA() {
        testAppliesManyAttackMoves(playerA, 1, new AttackMove(1, 1, 1, 2, 4), new AttackMove(1, 1, 2, 1, 2));
    }

    @Test
    public void appliesManyAttackMovesPlayerB() {
        testAppliesManyAttackMoves(playerB, 2, new AttackMove(2, 2, 1, 2, 4), new AttackMove(2, 2, 2, 1, 2));
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