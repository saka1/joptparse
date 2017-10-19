package net.saka1.joptparse;

import net.saka1.joptparse.util.Util;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UtilTest {

    @Test
    void head() {
        List<String> list = Arrays.asList("a", "b", "c");
        String result = Util.head(list);
        assertThat(result, is("a"));
    }


    @Test
    void emptyListHead() {
        assertThrows(NoSuchElementException.class, () -> {
            List<String> list = Collections.emptyList();
            Util.head(list);
        });
    }

    @Test
    void tail() {
        List<String> list = Arrays.asList("a", "b", "c");
        List<String> result = Util.tail(list);
        assertThat(result, is(Arrays.asList("b", "c")));
    }

    @Test
    void emptyListTail() {
        List<String> list = Collections.emptyList();
        List<String> result = Util.tail(list);
        assertThat(result, is(Collections.emptyList()));
    }

    @Test
    void splitCodePointList() {
        List<String> result = Util.splitCodePointList("aあc🍣");
        List<String> expected = Arrays.asList("a", "あ", "c", "🍣");
        assertThat(result, is(expected));
    }

    @Test
    void concatList() {
        String result = Util.concat(Arrays.asList("a", "b", "c"));
        assertThat(result, is("abc"));
    }

    @Test
    void concatEmptyList() {
        String result = Util.concat(Collections.emptyList());
        assertThat(result, is(""));
    }

    @Test
    void copyList() {
        List<String> list = Arrays.asList("a", "b", "c");
        List<String> result = Util.copyList(list);
        assertThat(list, is(result));
        assertThat(list, not(sameInstance(result)));
    }
}
