package com.outlay.data.sync;

import java.util.List;

import rx.Observable;

/**
 * Created by bmelnychuk on 11/2/16.
 */

public interface SyncSource<T> {
    Observable<List<T>> getAll();

    Observable<List<T>> saveAll(List<T> items);

    Observable<Void> clear();
}
