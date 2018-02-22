package rasos;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import rasos.players.AttackPlayer;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class AttackPlayerTest {

    private Board b;
    private AttackPlayer p;

    @Before
    public void setUp() throws Exception {
        b = new Board(10);
        p = new AttackPlayer();
        ((Player) p).setPlayerId(1);
    }

    @Test
    public void attacksNeighbouringCellFromHomebase() {
        testValidAttackMoveCreated(10, 1, 1);
    }

    @Test
    public void attacksNeighbouringCellFromCenter() {
        testValidAttackMoveCreated(11, 5, 5);
    }

    @Test
    public void attacksFromEveryControlledCell() {
        b = new Board(5);
        b.populateHomeBases(10);
        b.cellAt(3, 3).setValues(1, 15);

        List<AttackMove> attackMoves = Lists.newArrayList(p.onAttack(b));

        assertThat(attackMoves.size(), is(2));

        assertAttackMove(10, 1, 1, attackMoves.get(0));
        assertAttackMove(15, 3, 3, attackMoves.get(1));
    }

    private void testValidAttackMoveCreated(int srcAmount, int srcRow, int srcCol) {
        b.cellAt(srcCol, srcRow).setValues(1, srcAmount);

        ArrayList<AttackMove> moves = Lists.newArrayList(p.onAttack(b));

        assertThat("Attacks one cell", moves.size(), is(1));

        assertAttackMove(srcAmount, srcRow, srcCol, moves.get(0));
    }

    private void assertAttackMove(int srcAmount, int srcRow, int srcCol, AttackMove move) {
        assertThat("Attacks with half the source amount", move.getAmount(), is(srcAmount / 2));
        assertNeighbouring(move, srcRow, srcCol);
    }

    private void assertNeighbouring(AttackMove attackMove, int srcRow, int srcCol) {
        int colDst = Math.abs(attackMove.getDestCol() - srcCol);
        int rowDst = Math.abs(attackMove.getDestRow() - srcRow);
        assertTrue("At most dist 1 row-axis", rowDst <= 1);
        assertTrue("At most dist 1 col-axis", colDst <= 1);
        assertTrue("Not attack src cell", rowDst != 0 || colDst != 0);
    }
}