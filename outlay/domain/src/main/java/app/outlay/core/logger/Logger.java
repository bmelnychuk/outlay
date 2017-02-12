package app.outlay.core.logger;

/**
 * Created by Bogdan Melnychuk on 4/23/16.
 */
public interface Logger {
    void info(String message);

    void warn(String message);

    void warn(String message, Throwable e);

    void debug(String message);

    void error(String message);

    void error(String message, Throwable e);
}
