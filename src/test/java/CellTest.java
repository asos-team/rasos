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
    public void validatesConstructorParamsOnCellCreation() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NEGATIVE_AMOUNT_OF_SOLDIERS_ERROR);
        new Cell(9, -9);
    }

    @Test
    public void createsCellWithCorrectValuesOnConstruction() {
        Cell cell = new Cell(5, 5);
        TestUtils.assertCellContents(cell, 5, 5);
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
        expectedEx.expectMessage(Cell.NEGATIVE_CONTROLLING_PLAYER_ID_ERROR);
        new Cell(-5, 12);
    }

    @Test
    public void cellCannotContainNegativeNumberOfSoldiers() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NEGATIVE_AMOUNT_OF_SOLDIERS_ERROR);
        new Cell(2, -70);
    }

    @Test
    public void throwOnInvalidConstructorParamsThrowsOnNeutralCellContainingSoldiers() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NEUTRAL_CELL_CONTAINING_SOLDIERS_ERROR);
        Cell cell = new Cell(0, 0);
        cell.setValues(0, 12);
    }

    @Test
    public void throwOnInvalidConstructorParamsThrowsOnControlledCellWithZeroSoldiers() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.CONTROLLED_CELL_WITH_ZERO_SOLDIERS_ERROR);
        Cell cell = new Cell(0, 0);
        cell.setValues(5, 0);
    }

    @Test
    public void throwOnInvalidConstructorParamsThrowsOnNegativeControllingPlayerId() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NEGATIVE_CONTROLLING_PLAYER_ID_ERROR);
        Cell cell = new Cell(0, 0);
        cell.setValues(-5, 12);

    }

    @Test
    public void throwOnInvalidConstructorParamsThrowsOnNegativeAmountOfSoldiers() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NEGATIVE_AMOUNT_OF_SOLDIERS_ERROR);
        Cell cell = new Cell(0, 0);
        cell.setValues(2, -70);
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
    public void whenNumSoldiersSetToZeroCellBecomesNeutral() {
        Cell cell = new Cell(5, 12);
        cell.updateNumSoldiers(0);
        assertTrue(cell.isNeutral());
    }

    @Test
    public void setValues() {
        Cell cell = new Cell(3, 5);
        cell.setValues(2, 4);
        TestUtils.assertCellContents(cell, 2, 4);
    }

    @Test
    public void cantSetValuesWithInvalidParams() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NEGATIVE_AMOUNT_OF_SOLDIERS_ERROR);
        Cell cell = new Cell(6, 7);
        cell.setValues(5, -1);
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
        expectedEx.expectMessage(Cell.NEGATIVE_CONTROLLING_PLAYER_ID_ERROR);
        Cell cell = new Cell(5, 12);
        cell.updateControllingPlayerId(0);
    }

    @Test
    public void humanReadableToString() {
        Cell player1Cell = new Cell(1, 10);
        Cell neutralCell = Cell.neutral();

        assertThat(player1Cell.toString(), is("[10,1]"));
        assertThat(neutralCell.toString(), is("[0,0]"));
    }
}