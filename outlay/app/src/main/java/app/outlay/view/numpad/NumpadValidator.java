package app.outlay.view.numpad;

/**
 * Created by Bogdan Melnychuk on 1/15/16.
 */
public interface NumpadValidator {
    boolean valid(String value);

    void onInvalidInput(String value);
}
