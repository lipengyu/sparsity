package ru.algorithmist.sparsity.utils;


import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import ru.algorithmist.sparsity.data.DenseMatrix;
import ru.algorithmist.sparsity.data.Matrix;
import ru.algorithmist.sparsity.data.MatrixMapper;

public class CommonsUtils {

    public static RealMatrix matrix(Matrix matrix) {
        final RealMatrix res = new Array2DRowRealMatrix(matrix.rows(), matrix.cols());
        matrix.map(new MatrixMapper() {
            @Override
            public void map(int row, int col, float value) {
                res.setEntry(row, col, value);
            }

        });
        return res;
    }

    public static Matrix matrix(RealMatrix matrix) {
        Matrix res = new DenseMatrix(matrix.getRowDimension(), matrix.getColumnDimension());
        for(int r=0; r<matrix.getRowDimension(); r++) {
            for(int c=0; c<matrix.getColumnDimension(); c++) {
                res.set(r, c, (float) matrix.getEntry(r, c));
            }
        }
        return res;
    }
}
