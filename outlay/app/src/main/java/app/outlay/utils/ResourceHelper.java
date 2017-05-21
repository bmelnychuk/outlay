package app.outlay.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.github.johnkil.print.PrintDrawable;

import app.outlay.view.OutlayTheme;

/**
 * Created by bmelnychuk on 5/18/17.
 */

public class ResourceHelper {
    private Activity context;
    private OutlayTheme theme;

    public ResourceHelper(
            OutlayTheme theme,
            Activity context
    ) {
        this.theme = theme;
        this.context = context;
    }

    public Drawable getMaterialToolbarIcon(int iconResId) {
        return new PrintDrawable.Builder(context)
                .iconTextRes(iconResId)
                .iconFont("fonts/material-icon-font.ttf")
                .iconColor(theme.activeIconColor)
                .iconSizeRes(app.outlay.R.dimen.toolbar_icon_size)
                .build();
    }

    public Drawable getCustomToolbarIcon(int codeResId) {
        return new PrintDrawable.Builder(context)
                .iconCodeRes(codeResId)
                .iconFont("fonts/font-outlay.ttf")
                .iconColor(theme.activeIconColor)
                .iconSizeRes(app.outlay.R.dimen.toolbar_icon_size)
                .build();
    }

    public Drawable getFabIcon(int iconResId) {
        return new PrintDrawable.Builder(context)
                .iconTextRes(iconResId)
                .iconFont("fonts/material-icon-font.ttf")
                .iconColorRes(android.R.color.white)
                .iconSizeRes(app.outlay.R.dimen.toolbar_icon_size)
                .build();
    }

    public int getResourceId(String pVariableName, String pResourcename) {
        try {
            return context.getResources().getIdentifier(pVariableName, pResourcename, context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getStringResource(String stringResName) {
        return getResourceId(stringResName, "string");
    }

    public int getIntegerResource(String stringResName) {
        return getResourceId(stringResName, "integer");
    }

    public static int getIntegerResource(Context context, String stringResName) {
        try {
            return context.getResources().getIdentifier(stringResName, "integer", context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
