package com.outlay.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.github.clans.fab.FloatingActionButton;
import com.outlay.R;
import com.outlay.view.helper.AnimationUtils;
import com.outlay.view.helper.ViewHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class LoginForm extends RelativeLayout {
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

        fab.setOnClickListener(v -> {
            Point revealPoint = getViewCenter();
            if (signUpForm.getVisibility() == View.VISIBLE) {
                AnimationUtils.hideWithReveal(signUpForm, revealPoint);
            } else {
                AnimationUtils.showWithReveal(signUpForm, revealPoint);
            }
        });

        signInButton.setOnClickListener(v -> {
            if (signInListener != null) {
                signInListener.onSubmit("melnychuk.bogdan@gmail.com", "q1w2e3r4t5", v);
            }
        });

        signUpButton.setOnClickListener(v -> {
            if (signUpListener != null) {
                signUpListener.onSubmit("melnychuk.bogdan@gmail.com", "q1w2e3r4t5", v);
            }
        });

        forgetPassword.setOnClickListener(v -> {
            if (onPasswordForgetClick != null) {
                onPasswordForgetClick.onPasswordForget();
            }
        });
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

    private void toggleViewVisibility(View view) {
        boolean visible = view.getVisibility() == View.VISIBLE;
        view.setVisibility(visible ? View.INVISIBLE : View.VISIBLE);
    }

    private Point getViewCenter() {
        int x = signUpForm.getRight();
        int y = signUpForm.getTop() + ViewHelper.dpToPx(56);
        return new Point(x, y);
    }

    public void setProgress(boolean running) {
        fab.setIndeterminate(running);
        fab.setClickable(!running);
        signUpButton.setEnabled(!running);
    }

    public interface OnSubmitClickListener {
        void onSubmit(String email, String password, View src);
    }

    public interface OnPasswordForgetClick {
        void onPasswordForget();
    }
}
