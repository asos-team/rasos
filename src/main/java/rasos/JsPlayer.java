package rasos;

import jdk.nashorn.api.scripting.JSObject;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.Map;

public class JsPlayer extends Player {
    static final String REINFORCEMENT_JS_FUNCTION_NAME = "onReinforcement";
    static final String ATTACK_JS_FUNCTION_NAME = "onAttack";
    private JsonParser parser;
    private Invocable invocable;
    private RiskLogger logger;

    JsPlayer(String script, ScriptEngine engine, JsonParser parser, RiskLogger logger) {
        try {
            engine.eval(script);
            this.invocable = (Invocable) engine;
            this.parser = parser;
            this.logger = logger;

            invocable.invokeFunction("onGameStart", getPlayerId());
        } catch (ScriptException | NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Iterable<ReinforcementMove> onReinforcement(Board board, int reinforcement) {
        try {
            Map<String, Object> mapFromBoard = parser.createMapFromBoard(board);
            return executeJsMethod(REINFORCEMENT_JS_FUNCTION_NAME, ReinforcementMove[].class, mapFromBoard, reinforcement);
        } catch (Exception e) {
            logger.logPlayerReinforcementCodeException(getPlayerId(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Iterable<AttackMove> onAttack(Board board) {
        try {
            Map<String, Object> mapFromBoard = parser.createMapFromBoard(board);
            return executeJsMethod(ATTACK_JS_FUNCTION_NAME, AttackMove[].class, mapFromBoard);
        } catch (Exception e) {
            logger.logPlayerAttackCodeException(getPlayerId(), e);
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
