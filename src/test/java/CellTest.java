import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CellTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void emptyCell() throws Exception {
        assertTrue(new Cell().isEmpty());
    }

    @Test
    public void nonEmptyCell() throws Exception {
        assertFalse(new Cell(2, 50).isEmpty());
    }

    @Test
    public void impossibleToCreateCellWithNonControlledSoldiers() throws Exception {
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
    public void noNegativeNumSoldiers() throws Exception {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NEGATIVE_AMOUNT_OF_SOLDIERS_ERROR);
        new Cell(2, -70);
    }
}