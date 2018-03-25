package rasos;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class AttackerTest {

    private static final int boardDim = 2;
    private static final int ID_A = 1;
    private static final int ID_B = 2;
    private Attacker attacker;
    private Board board;
    private RiskLogger logger;

    @Before
    public void setUp() {
        logger = mock(RiskLogger.class);
        attacker = new Attacker(logger);
        board = new Board(boardDim);
        board.populateHomeBases(20, ID_A, ID_B);
    }

    @Test
    public void appliesAttackMoves() {
        AttackMove aAttackMove = new AttackMove(1, 1, 2, 1, 2);
        AttackMove bAttackMove = new AttackMove(2, 2, 1, 2, 3);

        attacker.apply(board, Collections.singleton(aAttackMove), Collections.singleton(bAttackMove), ID_A, ID_B);

        TestUtils.assertCellContents(board.getHome1Cell(), ID_A, 18);
        TestUtils.assertCellContents(board.cellAt(2, 1), ID_A, 2);
        TestUtils.assertCellContents(board.cellAt(2, 2), ID_B, 17);
        TestUtils.assertCellContents(board.cellAt(1, 2), ID_B, 3);
    }

    @Test
    public void appliesEntireCellAttackMove() {
        AttackMove am = new AttackMove(1, 1, 1, 2, 20);

        attacker.apply(board, Collections.singleton(am), Collections.emptyList(), ID_A, ID_B);

        TestUtils.assertCellContents(board.getHome1Cell(), 0, 0);
        TestUtils.assertCellContents(board.cellAt(1, 2), ID_A, 20);
    }

    @Test
    public void appliesTwoMovesFromSameCell() {
        AttackMove am1 = new AttackMove(1, 1, 1, 2, 10);
        AttackMove am2 = new AttackMove(1, 1, 2, 1, 5);

        attacker.apply(board, Lists.newArrayList(am1, am2), Collections.emptyList(), ID_A, ID_B);

        TestUtils.assertCellContents(board.getHome1Cell(), ID_A, 5);
        TestUtils.assertCellContents(board.cellAt(1, 2), ID_A, 10);
        TestUtils.assertCellContents(board.cellAt(2, 1), ID_A, 5);
    }

    @Test
    public void ignoresAmountExceedingAttackMove() {
        AttackMove am = new AttackMove(1, 1, 2, 2, 300);

        attacker.apply(board, Collections.singleton(am), Collections.emptyList(), ID_A, ID_B);

        assertBoardUnchanged();
    }

    @Test
    public void ignoresAttackMoveFromUnpopulatedCell() {
        AttackMove am = new AttackMove(1, 2, 2, 1, 5);

        attacker.apply(board, Collections.singleton(am), Collections.emptyList(), ID_A, ID_B);

        assertBoardUnchanged();
    }

    @Test
    public void ignoresMovesFromEnemyCells() {
        AttackMove am1 = new AttackMove(2, 2, 1, 2, 10);
        AttackMove am2 = new AttackMove(1, 1, 1, 2, 10);

        attacker.apply(board, Collections.singleton(am1), Collections.singleton(am2), ID_A, ID_B);

        assertBoardUnchanged();
    }

    @Test
    public void ignoresAmountExceedingAttackMovesAndAppliesLegalOnes() {
        AttackMove legal = new AttackMove(1, 1, 2, 1, 10);
        AttackMove illegal = new AttackMove(1, 1, 2, 2, 12);
        AttackMove legal2 = new AttackMove(1, 1, 1, 2, 5);

        attacker.apply(board, Lists.newArrayList(legal, illegal, legal2), Collections.emptyList(), ID_A, ID_B);

        TestUtils.assertCellContents(board.getHome1Cell(), ID_A, 5);
        TestUtils.assertCellContents(board.cellAt(2, 1), ID_A, 10);
        TestUtils.assertCellContents(board.cellAt(1, 2), ID_A, 5);
        TestUtils.assertCellContents(board.getHome2Cell(), ID_B, 20);
    }

    @Test
    public void ignoresAboveBoardExceedingMoves() {
        AttackMove am = new AttackMove(2, 2, 3, 3, 10);

        attacker.apply(board, Collections.emptyList(), Collections.singleton(am), ID_A, ID_B);

        assertBoardUnchanged();
    }

    @Test
    public void ignoresBelowBoardExceedingMoves() {
        AttackMove am = new AttackMove(1, 0, 1, 1, 10);

        attacker.apply(board, Collections.emptyList(), Collections.singleton(am), ID_A, ID_B);

        assertBoardUnchanged();
    }

    @Test
    public void conqueringAttackMove() {
        Board b = new Board(2);
        b.populateHomeBases(10, 1, 2);
        b.cellAt(1, 1).updateNumSoldiers(20);
        AttackMove am = new AttackMove(1, 1, 2, 2, 15);

        attacker.apply(b, Collections.singleton(am), Collections.emptyList(), ID_A, ID_B);

        TestUtils.assertCellContents(b.getHome1Cell(), ID_A, 5);
        TestUtils.assertCellContents(b.getHome2Cell(), ID_A, 5);
    }

    @Test
    public void nonConqueringAttackMove() {
        AttackMove am = new AttackMove(1, 1, 2, 2, 10);

        attacker.apply(board, Collections.singleton(am), Collections.emptyList(), ID_A, ID_B);

        TestUtils.assertCellContents(board.getHome1Cell(), ID_A, 10);
        TestUtils.assertCellContents(board.getHome2Cell(), ID_B, 10);
    }

    @Test
    public void totalDemolitionAttackMove() {
        AttackMove am = new AttackMove(1, 1, 2, 2, 20);

        attacker.apply(board, Collections.singleton(am), Collections.emptyList(), ID_A, ID_B);

        TestUtils.assertCellContents(board.getHome1Cell(), 0, 0);
        TestUtils.assertCellContents(board.getHome2Cell(), 0, 0);
    }

    @Test
    public void playerMovesTwoCellsToSameCell() {
        board.cellAt(2, 1).setValues(ID_A, 10);

        AttackMove am1 = new AttackMove(1, 1, 1, 2, 10);
        AttackMove am2 = new AttackMove(2, 1, 1, 2, 5);

        attacker.apply(board, Lists.newArrayList(am1, am2), Collections.emptyList(), ID_A, ID_B);

        TestUtils.assertCellContents(board.cellAt(1, 1), ID_A, 10);
        TestUtils.assertCellContents(board.cellAt(2, 1), ID_A, 5);
        TestUtils.assertCellContents(board.cellAt(1, 2), ID_A, 15);
    }

    @Test
    public void ignoresNonNeighbouringAttackMoves() {
        Board b = new Board(3);
        b.populateHomeBases(20, ID_A, ID_B);

        AttackMove am = new AttackMove(1, 1, 1, 3, 10);

        attacker.apply(b, Collections.singleton(am), Collections.emptyList(), ID_A, ID_B);

        TestUtils.assertCellContents(b.cellAt(1, 1), ID_A, 20);
        TestUtils.assertCellContents(b.cellAt(1, 3), 0, 0);
    }

    @Test
    public void ignoresNonNeighbouringAttackMovesAndAppliesLegalOnes() {
        Board b = new Board(3);
        b.populateHomeBases(20, ID_A, ID_B);

        AttackMove am1 = new AttackMove(1, 1, 1, 2, 5);
        AttackMove am2 = new AttackMove(1, 1, 1, 3, 5);
        AttackMove am3 = new AttackMove(1, 1, 2, 1, 5);

        attacker.apply(b, Lists.newArrayList(am1, am2, am3), Collections.emptyList(), ID_A, ID_B);

        TestUtils.assertCellContents(b.cellAt(1, 1), ID_A, 10);
        TestUtils.assertCellContents(b.cellAt(1, 2), ID_A, 5);
        TestUtils.assertCellContents(b.cellAt(2, 1), ID_A, 5);
        TestUtils.assertCellContents(b.cellAt(1, 3), 0, 0);
    }

    @Test
    public void concurrentMoveToSameCellFromDifferentPlayers() {
        Board b = new Board(3);
        b.populateHomeBases(20, ID_A, ID_B);

        AttackMove amA = new AttackMove(1, 1, 2, 2, 10);
        AttackMove amB = new AttackMove(3, 3, 2, 2, 8);

        attacker.apply(b, Collections.singleton(amA), Collections.singleton(amB), ID_A, ID_B);

        TestUtils.assertCellContents(b.cellAt(1, 1), ID_A, 10);
        TestUtils.assertCellContents(b.cellAt(2, 2), ID_A, 2);
        TestUtils.assertCellContents(b.cellAt(3, 3), ID_B, 12);
    }

    @Test
    public void appliedAttackMovesAreLogged() {
        Iterable<AttackMove> movesToLog = Collections.singleton(new AttackMove(1, 1, 2, 1, 2));
        attacker.apply(board, movesToLog, Collections.emptyList(), ID_A, ID_B);

        for (AttackMove move : movesToLog) {
            verify(logger).logSuccessfulAttack(1, move);
        }
    }

    @Test
    public void unappliedAttackMovesAreLogged() {
        Iterable<AttackMove> movesToLog = Collections.singleton(new AttackMove(1, 1, 2, 1, 909));
        attacker.apply(board, movesToLog, Collections.emptyList(), ID_A, ID_B);

        for (AttackMove move : movesToLog) {
            verify(logger).logFailedAttack(ID_A, move);
        }
    }

    private void assertBoardUnchanged() {
        TestUtils.assertCellContents(board.getHome1Cell(), ID_A, 20);
        TestUtils.assertCellContents(board.cellAt(1, 2), 0, 0);
        TestUtils.assertCellContents(board.cellAt(2, 1), 0, 0);
        TestUtils.assertCellContents(board.getHome2Cell(), ID_B, 20);
    }
}