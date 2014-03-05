package ru.algorithmist.sparsity.utils;

/**
 * @author Sergey Edunov
 */
public class NumberUtils {

    private static final float EPSILON = 1e-4f;

    public static boolean equal(float a, float b){
        return Math.abs(a - b) < EPSILON;
    }

    public static boolean isZero(float a){
        return Math.abs(a) < EPSILON;
    }
}
