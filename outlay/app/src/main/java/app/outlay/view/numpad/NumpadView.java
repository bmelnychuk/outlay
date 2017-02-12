package app.outlay.view.numpad;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Bogdan Melnychuk on 1/15/16.
 */
public class NumpadView extends LinearLayout implements View.OnClickListener {
    public interface NumpadClickListener {
        boolean onNumberClicked(int value);

        boolean onClearClicked();

        boolean onDecimalClicked();

        boolean onClearLongClicked();
    }

    private NumpadClickListener numpadClickListener;
    private NumpadEditable attachedEditable;
    private NumpadValidator validator;

    public NumpadView(Context context) {
        super(context);
        init();
    }

    public NumpadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NumpadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NumpadView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View parent = inflater.inflate(app.outlay.R.layout.view_numpad, this, true);

        parent.findViewById(app.outlay.R.id.btn1).setOnClickListener(this);
        parent.findViewById(app.outlay.R.id.btn2).setOnClickListener(this);
        parent.findViewById(app.outlay.R.id.btn3).setOnClickListener(this);
        parent.findViewById(app.outlay.R.id.btn4).setOnClickListener(this);
        parent.findViewById(app.outlay.R.id.btn5).setOnClickListener(this);
        parent.findViewById(app.outlay.R.id.btn6).setOnClickListener(this);
        parent.findViewById(app.outlay.R.id.btn7).setOnClickListener(this);
        parent.findViewById(app.outlay.R.id.btn8).setOnClickListener(this);
        parent.findViewById(app.outlay.R.id.btn9).setOnClickListener(this);
        parent.findViewById(app.outlay.R.id.btn0).setOnClickListener(this);
        parent.findViewById(app.outlay.R.id.btnDecimal).setOnClickListener(this);
        View clearButton = parent.findViewById(app.outlay.R.id.btnClear);
        clearButton.setOnClickListener(this);
        clearButton.setOnLongClickListener(v -> {
            if (numpadClickListener != null) {
                numpadClickListener.onClearLongClicked();
            }
            if (attachedEditable != null) {
                attachedEditable.setText("");
            }
            return true;
        });
    }

    public void setNumpadClickListener(NumpadClickListener numpadClickListener) {
        this.numpadClickListener = numpadClickListener;
    }

    public void attachEditable(NumpadEditable attachedEditable, NumpadValidator validator) {
        this.attachedEditable = attachedEditable;
        this.validator = validator;
    }

    public void attachEditable(NumpadEditable attachedEditable) {
        attachEditable(attachedEditable, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.outlay.R.id.btn0:
                onNumberClicked(0);
                break;
            case app.outlay.R.id.btn1:
                onNumberClicked(1);
                break;
            case app.outlay.R.id.btn2:
                onNumberClicked(2);
                break;
            case app.outlay.R.id.btn3:
                onNumberClicked(3);
                break;
            case app.outlay.R.id.btn4:
                onNumberClicked(4);
                break;
            case app.outlay.R.id.btn5:
                onNumberClicked(5);
                break;
            case app.outlay.R.id.btn6:
                onNumberClicked(6);
                break;
            case app.outlay.R.id.btn7:
                onNumberClicked(7);
                break;
            case app.outlay.R.id.btn8:
                onNumberClicked(8);
                break;
            case app.outlay.R.id.btn9:
                onNumberClicked(9);
                break;
            case app.outlay.R.id.btnClear:
                onClearClicked();
                break;
            case app.outlay.R.id.btnDecimal:
                onDecimalCLicked();
                break;
        }
    }

    private void onNumberClicked(int i) {
        if (numpadClickListener != null) {
            numpadClickListener.onNumberClicked(i);
        }
        if (attachedEditable != null) {
            String textBefore = attachedEditable.getText();
            updateAttachedView(textBefore + i);
        }
    }

    private void onDecimalCLicked() {
        if (numpadClickListener != null) {
            numpadClickListener.onDecimalClicked();
        }
        if (attachedEditable != null) {
            String textBefore = attachedEditable.getText();
            updateAttachedView(textBefore + ".");
        }
    }

    private void onClearClicked() {
        if (numpadClickListener != null) {
            numpadClickListener.onClearClicked();
        }
        if (attachedEditable != null) {
            String textBefore = attachedEditable.getText();

            if (textBefore.length() > 0) {
                String textAfter = textBefore.substring(0, textBefore.length() - 1);
                updateAttachedView(textAfter, false);
            }
        }
    }

    private void updateAttachedView(String str) {
        this.updateAttachedView(str, true);
    }

    private void updateAttachedView(String str, boolean useValidator) {
        if(useValidator && validator != null) {
            if (validator.valid(str)) {
                attachedEditable.setText(str);
            } else {
                validator.onInvalidInput(str);
            }
        } else {
            attachedEditable.setText(str);
        }
    }
}
