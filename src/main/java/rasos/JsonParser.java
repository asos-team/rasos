package rasos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Arrays;

public class JsonParser {
    public <T> Iterable<T> extractMovesFromJSResult(JSObject result, Class<T[]> moveClass) {
        ObjectMapper converter = new ObjectMapper();
        T[] moves = converter.convertValue(result.values(), moveClass);
        return Arrays.asList(moves);
    }

    public JSObject createJSObjectFromBoard(Board board) throws JsonProcessingException, ScriptException {
        ObjectMapper converter = new ObjectMapper();
        String boardJson = converter.writeValueAsString(board);
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        ScriptObjectMirror json = (ScriptObjectMirror) engine.eval("JSON");
        return (JSObject) json.callMember("parse", boardJson);
    }
}
