package main.java.logger;

public class StdLogger implements Logger {


    public static final boolean COLOR_ENABLED;

    static {
        COLOR_ENABLED = !System.getProperty("os.name").toLowerCase().contains("win");
    }

    private void setColor(Color color) {
        if (COLOR_ENABLED) {
            System.out.print(color);
        }
    }

    private void resetColor() {
        setColor(Color.RESET);
    }

    private void writeMessage(Color level, String messageFormat, Object... args) {
        setColor(level);
        System.out.printf(messageFormat, args);
        resetColor();

        System.out.println();
    }

    @Override
    public void Info(Object message) {
        writeMessage(Color.RESET, String.valueOf(message));
    }

    @Override
    public void Infof(String messageFormat, Object... args) {
        writeMessage(Color.RESET, messageFormat, args);
    }

    @Override
    public void Warn(Object message) {
        writeMessage(Color.WARN, String.valueOf(message));
    }

    @Override
    public void Warnf(String messageFormat, Object... args) {
        writeMessage(Color.WARN, messageFormat, args);
    }

    @Override
    public void Error(Object message) {
        writeMessage(Color.ERROR, String.valueOf(message));
    }

    @Override
    public void Errorf(String messageFormat, Object... args) {
        writeMessage(Color.ERROR, messageFormat, args);
    }
}
