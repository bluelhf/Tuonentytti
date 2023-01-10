package blue.lhf.tuonentytti;

import blue.lhf.tuonentytti.source.*;
import org.junit.*;

import static blue.lhf.tuonentytti.model.JsonValue.coerce;
import static org.junit.Assert.*;

public class PrimitiveTest {
    @Test
    public void testTrue() throws Exception {
        assertEquals("False negative primitive", coerce(true), Json.readBoolean(new StringSource("true")));
    }

    @Test
    public void testFalse() throws Exception {
        assertEquals("False negative primitive", coerce(false), Json.readBoolean(new StringSource("false")));
    }

    @Test
    public void testNull() throws Exception {
        assertEquals("False negative primitive", coerce(null), Json.readNull(new StringSource("null")));
    }

    @Test
    public void testNonsense() throws Exception {
        assertNull("False positive primitive", Json.readBoolean(new StringSource("boobs")));
    }

    @Test
    public void testNothing() throws Exception {
        assertNull("False positive primitive", Json.readBoolean(new StringSource("")));
    }
}
