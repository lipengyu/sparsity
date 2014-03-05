package ru.algorithmist.sparsity.utils;


import ru.algorithmist.sparsity.data.DenseMatrix;
import ru.algorithmist.sparsity.data.Matrix;

import java.util.Random;

public class RandomUtils {

    private static Random RND = new Random();

    public static Matrix normal(int rows, int cols) {
        Matrix res = new DenseMatrix(rows, cols);
        for(int r=0; r<rows; r++) {
            for(int c=0; c<cols; c++) {
                res.set(r, c, (float) RND.nextGaussian());
            }
        }
        return res;
    }

    public static int nextInt(int size) {
        return RND.nextInt(size);
    }

    public static double nextDouble() {
        return RND.nextDouble();
    }

    public static double nextGaussian() {
        return RND.nextGaussian();
    }
}
