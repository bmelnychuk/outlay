package app.outlay.view.numpad;

import android.text.TextUtils;

/**
 * Created by Bogdan Melnychuk on 1/15/16.
 */
public class SimpleNumpadValidator implements NumpadValidator {
    @Override
    public boolean valid(String value) {
        if (TextUtils.isEmpty(value) || value.length() > 8) {
            return false;
        }
        try {
            Double.valueOf(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onInvalidInput(String value) {

    }
}
