package main.java.logger;

public interface Logger {

    void Info(Object message);

    void Infof(String messageFormat, Object... args);

    void Warn(Object message);

    void Warnf(String messageFormat, Object... args);

    void Error(Object message);

    void Errorf(String messageFormat, Object... args);
}
