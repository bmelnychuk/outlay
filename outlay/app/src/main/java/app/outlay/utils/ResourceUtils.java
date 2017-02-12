package app.outlay.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.github.johnkil.print.PrintDrawable;

import java.util.Random;

/**
 * Created by Bogdan Melnychuk on 1/17/16.
 */
public final class ResourceUtils {
    public static int getResourceId(Context context, String pVariableName, String pResourcename) {
        try {
            return context.getResources().getIdentifier(pVariableName, pResourcename, context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int getStringResource(Context context, String stringResName) {
        return getResourceId(context, stringResName, "string");
    }

    public static int getIntegerResource(Context context, String stringResName) {
        return getResourceId(context, stringResName, "integer");
    }

    public static int randomColor(Context context, int seed) {
        int[] colors = context.getResources().getIntArray(app.outlay.R.array.categoryColors);
        Random r = new Random();
        r.setSeed(seed);
        int colorIndex = r.nextInt(colors.length);
        return colors[colorIndex];
    }

    public static Drawable getMaterialToolbarIcon(Context context, int iconResId) {
        return new PrintDrawable.Builder(context)
                .iconTextRes(iconResId)
                .iconFont("fonts/material-icon-font.ttf")
                .iconColorRes(android.R.color.white)
                .iconSizeRes(app.outlay.R.dimen.toolbar_icon_size)
                .build();
    }

    public static Drawable getCustomToolbarIcon(Context context, int codeResId) {
        return new PrintDrawable.Builder(context)
                .iconCodeRes(codeResId)
                .iconFont("fonts/font-outlay.ttf")
                .iconColorRes(android.R.color.white)
                .iconSizeRes(app.outlay.R.dimen.toolbar_icon_size)
                .build();
    }
}
