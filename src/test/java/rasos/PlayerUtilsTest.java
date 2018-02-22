package rasos;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

public class PlayerUtilsTest {


    @Test
    public void getControlledCells() {
        Player p = mock(Player.class, CALLS_REAL_METHODS);
        p.setPlayerId(1);
        Board b = new Board(3);
        b.cellAt(1, 1).setValues(1, 10);
        b.cellAt(2, 2).setValues(1, 10);
        PlayerUtils utils = new PlayerUtils(p);

        assertThat(utils.getControlledCells(b), hasItems(new CellCoordinates(1, 1), new CellCoordinates(2, 2)));

    }

    @Test
    public void getNeighbours() {
        PlayerUtils p = new PlayerUtils(mock(Player.class));
        Board b = new Board(3);

        List<CellCoordinates> neighbours = p.getNeighbours(b, new CellCoordinates(2, 2));

        assertThat(neighbours, hasItem(is(new CellCoordinates(1, 1))));
        assertThat(neighbours, hasItem(is(new CellCoordinates(1, 2))));
        assertThat(neighbours, hasItem(is(new CellCoordinates(1, 3))));
        assertThat(neighbours, hasItem(is(new CellCoordinates(2, 1))));
        assertThat(neighbours, hasItem(is(new CellCoordinates(2, 3))));
        assertThat(neighbours, hasItem(is(new CellCoordinates(3, 1))));
        assertThat(neighbours, hasItem(is(new CellCoordinates(3, 2))));
        assertThat(neighbours, hasItem(is(new CellCoordinates(3, 3))));
    }

    @Test
    public void getNeighboursDoesNotReturnOutOfBounds() {
        PlayerUtils p = new PlayerUtils(mock(Player.class));
        Board b = new Board(3);

        List<CellCoordinates> neighbours = p.getNeighbours(b, new CellCoordinates(1, 1));

        assertThat(neighbours, hasItem(is(new CellCoordinates(1, 2))));
        assertThat(neighbours, hasItem(is(new CellCoordinates(2, 2))));
        assertThat(neighbours, hasItem(is(new CellCoordinates(2, 1))));
    }
}