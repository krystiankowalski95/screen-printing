package pl.lodz.it.sitodruk.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberFormatterUtil {

    public static double formatDecimal(double value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
}
