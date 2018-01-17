import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestUtils {
    static void assertCellContents(Cell cell, int controllingPlayer, int numSoldiers) {
        assertThat(cell.getControllingPlayer(), is(controllingPlayer));
        assertThat(cell.getNumSoldiers(), is(numSoldiers));
    }
}
