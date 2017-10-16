package net.saka1.joptparse;

import net.saka1.joptparse.annotation.Operands;
import net.saka1.joptparse.annotation.Option;
import net.saka1.joptparse.annotation.ParseSucceeded;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class JoptParseTest {

    @SuppressWarnings("WeakerAccess")
    static class Args {
        @Option(name = "x")
        public boolean optionX;
        @Option(name = "y")
        public String optionY;
        @Operands
        public List<String> operands;
        @ParseSucceeded
        public boolean isParseSucceeded;
    }

    @Test
    public void option() {
        Args args = JoptParse.parse(Args.class, new String[]{"-x", "-y", "foo"});
        assertThat(args.optionX, is(true));
        assertThat(args.optionY, is("foo"));
        assertThat(args.isParseSucceeded, is(true));
    }

    @Test
    public void operands() {
        Args args = JoptParse.parse(Args.class, new String[]{"a", "b", "c"});
        assertThat(args, notNullValue());
        assertThat(args.operands, is(Arrays.asList("a", "b", "c")));
    }


    @SuppressWarnings("WeakerAccess")
    public static class LongArgs {
        @Option(name = "x", longName = "xxx")
        public Boolean optionX;
        @Option(name = "y", longName = "yyy")
        public String optionY;
    }

    @Test
    public void longOption() {
        LongArgs args = JoptParse.parse(LongArgs.class, new String[]{"--xxx", "--yyy=foo"});
        assertThat(args.optionX, is(true));
        assertThat(args.optionY, is("foo"));
    }
}
