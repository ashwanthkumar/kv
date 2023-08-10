package in.aklabs.kv.utils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Bytes {
    public static ByteBuffer ofString(String input) {
        var bytesToStore = input.getBytes(StandardCharsets.UTF_8);
        var size = Bytes.ofInt(bytesToStore.length);
        var bytes = ByteBuffer.wrap(bytesToStore);
        return ByteBuffer.allocate(Integer.BYTES + bytesToStore.length).put(size.array()).put(bytes.array()).rewind();
    }

    public static ByteBuffer ofInt(Integer input) {
        var bytes = ByteBuffer.allocate(Integer.BYTES);
        bytes.putInt(input);
        return bytes.rewind();
    }
    
    public static String toString(ByteBuffer input) {
        var lengthOfStringInBytes = input.getInt();
        var strInBytes = new byte[lengthOfStringInBytes];
        input.get(strInBytes);
        return new String(strInBytes, StandardCharsets.UTF_8);
    }

    public static ByteBuffer readFile(File file) throws IOException {
        DataInputStream dataInputStream = null;
        try {
            // FIXME: this is broken for files larger than 4GiB.
            int byteCount = (int) file.length();
            FileInputStream fileInputStream = new FileInputStream(file);
            dataInputStream = new DataInputStream(fileInputStream);
            final byte[] bytes = new byte[byteCount];
            dataInputStream.readFully(bytes);
            return ByteBuffer.wrap(bytes);
        } finally {
            if (dataInputStream != null) {
                dataInputStream.close();
            }
        }
    }

}
