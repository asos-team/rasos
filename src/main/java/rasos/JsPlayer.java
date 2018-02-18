package rasos;

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

    public JsPlayer(String script) {
        this.script = script;
    }

    @Override
    public Iterable<ReinforcementMove> onReinforcement(Board board, int reinforcement) {
        try {
            JSObject result = (JSObject) getInvocableJSEngine().invokeFunction("onReinforcement", board, reinforcement);
            return extractReinforcementMovesFromJSResult(result);
        } catch (ScriptException | NoSuchMethodException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Iterable<AttackMove> onAttack(Board board) {
        return null;
    }

    private List<ReinforcementMove> extractReinforcementMovesFromJSResult(JSObject result) {
        Gson converter = new Gson();
        String json = converter.toJson(result.values());
        ReinforcementMove[] moves = converter.fromJson(json, ReinforcementMove[].class);
        return Arrays.asList(moves);
    }

    private Invocable getInvocableJSEngine() throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        engine.eval(script);
        return (Invocable) engine;
    }
}
