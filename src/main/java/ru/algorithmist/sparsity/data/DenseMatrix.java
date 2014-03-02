package ru.algorithmist.sparsity.data;


public class DenseMatrix extends AbstractMatrix {

    private final float [][] data;

    public DenseMatrix(int rows, int cols) {
        data = new float[rows][cols];
    }

    public static DenseMatrix diagonal(Vector vector){
        DenseMatrix res = new DenseMatrix(vector.size(), vector.size());
        for(int i=0; i<vector.size(); i++) {
            res.set(i, i, vector.get(i));
        }
        return res;
    }

    @Override
    public void set(int row, int col, float value) {
        data[row][col] = value;
    }

    @Override
    public float get(int row, int col) {
        return data[row][col];
    }

    @Override
    public Vector get(int row) {
        return new DenseVector(data[row]);
    }

    @Override
    public int rows() {
        return data.length;
    }

    @Override
    public int cols() {
        return data[0].length;
    }

    @Override
    public Matrix add(Matrix other) {
        DenseMatrix res = new DenseMatrix(rows(), cols());
        add(other, res);
        return res;
    }

    @Override
    public Matrix multiply(float v) {
        return multiply(v, new DenseMatrix(rows(), cols()));
    }

    @Override
    public <T, R> R mapReduce(MatrixMapReducer<T, R> callback) {
        R res = null;
        for(int r = 0; r < rows(); r++) {
            for(int c=0; c<cols(); c++) {
                res = callback.reduce(res, callback.map(r, c, data[r][c]));
            }
        }
        return res;
    }

    @Override
    public void map(MatrixMapper callback) {
        for(int r = 0; r < rows(); r++) {
            for(int c=0; c<cols(); c++) {
                callback.map(r, c, data[r][c]);
            }
        }
    }

    @Override
    public Matrix transpose() {
        return super.transpose(new DenseMatrix(cols(), rows()));
    }

    @Override
    public Matrix dot(final Matrix other) {
        return denseDot(this, other);
    }

    @Override
    public void add(int row, int col, float value) {
        data[row][col] += value;
    }

    @Override
    public Matrix slice(final int r0, final int c0, final int r1, final int c1) {
        if (c1 > cols() || r1 > rows() || r0 < 0 || c0 < 0 || r0 > r1 || c0 > c1){
            throw new IllegalArgumentException("Can't slice matrix " + this + "  by (" + r0 + ":" + c0 + ") and (" + r1 + ":" + c1 + ")");
        }
        final Matrix res = new DenseMatrix(r1-r0, c1-c0);
        return slice(r0, c0, r1, c1, res);
    }

    public String toString() {
        return "DenseMatrix " + rows() + "x" + cols();
    }


    static Matrix denseDot(final Matrix a, final Matrix b) {
        if (a.cols() != b.rows()){
            throw new IllegalArgumentException("Can not multiply matrices " + a.rows() + "x" + a.cols() + " by " + b.rows() + "x" + b.cols());
        }
        final DenseMatrix res = new DenseMatrix(a.rows(), b.cols());
        a.map(new MatrixMapper() {
            @Override
            public void map(int row, int col, float value) {
                for (int nc = 0; nc < b.cols(); nc++) {
                    res.add(row, nc, b.get(col, nc) * value);
                }
            }

        });
        return res;
    }

}
