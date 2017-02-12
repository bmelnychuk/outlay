package app.outlay.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.github.johnkil.print.PrintDrawable;
import com.github.johnkil.print.PrintView;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import app.outlay.domain.model.Category;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Bogdan Melnychuk on 2/2/16.
 */
public final class IconUtils {
    private static final String[] all = {
            "ic_coins",
            "ic_flight",
            "ic_airplane",
            "ic_bakery",
            "ic_beach",
            "ic_basketball",
            "ic_front",
            "ic_flamenco",
            "ic_fast_food",
            "ic_subscribe",
            "ic_t_shirt",
            "ic_md",
            "ic_map",
            "ic_medical",
            "ic_flower",
            "ic_fuel",
            "ic_money",
            "ic_tea",
            "ic_tag",
            "ic_mobilephone",
            "ic_telephone",
            "ic_multiple",
            "ic_hairsalon",
            "ic_books",
            "ic_bowling",
            "ic_hand",
            "ic_note",
            "ic_tool",
            "ic_tools",
            "ic_people",
            "ic_hardbound",
            "ic_box",
            "ic_cars",
            "ic_healthcare",
            "ic_photo",
            "ic_two",
            "ic_weightlifting",
            "ic_poor",
            "ic_heart",
            "ic_chart",
            "ic_christmas",
            "ic_insurance",
            "ic_rent",
            "ic_restaurant",
            "ic_horse",
            "ic_cigarette",
            "ic_cleaning",
            "ic_house",
            "ic_run",
            "ic_scissors",
            "ic_italian_food",
            "ic_justice",
            "ic_climbing",
            "ic_screen",
            "ic_shopping",
            "ic_legal",
            "ic_controller",
            "ic_dog",
            "ic_lifeline",
            "ic_sofa",
            "ic_sportive",
            "ic_locked",
            "ic_donation",
            "ic_draw",
            "ic_luggage",
            "ic_makeup",
            "ic_ducks"
    };

    public static List<String> getAll() {
        return Arrays.asList(all);
    }

    public static void loadCategoryIcon(Category category, PrintView printView) {
        loadCategoryIcon(category.getIcon(), printView);
        printView.setIconColor(category.getColor());
    }

    public static Drawable getToolbarIcon(Context context, IIcon icon) {
        return new IconicsDrawable(context)
                .icon(icon)
                .color(Color.WHITE)
                .sizeDp(24);
    }

    public static Drawable getIconMaterialIcon(Context context, IIcon icon, int color, int sizeRes) {
        return new IconicsDrawable(context)
                .icon(icon)
                .color(color)
                .sizeRes(sizeRes);
    }

    public static Drawable getIconMaterialIcon(Context context, IIcon icon, int color, int sizeRes, int paddingDp) {
        return new IconicsDrawable(context)
                .icon(icon)
                .color(color)
                .paddingDp(paddingDp)
                .sizeRes(sizeRes);
    }

    public static Drawable getToolbarIcon(Context context, IIcon icon, int paddingDp) {
        return new IconicsDrawable(context)
                .icon(icon)
                .paddingDp(paddingDp)
                .color(Color.WHITE)
                .sizeDp(24);
    }

    public static void loadCategoryIcon(String icon, PrintView printView) {
        printView.setIconFont("fonts/font-outlay.ttf");
        printView.setIconCodeRes(ResourceUtils.getIntegerResource(printView.getContext(), icon));
    }

    public static Drawable getCategoryIcon(Context context, int codeResId, int color, int sizeRes) {
        return new PrintDrawable.Builder(context)
                .iconCodeRes(codeResId)
                .iconFont("fonts/font-outlay.ttf")
                .iconColor(color)
                .iconSizeRes(sizeRes)
                .build();
    }
}
