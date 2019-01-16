package model;

public enum Messages {

    HELP("-i - input\n" +
            "-o - output\n" +
            "-p - properties"),
    INVALID_PARAMS("Invalid execution parameters. Use -help for more details"),
    INVALID_PARAMS_ORDER("Invalid execution parameters order. Use -help for more details");

    public String value;

    Messages(String value) {
        this.value = value;
    }
}
