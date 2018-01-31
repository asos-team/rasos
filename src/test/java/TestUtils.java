import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestUtils {
    static void assertCellContents(Cell cell, int controllingPlayer, int numSoldiers) {
        assertTrue(String.format("Expected cell to be controlled by %d", controllingPlayer), cell.isControlledBy(controllingPlayer));
        assertEquals(String.format("Expected cell to have %d soldiers in it", numSoldiers), cell.getNumSoldiers(), numSoldiers);
    }
}
