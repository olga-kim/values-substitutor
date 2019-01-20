package model;

import java.util.Arrays;

public enum ExecutionParams {

    HELP("-help"),
    INPUT("-i"),
    OUTPUT("-o"),
    PROPERTIES("-p"),
    UNKNOWN("");

    public final String value;

    ExecutionParams(String value) {
        this.value = value;
    }

    public static ExecutionParams findByValue(String value) {
        return Arrays.stream(ExecutionParams.values())
                .filter(it -> it.value.equals(value))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
