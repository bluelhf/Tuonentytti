package blue.lhf.tuonentytti.model;

import blue.lhf.tuonentytti.parsing.*;
import blue.lhf.tuonentytti.reader.*;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringRoundtripTest {
    @Test
    public void testRoundtrip() throws IOException, JsonParseException {
        try (final InputStream resource = StringRoundtripTest.class.getResourceAsStream("/sample.json")) {
            if (resource == null) throw new IllegalStateException("Missing critical test resource sample.json");
            final JsonObject object = JsonParser.parseAs(JsonObject.class, new InputStreamSource(resource));
            final JsonObject reconstructed = JsonParser.parseAs(JsonObject.class, new StringSource(object.toString()));

            assertEquals(object, reconstructed, "Incomplete reconstruction");
        }
    }
}
