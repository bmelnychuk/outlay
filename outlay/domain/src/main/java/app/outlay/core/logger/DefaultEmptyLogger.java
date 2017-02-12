package app.outlay.core.logger;

/**
 * Created by Bogdan Melnychuk on 4/23/16.
 */
public class DefaultEmptyLogger implements Logger {
    @Override
    public void info(String message) {
    }

    @Override
    public void warn(String message) {
    }

    @Override
    public void warn(String message, Throwable e) {
    }

    @Override
    public void debug(String message) {
    }

    @Override
    public void error(String message) {
    }

    @Override
    public void error(String message, Throwable e) {
    }
}
