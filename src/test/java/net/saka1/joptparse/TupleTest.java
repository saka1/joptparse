package net.saka1.joptparse;

import net.saka1.joptparse.util.Tuple;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TupleTest {
    @Test
    public void test() {
        Tuple<String, String> tuple = Tuple.create("a", "b");
        assertThat(tuple.first, is("a"));
        assertThat(tuple.second, is("b"));
        Tuple<String, String> other = Tuple.create("a", "b");
        assertThat(tuple, is(other));
        assertThat(tuple.hashCode(), is(other.hashCode()));
    }
}
