package blue.lhf.tuonentytti.model;

import java.util.*;

import static java.util.stream.Collectors.joining;

@SuppressWarnings("unused") // API
public class JsonObject extends ArrayList<JsonMember> implements JsonValue<List<JsonMember>> {
    public JsonObject() {

    }

    public JsonObject(final Collection<JsonMember> members) {
        this.addAll(members);
    }

    public boolean containsKey(final String key) {
        for (final JsonMember member : this) {
            if (member.key().equals(key)) return true;
        }

        return false;
    }

    public boolean containsValue(final JsonValue<?> value) {
        for (final JsonMember member : this) {
            if (member.value().equals(value)) return true;
        }

        return false;
    }

    public JsonMember getFirst(final String key) {
        for (final JsonMember member : this) {
            if (member.key().equals(key)) return member;
        }

        return null;
    }

    public List<JsonValue<?>> getValues(final String key) {
        final List<JsonValue<?>> values = new ArrayList<>();
        for (final JsonMember member : this) {
            if (member.key().equals(key)) values.add(member.value());
        }

        return values;
    }

    public final List<JsonValue<?>> remove(String key) {
        final List<JsonValue<?>> removed = new ArrayList<>();
        final ListIterator<JsonMember> memberIter = listIterator();
        while (memberIter.hasNext()) {
            final JsonMember next = memberIter.next();
            if (next.key().equals(key)) {
                removed.add(next.value());
                memberIter.remove();
            }
        }

        return removed;
    }

    public Set<JsonString> keySet() {
        final Set<JsonString> keySet = new HashSet<>();
        for (final JsonMember member : this) {
            keySet.add(member.key());
        }

        return keySet;
    }

    public Collection<JsonValue<?>> values() {
        final List<JsonValue<?>> values = new ArrayList<>();
        for (final JsonMember member : this) {
            values.add(member.value());
        }

        return values;
    }

    @Override
    public List<JsonMember> get() {
        return this;
    }

    @Override
    public String toString() {
        return "{\n" + stream().map(JsonMember::toString).collect(joining(",\n")).indent(2) + "}";
    }
}
