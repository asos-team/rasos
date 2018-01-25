import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class CellTest {

    @Rule
    public final ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void neutralCell() throws Exception {
        assertTrue(new Cell().isNeutral());
    }

    @Test
    public void nonNeutralCell() throws Exception {
        assertFalse(new Cell(2, 50).isNeutral());
    }

    @Test
    public void cellWithoutSoldiersIsNeutral() throws Exception {
        assertTrue(new Cell(5, 0).isNeutral());
    }

    @Test
    public void impossibleToCreateNeutralCellWithSoldiers() throws Exception {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NEUTRAL_CELL_CONTAINING_SOLDIERS_ERROR);
        new Cell(0, 12);
    }

    @Test
    public void controllingPlayerMustBeAPositiveNumber() throws Exception {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NEGATIVE_CONTROLLING_PLAYER_ID_ERROR);
        new Cell(-5, 12);
    }

    @Test
    public void cellCannotContainNegativeNumberOfSoldiers() throws Exception {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NEGATIVE_AMOUNT_OF_SOLDIERS_ERROR);
        new Cell(2, -70);
    }

    @Test
    public void isControlledBy() throws Exception {
        assertTrue(new Cell(12, 456).isControlledBy(12));
    }

    @Test
    public void getNumSoldiers() throws Exception {
        assertEquals(32, new Cell(3, 32).getNumSoldiers());
    }

    @Test
    public void setNumSoldiers() throws Exception {
        Cell cell = new Cell(3, 32);
        cell.setNumSoldiers(13);
        assertEquals(13, cell.getNumSoldiers());
    }

    @Test
    public void setControllingPlayerId() throws Exception {
        Cell cell = new Cell(3, 32);
        cell.setControllingPlayerId(5);
        assertTrue("cell should be controlled by the playerId it was set to", cell.isControlledBy(5));
    }
}