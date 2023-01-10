package blue.lhf.tuonentytti.source;

public class StringSource extends RASource {

    private final String input;

    public StringSource(final String input) {
        this.input = input;
    }

    @Override
    protected int readAt(long position) {
        return input.length() <= position ? -1 : input.charAt((int) position);
    }

}
