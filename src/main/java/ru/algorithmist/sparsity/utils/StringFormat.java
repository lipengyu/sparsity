package ru.algorithmist.sparsity.utils;

import java.text.DecimalFormat;

/**
 * @author Sergey Edunov
 */
public class StringFormat {

    private static final DecimalFormat DF = new DecimalFormat("#.##");

    public static String format(double v) {
        return DF.format(v);
    }

    public static String formatTime(long time) {
        time /= 1000;
        String seconds = Long.toString(time % 60) + "s";
        if (time < 60) {
            return seconds;
        }
        String minutes = Long.toString((time % 3600) / 60) + "m ";
        if (time < 3600) {
            return minutes + seconds;
        }

        String hours = Long.toString((time % 86400) / 3600) + "h ";
        if (time < 86400) {
            return hours + minutes + seconds;
        }

        String days = Long.toString(time / 86400) + "d ";
        return days + hours + minutes + seconds;
    }
}
