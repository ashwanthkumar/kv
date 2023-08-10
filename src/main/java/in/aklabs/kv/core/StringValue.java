package in.aklabs.kv.core;

import in.aklabs.kv.utils.Bytes;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.nio.ByteBuffer;

public class StringValue implements Value {

    public static StringValue stringValue(String input) {
        var value = new StringValue();
        value.set(input);
        return value;
    }
    private String underlying;
    @Override
    public void set(Object value) {
        this.underlying = (String) value;
    }

    @Override
    public ByteBuffer write() {
        return Bytes.ofString(this.underlying);
    }

    @Override
    public Value read(ByteBuffer buffer) {
        StringValue value = new StringValue();
        value.set(Bytes.toString(buffer));
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        StringValue value = (StringValue) o;

        return new EqualsBuilder().append(underlying, value.underlying).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(underlying).toHashCode();
    }
}
