package app.outlay.utils;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import com.github.johnkil.print.PrintDrawable;

import app.outlay.R;

/**
 * Created by bmelnychuk on 5/18/17.
 */

public class ResourceManager {
    private Activity context;
    private boolean lightTheme = false;

    private int activeIconColor;
    private int primaryTextColor;
    private int secondaryTextColor;
    private int inactiveIconColor;
    private int backgroundDarkColor;

    public ResourceManager(Activity context) {
        this.context = context;
        resolveThemeAttributes();
    }

    private void resolveThemeAttributes() {

        int[] textSizeAttr = new int[]{
                R.attr.activeIconColor,
                R.attr.textColorPrimary,
                R.attr.inactiveIconColor,
                R.attr.textColorSecondary,
                R.attr.backgroundDarkColor
        };
        TypedArray a = context.obtainStyledAttributes(lightTheme ? R.style.Theme_Outlay_Light : R.style.Theme_Outlay_Dark, textSizeAttr);
        activeIconColor = a.getColor(0, -1);
        primaryTextColor = a.getColor(1, -1);
        inactiveIconColor = a.getColor(2, -1);
        secondaryTextColor = a.getColor(3, -1);
        backgroundDarkColor = a.getColor(4, -1);
        a.recycle();
    }

    public int getInactiveIconColor() {
        return inactiveIconColor;
    }

    public int getSecondaryTextColor() {
        return secondaryTextColor;
    }

    public int getBackgroundDarkColor() {
        return backgroundDarkColor;
    }

    public Drawable getMaterialToolbarIcon(int iconResId) {
        return new PrintDrawable.Builder(context)
                .iconTextRes(iconResId)
                .iconFont("fonts/material-icon-font.ttf")
                .iconColor(activeIconColor)
                .iconSizeRes(app.outlay.R.dimen.toolbar_icon_size)
                .build();
    }

    public Drawable getCustomToolbarIcon(int codeResId) {
        return new PrintDrawable.Builder(context)
                .iconCodeRes(codeResId)
                .iconFont("fonts/font-outlay.ttf")
                .iconColor(activeIconColor)
                .iconSizeRes(app.outlay.R.dimen.toolbar_icon_size)
                .build();
    }
}
