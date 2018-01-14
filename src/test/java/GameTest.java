import com.google.common.collect.Lists;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class GameTest {

    private static final int boardDim = 2;
    private Game game;
    private Player player1;
    private Player player2;

    @Before
    public void setUp() {
        player1 = mock(Player.class);
        player2 = mock(Player.class);
        when(player1.onReinforcement(any(Pair[][].class), any(int.class))).thenReturn(Lists.newArrayList());
        when(player2.onReinforcement(any(Pair[][].class), any(int.class))).thenReturn(Lists.newArrayList());
        game = new Game(boardDim, boardDim, player1, player2);
    }

    @Test
    public void getsBoardDimensions() {
        Pair<Integer, Integer>[][] board = game.getBoard();

        assertThat(board.length, is(boardDim));
        assertThat(board[0].length, is(boardDim));
    }

    @Test
    public void boardInitializesEmptyWithSoldiersOnStartPoints() {
        for (int i = 0; i < game.getBoard().length; i++) {
            for (int j = 0; j < game.getBoard()[i].length; j++) {
                Pair<Integer, Integer> cell = game.getBoard()[i][j];
                if (i == 0 && j == 0) {
                    assertThat(cell, is(new Pair<Integer, Integer>(1, 20)));
                } else if (i == boardDim - 1 && j == boardDim - 1) {
                    assertThat(cell, is(new Pair<Integer, Integer>(2, 20)));
                } else {
                    assertThat(cell, is(new Pair<Integer, Integer>(0, 0)));
                }
            }
        }
    }

    @Test
    public void callsPlayerOnReinforcementWithSuitableNumberOfSoldiers() {
        game.tick();
        verify(player1).onReinforcement(game.getBoard(), 1);
        verify(player2).onReinforcement(game.getBoard(), 1);
    }

    @Test
    public void appliesFirstMoveReinforcement() {
        when(player1.onReinforcement(any(Pair[][].class), any(int.class))).thenReturn(Lists.newArrayList(new ReinforcementMove(0, 0, 1)));
        when(player2.onReinforcement(any(Pair[][].class), any(int.class))).thenReturn(Lists.newArrayList(new ReinforcementMove(boardDim - 1, boardDim - 1, 1)));

        game.tick();

        Pair<Integer, Integer>[][] board = game.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Pair<Integer, Integer> cell = board[i][j];
                if (i == 0 && j == 0) {
                    assertThat(cell, is(new Pair<Integer, Integer>(1, 21)));
                } else if (i == boardDim - 1 && j == boardDim - 1) {
                    assertThat(cell, is(new Pair<Integer, Integer>(2, 21)));
                } else {
                    assertThat(cell, is(new Pair<Integer, Integer>(0, 0)));
                }
            }
        }
    }

    @Test
    public void appliesValidReinforcementForAMidgameBoardConfiguration() {
        Pair<Integer, Integer>[][] configuration = BoardUtils.getBlankBoard(3, 3);
        configuration[0][0] = new Pair<Integer, Integer>(1, 18);
        configuration[0][1] = new Pair<Integer, Integer>(1, 2);
        configuration[0][2] = new Pair<Integer, Integer>(1, 1);
        configuration[1][0] = new Pair<Integer, Integer>(0, 0);
        configuration[1][1] = new Pair<Integer, Integer>(2, 8);
        configuration[1][2] = new Pair<Integer, Integer>(2, 7);
        configuration[2][0] = new Pair<Integer, Integer>(1, 5);
        configuration[2][1] = new Pair<Integer, Integer>(2, 1);
        configuration[2][2] = new Pair<Integer, Integer>(2, 5);

        List<ReinforcementMove> player1Reinforcements = Lists.newArrayList(new ReinforcementMove(0, 0, 1),
                new ReinforcementMove(0, 1, 1),
                new ReinforcementMove(0, 2, 1),
                new ReinforcementMove(2, 0, 1));
        List<ReinforcementMove> player2Reinforcements = Lists.newArrayList(new ReinforcementMove(1, 1, 1),
                new ReinforcementMove(1, 2, 1),
                new ReinforcementMove(2, 1, 1),
                new ReinforcementMove(2, 2, 1));


        when(player1.onReinforcement(any(Pair[][].class), any(int.class))).thenReturn(player1Reinforcements);
        when(player2.onReinforcement(any(Pair[][].class), any(int.class))).thenReturn(player2Reinforcements);

        Game game = new Game(configuration, player1, player2);

        game.tick();

        Pair<Integer, Integer>[][] nextConfiguration = BoardUtils.getBlankBoard(3, 3);
        nextConfiguration[0][0] = new Pair<Integer, Integer>(1, 19);
        nextConfiguration[0][1] = new Pair<Integer, Integer>(1, 3);
        nextConfiguration[0][2] = new Pair<Integer, Integer>(1, 2);
        nextConfiguration[1][0] = new Pair<Integer, Integer>(0, 0);
        nextConfiguration[1][1] = new Pair<Integer, Integer>(2, 9);
        nextConfiguration[1][2] = new Pair<Integer, Integer>(2, 8);
        nextConfiguration[2][0] = new Pair<Integer, Integer>(1, 6);
        nextConfiguration[2][1] = new Pair<Integer, Integer>(2, 2);
        nextConfiguration[2][2] = new Pair<Integer, Integer>(2, 6);
        assertThat(game.getBoard(), is(nextConfiguration));
    }

    @Test
    public void initializeWithSpecialBoardConfiguration() {
        Pair<Integer, Integer>[][] configuration = BoardUtils.getBlankBoard(2, 2);
        Game g = new Game(configuration, player1, player2);
        assertThat(g.getBoard(), is(configuration));
    }

    @Test(expected = RuntimeException.class)
    public void throwOnUninitializedBoardConfiguration() {
        Pair<Integer, Integer>[][] configuration = new Pair[2][2];
        new Game(configuration, player1, player2);
    }

    @Test
    public void callsPlayerReinforcementOnSpecialBoardConfiguration() {
        Pair<Integer, Integer>[][] configuration = BoardUtils.getBlankBoard(2, 2);
        configuration[0][0] = new Pair<Integer, Integer>(1, 10);
        configuration[0][1] = new Pair<Integer, Integer>(1, 10);
        configuration[1][1] = new Pair<Integer, Integer>(2, 10);
        Game g = new Game(configuration, player1, player2);

        g.tick();

        verify(player1).onReinforcement(configuration, 2);
        verify(player2).onReinforcement(configuration, 1);
    }

    @Test
    public void callsPlayerOnAttackWithSuitableArguments() {
        game.tick();
        verify(player1).onAttack(game.getBoard());
        verify(player2).onAttack(game.getBoard());
    }

    @Test
    public void appliesAttackMoves() {
        when(player1.onAttack(any(Pair[][].class))).thenReturn(Collections.singleton(new AttackMove(0, 1, 1)));
        when(player2.onAttack(any(Pair[][].class))).thenReturn(Collections.singleton(new AttackMove(boardDim - 1, boardDim - 2, 1)));
        game.tick();
        Pair<Integer, Integer>[][] board = game.getBoard();
        assertThat(board[0][0], is(new Pair<Integer, Integer>(1, 19)));
        assertThat(board[0][1], is(new Pair<Integer, Integer>(1, 1)));
        assertThat(board[boardDim - 1][boardDim - 2], is(new Pair<Integer, Integer>(2, 1)));
        assertThat(board[boardDim - 1][boardDim - 1], is(new Pair<Integer, Integer>(2, 19)));
    }

    @Test
    public void appliesDifferentAttackMoves() {
        when(player1.onAttack(any(Pair[][].class))).thenReturn(Collections.singleton(new AttackMove(1, 0, 1)));
        when(player2.onAttack(any(Pair[][].class))).thenReturn(Collections.singleton(new AttackMove(boardDim - 2, boardDim - 1, 1)));
        game.tick();
        Pair<Integer, Integer>[][] board = game.getBoard();
        assertThat(board[0][0], is(new Pair<Integer, Integer>(1, 19)));
        assertThat(board[1][0], is(new Pair<Integer, Integer>(1, 1)));
        assertThat(board[boardDim - 2][boardDim - 1], is(new Pair<Integer, Integer>(2, 1)));
        assertThat(board[boardDim - 1][boardDim - 1], is(new Pair<Integer, Integer>(2, 19)));
    }

    @Test
    public void appliesAttackMovesAmount() {
        when(player1.onAttack(any(Pair[][].class))).thenReturn(Collections.singleton(new AttackMove(1, 0, 5)));
        when(player2.onAttack(any(Pair[][].class))).thenReturn(Collections.singleton(new AttackMove(boardDim - 2, boardDim - 1, 2)));
        game.tick();
        Pair<Integer, Integer>[][] board = game.getBoard();
        assertThat(board[0][0], is(new Pair<Integer, Integer>(1, 15)));
        assertThat(board[1][0], is(new Pair<Integer, Integer>(1, 5)));
        assertThat(board[boardDim - 2][boardDim - 1], is(new Pair<Integer, Integer>(2, 2)));
        assertThat(board[boardDim - 1][boardDim - 1], is(new Pair<Integer, Integer>(2, 18)));
    }

    @Test
    public void appliesManyAttackMoves() {
        AttackMove move1 = new AttackMove(0, 1, 4);
        AttackMove move2 = new AttackMove(1, 0, 2);
        when(player1.onAttack(any(Pair[][].class))).thenReturn(Arrays.asList(move1, move2));
        game.tick();
        Pair<Integer, Integer>[][] board = game.getBoard();
        assertThat(board[0][0], is(new Pair<Integer, Integer>(1, 14)));
        assertThat(board[0][1], is(new Pair<Integer, Integer>(1, 4)));
        assertThat(board[1][0], is(new Pair<Integer, Integer>(1, 2)));
        assertThat(board[boardDim - 1][boardDim - 1], is(new Pair<Integer, Integer>(2, 20)));
    }

//    @Test
//    public void appliesAttackOnSameCell_Tie() {
//        when(player1.onAttack(any(Pair[][].class))).thenReturn(new AttackMove(0, 1, 1));
//        when(player2.onAttack(any(Pair[][].class))).thenReturn(new AttackMove(0, 1, 1));
//        game.tick();
//        Pair<Integer, Integer>[][] board = game.getBoard();
//        assertThat(board[0][0], is(new Pair<Integer, Integer>(1, 19)));
//        assertThat(board[0][1], is(new Pair<Integer, Integer>(0, 0)));
//        assertThat(board[1][0], is(new Pair<Integer, Integer>(0, 0)));
//        assertThat(board[1][1], is(new Pair<Integer, Integer>(2, 19)));
//    }
}