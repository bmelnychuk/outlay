package app.outlay.view.progress;

/**
 * Created by Bogdan Melnychuk on 1/31/16.
 */
public interface ProgressLayoutListener {
    void onProgressCompleted();
    void onProgressChanged(int seconds);
}
