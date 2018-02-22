package rasos;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

public class JsPlayerTest {
    @Rule
    public final ExpectedException expectedEx = ExpectedException.none();

    private Board board;

    @Before
    public void setUp() {
        board = mock(Board.class);
    }

    @Test
    public void corruptedPlayerReturnsEmptyList() {
        String script = "script";
        Player player = new JsPlayer(script);
        Iterable<ReinforcementMove> moves = player.onReinforcement(board, 0);
        assertFalse(moves.iterator().hasNext());
    }

    @Test
    public void onUnparsableReinforcementMoveReturnsEmptyList() {
        String script = "function onReinforcement(board, soldiers) { return [{'adom':'yes', 'yarok':'yes', 'garinimShelAvatiach':'yes'}]; }";
        Player player = new JsPlayer(script);
        Iterable<ReinforcementMove> moves = player.onReinforcement(board, 0);
        assertFalse(moves.iterator().hasNext());
    }

    @Test
    public void onUnparsableAttackMoveReturnsEmptyList() {
        String script = "function onAttack(board) { return [{'hamburger': 'sandwich', 'hotdog':'sandwich', 'burrito':'not_sandwich'}]; }";
        Player player = new JsPlayer(script);
        Iterable<AttackMove> moves = player.onAttack(board);
        assertFalse(moves.iterator().hasNext());
    }

    @Test
    public void playerReturnsReinforcementMoves() {
        String script = "function onReinforcement(board, soldiers) { return [{'col':1, 'row':2, 'amount':5}]; }";
        Player player = new JsPlayer(script);
        Iterable<ReinforcementMove> moves = player.onReinforcement(board, 0);
        assertEquals(new ReinforcementMove(1, 2, 5), moves.iterator().next());
    }

    @Test
    public void playerReturnsAttackMoves() {
        String script = "function onAttack(board) { return [{'originCol':1, 'originRow':2,'destCol':3, 'destRow':4, 'amount':5}]; }";
        Player player = new JsPlayer(script);
        Iterable<AttackMove> moves = player.onAttack(board);
        assertEquals(new AttackMove(1, 2, 3, 4, 5), moves.iterator().next());
    }
}