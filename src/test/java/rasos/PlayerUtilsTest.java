package rasos;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertThat;

public class PlayerUtilsTest {


    @Test
    public void getControlledCells() {
        Player p = Mockito.mock(Player.class, Mockito.CALLS_REAL_METHODS);
        p.setPlayerId(1);
        Board b = new Board(3);
        b.cellAt(1, 1).setValues(1, 10);
        b.cellAt(2, 2).setValues(1, 10);
        PlayerUtils utils = new PlayerUtils(p);

        assertThat(utils.getControlledCells(b), CoreMatchers.hasItems(new CellCoordinates(1, 1), new CellCoordinates(2, 2)));

    }
}