package app.outlay.view.numpad;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * Created by Bogdan Melnychuk on 1/15/16.
 */
public class SimpleNumpadValidator implements NumpadValidator {
    @Override
    public boolean valid(String value) {
        if (TextUtils.isEmpty(value) || value.length() > 8) {
            return false;
        }

        if(Pattern.matches("([0-9]*)\\.([0-9]*)", value))
            return true;
        else
            return false;
    }

    @Override
    public void onInvalidInput(String value) {
        ///Input is always correct
        throw new UnsupportedOperationException();
    }
}
