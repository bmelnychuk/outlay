package app.outlay.core.logger;

/**
 * Created by Bogdan Melnychuk on 4/23/16.
 */
public class LoggerFactory {
    private static Logger logger;

    public static Logger getLogger() {
        if (logger == null) {
            logger = new DefaultEmptyLogger();
        }
        return logger;
    }

    public static void registerLogger(Logger logger) {
        LoggerFactory.logger = logger;
    }
}
