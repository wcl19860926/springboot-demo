package com.study.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by xiquee.com on 12/21/16.
 * <p>
 * BigDecimal.setScale()方法用于格式化小数点
 * <p>
 * setScale(1)表示保留一位小数，默认用四舍五入方式
 * setScale(1,BigDecimal.ROUND_DOWN)直接删除多余的小数位，如2.35会变成2.3
 * setScale(1,BigDecimal.ROUND_UP)进位处理，2.35变成2.4
 * setScale(1,BigDecimal.ROUND_HALF_UP)四舍五入，2.35变成2.4
 * setScaler(1,BigDecimal.ROUND_HALF_DOWN)四舍五入，2.35变成2.3，如果是5则向下舍
 * setScaler(1,BigDecimal.ROUND_CEILING)接近正无穷大的舍入
 * setScaler(1,BigDecimal.ROUND_FLOOR)接近负无穷大的舍入，数字>0和ROUND_UP作用一样，数字<0和ROUND_DOWN作用一样
 * setScaler(1,BigDecimal.ROUND_HALF_EVEN)向最接近的数字舍入，如果与两个相邻数字的距离相等，则向相邻的偶数舍入。
 */
public class DecimalUtils {

    public static boolean isBlank(BigDecimal bigDecimal) {
        boolean bOk = false;
        if (bigDecimal == null || bigDecimal.floatValue() <= 0) {
            bOk = true;
        }
        return bOk;
    }

    public static boolean isNotBlank(BigDecimal bigDecimal) {
        return !isBlank(bigDecimal);
    }


    public static String format(double d) {
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(d);
    }

    public static BigDecimal format(BigDecimal b) {
        BigDecimal bigDecimal = b.setScale(BigDecimal.ROUND_CEILING, RoundingMode.HALF_UP);
        return bigDecimal;
    }
}
