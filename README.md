# Tuonentytti

To-specification JSON Parser for Java.

### Goals

- Parse JSON with relative efficiency in a single pass.
- Practice parsing.

### Non-goals

- (Un)marshalling

## Usage Example

`$ jshell --class-path path/to/Tuonentytti.jar`
```jshelllanguage
void main() throws Exception {
    try (final var stream = new URL("http://headers.jsontest.com/").openStream()) {
        final Source source = new InputStreamSource(stream);
    
        // Calling toString() stringifies into neat, indented JSON.
        err.println(JsonParser.parseAs(JsonObject.class, source));
    }
    
    { // Reads raw values too.
        final Source source = new StringSource("""
            ["CAFEBABE", "5318008", 420.69, true]""");
        err.println(JsonParser.parseAs(JsonArray.class, source));
    }
    
    // JsonValue<?> represents any JSON value.
    final Path mysteryPath = Path.of("src/test/resources/sample.json");
    try (final var mystery = Files.newInputStream(mysteryPath)) {
        final Source source = new InputStreamSource(mystery);
        err.println(JsonParser.parse(source));
    }
}

import blue.lhf.tuonentytti.model.*;
import blue.lhf.tuonentytti.parsing.*;
import blue.lhf.tuonentytti.reader.*;

import java.nio.file.*;
import java.net.*;

import static java.lang.System.err;
main();
```

## To-do
- Documentation
