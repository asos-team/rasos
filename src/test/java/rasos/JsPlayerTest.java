package rasos;

import com.google.common.collect.Lists;
import jdk.nashorn.api.scripting.JSObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static rasos.JsPlayer.ATTACK_JS_FUNCTION_NAME;
import static rasos.JsPlayer.REINFORCEMENT_JS_FUNCTION_NAME;


//TODO: Create JsPlayerBuilder (so that JsPlayer will get only an invocable in the constructor and not a script+engine that it doesn't use
public class JsPlayerTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private Board board;
    private JsonParser parser;
    private ScriptEngine engine;
    private Invocable invocable;

    @Before
    public void setUp() {
        board = mock(Board.class);
        parser = mock(JsonParser.class);
        engine = mock(ScriptEngine.class, withSettings().extraInterfaces(Invocable.class));
        invocable = (Invocable) engine;
    }

    @Test
    public void throwRuntimeOnCorruptedPlayerImplementation() throws ScriptException, NoSuchMethodException {
        String script = "script";
        Player player = new JsPlayer(script, engine, parser);
        when(invocable.invokeFunction(eq(JsPlayer.REINFORCEMENT_JS_FUNCTION_NAME), anyVararg())).thenThrow(new RuntimeException());
        expectedException.expect(RuntimeException.class);

        player.onReinforcement(board, 0);
    }

    @Test
    public void throwRuntimeOnUnparsableReinforcement() {
        String script = "function " + REINFORCEMENT_JS_FUNCTION_NAME + "(board, soldiers) { return [{'adom':'yes', 'yarok':'yes', 'garinimShelAvatiach':'yes'}]; }";
        Player player = new JsPlayer(script, engine, parser);
        expectedException.expect(RuntimeException.class);
        when(parser.extractMovesFromJSResult(any(JSObject.class), any()))
                .thenThrow(new RuntimeException());
        player.onReinforcement(board, 0);
    }

    @Test
    public void throwRuntimeOnUnparsableAttack() {
        String script = "function " + ATTACK_JS_FUNCTION_NAME + "(board) { return [{'hamburger': 'sandwich', 'hotdog':'sandwich', 'burrito':'not_sandwich'}]; }";
        Player player = new JsPlayer(script, engine, parser);
        expectedException.expect(RuntimeException.class);
        when(parser.extractMovesFromJSResult(any(JSObject.class), any()))
                .thenThrow(new RuntimeException());

        player.onAttack(board);
    }

    @Test
    public void playerReturnsReinforcementMoves() {
        String script = "function " + REINFORCEMENT_JS_FUNCTION_NAME + "(board, soldiers) { return [{'col':1, 'row':2, 'amount':5}]; }";
        Player player = new JsPlayer(script, engine, parser);
        when(parser.extractMovesFromJSResult(any(JSObject.class), eq(ReinforcementMove[].class)))
                .thenReturn(Lists.newArrayList(new ReinforcementMove(1, 2, 5)));


        Iterable<ReinforcementMove> moves = player.onReinforcement(board, 0);

        assertEquals(new ReinforcementMove(1, 2, 5), moves.iterator().next());
    }

    @Test
    public void playerReturnsAttackMoves() {
        String script = "function " + ATTACK_JS_FUNCTION_NAME + "(board) { return [{'originCol':1, 'originRow':2, 'destCol':3, 'destRow':4, 'amount':5}]; }";
        Player player = new JsPlayer(script, engine, parser);
        when(parser.extractMovesFromJSResult(any(JSObject.class), eq(AttackMove[].class)))
                .thenReturn(Lists.newArrayList(new AttackMove(1, 2, 3, 4, 5)));

        Iterable<AttackMove> moves = player.onAttack(board);
        assertEquals(new AttackMove(1, 2, 3, 4, 5), moves.iterator().next());
    }

    @Test
    public void playerCallsReinforcementWithBoardAndReinforcementAmount() throws ScriptException, NoSuchMethodException {
        String script = "function " + REINFORCEMENT_JS_FUNCTION_NAME + "(board, soldiers) { return [{'col':1, 'row':2, 'amount':5}]; }";
        Player player = new JsPlayer(script, engine, parser);
        Map<String, Object> mapFromBoard = parser.createMapFromBoard(board);

        player.onReinforcement(board, 0);

        verify(invocable).invokeFunction(REINFORCEMENT_JS_FUNCTION_NAME, mapFromBoard, 0);
    }

    @Test
    public void playerCallsAttackWithBoard() throws ScriptException, NoSuchMethodException {
        String script = "function " + ATTACK_JS_FUNCTION_NAME + "(board) { return [{'originCol':1, 'originRow':2, 'destCol':3, 'destRow':4, 'amount':5}]; }";
        Player player = new JsPlayer(script, engine, parser);
        Map<String, Object> mapFromBoard = parser.createMapFromBoard(board);

        player.onAttack(board);
        verify(invocable).invokeFunction(ATTACK_JS_FUNCTION_NAME, mapFromBoard);
    }
}