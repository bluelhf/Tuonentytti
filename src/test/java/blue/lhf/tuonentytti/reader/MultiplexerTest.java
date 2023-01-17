package blue.lhf.tuonentytti.reader;

import blue.lhf.tuonentytti.model.JsonObject;
import blue.lhf.tuonentytti.parsing.*;
import org.junit.Test;

import java.io.*;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;

public class MultiplexerTest {
    @Test
    public void testMultiplexer() throws IOException {
        final InputStream stream = new ByteArrayInputStream("Hello, world!".getBytes(UTF_8));
        final InputStreamSource source = new InputStreamSource(stream);

        assertEquals("Multiplexer invalid base peek", 'H', source.peek());

        final Source branch = source.branch();
        assertEquals("Multiplexer invalid branch peek", 'H', branch.peek());
        assertEquals("Multiplexer invalid branch read", 'H', branch.read());
        assertEquals("Multiplexer invalid branch read", 'e', branch.read());
        assertEquals("Multiplexer invalid branch read", 'l', branch.read());
        branch.skip(32);
        assertEquals("Multiplexer invalid branch read", -1, branch.read());
        assertEquals("Multiplexer invalid branch read", -1, branch.read());

        assertEquals("Multiplexer invalid base read", 'H', source.read());
        source.read();
        assertEquals("Multiplexer time machine offset", 'l', (long) source.timeMachine.getFirst());
        for (int i = 0; i < 11; ++i) source.read();
        assertEquals("Multiplexer invalid base read", -1, source.read());
        assertEquals("Multiplexer invalid base peek", -1, source.peek());
    }

    @Test
    public void testSampleJson() throws IOException, JsonParseException {
        try (final InputStream resource = MultiplexerTest.class.getResourceAsStream("/sample.json")) {
            if (resource == null) throw new IllegalStateException("Missing critical test resource sample.json");
            final JsonObject object = JsonParser.parseAs(JsonObject.class, new InputStreamSource(resource));
            System.err.println(object);
        }
    }
}
