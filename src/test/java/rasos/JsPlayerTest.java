package rasos;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static rasos.JsPlayer.ATTACK_JS_FUNCTION_NAME;
import static rasos.JsPlayer.REINFORCEMENT_JS_FUNCTION_NAME;

public class JsPlayerTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private Board board;

    @Before
    public void setUp() {
        board = mock(Board.class);
    }

    @Test
    public void throwOnCorruptedPlayerImplementation() {
        String script = "script";
        Player player = new JsPlayer(script);
        expectedException.expect(RuntimeException.class);

        player.onReinforcement(board, 0);
    }

    @Test
    public void throwOnUnparsableReinforcement() {
        String script = "function " + REINFORCEMENT_JS_FUNCTION_NAME + "(board, soldiers) { return [{'adom':'yes', 'yarok':'yes', 'garinimShelAvatiach':'yes'}]; }";
        Player player = new JsPlayer(script);
        expectedException.expect(RuntimeException.class);

        player.onReinforcement(board, 0);
    }

    @Test
    public void throwOnUnparsableAttack() {
        String script = "function " + ATTACK_JS_FUNCTION_NAME + "(board) { return [{'hamburger': 'sandwich', 'hotdog':'sandwich', 'burrito':'not_sandwich'}]; }";
        Player player = new JsPlayer(script);
        expectedException.expect(RuntimeException.class);

        player.onAttack(board);
    }

    @Test
    public void playerReturnsReinforcementMoves() {
        String script = "function " + REINFORCEMENT_JS_FUNCTION_NAME + "(board, soldiers) { return [{'col':1, 'row':2, 'amount':5}]; }";
        Player player = new JsPlayer(script);
        Iterable<ReinforcementMove> moves = player.onReinforcement(board, 0);
        assertEquals(new ReinforcementMove(1, 2, 5), moves.iterator().next());
    }

    @Test
    public void playerReturnsAttackMoves() {
        String script = "function " + ATTACK_JS_FUNCTION_NAME + "(board) { return [{'originCol':1, 'originRow':2, 'destCol':3, 'destRow':4, 'amount':5}]; }";
        Player player = new JsPlayer(script);
        Iterable<AttackMove> moves = player.onAttack(board);
        assertEquals(new AttackMove(1, 2, 3, 4, 5), moves.iterator().next());
    }
}