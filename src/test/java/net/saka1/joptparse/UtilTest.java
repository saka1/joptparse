package net.saka1.joptparse;

import net.saka1.joptparse.utils.Util;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class UtilTest {

    @Test
    public void head() {
        List<String> list = Arrays.asList("a", "b", "c");
        String result = Util.head(list);
        assertThat(result, is("a"));
    }

    @Test(expected = NoSuchElementException.class)
    public void emptyListHead() {
        List<String> list = Collections.emptyList();
        Util.head(list);
    }

    @Test
    public void tail() {
        List<String> list = Arrays.asList("a", "b", "c");
        List<String> result = Util.tail(list);
        assertThat(result, is(Arrays.asList("b", "c")));
    }

    @Test
    public void emptyListTail() {
        List<String> list = Collections.emptyList();
        List<String> result = Util.tail(list);
        assertThat(result, is(Collections.emptyList()));
    }

    @Test
    public void splitCodePointList() {
        List<String> result = Util.splitCodePointList("aあc🍣");
        List<String> expected = Arrays.asList("a", "あ", "c", "🍣");
        assertThat(result, is(expected));
    }

    @Test
    public void concatList() {
        String result = Util.concat(Arrays.asList("a", "b", "c"));
        assertThat(result, is("abc"));
    }

    @Test
    public void concatEmptyList() {
        String result = Util.concat(Collections.emptyList());
        assertThat(result, is(""));
    }

    @Test
    public void copyList() {
        List<String> list = Arrays.asList("a", "b", "c");
        List<String> result = Util.copyList(list);
        assertThat(list, is(result));
        assertThat(list, not(sameInstance(result)));
    }
}
