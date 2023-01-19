package blue.lhf.tuonentytti.parsing;

import blue.lhf.tuonentytti.model.*;
import blue.lhf.tuonentytti.reader.StringSource;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PrimitiveTest {
    @Test
    public void testTrue() throws Exception {
        assertEquals(new JsonBoolean(true), JsonParser.readBoolean(new StringSource("true")), "False negative primitive");
    }

    @Test
    public void testFalse() throws Exception {
        assertEquals(new JsonBoolean(false), JsonParser.readBoolean(new StringSource("false")), "False negative primitive");
    }

    @Test
    public void testNull() throws Exception {
        assertEquals(new JsonNull<>(), JsonParser.readNull(new StringSource("null")), "False negative primitive");
    }

    @Test
    public void testNonsense() {
        assertThrows(JsonParseException.class, () -> JsonParser.readBoolean(new StringSource("boobs")), "False positive primitive");
    }

    @Test
    public void testNothing() {
        assertThrows(JsonParseException.class, () -> JsonParser.readBoolean(new StringSource("")), "False positive primitive");
    }
}