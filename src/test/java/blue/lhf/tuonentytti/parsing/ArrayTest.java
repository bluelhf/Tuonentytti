package blue.lhf.tuonentytti.parsing;

import blue.lhf.tuonentytti.model.*;
import blue.lhf.tuonentytti.reader.StringSource;
import org.junit.*;

import static org.junit.Assert.assertEquals;

public class ArrayTest {
    @Test
    public void testTypeVariance() throws Exception {
        Assert.assertEquals("False negative for array",
                            new JsonArray(new JsonString("tits"), new JsonNumber(69.0)),
                            JsonParser.readArray(new StringSource("[\"tits\", 69]")));
    }

    @Test
    public void testSimpleArray() throws Exception {
        assertEquals("False negative for array",
                     new JsonArray(new JsonString("boobs"), new JsonString("tiddies")),
                     JsonParser.readArray(new StringSource("[\"boobs\", \"tiddies\"]")));
    }

    @Test
    public void testWhitespaces() throws Exception {
        assertEquals("False negative for array",
            new JsonArray(new JsonString("69"), new JsonNumber(420.0), new JsonNumber(717.5)), JsonParser.readArray(new StringSource("""
                [
                    "69",
                    42.0E1,
                    7175E-1
                ]""")));
    }

}
