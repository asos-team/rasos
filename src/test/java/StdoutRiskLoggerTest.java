import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class StdoutRiskLoggerTest {


    private ByteArrayOutputStream outContent;
    private StdoutRiskLogger logger;

    @Before
    public void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        logger = new StdoutRiskLogger();
    }

    @Test
    public void newLoggerIsNotNull() {
        assertThat(logger, notNullValue());
    }

    @Test
    public void logStartPrintsGameStarted() {
        logger.logStart();

        assertEquals("Game started" + System.lineSeparator(), outContent.toString());
    }


}
