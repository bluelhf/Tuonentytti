package blue.lhf.tuonentytti.parsing;

import blue.lhf.tuonentytti.model.*;
import blue.lhf.tuonentytti.reader.StringSource;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ArrayTest {
    @Test
    public void testTypeVariance() throws Exception {
        assertEquals(new JsonArray(new JsonString("tits"), new JsonNumber(69.0)),
            JsonParser.readArray(new StringSource("[\"tits\", 69]")), "False negative for array");
    }

    @Test
    public void testSimpleArray() throws Exception {
        assertEquals(new JsonArray(new JsonString("boobs"), new JsonString("tiddies")),
            JsonParser.readArray(new StringSource("[\"boobs\", \"tiddies\"]")), "False negative for array");
    }

    @Test
    public void testWhitespaces() throws Exception {
        assertEquals(new JsonArray(new JsonString("69"), new JsonNumber(420.0), new JsonNumber(717.5)), JsonParser.readArray(new StringSource("""
                [
                    "69",
                    42.0E1,
                    7175E-1
                ]""")), "False negative for array");
    }

}
