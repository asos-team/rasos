package rasos;

import com.google.common.collect.Lists;
import org.junit.Test;
import rasos.players.AttackPlayer;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AttackPlayerTest {

    @Test
    public void attacksNeighbouringCells() {
        Board b = new Board(3);
        b.populateHomeBases(10);

        AttackPlayer p = new AttackPlayer();

        ArrayList<AttackMove> moves = Lists.newArrayList(p.onAttack(b));

        assertThat(moves.size(), is(1));
        assertThat(moves, hasItems(new AttackMove(1, 1, 2, 2, 9)));

    }
}