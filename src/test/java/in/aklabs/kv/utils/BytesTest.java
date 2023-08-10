package in.aklabs.kv.utils;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BytesTest {
    @Test
    public void testBytesOfString() {
        String expected = "a";
        var r = Bytes.ofString(expected);
        var actual = Bytes.toString(r);
        assertThat(actual, is(expected));
    }

    @Test
    public void testBytesOfInt() {
        var expected = 1;
        var r = Bytes.ofInt(expected);
        assertThat(r.getInt(), is(expected));
    }

}