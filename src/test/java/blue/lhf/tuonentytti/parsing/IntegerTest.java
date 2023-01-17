package blue.lhf.tuonentytti.parsing;

import blue.lhf.tuonentytti.reader.StringSource;
import org.junit.*;

import static org.junit.Assert.assertEquals;

public class IntegerTest {
    @Test
    public void testDigit() throws Exception {
        Assert.assertEquals("False negative for digit", "9", JsonLexer.digit(new StringSource("9")));
    }

    @Test
    public void testDigits() throws Exception {
        assertEquals("False negative for digits", "123", JsonLexer.digits(new StringSource("123")));
    }

    @Test
    public void testInteger() throws Exception {
        assertEquals("False negative for integer", "123", JsonLexer.integer(new StringSource("123")));
    }

    @Test
    public void testNegative() throws Exception {
        assertEquals("False negative for integer", "-123", JsonLexer.integer(new StringSource("-123")));
    }

    @Test
    public void testIntegerDigit() throws Exception {
        assertEquals("False negative for integer", "-9", JsonLexer.integer(new StringSource("-9")));
    }

    @Test
    public void testNegativeDigit() throws Exception {
        assertEquals("False negative for integer", "-9", JsonLexer.integer(new StringSource("-9")));
    }
}
