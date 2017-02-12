package app.outlay.core.executor;

import rx.Scheduler;

/**
 * Created by bmelnychuk on 5/6/16.
 */
public interface PostExecutionThread {
    Scheduler getScheduler();
}
