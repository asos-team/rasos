package rasos;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class JsPlayerTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void playerReturnsEmptyLists() {
        String script = "script";
        Player player = new JsPlayer(script);
        Iterable<ReinforcementMove> moves = player.onReinforcement(mock(Board.class), 0);
        assertTrue(!moves.iterator().hasNext());
    }
}