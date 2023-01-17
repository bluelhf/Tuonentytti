package blue.lhf.tuonentytti.reader;

public class StringSource implements Source {
    private final String text;
    private int position = 0;

    public StringSource(final String text) {
        this.text = text;
    }

    private StringSource(final String text, final int position) {
        this.text = text;
        this.position = position;
    }

    @Override
    public StringSource branch() {
        return new StringSource(text, position);
    }

    @Override
    public int read() {
        return position < text.length() ? text.charAt(position++) : -1;
    }

    @Override
    public long position() {
        return position;
    }

    @Override
    public int peek() {
        return position < text.length() ? text.charAt(position) : -1;
    }

    @Override
    public void skip(int amount) {
        position += amount;
    }
}
