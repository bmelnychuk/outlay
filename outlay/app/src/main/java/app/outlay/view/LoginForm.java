package app.outlay.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import app.outlay.utils.DeviceUtils;
import app.outlay.utils.IconUtils;
import app.outlay.view.helper.AnimationUtils;
import app.outlay.view.helper.TextWatcherAdapter;
import app.outlay.view.helper.ViewHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class LoginForm extends RelativeLayout {
    public static final int MODE_SIGN_IN = 0;
    public static final int MODE_SIGN_UP = 1;

    @Bind(app.outlay.R.id.signInForm)
    View signInForm;

    @Bind(app.outlay.R.id.signUpForm)
    View signUpForm;

    @Bind(app.outlay.R.id.fab)
    FloatingActionButton fab;

    @Bind(app.outlay.R.id.signUpButton)
    Button signUpButton;

    @Bind(app.outlay.R.id.signInButton)
    Button signInButton;

    @Bind(app.outlay.R.id.forgetPassword)
    Button forgetPassword;

    @Bind(app.outlay.R.id.skipButton)
    Button skipButton;

    @Bind(app.outlay.R.id.signInInputEmail)
    TextInputLayout signInEmail;

    @Bind(app.outlay.R.id.signInInputPassword)
    TextInputLayout signInPassword;

    @Bind(app.outlay.R.id.signUpInputEmail)
    TextInputLayout signUpEmail;

    @Bind(app.outlay.R.id.signUpInputPassword)
    TextInputLayout signUpPassword;

    @Bind(app.outlay.R.id.signUpInputRepeatPassword)
    TextInputLayout signUpRepeatPassword;

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
        View parent = inflater.inflate(app.outlay.R.layout.view_login_form, this, true);
        ButterKnife.bind(this, parent);

        signInEmail.getEditText().addTextChangedListener(new ClearErrorTextWatcher(signInEmail));
        signInPassword.getEditText().addTextChangedListener(new ClearErrorTextWatcher(signInPassword));

        signUpEmail.getEditText().addTextChangedListener(new ClearErrorTextWatcher(signUpEmail));
        signUpPassword.getEditText().addTextChangedListener(new ClearErrorTextWatcher(signUpPassword));
        signUpRepeatPassword.getEditText().addTextChangedListener(new ClearErrorTextWatcher(signUpRepeatPassword));

        fab.setImageDrawable(
                IconUtils.getToolbarIcon(getContext(), MaterialDesignIconic.Icon.gmi_account_add)
        );

        fab.setOnClickListener(v -> {
            DeviceUtils.hideKeyboard((Activity) getContext());
            Point revealPoint = getViewCenter();
            if (signUpForm.getVisibility() == View.VISIBLE) {
                AnimationUtils.hideWithReveal(signUpForm, revealPoint);
                fab.setImageDrawable(IconUtils.getToolbarIcon(getContext(), MaterialDesignIconic.Icon.gmi_account_add));
            } else {
                AnimationUtils.showWithReveal(signUpForm, revealPoint);
                fab.setImageDrawable(IconUtils.getToolbarIcon(getContext(), MaterialDesignIconic.Icon.gmi_close, 4));
            }
        });

        signInButton.setOnClickListener(v -> {
            if (isSignInInputValid() && signInListener != null) {
                signInListener.onSubmit(
                        signInEmail.getEditText().getText().toString(),
                        signInPassword.getEditText().getText().toString(),
                        v
                );
            }
        });

        signUpButton.setOnClickListener(v -> {
            if (isSignUpInputValid() && signUpListener != null) {
                signUpListener.onSubmit(
                        signUpEmail.getEditText().getText().toString(),
                        signUpPassword.getEditText().getText().toString(),
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

    private boolean isSignInInputValid() {
        boolean result = true;
        if (!isEmailValid(signInEmail.getEditText().getText())) {
            signInEmail.setErrorEnabled(true);
            signInEmail.setError(getContext().getString(app.outlay.R.string.error_signin_invalid_email));
            result = false;
        }
        String password = signInPassword.getEditText().getText().toString();
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            signInPassword.setErrorEnabled(true);
            signInPassword.setError(getContext().getString(app.outlay.R.string.error_signin_invalid_password));
            result = false;
        }
        return result;
    }

    private boolean isSignUpInputValid() {
        boolean result = true;
        if (!isEmailValid(signUpEmail.getEditText().getText())) {
            signUpEmail.setErrorEnabled(true);
            signUpEmail.setError(getContext().getString(app.outlay.R.string.error_signin_invalid_email));
            result = false;
        }

        String password = signUpPassword.getEditText().getText().toString();

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            signUpPassword.setErrorEnabled(true);
            signUpPassword.setError(getContext().getString(app.outlay.R.string.error_signin_invalid_password));
            result = false;
            return result;
        }

        String repeatPassword = signUpRepeatPassword.getEditText().getText().toString();
        if (!password.equals(repeatPassword)) {
            signUpRepeatPassword.setErrorEnabled(true);
            signUpRepeatPassword.setError(getContext().getString(app.outlay.R.string.error_signin_password_match));
            result = false;
        }

        return result;
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

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private static class ClearErrorTextWatcher extends TextWatcherAdapter {
        TextInputLayout inputLayout;

        public ClearErrorTextWatcher(TextInputLayout inputLayout) {
            this.inputLayout = inputLayout;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            inputLayout.setErrorEnabled(false);
        }
    }
}
