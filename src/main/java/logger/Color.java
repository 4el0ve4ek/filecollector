package main.java.logger;

public enum Color {
    RESET("\u001B[0m"),

    ERROR("\u001B[31m"),

    WARN("\u001B[33m");

    private final String consoleName;

    Color(String consoleName) {
        this.consoleName = consoleName;
    }

    @Override
    public String toString() {
        return consoleName;
    }
}
