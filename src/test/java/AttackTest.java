import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class AttackTest {

    private static final int boardDim = 2;
    private Game game;
    private Player playerA;
    private Player playerB;
    private Attacker attacker;
    private Board board;

    @Before
    public void setUp() {


        attacker = new Attacker();
        board = new Board(boardDim);
        board.populateHomeBases(20);
    }



    @Test
    public void callsAttackerWithAttackMoves() {
//        game.start();

//        verify(attacker).apply();
    }

    @Test
    public void appliesAttackMoves() {
        AttackMove attackMove = new AttackMove(1, 1, 2, 1, 2);
        Set<AttackMove> attackMoves = Collections.singleton(attackMove);

        attacker.apply(1, attackMoves, board);

        TestUtils.assertCellContents(board.getHome1Cell(), 1, 18);
        TestUtils.assertCellContents(board.cellAt(2, 1), 1, 2);
    }

    @Test
    public void ignoresExceedingAttackMove() {
        AttackMove am = new AttackMove(1, 1, 2, 2, 300);

        attacker.apply(1, Collections.singleton(am), board);

        TestUtils.assertCellContents(board.getHome1Cell(), 1, 20);
        TestUtils.assertCellContents(board.getHome2Cell(), 2, 20);
    }

    @Test
    public void ignoreExceedingAttackMovesAndAppliesLegalOnes() {
        AttackMove legal = new AttackMove(1, 1, 2, 1, 10);
        AttackMove illegal = new AttackMove(1, 1, 2, 2, 12);
        AttackMove legal2 = new AttackMove(1, 1, 1, 2, 5);

        attacker.apply(1, Lists.newArrayList(legal, illegal, legal2), board);

        TestUtils.assertCellContents(board.getHome1Cell(), 1, 5);
        TestUtils.assertCellContents(board.cellAt(2, 1), 1, 10);
        TestUtils.assertCellContents(board.cellAt(1, 2), 1, 5);
        TestUtils.assertCellContents(board.getHome2Cell(), 2, 20);


    }

    @Ignore
    @Test
    public void conqueringAttackMove() {
        Board b = new Board(2);
        b.populateHomeBases(10);
        b.cellAt(1, 1).setNumSoldiers(20);
        AttackMove am = new AttackMove(1, 1, 2, 2, 15);

        attacker.apply(1, Collections.singleton(am), b);

        TestUtils.assertCellContents(b.getHome1Cell(), 1, 5);
        TestUtils.assertCellContents(b.getHome2Cell(), 1, 5);
    }
}