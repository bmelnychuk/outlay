package app.outlay.view.helper;

import android.content.res.Resources;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class ViewHelper {
    public static int dpToPx(int dp) {
        return Math.round(dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return Math.round(px / Resources.getSystem().getDisplayMetrics().density);
    }
}
