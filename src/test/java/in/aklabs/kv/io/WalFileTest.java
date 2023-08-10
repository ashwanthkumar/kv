package in.aklabs.kv.io;

import in.aklabs.kv.core.StringValue;
import in.aklabs.kv.models.WalAction;
import in.aklabs.kv.models.WalRecord;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class WalFileTest {
    @Test
    public void testReadWriteRecord() throws Exception {
        WalRecord record = new WalRecord(WalAction.ADD, "k", StringValue.stringValue("v"));
        ByteBuffer bytes = WalFile.writeRecord(record);
        bytes.rewind(); // rewind the buffer so we can read it from first
        var actualRecord = WalFile.readRecord(bytes);
        assertThat(actualRecord, is(record));
    }

}