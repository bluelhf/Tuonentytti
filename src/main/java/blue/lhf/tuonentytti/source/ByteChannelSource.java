package blue.lhf.tuonentytti.source;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class ByteChannelSource extends RASource {
    private final SeekableByteChannel channel;
    final ByteBuffer buffer = ByteBuffer.allocate(1);

    public ByteChannelSource(final SeekableByteChannel channel) {
        this.channel = channel;
    }

    protected int readAt(long position) throws IOException {
        channel.position(position);
        final int read = channel.read(buffer);
        if (read == -1) return -1;
        final int result = buffer.get(0);
        buffer.flip();
        return result;
    }

    @Override
    public void move(long pointer) throws Exception {
        channel.position(pointer);
    }

    @Override
    public long pointer() throws Exception {
        return channel.position();
    }

    @Override
    public boolean startsWith(String subtext) throws Exception {
        final long pos = pointer();
        for (int i = 0; i < subtext.length(); ++i) {
            if (readAt(pos + i) != subtext.charAt(i)) return false;
        }
        return true;
    }

    @Override
    public int read() throws Exception {
        final int read = readAt(pointer());
        move(pointer() + 1);
        return read;
    }
}
