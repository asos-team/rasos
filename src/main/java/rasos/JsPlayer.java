package rasos;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.api.scripting.JSObject;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Arrays;
import java.util.Collections;

public class JsPlayer extends Player {
    private String script;

    JsPlayer(String script) {
        this.script = script;
    }

    @Override
    public Iterable<ReinforcementMove> onReinforcement(Board board, int reinforcement) {
        try {
            return executeJsMethod("onReinforcement", ReinforcementMove[].class);
        } catch (ScriptException | NoSuchMethodException | IllegalArgumentException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Iterable<AttackMove> onAttack(Board board) {
        try {
            return executeJsMethod("onAttack", AttackMove[].class);
        } catch (ScriptException | NoSuchMethodException | IllegalArgumentException e) {
            return Collections.emptyList();
        }
    }

    private <T> Iterable<T> executeJsMethod(String methodName, Class<T[]> moveType, Object... params) throws ScriptException, NoSuchMethodException {
        JSObject result = (JSObject) getInvocableJSEngine().invokeFunction(methodName, params);
        return extractMovesFromJSResult(result, moveType);
    }

    private <T> Iterable<T> extractMovesFromJSResult(JSObject result, Class<T[]> moveClass) {
        ObjectMapper converter = new ObjectMapper();
        T[] moves = converter.convertValue(result.values(), moveClass);
        return Arrays.asList(moves);

    }

    private Invocable getInvocableJSEngine() throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        engine.eval(script);
        return (Invocable) engine;
    }
}
