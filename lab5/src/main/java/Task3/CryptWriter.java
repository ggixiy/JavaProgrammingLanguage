package Task3;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

public class CryptWriter extends FilterWriter {
    private final char key;

    public CryptWriter(Writer out, char key) {
        super(out);
        this.key = key;
    }

    @Override
    public void write(int c) throws IOException {
        out.write(c + key);
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        for (int i = off; i < off + len; i++) {
            write(cbuf[i]);
        }
    }
}