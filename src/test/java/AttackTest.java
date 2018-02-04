import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class AttackTest {

    private static final int boardDim = 2;
    private Game game;
    private Attacker attacker;
    private Player playerA;
    private Player playerB;
    private Board board;

    @Before
    public void setUp() {
        playerA = mock(Player.class);
        playerB = mock(Player.class);
        when(playerA.onReinforcement(any(Board.class), any(int.class))).thenReturn(Lists.newArrayList());
        when(playerB.onReinforcement(any(Board.class), any(int.class))).thenReturn(Lists.newArrayList());
        when(playerA.onAttack(any(Board.class))).thenReturn(Lists.newArrayList());
        when(playerB.onAttack(any(Board.class))).thenReturn(Lists.newArrayList());
        game = new Game(boardDim, 20, playerA, playerB);

        attacker = new Attacker();
        board = new Board(boardDim);
        board.populateHomeBases(20);
    }

    @Test
    public void callsPlayerOnAttackWithGameBoard() {
        game.start();

        verify(playerA).onAttack(game.getBoard());
        verify(playerB).onAttack(game.getBoard());
    }

    @Test
    public void appliesAttackMoves() {
        AttackMove attackMove = new AttackMove(1, 1, 2, 1, 2);
        Set<AttackMove> attackMoves = Collections.singleton(attackMove);

        attacker.apply(1, attackMoves, board);

        TestUtils.assertCellContents(board.getHome1Cell(), 1, 18);
        TestUtils.assertCellContents(board.cellAt(2, 1), 1, 2);
    }
}