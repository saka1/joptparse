package net.saka1.joptparse;

import net.saka1.joptparse.parser.ParseResult;
import net.saka1.joptparse.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("ConstantConditions") // To suppress warning about Optional#get()
public class ParserTest {

    private Parser parser;
    private OptionSpec optionSpec;

    @Before
    public void setup() {
        optionSpec = new OptionSpec();
        optionSpec.setShortName("x", OptionArgumentType.NONE);
        optionSpec.setShortName("y", OptionArgumentType.ARGUMENT);
        optionSpec.setShortName("z", OptionArgumentType.ARGUMENT_LIST);
        optionSpec.setLongName("xxx", OptionArgumentType.NONE);
        optionSpec.setLongName("yyy", OptionArgumentType.ARGUMENT);
        optionSpec.setLongName("zzz", OptionArgumentType.ARGUMENT_LIST);
        parser = new Parser(optionSpec);
    }

    @Test
    public void empty() {
        ParseResult result = parser.parse();
        assertThat(result.isSucceeded(), is(true));
        assertThat(result.getOptionSet().isEmpty(), is(true));
    }

    @Test
    public void operandsOnly() {
        ParseResult result = parser.parse("hoge", "foo");
        assertThat(result.getOperands().get(0), is("hoge"));
        assertThat(result.getOperands().get(1), is("foo"));
    }

    @Test
    public void oneOption() {
        ParseResult result = parser.parse("-x");
        assertThat(result.getArgument("x").isPresent(), is(true));
        assertThat(result.getArgument("y").isPresent(), is(false));
    }

    @Test
    public void oneLongOption() {
        ParseResult result = parser.parse("--xxx");
        assertThat(result.getArgument("xxx").isPresent(), is(true));
    }

    @Test
    public void endOfOption() {
        ParseResult result = parser.parse("--", "-a");
        assertThat(result.getOptionSet().isEmpty(), is(true));
        assertThat(result.getOperands().get(0), is("-a"));
    }

    @Test
    public void concatOptions() {
        optionSpec.setShortName("a", OptionArgumentType.NONE);
        optionSpec.setShortName("b", OptionArgumentType.NONE);
        ParseResult result = parser.parse("-ab");

        assertThat(result.getArgument("a").isPresent(), is(true));
        assertThat(result.getArgument("b").isPresent(), is(true));
    }

    @Test
    public void concatOptionArgument() {
        ParseResult result = parser.parse("-yhoge");
        assertThat(result.getArgument("y").isPresent(), is(true));
    }

    @Test
    public void optionArgument() {
        ParseResult result = parser.parse("-y", "hoge");
        assertThat(result.getArgument("y").get(), is(Collections.singletonList("hoge")));
    }

    @Test
    public void longOptionArgument() {
        ParseResult result = parser.parse("--yyy=hoge");
        assertThat(result.getArgument("yyy").isPresent(), is(true));
        assertThat(result.getArgument("yyy").get().get(0), is("hoge"));
    }

    @Test
    public void complexTest() {
        ParseResult result = parser.parse("-x", "hoge", "--yyy=y", "fuga", "--", "-foo");
        assertThat(result.isSucceeded(), is(true));
        assertThat(result.getArgument("x").get().isEmpty(), is(true));
        assertThat(result.getArgument("yyy").get().get(0), is("y"));
        assertThat(result.getOperands(), is(Arrays.asList("hoge", "fuga", "-foo")));
    }
}
