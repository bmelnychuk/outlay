package app.outlay.view.alert;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

/**
 * Created by Bogdan Melnychuk on 2/6/16.
 */
public final class Alert {
    public static void info(View view, String message) {
        info(view, message, null);
    }

    public static void info(View view, String message, View.OnClickListener clickListener) {
        Context context = view.getContext();
        Snackbar bar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        if (clickListener != null) {
            bar.setAction(context.getString(app.outlay.R.string.label_undo), clickListener);
            bar.setActionTextColor(ContextCompat.getColor(context, app.outlay.R.color.red));
        }
        bar.show();
    }

    public static void error(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .show();
    }
}
