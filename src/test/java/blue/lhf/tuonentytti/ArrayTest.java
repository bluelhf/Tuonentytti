package blue.lhf.tuonentytti;

import blue.lhf.tuonentytti.model.*;
import blue.lhf.tuonentytti.source.*;
import org.junit.*;

import static org.junit.Assert.*;

public class ArrayTest {
    @Test
    public void testTypeVariance() throws Exception {
        assertEquals("False negative for array",
            JsonArray.from("tits", 69.0),
            Json.readArray(new StringSource("[\"tits\", 69]")));
    }

    @Test
    public void testSimpleArray() throws Exception {
        assertEquals("False negative for array", JsonArray.from("boobs", "tiddies"), Json.readArray(new StringSource("[\"boobs\", \"tiddies\"]")));
    }

    @Test
    public void testWhitespaced() throws Exception {
        assertEquals("False negative for array",
            JsonArray.from("69", 420.0, 717.5), Json.readArray(new StringSource("""
                [
                    "69",
                    42.0E1,
                    7175E-1
                ]""")));
    }

}
