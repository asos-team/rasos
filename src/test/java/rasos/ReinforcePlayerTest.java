package rasos;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import rasos.players.ReinforcePlayer;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReinforcePlayerTest {

    private Board b;
    private ReinforcePlayer p;

    @Before
    public void setUp() throws Exception {
        b = new Board(5);
        b.cellAt(1, 1).setValues(1, 10);

        p = new ReinforcePlayer();
        ((Player) p).setPlayerId(1);
    }

    @Test
    public void reinforceSingleCell() {
        ArrayList<ReinforcementMove> moves = getMoves();

        assertThat(moves.size(), is(1));
        assertThat(moves, hasItems(new ReinforcementMove(1, 1, 5)));
    }

    @Test
    public void reinforceMultipleCells() {
        b.cellAt(1, 2).setValues(1, 5);

        ArrayList<ReinforcementMove> moves = getMoves();

        assertThat(moves.size(), is(2));
        assertThat(moves, hasItems(new ReinforcementMove(1, 1, 2), new ReinforcementMove(1, 2, 2)));
    }

    private ArrayList<ReinforcementMove> getMoves() {
        return Lists.newArrayList(p.onReinforcement(b, 5));
    }
}