package rasos;

import jdk.nashorn.api.scripting.JSObject;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class JsPlayer extends Player {
    static final String REINFORCEMENT_JS_FUNCTION_NAME = "onReinforcement";
    static final String ATTACK_JS_FUNCTION_NAME = "onAttack";
    private JsonParser parser;
    private Invocable invocable;

    JsPlayer(String script, ScriptEngine engine, JsonParser parser) {
        try {
            engine.eval(script);
            this.invocable = (Invocable) engine;
            this.parser = parser;
        } catch (ScriptException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Iterable<ReinforcementMove> onReinforcement(Board board, int reinforcement) {
        try {
            return executeJsMethod(REINFORCEMENT_JS_FUNCTION_NAME, ReinforcementMove[].class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Iterable<AttackMove> onAttack(Board board) {
        try {
            return executeJsMethod(ATTACK_JS_FUNCTION_NAME, AttackMove[].class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private <T> Iterable<T> executeJsMethod(String methodName, Class<T[]> moveType, Object... params) throws ScriptException, NoSuchMethodException {
        JSObject result = (JSObject) getInvocableJSEngine().invokeFunction(methodName, params);

        return parser.extractMovesFromJSResult(result, moveType);
    }

    private Invocable getInvocableJSEngine() {
        return this.invocable;
    }
}
