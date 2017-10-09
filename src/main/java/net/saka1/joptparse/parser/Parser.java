package net.saka1.joptparse.parser;

import net.saka1.joptparse.OptionSpec;
import net.saka1.joptparse.utils.Tuple;

import java.util.Arrays;
import java.util.List;

import static net.saka1.joptparse.utils.Util.*;

/**
 * CommandLine parser.
 */
public class Parser {
    private final OptionSpec optionSpec;

    public Parser(OptionSpec optionSpec) {
        this.optionSpec = optionSpec;
    }

    public ParseResult parse(String... args) {
        return doParse(Arrays.asList(args));
    }

    private ParseResult doParse(List<String> list) {
        if (list.isEmpty()) {
            return ParseResult.emptyResult();
        }
        String arg = head(list);
        Scanner scanner = new Scanner(arg);
        if (scanner.scan("-").isPresent()) {
            if (scanner.isEof()) {
                // case: "-"
                //TODO right behavior?
                return ParseResult.operandsOf("-")
                        .merge(doParse(tail(list)));
            } else if (scanner.scan("-").isPresent()) {
                // case: "--"
                if (scanner.isEof()) {
                    List<String> rest = tail(list);
                    return rest.stream()
                            .map(ParseResult::operandsOf)
                            .reduce(ParseResult.emptyResult(), ParseResult::merge);
                }
                // case: "--<longname>"
                return scanner.scanRegexp("[a-zA-Z0-9]+")
                        .map(optionName -> parseLongOptionArgument(optionName, scanner))
                        .orElseGet(() -> ParseResult.errorOf("invalid input"))
                        .merge(doParse(tail(list)));
            } else {
                // case: "-<shortname>"
                return scanner.scanRegexp("[a-zA-Z0-9]+")
                        .map(chunk -> {
                            Tuple<ParseResult, List<String>> result =
                                    parseShortNameChunk(chunk, tail(list));
                            return result.first.merge(doParse(result.second));
                        })
                        .orElseGet(() -> ParseResult.errorOf("invalid input"));
            }
        }
        // case: operand
        return ParseResult.operandsOf(arg)
                .merge(doParse(tail(list)));
    }

    private ParseResult parseLongOptionArgument(String optionName, Scanner scanner) {
        return optionSpec.getArgumentType(optionName).map(prop -> {
            switch (prop) {
                case NONE:
                    return ParseResult.optionOf(optionName);
                case ARGUMENT:
                    return scanner.scan("=")
                            .flatMap(x -> scanner.scanRegexp(".+"))
                            .map(argument -> ParseResult.optionOf(optionName, argument))
                            .orElseGet(() -> ParseResult.errorOf("invalid input"));
                case ARGUMENT_LIST:
                default:
                    throw new IllegalStateException("must not happen");
            }
        }).orElseGet(() -> ParseResult.errorOf("invalid input"));
    }

    private Tuple<ParseResult, List<String>> parseShortNameChunk(String chunk, List<String> input) {
        return parseShortNameChunkRec(splitCodePointList(chunk), input);
    }

    // Q. Why Tuple?
    // A. In some cases, this method consumes input, but others not.
    //    So this method should return not only ParseResult, but also the next position to consume input.
    private Tuple<ParseResult, List<String>> parseShortNameChunkRec(List<String> chunk, List<String> input) {
        if (chunk.isEmpty()) {
            return Tuple.create(ParseResult.emptyResult(), input);
        }
        String oneString = head(chunk);
        return optionSpec.getArgumentType(oneString)
                .map(prop -> {
                    switch (prop) {
                        case NONE:
                            return Tuple.create(
                                    ParseResult.optionOf(oneString)
                                            .merge(parseShortNameChunkRec(tail(chunk), input).first),
                                    input);
                        case ARGUMENT:
                            String tail = concat(tail(chunk));
                            if (tail.isEmpty() && input.isEmpty()) {
                                return Tuple.create(ParseResult.errorOf("invalid input(option argument not found)"), input);
                            }
                            if (tail.isEmpty()) {
                                return Tuple.create(ParseResult.optionOf(oneString, head(input)), tail(input));
                            } else {
                                return Tuple.create(ParseResult.optionOf(oneString, tail), input);
                            }
                        case ARGUMENT_LIST:
                            throw new IllegalStateException("no impl"); //TODO
                        default:
                            throw new IllegalStateException("must not happen");
                    }
                })
                .orElseGet(() -> Tuple.create(ParseResult.errorOf("invalid input"), input));
    }
}
