package blue.lhf.tuonentytti.reader;

import blue.lhf.tuonentytti.model.JsonObject;
import blue.lhf.tuonentytti.parsing.*;
import org.junit.jupiter.api.Test;

import java.io.*;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

public class MultiplexerTest {
    @Test
    public void testMultiplexer() throws IOException {
        final InputStream stream = new ByteArrayInputStream("Hello, world!".getBytes(UTF_8));
        final InputStreamSource source = new InputStreamSource(stream);

        assertEquals('H', source.peek(), "Multiplexer invalid base peek");

        final Source branch = source.branch();
        assertEquals('H', branch.peek(), "Multiplexer invalid branch peek");
        assertEquals('H', branch.read(), "Multiplexer invalid branch read");
        assertEquals('e', branch.read(), "Multiplexer invalid branch read");
        assertEquals('l', branch.read(), "Multiplexer invalid branch read");
        branch.skip(32);
        assertEquals(-1, branch.read(), "Multiplexer invalid branch read");
        assertEquals(-1, branch.read(), "Multiplexer invalid branch read");

        assertEquals('H', source.read(), "Multiplexer invalid base read");
        source.read();
        assertEquals('l', (long) source.timeMachine.getFirst(), "Multiplexer time machine offset");
        for (int i = 0; i < 11; ++i) source.read();
        assertEquals(-1, source.read(), "Multiplexer invalid base read");
        assertEquals(-1, source.peek(), "Multiplexer invalid base peek");
    }

    @Test
    public void testSampleJson() throws IOException {
        try (final InputStream resource = MultiplexerTest.class.getResourceAsStream("/sample.json")) {
            if (resource == null) throw new IllegalStateException("Missing critical test resource sample.json");
            assertDoesNotThrow(() -> JsonParser.parseAs(JsonObject.class,
                new InputStreamSource(resource)), "Sample JSON failed to parse");
        }
    }
}