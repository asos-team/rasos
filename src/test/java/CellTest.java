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
    public void cellWithoutSoldiersIsNeutral() {
        assertTrue(new Cell(5, 0).isNeutral());
    }

    @Test
    public void impossibleToCreateNeutralCellWithSoldiers() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NEUTRAL_CELL_CONTAINING_SOLDIERS_ERROR);
        new Cell(0, 12);
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
    public void isControlledBy() {
        assertTrue(new Cell(12, 456).isControlledBy(12));
    }

    @Test
    public void getNumSoldiers() {
        assertEquals(32, new Cell(3, 32).getNumSoldiers());
    }

    @Test
    public void setNumSoldiers() {
        Cell cell = new Cell(3, 32);
        cell.setNumSoldiers(13);
        assertEquals(13, cell.getNumSoldiers());
    }

    @Test
    public void cannotSetNegativeNumSoldiers() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NEGATIVE_AMOUNT_OF_SOLDIERS_ERROR);
        Cell cell = new Cell(3, 32);
        cell.setNumSoldiers(-43);
    }

    @Test
    public void setControllingPlayerId() {
        Cell cell = new Cell(3, 32);
        cell.setControllingPlayerId(5);
        assertTrue("cell should be controlled by the playerId it was set to", cell.isControlledBy(5));
    }

    @Test
    public void cannotSetControllingPlayerToANegativeNumber() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NEGATIVE_CONTROLLING_PLAYER_ID_ERROR);
        Cell cell = new Cell(5, 12);
        cell.setControllingPlayerId(-8);
    }

    @Test
    public void humanReadableToString() {
        Cell player1Cell = new Cell(1, 10);
        Cell neutralCell = Cell.neutral();

        assertThat(player1Cell.toString(), is("[10,1]"));
        assertThat(neutralCell.toString(), is("[0,0]"));
    }
}