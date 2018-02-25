package rasos;

import jdk.nashorn.api.scripting.JSObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import static org.junit.Assert.assertEquals;

public class JsonParserTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private ScriptEngine engine;
    private JsonParser parser;

    @Before
    public void setUp() {
        engine = new ScriptEngineManager().getEngineByName("JavaScript");
        parser = new JsonParser();
    }

    @Test
    public void throwsOnUnparsableAttack() throws ScriptException {
        String json = "[{'hamburger': 'sandwich', 'hotdog':'sandwich', 'burrito':'not_sandwich'}]";
        JSObject object = (JSObject) engine.eval(json);

        expectedException.expect(RuntimeException.class);

        parser.extractMovesFromJSResult(object, AttackMove[].class);
    }

    @Test
    public void throwOnUnparsableReinforcement() throws ScriptException {
        String json = "[{'adom':'yes', 'yarok':'yes', 'garinimShelAvatiach':'yes'}]";
        JSObject object = (JSObject) engine.eval(json);

        expectedException.expect(RuntimeException.class);

        parser.extractMovesFromJSResult(object, ReinforcementMove[].class);
    }

    @Test
    public void playerReturnsReinforcementMoves() throws ScriptException {
        String json = "[{'col':1, 'row':2, 'amount':5}]";
        JSObject object = (JSObject) engine.eval(json);
        Iterable<ReinforcementMove> moves = parser.extractMovesFromJSResult(object, ReinforcementMove[].class);
        assertEquals(new ReinforcementMove(1, 2, 5), moves.iterator().next());
    }

    @Test
    public void playerReturnsAttackMoves() throws ScriptException {
        String json = "[{'originCol':1, 'originRow':2, 'destCol':3, 'destRow':4, 'amount':5}]";
        JSObject object = (JSObject) engine.eval(json);
        Iterable<AttackMove> moves = parser.extractMovesFromJSResult(object, AttackMove[].class);
        assertEquals(new AttackMove(1, 2, 3, 4, 5), moves.iterator().next());
    }
}