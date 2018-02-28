package rasos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.api.scripting.JSObject;

import java.util.Arrays;

public class JsonParser {
    public <T> Iterable<T> extractMovesFromJSResult(JSObject result, Class<T[]> moveClass) {
        ObjectMapper converter = new ObjectMapper();
        T[] moves = converter.convertValue(result.values(), moveClass);
        return Arrays.asList(moves);
    }

    public String createBoardJson(Board board) throws JsonProcessingException {
        ObjectMapper converter = new ObjectMapper();
        return converter.writeValueAsString(board);
    }
}
