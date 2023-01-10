package blue.lhf.tuonentytti;

import blue.lhf.tuonentytti.source.*;
import org.junit.*;

import java.util.*;

import static blue.lhf.tuonentytti.model.JsonValue.coerce;
import static org.junit.Assert.*;

public class StringTest {

    @Test
    public void testString() throws Exception {
        testFN("boobs", "boobs");
    }

    protected void testFN(final String input, final String expected) throws Exception {
        final StringSource reader = new StringSource("\"" + input + "\"");
        assertEquals("False negative string", coerce(expected), Json.readString(reader));
    }

    protected void testFP(final StringSource reader) throws Exception {
        assertNull("False positive string", Json.readString(reader));
    }

    @Test
    public void testBasicEscapes() throws Exception {
        testFN("\\b", "\b");
        testFN("\\f", "\f");
        testFN("\\n", "\n");
        testFN("\\r", "\r");
        testFN("\\t", "\t");
    }

    @Test
    public void testUnicode() throws Exception {
        final Random random = new Random();
        for (int i = 0; i < 0xFFFF; ++i) {
            final int num = random.nextInt(0xFFFF);
            String hex = Integer.toHexString(num);
            hex = "\\u" + "0".repeat(4 - hex.length()) + hex;
            testFN(hex, Character.toString(num));
        }
    }

    @Test
    public void testMidEscape() throws Exception {
        testFN("test \\\" string", "test \" string");
    }

    @Test
    public void testNoQuotes() throws Exception {
        testFP(new StringSource("no quotes here"));
    }

    @Test
    public void testTrailing() throws Exception {
        testFP(new StringSource("\"one quote"));
    }

    @Test
    public void testLeading() throws Exception {
        testFP(new StringSource("one quote\""));
    }
}
