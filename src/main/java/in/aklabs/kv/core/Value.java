package in.aklabs.kv.core;

import java.nio.ByteBuffer;

public interface Value {
    /**
     * Setter for this value so we can have a default constructor that is needed for WalRecord to Reflect on.
     *
     * @param value
     */
    void set(Object value);

    /**
     * Serialize the value to a ByteBuffer
     *
     * @return Buffer containing the value that can be persisted to disk
     */
    ByteBuffer write();

    /**
     * Deserialize the value from a ByteBuffer
     *
     * @param buffer Buffer containing the serialized data that was written to disk using the {@see write} method
     * @return Instance of this value
     */
    Value read(ByteBuffer buffer);
}
