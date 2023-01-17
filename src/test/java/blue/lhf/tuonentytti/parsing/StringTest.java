package blue.lhf.tuonentytti.parsing;

import blue.lhf.tuonentytti.model.JsonString;
import blue.lhf.tuonentytti.reader.StringSource;
import org.junit.*;

import java.util.Random;

import static org.junit.Assert.assertThrows;

public class StringTest {

    @Test
    public void testString() throws Exception {
        testFN("boobs", "boobs");
    }

    protected void testFN(final String input, final String expected) throws Exception {
        final StringSource reader = new StringSource("\"" + input + "\"");
        Assert.assertEquals("False negative string", new JsonString(expected), JsonParser.readString(reader));
    }

    protected void testFP(final StringSource reader) {
        assertThrows("False positive string", JsonParseException.class, () -> JsonParser.readString(reader));
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
    public void testNoQuotes() {
        testFP(new StringSource("no quotes here"));
    }

    @Test
    public void testTrailing() {
        testFP(new StringSource("\"one quote"));
    }

    @Test
    public void testLeading() {
        testFP(new StringSource("one quote\""));
    }
}
