package ru.algorithmist.sparsity.algorithms;

import org.junit.Test;
import ru.algorithmist.sparsity.data.Matrix;
import ru.algorithmist.sparsity.data.MatrixMapReducer;
import ru.algorithmist.sparsity.data.SparseMatrix;
import ru.algorithmist.sparsity.data.Vector;

import java.util.Random;

import static junit.framework.Assert.*;

public class RandomizedSVDTest {

    private static final Random RND = new Random();

    @Test
    public void testSVD() {
        SparseMatrix matrix = new SparseMatrix();
        for(int i=0; i<5000; i++) {
            for(int j=0; j<50; j++){
                if (j % 2 == 0) {
                    matrix.set(i, j, (float) (Math.sin(i) + RND.nextDouble()*0.01));
                } else {
                    matrix.set(i, j, (float) (Math.sin(i*2) + RND.nextDouble()*0.01));
                }
            }
        }

        Vector[] res = new Vector[1];
        for(int i=0; i<res.length; i++) {
            RandomizedSVD svd = new RandomizedSVD(matrix, 50, i);
            res[i] = svd.getS();

            assertEquals(50, res[i].size());

            Matrix reconstr = svd.getU().dot(SparseMatrix.diagonal(res[i])).dot(svd.getV().transpose());

            Matrix delta = matrix.add(reconstr.multiply(-1));
            float residuals = delta.mapReduce(new MatrixMapReducer<Float, Float>() {
                @Override
                public Float map(int row, int col, float value) {
                   return value*value;
                }

                @Override
                public Float reduce(Float current, Float value) {
                    if (current == null) {
                        current = 0f;
                    }
                    return  current + value;
                }
            });
            assertTrue("Fail, residuals too large " + residuals + " on iter " + i, residuals < 1e-5);
        }
    }
}
