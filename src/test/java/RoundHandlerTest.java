import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class RoundHandlerTest {
    private Board board;
    private Player playerA;
    private Player playerB;

    @Before
    public void setUp() {
        board = new Board(7);
        playerA = mock(Player.class);
        playerB = mock(Player.class);
    }

    @Test
    public void callsPlayerOnReinforcementWithGameBoard() {
        RoundHandler roundHandler = createRoundHandler();

        roundHandler.playOneRound(board);

        verify(playerA).onReinforcement(eq(board), any(int.class));
        verify(playerB).onReinforcement(eq(board), any(int.class));
    }

    @Test
    public void callsPlayerOnReinforcementWithNumberOfSoldiers() {
        RoundHandler roundHandler = createRoundHandler();
        makePlayerAControlTotalOf_3_Cells(board);
        makePlayerBControlTotalOf_2_Cells(board);

        roundHandler.playOneRound(board);

        verify(playerA).onReinforcement(any(Board.class), eq(3));
        verify(playerB).onReinforcement(any(Board.class), eq(2));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void callsReinforcerWithReinforcementMoves() {
        Reinforcer reinforcer = mock(Reinforcer.class);

        Iterable<ReinforcementMove> movesA = mock(Iterable.class);
        Iterable<ReinforcementMove> movesB = mock(Iterable.class);
        when(playerA.onReinforcement(any(Board.class), any(int.class))).thenReturn(movesA);
        when(playerB.onReinforcement(any(Board.class), any(int.class))).thenReturn(movesB);

        RoundHandler roundHandler = createRoundHandler(reinforcer, new Attacker());
        makePlayerAControlTotalOf_3_Cells(board);
        makePlayerBControlTotalOf_2_Cells(board);

        roundHandler.playOneRound(board);

        verify(reinforcer).apply(board, movesA, 3, 1);
        verify(reinforcer).apply(board, movesB, 2, 2);
    }

    @Test
    public void callsPlayerOnAttackWithGameBoard() {
        RoundHandler roundHandler = createRoundHandler();

        roundHandler.playOneRound(board);

        verify(playerA).onAttack(board);
        verify(playerB).onAttack(board);
    }

    @SuppressWarnings("unchecked")
    @Test
    @Ignore
    public void callsAttackerWithAttackMoves() {
        Attacker attacker = mock(Attacker.class);

        Iterable<AttackMove> movesA = mock(Iterable.class);
        Iterable<AttackMove> movesB = mock(Iterable.class);
        when(playerA.onAttack(any(Board.class))).thenReturn(movesA);
        when(playerB.onAttack(any(Board.class))).thenReturn(movesB);

        RoundHandler roundHandler = createRoundHandler(new Reinforcer(), attacker);

        roundHandler.playOneRound(board);

        verify(attacker).apply(board, movesA, movesB);
    }

    private RoundHandler createRoundHandler() {
        return createRoundHandler(new Reinforcer(), new Attacker());
    }

    private RoundHandler createRoundHandler(Reinforcer reinforcer, Attacker attacker) {
        return new RoundHandler(playerA, playerB, attacker, reinforcer);
    }

    private void makePlayerAControlTotalOf_3_Cells(Board board) {
        board.setCell(1, 1, new Cell(1, 4));
        board.setCell(1, 3, new Cell(1, 4));
        board.setCell(3, 1, new Cell(1, 19));
    }

    private void makePlayerBControlTotalOf_2_Cells(Board board) {
        board.setCell(2, 2, new Cell(2, 2));
        board.setCell(3, 2, new Cell(2, 2));
    }
}
