package rasos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jdk.nashorn.api.scripting.JSObject;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JsPlayer extends Player {
    private String script;

    JsPlayer(String script) {
        this.script = script;
    }

    @Override
    public Iterable<ReinforcementMove> onReinforcement(Board board, int reinforcement) {
        try {
            JSObject result = (JSObject) getInvocableJSEngine().invokeFunction("onReinforcement", board, reinforcement);
            return extractReinforcementMovesFromJSResult(result);
        } catch (ScriptException | NoSuchMethodException | IllegalArgumentException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Iterable<AttackMove> onAttack(Board board) {
        return null;
    }

    private List<ReinforcementMove> extractReinforcementMovesFromJSResult(JSObject result) {
        ObjectMapper converter = new ObjectMapper();
        ReinforcementMove[] moves = converter.convertValue(result.values(), ReinforcementMove[].class);
        return Arrays.asList(moves);
    }

    private Invocable getInvocableJSEngine() throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        engine.eval(script);
        return (Invocable) engine;
    }
}
