package rasos;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

public class JsPlayerTest {
    @Rule
    public final ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void corruptedPlayerReturnsEmptyLists() {
        String script = "script";
        Player player = new JsPlayer(script);
        Iterable<ReinforcementMove> moves = player.onReinforcement(mock(Board.class), 0);
        assertFalse(moves.iterator().hasNext());
    }

    @Test
    public void playerReturnsReinforcementMoves() {
        String script = "function onReinforcement(board, soldiers) { return [{'col':1, 'row':2, 'amount':5}]; }";
        Player player = new JsPlayer(script);
        Iterable<ReinforcementMove> moves = player.onReinforcement(mock(Board.class), 0);
        assertEquals(new ReinforcementMove(1, 2, 5), moves.iterator().next());
    }

//    @Test
//    public void jsRunnerThrowsOnUnparsableMove(){
////        expectedEx.expect(RuntimeException.class);
//        String script = "function onReinforcement(board, soldiers) { return [{'adom':'yes', 'yarok':'yes', 'garinimShelAvatiach':'yes'}]; }";
//        Player player = new JsPlayer(script);
//        Iterable<ReinforcementMove> moves = player.onReinforcement(mock(Board.class), 0);;
//    }
}