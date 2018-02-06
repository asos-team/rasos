import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

public class AttackTest {

    private static final int boardDim = 2;
    private Attacker attacker;
    private Board board;

    @Before
    public void setUp() {
        attacker = new Attacker();
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
}