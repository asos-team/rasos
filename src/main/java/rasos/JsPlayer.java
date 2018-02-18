package rasos;

import com.google.gson.Gson;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Collections;

public class JsPlayer extends Player {
    private String script;

    public JsPlayer(String script) {
        this.script = script;
    }

    @Override
    public Iterable<ReinforcementMove> onReinforcement(Board board, int reinforcement) {
        Gson gson = new Gson();
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        ScriptObjectMirror result;
        try {
            engine.eval(script);
            result = (ScriptObjectMirror) ((Invocable) engine).invokeFunction("onReinforcement", board, reinforcement);
        } catch (ScriptException | NoSuchMethodException ignored) {
            return Collections.emptyList();
        }
        ReinforcementMove move = gson.fromJson(gson.toJson(result.get("0")), ReinforcementMove.class);
        return Collections.singletonList(move);
    }

    @Override
    public Iterable<AttackMove> onAttack(Board board) {
        return null;
    }
}
