import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
}