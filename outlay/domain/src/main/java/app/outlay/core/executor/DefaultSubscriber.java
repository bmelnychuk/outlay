package app.outlay.core.executor;


import app.outlay.core.logger.Logger;
import app.outlay.core.logger.LoggerFactory;

import rx.Subscriber;

/**
 * Created by Bogdan Melnychuk on 2/19/16.
 */
public class DefaultSubscriber<T> extends Subscriber<T> {
    private static Logger log = LoggerFactory.getLogger();

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        log.error("Rx uncaught error.", e);
    }

    @Override
    public void onNext(T t) {

    }
}
