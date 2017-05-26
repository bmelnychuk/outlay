package app.outlay.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import javax.inject.Inject;

import app.outlay.R;
import app.outlay.impl.AppPreferences;
import app.outlay.mvp.presenter.SettingsPresenter;
import app.outlay.mvp.view.SettingsView;
import app.outlay.view.Navigator;
import app.outlay.view.fragment.base.BaseMvpFragment;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bogdan Melnychuk on 1/20/16.
 */
public class SettingsFragment extends BaseMvpFragment<SettingsView, SettingsPresenter> implements SettingsView {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.themeControl)
    View themeControl;
    @Bind(R.id.themeName)
    TextView themeName;

    @Inject
    SettingsPresenter presenter;

    @Override
    public SettingsPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApp().getUserComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, null, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);


        setToolbar(toolbar);
        setDisplayHomeAsUpEnabled(true);
        getActivity().setTitle(getString(R.string.menu_item_settings));

        if (getBaseActivity().appPreferences().getTheme() == AppPreferences.THEME_DARK) {
            themeName.setText("Dark");
        } else {
            themeName.setText("Light");
        }

        themeControl.setOnClickListener(view1 ->
                new MaterialDialog.Builder(getActivity())
                        .backgroundColor(getOutlayTheme().backgroundColor)
                        .title(R.string.settings_dialog_select_theme)
                        .items("Dark", "Light")
                        .itemsCallback((dialog, view11, which, text) -> {
                            getPresenter().updateTheme(which);
                            analytics().trackThemeChanged(which);
                            Navigator.goToMainScreen(getActivity());
                        })
                        .show());
    }

    @Override
    public void setCurrentThemeSetting(int theme) {

    }
}
