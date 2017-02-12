package app.outlay.core.data;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public interface AppPreferences {
    boolean isFirstRun();
    void setFirstRun(boolean firstRun);
    String getSessionId();
    void setSessionId(String sessionId);
}
