package com.outlay.mvp.presenter;

import com.outlay.mvp.view.MvpView;

/**
 * Created by bmelnychuk on 10/25/16.
 */

public class MvpPresenter<T extends MvpView> {
    private T view;

    public void attachView(T view) {
        this.view = view;
    }

    protected T getView() {
        return view;
    }

    public boolean isViewAttached() {
        return view != null;
    }
}
