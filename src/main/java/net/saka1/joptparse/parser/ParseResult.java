package net.saka1.joptparse.parser;

import net.saka1.joptparse.util.Tuple;
import net.saka1.joptparse.util.Util;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class ParseResult {
    private final Map<String, List<String>> argsMap;
    private final List<String> operands;
    private final List<String> errors;

    private ParseResult() {
        this.argsMap = new HashMap<>();
        this.operands = new ArrayList<>();
        this.errors = new ArrayList<>();
    }

    public static ParseResult emptyResult() {
        return new ParseResult();
    }

    public static ParseResult operandsOf(String... operands) {
        ParseResult result = new ParseResult();
        result.operands.addAll(Arrays.asList(operands));
        return result;
    }

    public static ParseResult optionOf(String optionName) {
        ParseResult result = new ParseResult();
        if (!result.argsMap.containsKey(optionName)) {
            result.argsMap.put(optionName, new ArrayList<>());
        }
        return result;
    }

    public static ParseResult optionOf(String optionName, String optionArgument) {
        ParseResult result = optionOf(optionName);
        result.argsMap.get(optionName).add(optionArgument);
        return result;
    }

    public static ParseResult errorOf(String message) {
        ParseResult result = new ParseResult();
        result.errors.add(message);
        return result;
    }

    public ParseResult merge(ParseResult other) {
        BinaryOperator<List<String>> mergeFunction = (lst1, lst2) -> {
            List<String> list = new ArrayList<>(lst1);
            list.addAll(lst2);
            return list;
        };
        // copy all fields deeply & merge
        ParseResult result = new ParseResult();
        this.argsMap.forEach((key, value) -> result.argsMap.merge(key, value, mergeFunction));
        other.argsMap.forEach((key, value) -> result.argsMap.merge(key, value, mergeFunction));
        result.operands.addAll(this.operands);
        result.operands.addAll(other.operands);
        result.errors.addAll(other.errors);
        return result;
    }

    public List<String> getOperands() {
        return Util.copyList(operands);
    }

    public List<String> getErrors() {
        return Util.copyList(errors);
    }

    public Optional<List<String>> getArgument(String optionName) {
        return Optional.ofNullable(argsMap.get(optionName));
    }

    public boolean isSucceeded() {
        return errors.isEmpty();
    }

    public Set<Tuple<String, List<String>>> getOptionSet() {
        return this.argsMap.entrySet().stream()
                .map(e -> Tuple.create(e.getKey(), e.getValue()))
                .collect(Collectors.toSet());
    }
}
