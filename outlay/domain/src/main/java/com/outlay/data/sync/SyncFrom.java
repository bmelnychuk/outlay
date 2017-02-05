package com.outlay.data.sync;

import java.util.List;

import rx.Observable;

/**
 * Created by bmelnychuk on 2/5/17.
 */

public interface SyncFrom<T> {
    Observable<List<T>> getAll();
}
