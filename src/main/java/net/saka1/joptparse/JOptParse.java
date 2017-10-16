package net.saka1.joptparse;

import net.saka1.joptparse.annotation.Operands;
import net.saka1.joptparse.annotation.Option;
import net.saka1.joptparse.annotation.FailedReason;
import net.saka1.joptparse.annotation.ParseSucceeded;
import net.saka1.joptparse.parser.ParseResult;
import net.saka1.joptparse.parser.Parser;
import net.saka1.joptparse.util.Tuple;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class JOptParse<T> {
    private final Class<T> argsClazz;
    public JOptParse(Class<T> clazz) {
        this.argsClazz = clazz;
    }

    public T parse(String[] args) {
        return parse(this.argsClazz, args);
    }

    public String usage() {
        throw new IllegalStateException("no implementation yet."); //TODO
    }

    /** Shorthand version of JOptParse#parse. */
    public static <T> T parse(Class<T> clazz, String[] args) {
        return doParse(clazz, args);
    }

    private static <T> T doParse(Class<T> clazz, String[] args) {
        OptionSpec optionSpec = setupOptionSpec(clazz);
        Parser parser = new Parser(optionSpec);
        ParseResult parseResult = parser.parse(args);
        try {
            T instance = clazz.newInstance();
            // options
            Map<String, String> optionName2FieldName = resolveOptionName2FieldName(clazz);
            for (Tuple<String, List<String>> tuple : parseResult.getOptionSet()) {
                String fieldName = optionName2FieldName.get(tuple.first);
                Class<?> type = instance.getClass().getField(fieldName).getType();
                if (type.equals(boolean.class)) {
                    instance.getClass().getField(fieldName).setBoolean(instance, true);
                } else if (type.equals(Boolean.class)) {
                    instance.getClass().getField(fieldName).set(instance, Boolean.TRUE);
                } else if (type.equals(String.class)) {
                    //TODO handle if list is empty
                    instance.getClass().getField(fieldName).set(instance, tuple.second.get(0));
                } else if (List.class.isAssignableFrom(type)) {
                    throw new IllegalStateException("no impl");
                } else {
                    throw new IllegalStateException("must not happen");
                }
            }
            // operands
            Optional<String> operandsName = resolveOperandsName(clazz);
            if (operandsName.isPresent()) { // Optional#map() is not good choice here, because of Checked Exception
                String name = operandsName.get();
                instance.getClass().getField(name).set(instance, parseResult.getOperands());
            }
            // pass whether parse succeeded or failed
            Optional<String> parseSucceededName = resolveParseSucceededName(clazz);
            if (parseSucceededName.isPresent()) {
                String name = parseSucceededName.get();
                instance.getClass().getField(name).setBoolean(instance, parseResult.isSucceeded());
            }
            // pass parse info
            Optional<String> parseInfoName = resolveParseInfoName(clazz);
            if (parseInfoName.isPresent()) {
                String name = parseInfoName.get();
                //TODO rethink pass instance
                instance.getClass().getField(name).set(instance, parseResult.getErrors());
            }
            return instance;
        } catch (ReflectiveOperationException e) {
            //TODO rethink error handling
            throw new IllegalArgumentException("Fail to instantiate argument class", e);
        }
    }

    private static OptionSpec setupOptionSpec(Class<?> clazz) {
        OptionSpec optionSpec = new OptionSpec();
        annotationStream(clazz)
                .filter(tuple -> tuple.second instanceof Option)
                .map(tuple -> Tuple.create(tuple.first, (Option) (tuple.second)))
                .forEach(tuple -> {
                    Field field = tuple.first;
                    final OptionArgumentType optionArgumentType;
                    if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
                        optionArgumentType = OptionArgumentType.NONE;
                    } else if (String.class.isAssignableFrom(field.getType())) {
                        optionArgumentType = OptionArgumentType.ARGUMENT;
                    } else if (List.class.isAssignableFrom(field.getType())) {
                        optionArgumentType = OptionArgumentType.ARGUMENT_LIST;
                    } else {
                        throw new IllegalArgumentException("invalid field type");
                    }
                    // truncate "-" at the head if exists
                    String normalizedOptionName = tuple.second.name().replaceFirst("^-", "");
                    String normalizedLongOptionName = tuple.second.longName().replaceFirst("^-", "");
                    optionSpec.setShortName(normalizedOptionName, optionArgumentType);
                    if (!tuple.second.longName().isEmpty()) {
                        optionSpec.setLongName(normalizedLongOptionName, optionArgumentType);
                    }
                });
        return optionSpec;
    }

    private static Map<String, String> resolveOptionName2FieldName(Class<?> clazz) {
        //TODO handle duplicated key
        Map<String, String> result = new HashMap<>();
        annotationStream(clazz)
                .filter(tuple -> tuple.second instanceof Option)
                .map(tuple -> Tuple.create(tuple.first, (Option) (tuple.second)))
                .forEach(tuple -> {
                    result.put(tuple.second.name(), tuple.first.getName());
                    result.put(tuple.second.longName(), tuple.first.getName());
                });
        return result;
    }

    private static Optional<String> resolveParseInfoName(Class<?> clazz) {
        return findFirstAnnotatedFieldName(clazz, FailedReason.class);
    }

    private static Optional<String> resolveParseSucceededName(Class<?> clazz) {
        return findFirstAnnotatedFieldName(clazz, ParseSucceeded.class);
    }

    private static Optional<String> resolveOperandsName(Class<?> clazz) {
        return findFirstAnnotatedFieldName(clazz, Operands.class);
    }

    private static Optional<String> findFirstAnnotatedFieldName(
            Class<?> clazz, Class<? extends Annotation> annotation) {
        return annotationStream(clazz)
                .filter(tuple -> annotation.isInstance(tuple.second))
                .findFirst()
                .map(tuple -> tuple.first.getName());
    }

    private static Stream<Tuple<Field, Annotation>> annotationStream(Class<?> clazz) {
        Stream.Builder<Tuple<Field, Annotation>> builder = Stream.builder();
        for (Field field : clazz.getFields()) {
            for (Annotation annotation : field.getAnnotations()) {
                builder.add(Tuple.create(field, annotation));
            }
        }
        return builder.build();
    }
}
