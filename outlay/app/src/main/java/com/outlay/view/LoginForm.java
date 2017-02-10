package com.outlay.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.outlay.R;
import com.outlay.utils.IconUtils;
import com.outlay.utils.ResourceUtils;
import com.outlay.view.helper.AnimationUtils;
import com.outlay.view.helper.ViewHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class LoginForm extends RelativeLayout {
    public static final int MODE_SIGN_IN = 0;
    public static final int MODE_SIGN_UP = 1;

    @Bind(R.id.signInForm)
    View signInForm;

    @Bind(R.id.signUpForm)
    View signUpForm;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.signUpButton)
    Button signUpButton;

    @Bind(R.id.signInButton)
    Button signInButton;

    @Bind(R.id.forgetPassword)
    Button forgetPassword;

    @Bind(R.id.skipButton)
    Button skipButton;

    @Bind(R.id.signInEmail)
    EditText signInEmail;

    @Bind(R.id.signInPassword)
    EditText signInPassword;

    @Bind(R.id.signUpEmail)
    EditText signUpEmail;

    @Bind(R.id.signUpPassword)
    EditText signUpPassword;

    @Bind(R.id.signUpRepeatPassword)
    EditText signUpRepeatPassword;

    private OnSubmitClickListener signInListener;
    private OnSubmitClickListener signUpListener;
    private OnPasswordForgetClick onPasswordForgetClick;

    public LoginForm(Context context) {
        super(context);
        init(null);
    }

    public LoginForm(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LoginForm(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoginForm(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View parent = inflater.inflate(R.layout.view_login_form, this, true);
        ButterKnife.bind(this, parent);

        fab.setImageDrawable(
                IconUtils.getToolbarIcon(getContext(), MaterialDesignIconic.Icon.gmi_account_add)
        );

        fab.setOnClickListener(v -> {
            Point revealPoint = getViewCenter();
            if (signUpForm.getVisibility() == View.VISIBLE) {
                AnimationUtils.hideWithReveal(signUpForm, revealPoint);
                fab.setImageDrawable(IconUtils.getToolbarIcon(getContext(), MaterialDesignIconic.Icon.gmi_account_add));
            } else {
                AnimationUtils.showWithReveal(signUpForm, revealPoint);
                fab.setImageDrawable(IconUtils.getToolbarIcon(getContext(), MaterialDesignIconic.Icon.gmi_close));
            }
        });

        signInButton.setOnClickListener(v -> {
            if (signInListener != null) {
                signInListener.onSubmit(
                        signInEmail.getText().toString(),
                        signInPassword.getText().toString(),
                        v
                );
            }
        });

        signUpButton.setOnClickListener(v -> {
            if (signUpListener != null) {
                signUpListener.onSubmit(
                        signUpEmail.getText().toString(),
                        signUpPassword.getText().toString(),
                        v
                );
            }
        });

        forgetPassword.setOnClickListener(v -> {
            if (onPasswordForgetClick != null) {
                onPasswordForgetClick.onPasswordForget();
            }
        });
    }

    public void setOnSkipButtonClick(View.OnClickListener click) {
        skipButton.setOnClickListener(click);
    }

    public void setOnSignInClickListener(OnSubmitClickListener signInListener) {
        this.signInListener = signInListener;
    }

    public void setOnSignUpClickListener(OnSubmitClickListener signUpListener) {
        this.signUpListener = signUpListener;
    }

    public void setOnPasswordForgetClick(OnPasswordForgetClick onPasswordForgetClick) {
        this.onPasswordForgetClick = onPasswordForgetClick;
    }

    public void setMode(int mode) {
        signUpForm.setVisibility(mode == MODE_SIGN_UP ? VISIBLE : INVISIBLE);
        fab.setImageDrawable(mode == MODE_SIGN_UP ?
                IconUtils.getToolbarIcon(getContext(), MaterialDesignIconic.Icon.gmi_account_add) :
                IconUtils.getToolbarIcon(getContext(), MaterialDesignIconic.Icon.gmi_close)
        );
    }

    public void setToggleModeButtonVisible(boolean visible) {
        fab.setVisibility(visible ? VISIBLE : GONE);
    }

    private void toggleViewVisibility(View view) {
        boolean visible = view.getVisibility() == View.VISIBLE;
        view.setVisibility(visible ? View.INVISIBLE : View.VISIBLE);
    }

    private Point getViewCenter() {
        int x = signUpForm.getRight();
        int y = signUpForm.getTop() + ViewHelper.dpToPx(56);
        return new Point(x, y);
    }

    public interface OnSubmitClickListener {
        void onSubmit(String email, String password, View src);
    }

    public interface OnPasswordForgetClick {
        void onPasswordForget();
    }
}
