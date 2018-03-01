package rasos;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.api.scripting.JSObject;

import java.util.Arrays;
import java.util.Map;

@SuppressWarnings("unchecked")
public class JsonParser {

    private ObjectMapper converter;

    JsonParser() {
        this.converter = new ObjectMapper();
    }

    public <T> Iterable<T> extractMovesFromJSResult(JSObject result, Class<T[]> moveClass) {
        T[] moves = converter.convertValue(result.values(), moveClass);
        return Arrays.asList(moves);
    }

    public Map<String, Object> createMapFromBoard(Board board) {
        return converter.convertValue(board, Map.class);
    }

}
