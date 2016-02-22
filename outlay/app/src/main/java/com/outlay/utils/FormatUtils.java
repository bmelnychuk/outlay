package com.outlay.utils;

/**
 * Created by Bogdan Melnychuk on 2/10/16.
 */
public final class FormatUtils {
    public static String formatAmount(Double amount) {
        return String.format("%.2f", amount);
    }
}
