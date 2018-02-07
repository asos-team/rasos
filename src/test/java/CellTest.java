import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class CellTest {

    @Rule
    public final ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void neutralCell() {
        assertTrue(Cell.neutral().isNeutral());
    }

    @Test
    public void nonNeutralCell() {
        assertFalse(new Cell(2, 50).isNeutral());
    }

    @Test
    public void impossibleToCreateNeutralCellWithSoldiers() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NEUTRAL_CELL_CONTAINING_SOLDIERS_ERROR);
        new Cell(0, 12);
    }

    @Test
    public void impossibleToCreateControlledCellWithNoSoldiers() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.CONTROLLED_CELL_WITH_ZERO_SOLDIERS_ERROR);
        new Cell(5, 0);
    }

    @Test
    public void controllingPlayerMustBeAPositiveNumber() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NON_POSITIVE_CONTROLLING_PLAYER_ID_ERROR);
        new Cell(-5, 12);
    }

    @Test
    public void cellCannotContainNegativeNumberOfSoldiers() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NEGATIVE_AMOUNT_OF_SOLDIERS_ERROR);
        new Cell(2, -70);
    }

    @Test
    public void getNumSoldiers() {
        assertEquals(32, new Cell(3, 32).getNumSoldiers());
    }

    @Test
    public void getControllingPlayerId() {
        assertEquals(3, new Cell(3, 32).getControllingPlayerId());
    }

    @Test
    public void isControlledBy() {
        assertTrue(new Cell(12, 456).isControlledBy(12));
    }

    @Test
    public void updateNumSoldiers() {
        Cell cell = new Cell(3, 32);
        cell.updateNumSoldiers(13);
        assertEquals(13, cell.getNumSoldiers());
    }

    @Test
    public void cannotSetNegativeNumSoldiers() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NEGATIVE_AMOUNT_OF_SOLDIERS_ERROR);
        Cell cell = new Cell(3, 32);
        cell.updateNumSoldiers(-43);
    }

    @Test
    public void updateControllingPlayerId() {
        Cell cell = new Cell(3, 32);
        cell.updateControllingPlayerId(5);
        assertTrue("cell should be controlled by the playerId it was set to", cell.isControlledBy(5));
    }

    @Test
    public void cannotSetControllingPlayerToANonPositiveNumber() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NON_POSITIVE_CONTROLLING_PLAYER_ID_ERROR);
        Cell cell = new Cell(5, 12);
        cell.updateControllingPlayerId(0);
    }

    @Test
    public void whenNumSoldiersSetToZeroCellBecomesNeutral() {
        Cell cell = new Cell(5, 12);
        cell.updateNumSoldiers(0);
        assertTrue(cell.isNeutral());
    }

    @Test
    public void humanReadableToString() {
        Cell player1Cell = new Cell(1, 10);
        Cell neutralCell = Cell.neutral();

        assertThat(player1Cell.toString(), is("[10,1]"));
        assertThat(neutralCell.toString(), is("[0,0]"));
    }
}