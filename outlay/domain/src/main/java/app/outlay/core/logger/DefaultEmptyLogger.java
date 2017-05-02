package app.outlay.core.logger;

/**
 * Created by Bogdan Melnychuk on 4/23/16.
 */
public class DefaultEmptyLogger implements Logger {
    @Override
    public void info(String message) {
        // Empty default implementation, since the class is an "empty logger"
        // To be overriden depending on the context it is used in
    }

    @Override
    public void warn(String message) {
        // Empty default implementation, since the class is an "empty logger"
        // To be overriden depending on the context it is used in
    }

    @Override
    public void warn(String message, Throwable e) {
        // Empty default implementation, since the class is an "empty logger"
        // To be overriden depending on the context it is used in
    }

    @Override
    public void debug(String message) {
        // Empty default implementation, since the class is an "empty logger"
        // To be overriden depending on the context it is used in
    }

    @Override
    public void error(String message) {
        // Empty default implementation, since the class is an "empty logger"
        // To be overriden depending on the context it is used in
    }

    @Override
    public void error(String message, Throwable e) {
        // No default implementation, since the class is an "empty logger"
        // To be overriden depending on the context it is used in
    }
}
