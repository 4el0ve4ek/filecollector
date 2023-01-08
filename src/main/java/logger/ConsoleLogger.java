package main.java.logger;

/**
 * prints messages using System.out.printf.
 */
public class ConsoleLogger implements Logger {


    public static final boolean COLOR_ENABLED;

    static {
        COLOR_ENABLED = !System.getProperty("os.name").toLowerCase().contains("win");
    }

    private void setColor(ConsoleColor color) {
        if (COLOR_ENABLED) {
            System.out.print(color);
        }
    }

    private void resetColor() {
        setColor(ConsoleColor.RESET);
    }

    private void writeMessage(ConsoleColor level, String messageFormat, Object... args) {
        setColor(level);
        System.out.printf(messageFormat, args);
        resetColor();

        System.out.println();
    }

    @Override
    public void Info(Object message) {
        writeMessage(ConsoleColor.RESET, String.valueOf(message));
    }

    @Override
    public void Infof(String messageFormat, Object... args) {
        writeMessage(ConsoleColor.RESET, messageFormat, args);
    }

    @Override
    public void Warn(Object message) {
        writeMessage(ConsoleColor.WARN, String.valueOf(message));
    }

    @Override
    public void Warnf(String messageFormat, Object... args) {
        writeMessage(ConsoleColor.WARN, messageFormat, args);
    }

    @Override
    public void Error(Object message) {
        writeMessage(ConsoleColor.ERROR, String.valueOf(message));
    }

    @Override
    public void Errorf(String messageFormat, Object... args) {
        writeMessage(ConsoleColor.ERROR, messageFormat, args);
    }
}
