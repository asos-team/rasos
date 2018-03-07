package rasos;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class AttackTest {

    private static final int boardDim = 2;
    private Attacker attacker;
    private Board board;
    private RiskLogger logger;

    @Before
    public void setUp() {
        logger = mock(RiskLogger.class);
        attacker = new Attacker(logger);
        board = new Board(boardDim);
        board.populateHomeBases(20, 1, 2);
    }

    @Test
    public void appliesAttackMoves() {
        AttackMove aAttackMove = new AttackMove(1, 1, 2, 1, 2);
        AttackMove bAttackMove = new AttackMove(2, 2, 1, 2, 3);

        attacker.apply(board, Collections.singleton(aAttackMove), Collections.singleton(bAttackMove), 1, 2);

        TestUtils.assertCellContents(board.getHome1Cell(), 1, 18);
        TestUtils.assertCellContents(board.cellAt(2, 1), 1, 2);
        TestUtils.assertCellContents(board.cellAt(2, 2), 2, 17);
        TestUtils.assertCellContents(board.cellAt(1, 2), 2, 3);
    }

    @Test
    public void appliesEntireCellAttackMove() {
        AttackMove am = new AttackMove(1, 1, 1, 2, 20);

        attacker.apply(board, Lists.newArrayList(am));

        TestUtils.assertCellContents(board.getHome1Cell(), 0, 0);
        TestUtils.assertCellContents(board.cellAt(1, 2), 1, 20);
    }

    @Test
    public void appliesTwoMovesFromSameCell() {
        AttackMove am1 = new AttackMove(1, 1, 1, 2, 10);
        AttackMove am2 = new AttackMove(1, 1, 2, 1, 5);

        attacker.apply(board, Lists.newArrayList(am1, am2));

        TestUtils.assertCellContents(board.getHome1Cell(), 1, 5);
        TestUtils.assertCellContents(board.cellAt(1, 2), 1, 10);
        TestUtils.assertCellContents(board.cellAt(2, 1), 1, 5);
    }

    @Test
    public void ignoresAmountExceedingAttackMove() {
        AttackMove am = new AttackMove(1, 1, 2, 2, 300);

        attacker.apply(board, Collections.singleton(am));

        assertBoardUnchanged();
    }

    @Test
    public void ignoresAttackMoveFromUnpopulatedCell() {
        AttackMove am = new AttackMove(1, 2, 2, 1, 5);

        attacker.apply(board, Lists.newArrayList(am));

        assertBoardUnchanged();
    }

    @Test
    public void ignoresMovesFromEnemyCells() {
        AttackMove am1 = new AttackMove(2, 2, 1, 2, 10);
        AttackMove am2 = new AttackMove(1, 1, 1, 2, 10);

        attacker.apply(board, Lists.newArrayList(am1), Lists.newArrayList(am2));

        assertBoardUnchanged();
    }

    @Test
    public void ignoresAmountExceedingAttackMovesAndAppliesLegalOnes() {
        AttackMove legal = new AttackMove(1, 1, 2, 1, 10);
        AttackMove illegal = new AttackMove(1, 1, 2, 2, 12);
        AttackMove legal2 = new AttackMove(1, 1, 1, 2, 5);

        attacker.apply(board, Lists.newArrayList(legal, illegal, legal2));

        TestUtils.assertCellContents(board.getHome1Cell(), 1, 5);
        TestUtils.assertCellContents(board.cellAt(2, 1), 1, 10);
        TestUtils.assertCellContents(board.cellAt(1, 2), 1, 5);
        TestUtils.assertCellContents(board.getHome2Cell(), 2, 20);
    }

    @Test
    public void ignoresAboveBoardExceedingMoves() {
        AttackMove am = new AttackMove(2, 2, 3, 3, 10);

        attacker.apply(board, Lists.newArrayList(), Lists.newArrayList(am));

        assertBoardUnchanged();
    }

    @Test
    public void ignoresBelowBoardExceedingMoves() {
        AttackMove am = new AttackMove(1, 0, 1, 1, 10);

        attacker.apply(board, Lists.newArrayList(), Lists.newArrayList(am));

        assertBoardUnchanged();
    }

    @Test
    public void conqueringAttackMove() {
        Board b = new Board(2);
        b.populateHomeBases(10, 1, 2);
        b.cellAt(1, 1).updateNumSoldiers(20);
        AttackMove am = new AttackMove(1, 1, 2, 2, 15);

        attacker.apply(b, Collections.singleton(am));

        TestUtils.assertCellContents(b.getHome1Cell(), 1, 5);
        TestUtils.assertCellContents(b.getHome2Cell(), 1, 5);
    }

    @Test
    public void nonConqueringAttackMove() {
        AttackMove am = new AttackMove(1, 1, 2, 2, 10);

        attacker.apply(board, Collections.singleton(am));

        TestUtils.assertCellContents(board.getHome1Cell(), 1, 10);
        TestUtils.assertCellContents(board.getHome2Cell(), 2, 10);
    }

    @Test
    public void totalDemolitionAttackMove() {
        AttackMove am = new AttackMove(1, 1, 2, 2, 20);

        attacker.apply(board, Collections.singleton(am));

        TestUtils.assertCellContents(board.getHome1Cell(), 0, 0);
        TestUtils.assertCellContents(board.getHome2Cell(), 0, 0);
    }

    @Test
    public void playerMovesTwoCellsToSameCell() {
        board.cellAt(2, 1).setValues(1, 10);

        AttackMove am1 = new AttackMove(1, 1, 1, 2, 10);
        AttackMove am2 = new AttackMove(2, 1, 1, 2, 5);

        attacker.apply(board, Lists.newArrayList(am1, am2));

        TestUtils.assertCellContents(board.cellAt(1, 1), 1, 10);
        TestUtils.assertCellContents(board.cellAt(2, 1), 1, 5);
        TestUtils.assertCellContents(board.cellAt(1, 2), 1, 15);
    }

    @Test
    public void ignoresNonNeighbouringAttackMoves() {
        Board b = new Board(3);
        b.populateHomeBases(20, 1, 2);

        AttackMove am = new AttackMove(1, 1, 1, 3, 10);

        attacker.apply(b, Collections.singleton(am));

        TestUtils.assertCellContents(b.cellAt(1, 1), 1, 20);
        TestUtils.assertCellContents(b.cellAt(1, 3), 0, 0);
    }

    @Test
    public void ignoresNonNeighbouringAttackMovesAndAppliesLegalOnes() {
        Board b = new Board(3);
        b.populateHomeBases(20, 1, 2);

        AttackMove am1 = new AttackMove(1, 1, 1, 2, 5);
        AttackMove am2 = new AttackMove(1, 1, 1, 3, 5);
        AttackMove am3 = new AttackMove(1, 1, 2, 1, 5);

        attacker.apply(b, Lists.newArrayList(am1, am2, am3));

        TestUtils.assertCellContents(b.cellAt(1, 1), 1, 10);
        TestUtils.assertCellContents(b.cellAt(1, 2), 1, 5);
        TestUtils.assertCellContents(b.cellAt(2, 1), 1, 5);
        TestUtils.assertCellContents(b.cellAt(1, 3), 0, 0);
    }

    @Test
    public void concurrentMoveToSameCellFromDifferentPlayers() {
        Board b = new Board(3);
        b.populateHomeBases(20, 1, 2);

        AttackMove amA = new AttackMove(1, 1, 2, 2, 10);
        AttackMove amB = new AttackMove(3, 3, 2, 2, 8);

        attacker.apply(b, Lists.newArrayList(amA), Lists.newArrayList(amB));

        TestUtils.assertCellContents(b.cellAt(1, 1), 1, 10);
        TestUtils.assertCellContents(b.cellAt(2, 2), 1, 2);
        TestUtils.assertCellContents(b.cellAt(3, 3), 2, 12);
    }

    @Test
    public void appliedAttackMovesAreLogged() {
        Iterable<AttackMove> movesToLog = Collections.singleton(new AttackMove(1, 1, 2, 1, 2));
        attacker.apply(board, movesToLog);

        for (AttackMove move : movesToLog) {
            verify(logger).logSuccessfulAttack(1, move);
        }
    }

    @Test
    public void unappliedAttackMovesAreLogged() {
        Iterable<AttackMove> movesToLog = Collections.singleton(new AttackMove(1, 1, 2, 1, 909));
        attacker.apply(board, movesToLog);

        for (AttackMove move : movesToLog) {
            verify(logger).logFailedAttack(1, move);
        }
    }

    private void assertBoardUnchanged() {
        TestUtils.assertCellContents(board.getHome1Cell(), 1, 20);
        TestUtils.assertCellContents(board.cellAt(1, 2), 0, 0);
        TestUtils.assertCellContents(board.cellAt(2, 1), 0, 0);
        TestUtils.assertCellContents(board.getHome2Cell(), 2, 20);
    }
}