package app.outlay.core.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Bogdan Melnychuk on 2/10/16.
 */
public final class NumberUtils {
    public static String formatAmount(Double amount) {
        return String.format("%.2f", amount);
    }

    public static String formatAmount(BigDecimal amount) {
        return new DecimalFormat("#0.00").format(amount);
    }
}
