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
    public void setUp() throws Exception {
        reinforcer = new Reinforcer();
        board = new Board(18);
    }

    @Test
    public void nullReinforcementMoves_areIgnored() {
        reinforcer.apply(7, 52, null, board);
    }

    @Test
    public void reinforcementMove_isApplied_playerA() {
        int id = 1;
        int soldiers = 13;
        int quota = 1;
        int col = 4;
        int row = 1;
        board.setCell(col, row, new Cell(id, soldiers));

        reinforcer.apply(id, quota, Collections.singleton(new ReinforcementMove(col, row, quota)), board);

        TestUtils.assertCellContents(board.cellAt(col, row), id, soldiers + quota);
    }

    @Test
    public void reinforcementMove_isApplied_playerB() {
        int id = 2;
        int soldiers = 56;
        int quota = 1;
        int col = 2;
        int row = 6;
        board.setCell(col, row, new Cell(id, soldiers));

        reinforcer.apply(id, quota, Collections.singleton(new ReinforcementMove(col, row, quota)), board);

        TestUtils.assertCellContents(board.cellAt(col, row), id, soldiers + quota);
    }

    @Test
    public void reinforcementMove_toCellYouDoNotControl_isIgnored() throws Exception {
        board.setCell(1, 1, new Cell(1, 4));

        reinforcer.apply(1, 1, Collections.singleton(new ReinforcementMove(3, 1, 1)), board);

        assertTrue("Cell was reinforced although not controlled by the reinforcing player",
                board.cellAt(3, 1).isNeutral());
    }

    @Test
    public void reinforcementMove_withExceedingAmount_isIgnored() throws Exception {
        board.setCell(3, 1, new Cell(2, 4));

        reinforcer.apply(2, 1, Collections.singleton(new ReinforcementMove(3, 1, 5)), board);

        TestUtils.assertCellContents(board.cellAt(3, 1), 2, 4);
    }

    @Test
    public void reinforcementMove_withInvalidCoordinates_isIgnored() throws Exception {
        board.setCell(3, 2, new Cell(2, 7));

        reinforcer.apply(2, 2, Collections.singleton(new ReinforcementMove(3, 0, 1)), board);
    }

    @Test
    public void manyReinforcementMoves_areAllApplied() throws Exception {
        board.setCell(3, 2, new Cell(2, 7));
        board.setCell(3, 1, new Cell(2, 3));
        ArrayList<ReinforcementMove> moves = new ArrayList<>();
        moves.add(new ReinforcementMove(3, 2, 1));
        moves.add(new ReinforcementMove(3, 1, 1));

        reinforcer.apply(2, 2, moves, board);

        TestUtils.assertCellContents(board.cellAt(3, 2), 2, 8);
        TestUtils.assertCellContents(board.cellAt(3, 1), 2, 4);
    }

    @Test
    public void amongValidAndInvalidReinforcementMoves_validMovesAreStillApplied() throws Exception {
        board.setCell(1, 1, new Cell(2, 4));
        ArrayList<ReinforcementMove> moves = new ArrayList<>();
        moves.add(new ReinforcementMove(3, 2, 1));
        moves.add(new ReinforcementMove(1, 1, 1));

        reinforcer.apply(2, 1, moves, board);

        assertTrue("Cell was reinforced although not controlled by the reinforcing player",
                board.cellAt(3, 2).isNeutral());
        TestUtils.assertCellContents(board.cellAt(1, 1), 2, 5);
    }

    @Test
    public void manyReinforcementMoves_areAppliedToTheAllowedQuota() throws Exception {
        board.setCell(3, 2, new Cell(2, 7));
        board.setCell(3, 1, new Cell(2, 3));
        ArrayList<ReinforcementMove> moves = new ArrayList<>();
        moves.add(new ReinforcementMove(3, 2, 1));
        moves.add(new ReinforcementMove(3, 1, 2));
        moves.add(new ReinforcementMove(3, 1, 1));

        reinforcer.apply(2, 2, moves, board);

        TestUtils.assertCellContents(board.cellAt(3, 2), 2, 8);
        TestUtils.assertCellContents(board.cellAt(3, 1), 2, 4);
    }

}