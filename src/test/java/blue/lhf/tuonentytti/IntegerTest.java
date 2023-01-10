package blue.lhf.tuonentytti;

import blue.lhf.tuonentytti.source.*;
import org.junit.*;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class IntegerTest {
    @Test
    public void testDigit() throws Exception {
        assertEquals("False negative for digit", "9", Json.digitToken(new StringSource("9")));
    }

    @Test
    public void testDigits() throws Exception {
        assertEquals("False negative for digits", "123", Json.digitsToken(new StringSource("123")));
    }

    @Test
    public void testInteger() throws Exception {
        assertEquals("False negative for integer", "123", Json.integerToken(new StringSource("123")));
    }

    @Test
    public void testNegative() throws Exception {
        assertEquals("False negative for integer", "-123", Json.integerToken(new StringSource("-123")));
    }

    @Test
    public void testIntegerDigit() throws Exception {
        assertEquals("False negative for integer", "-9", Json.integerToken(new StringSource("-9")));
    }

    @Test
    public void testNegativeDigit() throws Exception {
        assertEquals("False negative for integer", "-9", Json.integerToken(new StringSource("-9")));
    }
}
