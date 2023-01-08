package main.java.logger;

/**
 * Contains constants for different console color.
 */
enum ConsoleColor {
    RESET("\u001B[0m"),

    ERROR("\u001B[31m"), // Red.

    WARN("\u001B[33m");  // Yellow.

    private final String consoleName;

    ConsoleColor(String consoleName) {
        this.consoleName = consoleName;
    }

    @Override
    public String toString() {
        return consoleName;
    }
}
