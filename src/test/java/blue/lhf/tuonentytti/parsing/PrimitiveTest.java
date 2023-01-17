package blue.lhf.tuonentytti.parsing;

import blue.lhf.tuonentytti.model.*;
import blue.lhf.tuonentytti.reader.StringSource;
import org.junit.Test;

import static org.junit.Assert.*;

public class PrimitiveTest {
    @Test
    public void testTrue() throws Exception {
        assertEquals("False negative primitive", new JsonBoolean(true), JsonParser.readBoolean(new StringSource("true")));
    }

    @Test
    public void testFalse() throws Exception {
        assertEquals("False negative primitive", new JsonBoolean(false), JsonParser.readBoolean(new StringSource("false")));
    }

    @Test
    public void testNull() throws Exception {
        assertEquals("False negative primitive", new JsonNull<>(), JsonParser.readNull(new StringSource("null")));
    }

    @Test
    public void testNonsense() {
        assertThrows("False positive primitive", JsonParseException.class, () -> JsonParser.readBoolean(new StringSource("boobs")));
    }

    @Test
    public void testNothing() {
        assertThrows("False positive primitive", JsonParseException.class, () -> JsonParser.readBoolean(new StringSource("")));
    }
}
