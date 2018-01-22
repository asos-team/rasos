import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class AttackTest {

    private static final int boardDim = 2;
    private Game game;
    private Player player1;
    private Player player2;

    @Before
    public void setUp() {
        player1 = mock(Player.class);
        player2 = mock(Player.class);
        when(player1.onReinforcement(any(Board.class), any(int.class))).thenReturn(Lists.<ReinforcementMove>newArrayList());
        when(player2.onReinforcement(any(Board.class), any(int.class))).thenReturn(Lists.<ReinforcementMove>newArrayList());
        when(player1.onAttack(any(Board.class))).thenReturn(Lists.<AttackMove>newArrayList());
        when(player2.onAttack(any(Board.class))).thenReturn(Lists.<AttackMove>newArrayList());
        game = new Game(boardDim, player1, player2);
    }

    @Test
    public void callsPlayerOnAttackWithGameBoard() {
        game.tick();

        verify(player1).onAttack(game.getBoard());
        verify(player2).onAttack(game.getBoard());
    }

    @Test
    public void appliesAttackMoves() {
        when(player1.onAttack(any(Board.class))).thenReturn(Collections.singleton(new AttackMove(0, 0, 1, 0, 1)));
        when(player2.onAttack(any(Board.class))).thenReturn(Collections.singleton(new AttackMove(1, 1, boardDim - 2, boardDim - 1, 1)));
        game.tick();
        Board board = game.getBoard();
        TestUtils.assertCellContents(board.getCell(0, 0), 1, 19);
        TestUtils.assertCellContents(board.getCell(1, 0), 1, 1);
        TestUtils.assertCellContents(board.getCell(boardDim - 2, boardDim - 1), 2, 1);
        TestUtils.assertCellContents(board.getCell(boardDim - 1, boardDim - 1), 2, 19);
    }

    @Test
    public void appliesAttackMovesAmount() {
        when(player1.onAttack(any(Board.class))).thenReturn(Collections.singleton(new AttackMove(0, 0, 1, 0, 5)));
        when(player2.onAttack(any(Board.class))).thenReturn(Collections.singleton(new AttackMove(1, 1, boardDim - 2, boardDim - 1, 2)));
        game.tick();
        Board board = game.getBoard();
        TestUtils.assertCellContents(board.getCell(0, 0), 1, 15);
        TestUtils.assertCellContents(board.getCell(1, 0), 1, 5);
        TestUtils.assertCellContents(board.getCell(boardDim - 2, boardDim - 1), 2, 2);
        TestUtils.assertCellContents(board.getCell(boardDim - 1, boardDim - 1), 2, 18);
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
        when(player.onAttack(any(Board.class))).thenReturn(Arrays.asList(move1, move2));
        game.tick();
        Board board = game.getBoard();
        Cell playerHome = playerId == 1 ? board.getCell(0, 0) : board.getCell(boardDim - 1, boardDim - 1);
        Cell otherPlayerHome = playerId == 1 ? board.getCell(boardDim - 1, boardDim - 1) : board.getCell(0, 0);
        TestUtils.assertCellContents(playerHome, playerId, 14);
        TestUtils.assertCellContents(board.getCell(0, 1), playerId, 4);
        TestUtils.assertCellContents(board.getCell(1, 0), playerId, 2);
        int otherPlayerId = 3 - playerId;
        TestUtils.assertCellContents(otherPlayerHome, otherPlayerId, 20);
    }
}