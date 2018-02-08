import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertTrue;

public class ReinforcerTest {

    @Rule
    public final ExpectedException expectedEx = ExpectedException.none();
    private Reinforcer reinforcer;
    private Board board;

    @Before
    public void setUp() {
        reinforcer = new Reinforcer();
        board = new Board(18);
    }

    @Test
    public void nullReinforcementMoves_areIgnored() {
        reinforcer.apply(board, null, 52, 7);
    }

    @Test
    public void reinforcementMove_isApplied_playerA() {
        int id = 1;
        int soldiers = 13;
        int quota = 1;
        int col = 4;
        int row = 1;
        test_reinforcementMove_isApplied(id, soldiers, quota, col, row);
    }

    @Test
    public void reinforcementMove_isApplied_playerB() {
        int id = 2;
        int soldiers = 56;
        int quota = 1;
        int col = 2;
        int row = 6;
        test_reinforcementMove_isApplied(id, soldiers, quota, col, row);
    }

    @Test
    public void reinforcementMove_toCellYouDoNotControl_isIgnored() {
        board.setCell(1, 1, new Cell(1, 4));

        reinforcer.apply(board, Collections.singleton(new ReinforcementMove(3, 1, 1)), 1, 1);

        assertTrue("Cell was reinforced although not controlled by the reinforcing player",
                board.cellAt(3, 1).isNeutral());
    }

    @Test
    public void reinforcementMove_withExceedingAmount_isIgnored() {
        board.setCell(3, 1, new Cell(2, 4));

        reinforcer.apply(board, Collections.singleton(new ReinforcementMove(3, 1, 5)), 1, 2);

        TestUtils.assertCellContents(board.cellAt(3, 1), 2, 4);
    }

    @Test
    public void reinforcementMove_withInvalidCoordinates_isIgnored() {
        board.setCell(3, 2, new Cell(2, 7));

        reinforcer.apply(board, Collections.singleton(new ReinforcementMove(3, 0, 1)), 2, 2);
    }

    @Test
    public void manyReinforcementMoves_areAllApplied() {
        board.setCell(3, 2, new Cell(2, 7));
        board.setCell(3, 1, new Cell(2, 3));
        ArrayList<ReinforcementMove> moves = new ArrayList<>();
        moves.add(new ReinforcementMove(3, 2, 1));
        moves.add(new ReinforcementMove(3, 1, 1));

        reinforcer.apply(board, moves, 2, 2);

        TestUtils.assertCellContents(board.cellAt(3, 2), 2, 8);
        TestUtils.assertCellContents(board.cellAt(3, 1), 2, 4);
    }

    @Test
    public void amongValidAndInvalidReinforcementMoves_validMovesAreStillApplied() {
        board.setCell(1, 1, new Cell(2, 4));
        ArrayList<ReinforcementMove> moves = new ArrayList<>();
        moves.add(new ReinforcementMove(3, 2, 1));
        moves.add(new ReinforcementMove(1, 1, 1));

        reinforcer.apply(board, moves, 1, 2);

        assertTrue("Cell was reinforced although not controlled by the reinforcing player",
                board.cellAt(3, 2).isNeutral());
        TestUtils.assertCellContents(board.cellAt(1, 1), 2, 5);
    }

    @Test
    public void manyReinforcementMoves_areAppliedToTheAllowedQuota() {
        board.setCell(3, 2, new Cell(2, 7));
        board.setCell(3, 1, new Cell(2, 3));
        ArrayList<ReinforcementMove> moves = new ArrayList<>();
        moves.add(new ReinforcementMove(3, 2, 1));
        moves.add(new ReinforcementMove(3, 1, 2));
        moves.add(new ReinforcementMove(3, 1, 1));

        reinforcer.apply(board, moves, 2, 2);

        TestUtils.assertCellContents(board.cellAt(3, 2), 2, 8);
        TestUtils.assertCellContents(board.cellAt(3, 1), 2, 4);
    }

    private void test_reinforcementMove_isApplied(int id, int soldiers, int quota, int col, int row) {
        board.setCell(col, row, new Cell(id, soldiers));

        reinforcer.apply(board, Collections.singleton(new ReinforcementMove(col, row, quota)), quota, id);

        TestUtils.assertCellContents(board.cellAt(col, row), id, soldiers + quota);
    }
}