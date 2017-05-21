package app.outlay.view;

import android.app.Activity;
import android.content.res.TypedArray;

import app.outlay.R;

/**
 * Created by bmelnychuk on 5/21/17.
 */

public class OutlayTheme {
    public final int activeIconColor;
    public final int inactiveIconColor;

    public final int primaryTextColor;
    public final int secondaryTextColor;

    public final int backgroundDarkColor;
    public final int backgroundColor;


    public OutlayTheme(
            Activity context,
            int themeId
    ) {
        int[] textSizeAttr = new int[]{
                R.attr.activeIconColor,
                R.attr.textColorPrimary,
                R.attr.inactiveIconColor,
                R.attr.textColorSecondary,
                R.attr.backgroundDarkColor,
                R.attr.backgroundColor
        };
        TypedArray a = context.obtainStyledAttributes(themeId, textSizeAttr);
        activeIconColor = a.getColor(0, -1);
        primaryTextColor = a.getColor(1, -1);
        inactiveIconColor = a.getColor(2, -1);
        secondaryTextColor = a.getColor(3, -1);
        backgroundDarkColor = a.getColor(4, -1);
        backgroundColor = a.getColor(5, -1);
        a.recycle();
    }
}
