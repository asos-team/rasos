package rasos;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
        board.populateHomeBases(20);
    }

    @Test
    public void appliesAttackMoves() {
        AttackMove aAttackMove = new AttackMove(1, 1, 2, 1, 2);
        AttackMove bAttackMove = new AttackMove(2, 2, 1, 2, 3);

        attacker.apply(board, Collections.singleton(aAttackMove), Collections.singleton(bAttackMove));

        TestUtils.assertCellContents(board.getHome1Cell(), 1, 18);
        TestUtils.assertCellContents(board.cellAt(2, 1), 1, 2);
        TestUtils.assertCellContents(board.cellAt(2, 2), 2, 17);
        TestUtils.assertCellContents(board.cellAt(1, 2), 2, 3);
    }

    @Test
    public void ignoresExceedingAttackMove() {
        AttackMove am = new AttackMove(1, 1, 2, 2, 300);

        attacker.apply(board, Collections.singleton(am));

        TestUtils.assertCellContents(board.getHome1Cell(), 1, 20);
        TestUtils.assertCellContents(board.getHome2Cell(), 2, 20);
    }

    @Test
    public void ignoreExceedingAttackMovesAndAppliesLegalOnes() {
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
    public void conqueringAttackMove() {
        Board b = new Board(2);
        b.populateHomeBases(10);
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
        b.populateHomeBases(20);

        AttackMove am = new AttackMove(1, 1, 1, 3, 10);

        attacker.apply(b, Collections.singleton(am));

        TestUtils.assertCellContents(b.cellAt(1, 1), 1, 20);
        TestUtils.assertCellContents(b.cellAt(1, 3), 0, 0);
    }

    @Test
    public void ignoresNonNeighbouringAttackMovesAndAppliesLegalOnes() {
        Board b = new Board(3);
        b.populateHomeBases(20);

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
    public void appliedAttackMovesAreLogged() {
        Iterable<AttackMove> movesToLog = Collections.singleton(new AttackMove(1, 1, 2, 1, 2));
        attacker.apply(board, movesToLog);

        for (AttackMove move : movesToLog) {
            verify(logger).logSuccessfulAttack(1, move);
        }
    }

    @Test
    public void unappliedAttackMovesAreLogged(){
        Iterable<AttackMove> movesToLog = Collections.singleton(new AttackMove(1, 1, 2, 1, 909));
        attacker.apply(board, movesToLog);

        for (AttackMove move : movesToLog) {
            verify(logger).logFailedAttack(1, move);
        }
    }
}