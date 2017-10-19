package net.saka1.joptparse;

import net.saka1.joptparse.annotation.FailedReason;
import net.saka1.joptparse.annotation.Operands;
import net.saka1.joptparse.annotation.Option;
import net.saka1.joptparse.annotation.ParseSucceeded;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

class JOptParseTest {

    @SuppressWarnings("WeakerAccess")
    static class Args {
        @Option(name = "x")
        public boolean optionX;
        @Option(name = "y")
        public String optionY;
        @Option(name = "z")
        public String optionZ;
        @Operands
        public List<String> operands;
        @ParseSucceeded
        public boolean isParseSucceeded;
        @FailedReason
        public List<String> reason;
    }

    @Test
    void option() {
        Args args = JOptParse.parse(Args.class, new String[]{"-x", "-y", "foo"});
        assertThat(args.optionX, is(true));
        assertThat(args.optionY, is("foo"));
        assertThat(args.optionZ, nullValue());
        assertThat(args.isParseSucceeded, is(true));
        assertThat(args.reason, notNullValue());
    }

    @Test
    void operands() {
        Args args = JOptParse.parse(Args.class, new String[]{"a", "b", "c"});
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
    void longOption() {
        LongArgs args = JOptParse.parse(LongArgs.class, new String[]{"--xxx", "--yyy=foo"});
        assertThat(args.optionX, is(true));
        assertThat(args.optionY, is("foo"));
    }
}
