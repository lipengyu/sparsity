package ru.algorithmist.sparsity.utils;

import java.text.DecimalFormat;

/**
 * @author Sergey Edunov
 */
public class StringFormat {

    private static final DecimalFormat DF = new DecimalFormat("#.##");

    public static String format(double v){
        return DF.format(v);
    }
}
