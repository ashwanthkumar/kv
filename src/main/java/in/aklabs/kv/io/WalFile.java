package in.aklabs.kv.io;

import in.aklabs.kv.core.Value;
import in.aklabs.kv.models.WalAction;
import in.aklabs.kv.models.WalRecord;
import in.aklabs.kv.utils.Bytes;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.ConstructorUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Manages I/O for WalRecords
 */
@Slf4j
public class WalFile implements AutoCloseable {
    private final DataOutputStream walWriter;
    @Getter
    private final File underlyingFile;
    @Getter
    private boolean isClosed = false;

    public WalFile(String dataPath) throws FileNotFoundException {
        var id = UUID.randomUUID().toString();
        var walFilePath = Paths.get(dataPath, id + ".wal");
        File walFile = walFilePath.toFile();
        // just to make sure the data path exists
        walFile.getParentFile().mkdirs();
        this.underlyingFile = walFile;
        this.walWriter = new DataOutputStream(new FileOutputStream(walFile));
    }

    public void record(WalRecord record) throws IOException {
        var bytes = writeRecord(record);
        this.walWriter.write(bytes.array());
        // this is important to make sure all of our content is fsync'd to disk
        this.walWriter.flush();
    }

    @Override
    public synchronized void close() throws IOException {
        this.isClosed = true;
        this.walWriter.flush();
        this.walWriter.close();
    }

    /**
     * Read the entire walFile from disk to List&lt;WalRecord&gt; instance
     *
     * @param walFileOnDisk
     * @return
     * @throws IOException
     */
    static List<WalRecord> readFile(File walFileOnDisk) throws IOException {
        ByteBuffer bytes = Bytes.readFile(walFileOnDisk);
        List<WalRecord> records = new LinkedList<>();
        while (bytes.position() != bytes.limit()) {
            try {
                var record = readRecord(bytes);
                records.add(record);
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
            }
        }
        return records;
    }

    static WalRecord readRecord(ByteBuffer buffer) throws Exception {
        var action = WalAction.values()[buffer.getInt()];
        var key = Bytes.toString(buffer);
        if (action == WalAction.ADD) {
            var valueType = Bytes.toString(buffer);
            var valueClass = Class.forName(valueType).asSubclass(Value.class);
            var value = ConstructorUtils.invokeConstructor(valueClass);
            return new WalRecord(action, key, value.read(buffer));
        } else {
            return new WalRecord(action, key, null);
        }
    }

    /**
     * Write each WAL Entry to disk
     *
     * @param record
     * @return
     */
    static ByteBuffer writeRecord(WalRecord record) {
        var action = Bytes.ofInt(record.action().ordinal()).rewind();
        var key = Bytes.ofString(record.key()).rewind();
        if (record.action() == WalAction.ADD) {
            var valueType = Bytes.ofString(record.value().getClass().getCanonicalName());
            var value = record.value().write();
            return ByteBuffer.allocate(action.capacity() + key.capacity() + valueType.capacity() + value.capacity())
                    .put(action.array())
                    .put(key.array())
                    .put(valueType.array())
                    .put(value.array());
        } else {
            return ByteBuffer.allocate(action.capacity() + key.capacity())
                    .put(action)
                    .put(key);
        }
    }
}
