package blue.lhf.tuonentytti.reader;

import java.io.*;
import java.util.*;

public class InputStreamSource implements Source {
    private final InputStream backingStream;
    private long position = 0;

    // package-private for unit tests
    final LinkedList<Integer> timeMachine = new LinkedList<>();

    public InputStreamSource(final InputStream backingStream) throws IOException {
        this.backingStream = backingStream;
        ensureTimeMachine();
    }

    private void ensureTimeMachine() throws IOException {
        if (timeMachine.isEmpty()) timeMachine.add(backingStream.read());
    }

    private int peekAt(final long position) throws IOException {
        if (position < this.position) throw new IllegalArgumentException("Cannot read backwards");
        ensureTimeMachine();

        //noinspection DataFlowIssue ensureTimeMachine() forces non-null peekLast
        while (position >= this.position + this.timeMachine.size() && timeMachine.peekLast() != -1) {
            timeMachine.add(backingStream.read());
        }

        final int index = (int) (position - this.position);
        if (index >= timeMachine.size()) return -1;
        return timeMachine.get(index);
    }

    @Override
    public Source branch() {
        return multiplexBranch();
    }

    private MultiplexingSource multiplexBranch() {
        final MultiplexingSource source = new MultiplexingSource();
        source.position = this.position;
        return source;
    }

    @Override
    public int read() throws IOException {
        final int read = timeMachine.pop();
        ensureTimeMachine();
        ++position;
        return read;
    }

    @Override
    public long position() {
        return position;
    }

    @Override
    public synchronized int peek() throws IOException {
        ensureTimeMachine();

        //noinspection DataFlowIssue ensureTimeMachine() forces non-null peek
        return timeMachine.peek();
    }

    @Override
    public synchronized void skip(int amount) throws IOException {
        for (int i = 0; i < amount; ++i) read();
    }

    private class MultiplexingSource implements Source {
        private long position = 0;

        @Override
        public Source branch() {
            final MultiplexingSource source = InputStreamSource.this.multiplexBranch();
            source.position = this.position;
            return source;
        }

        @Override
        public int read() throws IOException {
            return InputStreamSource.this.peekAt(this.position++);
        }

        @Override
        public long position() {
            return position;
        }

        @Override
        public int peek() throws IOException {
            return InputStreamSource.this.peekAt(this.position);
        }

        @Override
        public void skip(int amount) throws IOException {
            InputStreamSource.this.peekAt(this.position += amount);
        }
    }
}
