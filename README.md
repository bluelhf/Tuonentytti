# Tuonentytti

To-specification JSON Parser for Java.

### Goals

- Parse JSON with relative efficiency in a single pass.
- Practice parsing.

### Non-goals

- (Un)marshalling

## Usage Examples
```java
final InputStream stream = new URL("http://headers.jsontest.com/").openStream();
final Source source = new InputStreamSource(stream);
final JsonObject object = JsonParser.parseAs(JsonObject.class, source);
```
```java
final Source source = new StringSource("""
     ["CAFEBABE", "5318008", 420.69, true]""");
final JsonArray array = JsonParser.parseAs(JsonArray.class, source);
```
```java
final InputStream mysteryStream = /* ... */;
final Source source = new InputStreamSource(mysteryStream);
final JsonValue<?> mysteryValue = JsonParser.parse(mysteryStream);
```

## To-do
- Documentation
