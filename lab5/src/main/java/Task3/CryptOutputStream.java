package Task3;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CryptOutputStream extends FilterOutputStream {
    private final int key;

    public CryptOutputStream(OutputStream out, char key) {
        super(out);
        this.key = key;
    }

    @Override
    public void write(int b) throws IOException {
        out.write((b + key) & 0xFF);   // & 0xFF залишає тільки молодші 8 біт числа, щоб отримати значення в діапазоні 0 - 255
    }
}