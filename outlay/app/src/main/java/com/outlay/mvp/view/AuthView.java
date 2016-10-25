package com.outlay.mvp.view;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public interface AuthView extends MvpView {
    void setProgress(boolean running);
    void error(String message);
    void info(String message);
}
