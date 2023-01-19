package blue.lhf.tuonentytti.parsing;

import blue.lhf.tuonentytti.model.*;
import blue.lhf.tuonentytti.reader.StringSource;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.util.Collections.*;
import static org.junit.jupiter.api.Assertions.*;

public class ObjectTest {
    @Test
    public void testFilled() throws IOException, JsonParseException {
        final JsonObject expected = new JsonObject(singleton(
            new JsonMember(new JsonString("foo"), new JsonBoolean(true))
        ));
        assertEquals(expected, JsonParser.readObject(new StringSource("{\"foo\": true}")),
            "Filled object mismatch");
    }

    @Test
    public void testEmpty() throws IOException, JsonParseException {
        final StringSource source = new StringSource("{\n\r\t }");
        assertEquals(new JsonObject(emptySet()), JsonParser.readObject(source),
            "Empty object mismatch");
    }

    @Test
    public void testTrailing() {
        final StringSource source = new StringSource("{\"opens\": true");
        assertThrows(JsonParseException.class, () -> JsonParser.readObject(source),
            "Trailing object parses");
    }
}
