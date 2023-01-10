package blue.lhf.tuonentytti;

import blue.lhf.tuonentytti.model.*;
import blue.lhf.tuonentytti.source.*;
import org.junit.*;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class NumberTest {
    @Test
    public void testNumber() throws Exception {
        assertEquals("False negative number", new JsonNumber(5318008.0), Json.readNumber(new StringSource("5318008")));
    }

    @Test
    public void testExponent() throws Exception {
        assertEquals("False negative number", new JsonNumber(4E6), Json.readNumber(new StringSource("4E6")));
    }

    @Test
    public void testFraction() throws Exception {
        assertEquals("False negative number", new JsonNumber(420.69), Json.readNumber(new StringSource("420.69")));
    }

    @Test
    public void testFractionExponent() throws Exception {
        assertEquals("False negative number", new JsonNumber(420.69E6), Json.readNumber(new StringSource("420.69E6")));
    }

    @Test
    public void testNegativeExponent() throws Exception {
        assertEquals("False negative number", new JsonNumber(420.69E-6), Json.readNumber(new StringSource("420.69E-6")));
    }

    @Test
    public void testPositiveExponent() throws Exception {
        assertEquals("False negative number", new JsonNumber(420.69E+6), Json.readNumber(new StringSource("420.69E+6")));
    }
}
