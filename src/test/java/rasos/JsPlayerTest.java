package rasos;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

public class JsPlayerTest {

    @Test
    public void corruptedPlayerReturnsEmptyLists() {
        String script = "script";
        Player player = new JsPlayer(script);
        Iterable<ReinforcementMove> moves = player.onReinforcement(mock(Board.class), 0);
        assertFalse(moves.iterator().hasNext());
    }

    @Test
    public void simplePlayerReturnsEmptyLists() {
        String script = "function onReinforcement(board, soldiers) { return [{'col':1, 'row':2, 'amount':5}]; }";
        Player player = new JsPlayer(script);
        Iterable<ReinforcementMove> moves = player.onReinforcement(mock(Board.class), 0);
        assertEquals(new ReinforcementMove(1, 2, 5), moves.iterator().next());
    }
}