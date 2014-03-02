package ru.algorithmist.sparsity.utils;


import cern.colt.function.IntIntDoubleFunction;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import ru.algorithmist.sparsity.data.DenseMatrix;
import ru.algorithmist.sparsity.data.Matrix;
import ru.algorithmist.sparsity.data.MatrixMapper;
import ru.algorithmist.sparsity.data.SparseMatrix;

public class ColtUtils {

    public static DoubleMatrix2D matrix(Matrix matrix) {
        final DoubleMatrix2D res = (matrix instanceof SparseMatrix) ? new SparseDoubleMatrix2D(matrix.rows(), matrix.cols()) : new DenseDoubleMatrix2D(matrix.rows(), matrix.cols());
        matrix.map(new MatrixMapper() {
            @Override
            public void map(int row, int col, float value) {
                res.set(row, col, value);
            }
        });
        return res;
    }

    public static Matrix matrix(DoubleMatrix2D matrix) {
        if (matrix instanceof SparseDoubleMatrix2D) {
            final Matrix res = new SparseMatrix();
            matrix.forEachNonZero(new IntIntDoubleFunction() {
                @Override
                public double apply(int r, int c, double v) {
                    res.set(r, c, (float) v);
                    return v;
                }
            });
            return res;
        } else {
            final  Matrix res = new DenseMatrix(matrix.rows(), matrix.columns());
            for(int r=0; r<matrix.rows(); r++){
                for(int c=0; c<matrix.columns(); c++) {
                    res.set(r, c, (float) matrix.get(r, c));
                }
            }
            return res;
        }
    }
}
