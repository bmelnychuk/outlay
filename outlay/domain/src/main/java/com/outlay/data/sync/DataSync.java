package com.outlay.data.sync;

import rx.Observable;

/**
 * Created by bmelnychuk on 11/1/16.
 */

public class DataSync<E, T extends SyncSource<E>> {
    private T sourceFrom;
    private T sourceTo;

    public DataSync(T sourceFrom, T sourceTo) {
        this.sourceFrom = sourceFrom;
        this.sourceTo = sourceTo;
    }

    public Observable<Void> sync() {
        return sourceFrom.getAll()
                .switchMap(data -> sourceTo.saveAll(data))
                .switchMap(es -> sourceFrom.clear());
    }
}
