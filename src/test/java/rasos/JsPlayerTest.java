package rasos;

import jdk.nashorn.api.scripting.JSObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static rasos.JsPlayer.ATTACK_JS_FUNCTION_NAME;
import static rasos.JsPlayer.REINFORCEMENT_JS_FUNCTION_NAME;

public class JsPlayerTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private Board board;
    private JsonParser parser;

    @Before
    public void setUp() {
        board = mock(Board.class);
        parser = mock(JsonParser.class);
    }

    @Test
    public void throwOnCorruptedPlayerImplementation() {
        String script = "script";
        Player player = new JsPlayer(script, parser);
        expectedException.expect(RuntimeException.class);

        player.onReinforcement(board, 0);
    }

    @Test
    public void throwOnUnparsableReinforcement() {
        String script = "function " + REINFORCEMENT_JS_FUNCTION_NAME + "(board, soldiers) { return [{'adom':'yes', 'yarok':'yes', 'garinimShelAvatiach':'yes'}]; }";
        Player player = new JsPlayer(script, parser);
        expectedException.expect(RuntimeException.class);
        when(parser.extractMovesFromJSResult(any(JSObject.class), any()))
                .thenThrow(new RuntimeException());
        player.onReinforcement(board, 0);
    }

    @Test
    public void throwOnUnparsableAttack() {
        String script = "function " + ATTACK_JS_FUNCTION_NAME + "(board) { return [{'hamburger': 'sandwich', 'hotdog':'sandwich', 'burrito':'not_sandwich'}]; }";
        Player player = new JsPlayer(script, parser);
        expectedException.expect(RuntimeException.class);
        when(parser.extractMovesFromJSResult(any(JSObject.class), any()))
                .thenThrow(new RuntimeException());

        player.onAttack(board);
    }
}