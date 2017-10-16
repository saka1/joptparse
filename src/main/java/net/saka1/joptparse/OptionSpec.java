package net.saka1.joptparse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OptionSpec {
    private final Map<String, OptionArgumentType> shortNames;
    private final Map<String, OptionArgumentType> longNames;

    public OptionSpec() {
        this.shortNames = new HashMap<>();
        this.longNames = new HashMap<>();
    }

    public void setShortName(String optionName, OptionArgumentType prop) {
        shortNames.put(optionName, prop);
    }

    public void setLongName(String optionName, OptionArgumentType prop) {
        longNames.put(optionName, prop);
    }

    public Optional<OptionArgumentType> getArgumentType(String optionName) {
        // rewrite with Optional#or() in Java9
        Optional<OptionArgumentType> shortNameOptional = Optional.ofNullable(shortNames.get(optionName));
        return shortNameOptional.isPresent() ? shortNameOptional : Optional.ofNullable(longNames.get(optionName));
    }
}
