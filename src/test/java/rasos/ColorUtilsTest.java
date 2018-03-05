package rasos;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static rasos.ColorUtils.ANSI_RESET_COLOR;
import static rasos.ColorUtils.ANSI_YELLOW;

public class ColorUtilsTest {

    @Test
    public void colorString() {
        String colorString = ColorUtils.colorString("my_str", ANSI_YELLOW);
        assertEquals(ANSI_YELLOW + "my_str" + ANSI_RESET_COLOR, colorString);
    }
}