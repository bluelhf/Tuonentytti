package blue.lhf.tuonentytti.source;

import java.io.*;

public abstract class RASource implements Source {
    private long pointer;

    @Override
    public final Source copy() {
        final RASource source = new RASource() {
            @Override
            protected int readAt(long position) throws IOException {
                return RASource.this.readAt(position);
            }
        };

        source.pointer = pointer;
        return source;
    }

    @Override
    public void move(long pointer) throws Exception {
        this.pointer = pointer;
    }

    @Override
    public long pointer() throws Exception {
        return this.pointer;
    }

    @Override
    public boolean startsWith(String subtext) throws Exception {
        for (int i = 0; i < subtext.length(); ++i) {
            if (readAt(pointer + i) != subtext.charAt(i)) return false;
        }
        return true;
    }

    @Override
    public int read() throws Exception {
        return readAt(pointer++);
    }

    protected abstract int readAt(final long position) throws IOException;
}
