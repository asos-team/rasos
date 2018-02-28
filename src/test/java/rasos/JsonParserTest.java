package rasos;

import com.fasterxml.jackson.core.JsonProcessingException;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
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
    private JsonParser parser;
    private ScriptObjectMirror json;

    @Before
    public void setUp() throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        json = (ScriptObjectMirror) engine.eval("JSON");
        parser = new JsonParser();
    }

    @Test
    public void throwsOnUnparsableAttack() {
        String attack = "[{\"hamburger\": \"sandwich\", \"hotdog\":\"sandwich\", \"burrito\":\"not_sandwich\"}]";
        JSObject object = (JSObject) json.callMember("parse", attack);

        expectedException.expect(RuntimeException.class);

        parser.extractMovesFromJSResult(object, AttackMove[].class);
    }

    @Test
    public void throwOnUnparsableReinforcement() {
        String reinforcement = "[{\"adom\":\"yes\", \"yarok\":\"yes\", \"garinimShelAvatiach\":\"yes\"}]";
        JSObject object = (JSObject) json.callMember("parse", reinforcement);

        expectedException.expect(RuntimeException.class);

        parser.extractMovesFromJSResult(object, ReinforcementMove[].class);
    }

    @Test
    public void extractMovesFromJSResultParsesReinforcementMoves() {
        String reinforcement = "[{\"col\":1, \"row\":2, \"amount\":5}]";
        JSObject object = (JSObject) json.callMember("parse", reinforcement);
        Iterable<ReinforcementMove> moves = parser.extractMovesFromJSResult(object, ReinforcementMove[].class);
        assertEquals(new ReinforcementMove(1, 2, 5), moves.iterator().next());
    }

    @Test
    public void extractMovesFromJSResultParsesAttackMoves() {
        String attack= "[{\"originCol\":1, \"originRow\":2, \"destCol\":3, \"destRow\":4, \"amount\":5}]";
        JSObject object = (JSObject) json.callMember("parse", attack);

        Iterable<AttackMove> moves = parser.extractMovesFromJSResult(object, AttackMove[].class);
        assertEquals(new AttackMove(1, 2, 3, 4, 5), moves.iterator().next());
    }

    @Test
    public void createJSONFromBoardWorks() throws JsonProcessingException, ScriptException {
        String expectedBoardJson = "{\"configuration\":[[{\"controllingPlayerId\":1,\"numSoldiers\":5}," +
                "{\"controllingPlayerId\":1,\"numSoldiers\":7}]," +
                "[{\"controllingPlayerId\":0,\"numSoldiers\":0}," +
                "{\"controllingPlayerId\":2,\"numSoldiers\":5}]],\"dim\":2}";

        JSObject expectedBoardJSObject = (JSObject) json.callMember("parse",expectedBoardJson);

        Board board = new Board(2);
        board.populateHomeBases(5);
        board.cellAt(1,2).setValues(1,7);

        JSObject boardJSObject= parser.createJSObjectFromBoard(board);
        String actualBoardJSON = (String) json.callMember("stringify", boardJSObject);
//
        assertEquals(expectedBoardJson, actualBoardJSON);
    }
}