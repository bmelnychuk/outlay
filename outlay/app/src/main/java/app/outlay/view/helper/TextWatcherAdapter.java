package app.outlay.view.helper;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Bogdan Melnychuk on 1/30/16.
 */
public class TextWatcherAdapter implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Empty default implementation
        // To be overriden depending on the context it is used in
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Empty default implementation
        // To be overriden depending on the context it is used in
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Empty default implementation
        // To be overriden depending on the context it is used in
    }
}
