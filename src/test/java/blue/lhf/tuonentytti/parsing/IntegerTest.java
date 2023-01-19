package blue.lhf.tuonentytti.parsing;

import blue.lhf.tuonentytti.reader.StringSource;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegerTest {
    @Test
    public void testDigit() throws Exception {
        Assertions.assertEquals("9", JsonLexer.digit(new StringSource("9")), "False negative for digit");
    }

    @Test
    public void testDigits() throws Exception {
        assertEquals("123", JsonLexer.digits(new StringSource("123")), "False negative for digits");
    }

    @Test
    public void testInteger() throws Exception {
        assertEquals("123", JsonLexer.integer(new StringSource("123")), "False negative for integer");
    }

    @Test
    public void testNegative() throws Exception {
        assertEquals("-123", JsonLexer.integer(new StringSource("-123")), "False negative for integer");
    }

    @Test
    public void testIntegerDigit() throws Exception {
        assertEquals("-9", JsonLexer.integer(new StringSource("-9")), "False negative for integer");
    }

    @Test
    public void testNegativeDigit() throws Exception {
        assertEquals("-9", JsonLexer.integer(new StringSource("-9")), "False negative for integer");
    }
}