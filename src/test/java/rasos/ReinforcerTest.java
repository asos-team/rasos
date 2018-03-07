package rasos;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReinforcerTest {

    @Rule
    public final ExpectedException expectedEx = ExpectedException.none();
    private Reinforcer reinforcer;
    private Board board;
    private RiskLogger logger;

    @Before
    public void setUp() {
        logger = mock(RiskLogger.class);
        reinforcer = new Reinforcer(logger);
        board = new Board(18);
    }

    @Test
    public void reinforcementMove_isApplied_playerA() {
        test_reinforcementMove_isApplied(1, 13, 1, 4, 1);
    }

    @Test
    public void reinforcementMove_isApplied_playerB() {
        test_reinforcementMove_isApplied(2, 56, 1, 2, 6);
    }

    @Test
    public void reinforcementMove_toCellYouDoNotControl_isIgnored() {
        board.cellAt(1, 1).setValues(1, 4);

        reinforcer.apply(board, 1, Collections.singleton(new ReinforcementMove(3, 1, 1)), 1);

        assertTrue("Cell was reinforced although not controlled by the reinforcing player",
                board.cellAt(3, 1).isNeutral());
    }

    @Test
    public void reinforcementMove_withExceedingAmount_isIgnored() {
        board.cellAt(3, 1).setValues(2, 4);

        reinforcer.apply(board, 2, Collections.singleton(new ReinforcementMove(3, 1, 5)), 1);

        TestUtils.assertCellContents(board.cellAt(3, 1), 2, 4);
    }

    @Test
    public void reinforcementMove_withInvalidCoordinates_isIgnored() {
        board.cellAt(3, 2).setValues(2, 7);

        reinforcer.apply(board, 2, Collections.singleton(new ReinforcementMove(3, 0, 1)), 2);
    }

    @Test
    public void manyReinforcementMoves_areAllApplied() {
        board.cellAt(3, 2).setValues(2, 7);
        board.cellAt(3, 1).setValues(2, 3);
        ArrayList<ReinforcementMove> moves = new ArrayList<>();
        moves.add(new ReinforcementMove(3, 2, 1));
        moves.add(new ReinforcementMove(3, 1, 1));

        reinforcer.apply(board, 2, moves, 2);

        TestUtils.assertCellContents(board.cellAt(3, 2), 2, 8);
        TestUtils.assertCellContents(board.cellAt(3, 1), 2, 4);
    }

    @Test
    public void amongValidAndInvalidReinforcementMoves_validMovesAreStillApplied() {
        board.cellAt(1, 1).setValues(2, 4);
        ArrayList<ReinforcementMove> moves = new ArrayList<>();
        moves.add(new ReinforcementMove(3, 2, 1));
        moves.add(new ReinforcementMove(1, 1, 1));

        reinforcer.apply(board, 2, moves, 1);

        assertTrue("Cell was reinforced although not controlled by the reinforcing player",
                board.cellAt(3, 2).isNeutral());
        TestUtils.assertCellContents(board.cellAt(1, 1), 2, 5);
    }

    @Test
    public void manyReinforcementMoves_areAppliedToTheAllowedQuota() {
        board.cellAt(3, 2).setValues(2, 7);
        board.cellAt(3, 1).setValues(2, 3);
        ArrayList<ReinforcementMove> moves = new ArrayList<>();
        moves.add(new ReinforcementMove(3, 2, 1));
        moves.add(new ReinforcementMove(3, 1, 2));
        moves.add(new ReinforcementMove(3, 1, 1));

        reinforcer.apply(board, 2, moves, 2);

        TestUtils.assertCellContents(board.cellAt(3, 2), 2, 8);
        TestUtils.assertCellContents(board.cellAt(3, 1), 2, 4);
    }

    @Test
    public void appliedPlayerReinforcementMovesAreLogged() {
        HashSet<ReinforcementMove> appliedMoves = Sets.newHashSet(new ReinforcementMove(4, 1, 1), new ReinforcementMove(4, 1, 1));
        board.cellAt(4, 1).setValues(1, 1);
        int playerId = 1;

        reinforcer.apply(board, playerId, appliedMoves, 2);

        for (ReinforcementMove move : appliedMoves) {
            verify(logger).logSuccessfulReinforcement(playerId, move);
        }
    }

    @Test
    public void unappliedPlayerReinforcementMovesAreLogged() {
        HashSet<ReinforcementMove> appliedMoves = Sets.newHashSet(new ReinforcementMove(4, 1, 1), new ReinforcementMove(4, 1, 1));
        board.cellAt(4, 1).setValues(1, 1);
        int playerId = 1;

        reinforcer.apply(board, playerId, appliedMoves, 0);

        for (ReinforcementMove move : appliedMoves) {
            verify(logger).logFailedReinforcement(playerId, move);
        }
    }

    private void test_reinforcementMove_isApplied(int id, int soldiers, int quota, int col, int row) {
        board.cellAt(col, row).setValues(id, soldiers);

        reinforcer.apply(board, id, Collections.singleton(new ReinforcementMove(col, row, quota)), quota);

        TestUtils.assertCellContents(board.cellAt(col, row), id, soldiers + quota);
    }
}