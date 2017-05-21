package app.outlay.mvp.view;

import com.hannesdorfmann.mosby.mvp.MvpView;

import app.outlay.domain.model.Category;

/**
 * Created by bmelnychuk on 10/25/16.
 */

public interface SettingsView extends MvpView {
    void setCurrentThemeSetting(int theme);
}
