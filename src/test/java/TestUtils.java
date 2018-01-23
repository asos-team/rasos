import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TestUtils {
    static void assertCellContents(Cell cell, int controllingPlayer, int numSoldiers) {
        assertTrue(cell.isControlledBy(controllingPlayer));
        assertThat(cell.getNumSoldiers(), is(numSoldiers));
    }
}
