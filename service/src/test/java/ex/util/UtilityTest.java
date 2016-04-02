package ex.util;

import org.junit.Test;
import ru.simflex.ex.util.Utility;

import static org.junit.Assert.assertTrue;

/**
 * Test class for Utility methods.
 */
public class UtilityTest {

    @Test
    public void parsePrice_Must_Return_Correct_Value() throws Exception {
        String priceString = "2.5";
        double expectedResult = 2.5;
        double result = Utility.parsePrice(priceString);
        assertTrue(expectedResult == result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parsePrice_Must_Return_Exception_If_Incorrect() throws Exception {
        String priceString = "2,5s";
        Utility.parsePrice(priceString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parsePrice_Must_Return_Exception_If_Negative() throws Exception {
        String priceString = "-2.5";
        Utility.parsePrice(priceString);
    }
}