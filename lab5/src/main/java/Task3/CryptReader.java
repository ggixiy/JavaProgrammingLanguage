package Task3;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

public class CryptReader extends FilterReader {
    private final char key;

    public CryptReader(Reader in, char key) {
        super(in);
        this.key = key;
    }

    @Override
    public int read() throws IOException {
        int c = in.read();
        return (c == -1) ? -1 : (c - key);
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int n = in.read(cbuf, off, len);
        if (n == -1) return -1;
        for (int i = off; i < off + n; i++) {
            cbuf[i] = (char) (cbuf[i] - key);
        }
        return n;
    }
}