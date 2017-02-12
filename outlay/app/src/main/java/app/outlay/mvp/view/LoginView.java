package app.outlay.mvp.view;

import com.hannesdorfmann.mosby.mvp.MvpView;
import app.outlay.domain.model.User;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public interface LoginView extends MvpView {
    void setProgress(boolean running);
    void error(Throwable throwable);
    void info(String message);
    void onSuccess(User user);
}
