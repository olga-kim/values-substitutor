package model;

public enum Messages {

    HELP("Available parameters are:" +
            "-i - for input file,\n" +
            "-o - for output file,\n" +
            "-p - for properties file"),
    INVALID_PARAMS("Invalid execution parameters. Use -help for more details"),
    INVALID_PARAMS_ORDER("Invalid execution parameters order. Use -help for more details"),
    INVALID_FILE_NAME("Invalid file name: %s"),
    INVALID_XML("%s file contains invalid XML"),
    FILE_NOT_FOUND("%s file was not found"),
    IO_ERROR("I/O error was found while %s"),
    PROPERTY_NOT_FOUND("Property was not found");

    public String value;

    Messages(String value) {
        this.value = value;
    }
}
