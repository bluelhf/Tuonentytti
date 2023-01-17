package blue.lhf.tuonentytti.parsing;

import blue.lhf.tuonentytti.model.JsonNumber;
import blue.lhf.tuonentytti.reader.StringSource;
import org.junit.*;

import static org.junit.Assert.assertEquals;

public class NumberTest {
    @Test
    public void testNumber() throws Exception {
        Assert.assertEquals("False negative number", new JsonNumber(5318008.0), JsonParser.readNumber(new StringSource("5318008")));
    }

    @Test
    public void testExponent() throws Exception {
        assertEquals("False negative number", new JsonNumber(4E6), JsonParser.readNumber(new StringSource("4E6")));
    }

    @Test
    public void testFraction() throws Exception {
        assertEquals("False negative number", new JsonNumber(420.69), JsonParser.readNumber(new StringSource("420.69")));
    }

    @Test
    public void testFractionExponent() throws Exception {
        assertEquals("False negative number", new JsonNumber(420.69E6), JsonParser.readNumber(new StringSource("420.69E6")));
    }

    @Test
    public void testNegativeExponent() throws Exception {
        assertEquals("False negative number", new JsonNumber(420.69E-6), JsonParser.readNumber(new StringSource("420.69E-6")));
    }

    @Test
    public void testPositiveExponent() throws Exception {
        assertEquals("False negative number", new JsonNumber(420.69E+6), JsonParser.readNumber(new StringSource("420.69E+6")));
    }
}
