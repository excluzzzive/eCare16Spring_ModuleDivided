package ex.util;

import org.junit.Test;
import ru.simflex.ex.util.Utility;

import static org.junit.Assert.assertEquals;
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

    @Test
    public void getTotalPages_Correct_When_Page_Is_Full() {
        int entityCount = 50;
        int limitPerPages = 10;
        int expectedResult = 5;
        int result = Utility.getTotalPages(entityCount, limitPerPages);
        assertEquals(expectedResult, result);
    }

    @Test
    public void getTotalPages_Correct_When_Page_Is_Not_Full() {
        int limitPerPages = 10;
        int entityCount = 48;
        int expectedResult = 5;
        int result = Utility.getTotalPages(entityCount, limitPerPages);
        assertEquals(expectedResult, result);
    }

    @Test
    public void getHashedPassword_Correct() {
        String password = "password";
        String expectedResult = "EFCED041A80373CCE89DC7FAF648421DA8F2AB19A3E7EDEBE6C21DECED09DAB7";
        String result = Utility.getHashedPassword(password);
        assertEquals(expectedResult, result);
    }
}