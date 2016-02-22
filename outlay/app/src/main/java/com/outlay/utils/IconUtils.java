package com.outlay.utils;

import com.github.johnkil.print.PrintView;
import com.outlay.dao.Category;

/**
 * Created by Bogdan Melnychuk on 2/2/16.
 */
public final class IconUtils {
    public static void loadCategoryIcon(Category category, PrintView printView) {
        loadCategoryIcon(category.getIcon(), printView);
        printView.setIconColor(category.getColor());
    }

    public static void loadCategoryIcon(String icon, PrintView printView) {
        printView.setIconFont("fonts/font-outlay.ttf");
        printView.setIconCodeRes(ResourceUtils.getIntegerResource(printView.getContext(), icon));
    }
}
