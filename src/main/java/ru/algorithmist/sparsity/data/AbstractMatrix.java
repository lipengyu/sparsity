package ru.algorithmist.sparsity.data;

public abstract class AbstractMatrix implements Matrix{

    protected Matrix add(Matrix other,final Matrix res) {
        other.map(new MatrixMapper() {
            @Override
            public void map(int row, int col, float value) {
                float newValue = get(row, col) + value;
                res.set(row, col, newValue);
            }

        });
        return this;
    }

    protected Matrix transpose(final Matrix res) {
        map(new MatrixMapper() {
            @Override
            public void map(int row, int col, float value) {
                res.set(col, row, value);
            }
        });
        return res;
    }

    protected Matrix slice(final int r0, final int c0, final int r1, final int c1, final Matrix res) {
        map(new MatrixMapper() {
            @Override
            public void map(int row, int col, float value) {
                if (row >= r0 && row < r1 && col >= c0 && col < c1) {
                    res.set(row - r0, col - c0, value);
                }
            }

        });
        return res;
    }

    protected Matrix multiply(final float v, final Matrix res) {
        map(new MatrixMapper() {
            @Override
            public void map(int row, int col, float value) {
                res.set(row, col, value * v);
            }
        });
        return res;
    }
}
