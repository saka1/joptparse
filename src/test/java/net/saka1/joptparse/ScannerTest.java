package net.saka1.joptparse;

import net.saka1.joptparse.parser.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("ConstantConditions") // To suppress warning about Optional#get()
class ScannerTest {

    private Scanner scanner;

    @BeforeEach
    void setup() {
        scanner = new Scanner("abc");
    }

    @Test
    void scanMatch() {
        assertThat(scanner.getCurrentIndex(), is(0));
        Optional<String> result = scanner.scan("ab");
        assertThat(result.get(), is("ab"));
        assertThat(scanner.getCurrentIndex(), is(2));

        Optional<String> result2 = scanner.scan("c");
        assertThat(result2.get(), is("c"));
        assertThat(scanner.getCurrentIndex(), is(3));
    }

    @Test
    void scanNotMatch() {
        Optional<String> result = scanner.scan("b");
        assertThat(result, is(Optional.empty()));
        assertThat(scanner.getCurrentIndex(), is(0));
    }

    @Test
    void isEof() {
        assertThat(scanner.isEos(), is(false));
        scanner.scan("abc");
        assertThat(scanner.isEos(), is(true));
    }


    @Test
    void scanAtEnd() {
        Scanner scanner = new Scanner("a");
        scanner.scan("a");
        Optional<String> result = scanner.scan("a");
        assertThat(result, is(Optional.empty()));
        assertThat(scanner.isEos(), is(true));
    }

    @Test
    void scanMatchAsPattern() {
        Optional<String> result = scanner.scanRegexp("a.?");
        assertThat(result.get(), is("ab"));
    }

    @Test
    void scanNotMatchAsPattern() {
        Optional<String> result = scanner.scanRegexp("c");
        assertThat(result, is(Optional.empty()));
    }

    @Test
    void scanRegexpStep() {
        scanner.scanRegexp("a");
        Optional<String> result = scanner.scanRegexp("b");
        assertThat(result.get(), is("b"));
    }
}
