package blue.lhf.tuonentytti.parsing;

import blue.lhf.tuonentytti.model.JsonNumber;
import blue.lhf.tuonentytti.reader.StringSource;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class NumberTest {
    @Test
    public void testNumber() throws Exception {
        assertEquals(new JsonNumber(5318008.0), JsonParser.readNumber(new StringSource("5318008")), "False negative number");
    }

    @Test
    public void testExponent() throws Exception {
        assertEquals(new JsonNumber(4E6), JsonParser.readNumber(new StringSource("4E6")), "False negative number");
    }

    @Test
    public void testFraction() throws Exception {
        assertEquals(new JsonNumber(420.69), JsonParser.readNumber(new StringSource("420.69")), "False negative number");
    }

    @Test
    public void testFractionExponent() throws Exception {
        assertEquals(new JsonNumber(420.69E6), JsonParser.readNumber(new StringSource("420.69E6")), "False negative number");
    }

    @Test
    public void testNegativeExponent() throws Exception {
        assertEquals(new JsonNumber(420.69E-6), JsonParser.readNumber(new StringSource("420.69E-6")), "False negative number");
    }

    @Test
    public void testPositiveExponent() throws Exception {
        assertEquals(new JsonNumber(420.69E+6), JsonParser.readNumber(new StringSource("420.69E+6")), "False negative number");
    }
}
