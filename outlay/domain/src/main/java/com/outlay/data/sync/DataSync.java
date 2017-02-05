package com.outlay.data.sync;

import java.util.List;

import rx.Observable;

/**
 * Created by bmelnychuk on 11/1/16.
 */

public class DataSync<T> {
    private SyncFrom<T> syncFrom;
    private SyncTo<T> syncTo;

    public DataSync(SyncFrom<T> syncFrom, SyncTo<T> syncTo) {
        this.syncFrom = syncFrom;
        this.syncTo = syncTo;
    }

    public Observable<List<T>> synchronize() {
        return syncFrom.getAll().switchMap(categories -> syncTo.saveAll(categories));
    }
}
