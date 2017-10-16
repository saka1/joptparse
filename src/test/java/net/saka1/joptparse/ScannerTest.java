package net.saka1.joptparse;

import net.saka1.joptparse.parser.Scanner;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("ConstantConditions") // To suppress warning about Optional#get()
public class ScannerTest {

    private Scanner scanner;

    @Before
    public void setup() {
        scanner = new Scanner("abc");
    }

    @Test
    public void scanMatch() {
        assertThat(scanner.getCurrentIndex(), is(0));
        Optional<String> result = scanner.scan("ab");
        assertThat(result.get(), is("ab"));
        assertThat(scanner.getCurrentIndex(), is(2));

        Optional<String> result2 = scanner.scan("c");
        assertThat(result2.get(), is("c"));
        assertThat(scanner.getCurrentIndex(), is(3));
    }

    @Test
    public void scanNotMatch() {
        Optional<String> result = scanner.scan("b");
        assertThat(result, is(Optional.empty()));
        assertThat(scanner.getCurrentIndex(), is(0));
    }

    @Test
    public void isEof() {
        assertThat(scanner.isEos(), is(false));
        scanner.scan("abc");
        assertThat(scanner.isEos(), is(true));
    }


    @Test
    public void scanAtEnd() {
        Scanner scanner = new Scanner("a");
        scanner.scan("a");
        Optional<String> result = scanner.scan("a");
        assertThat(result, is(Optional.empty()));
        assertThat(scanner.isEos(), is(true));
    }

    @Test
    public void scanMatchAsPattern() {
        Optional<String> result = scanner.scanRegexp("a.?");
        assertThat(result.get(), is("ab"));
    }

    @Test
    public void scanNotMatchAsPattern() {
        Optional<String> result = scanner.scanRegexp("c");
        assertThat(result, is(Optional.empty()));
    }

    @Test
    public void scanRegexpStep() {
        scanner.scanRegexp("a");
        Optional<String> result = scanner.scanRegexp("b");
        assertThat(result.get(), is("b"));
    }
}
