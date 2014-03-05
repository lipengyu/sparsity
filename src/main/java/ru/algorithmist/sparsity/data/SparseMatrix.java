package ru.algorithmist.sparsity.data;

import gnu.trove.map.hash.THashMap;

import java.util.Map;


/**
 * Mutable and not thread safe. Has to be fast (not yet)
 */
public final class SparseMatrix extends AbstractMatrix implements Matrix {

    private Map<Integer, SparseVector> data = new THashMap<Integer, SparseVector>();
    private int rows;
    private int cols;

    public static SparseMatrix diagonal(Vector vector){
        SparseMatrix res = new SparseMatrix();
        for(int i=0; i<vector.size(); i++) {
            res.set(i, i, vector.get(i));
        }
        return res;
    }

    @Override
    public void set(int row, int col, float value) {
        SparseVector r = this.data.get(row);
        if (r == null) {
            r = new SparseVector();
            if (row >= rows) {
                rows = row + 1;
            }
            this.data.put(row, r);
        }
        if (col >= cols) {
            cols = col + 1;
        }
        r.set(col, value);
    }

    @Override
    public float get(int row, int col) {
        SparseVector r = this.data.get(row);
        if (r == null) {
            return 0;
        }
        return r.get(col);
    }

    @Override
    public int rows() {
        return rows;
    }

    @Override
    public int cols() {
        return cols;
    }

    public Matrix dot(final Matrix o) {
        if (o instanceof SparseMatrix) {
            final SparseMatrix other = (SparseMatrix) o;
            final SparseMatrix res = new SparseMatrix();
            map(new MatrixMapper() {
                @Override
                public void map(int row, int col, float value) {
                    SparseVector r = other.get(col);
                    if (r != null) {
                        for (int newCol : r.indexes()) {
                            res.add(row, newCol, value * r.get(newCol));
                        }
                    }
                }

            });
            return res;
        } else {
            return DenseMatrix.denseDot(this, o);
        }
    }

    public void add(int row, int col, float v) {
        SparseVector r = this.data.get(row);
        if (r == null) {
            r = new SparseVector();
            if (row >= rows) {
                rows = row + 1;
            }
            this.data.put(row, r);
        }
        if (col >= cols) {
            cols = col + 1;
        }
        r.add(col, v);
    }

    @Override
    public Matrix slice(final int r0, final int c0, final int r1, final int c1) {
        if (c1 > cols() || r1 > rows() || r0 < 0 || c0 < 0 || r0 > r1 || c0 > c1){
            throw new IllegalArgumentException("Can't slice matrix " + this + " by (" + r0 + ":" + c0 + ") and (" + r1 + ":" + c1 + ")");
        }
        final Matrix res = new SparseMatrix();
        return slice(r0, c0, r1, c1, res);
    }

    public String toString() {
         return "SparseMatrix " + rows() + "x" + cols();
    }

    @Override
    public Matrix add(Matrix other) {
        SparseMatrix res = new SparseMatrix();
        add(other, res);
        return res;
    }

    @Override
    public Matrix multiply(float v) {
        return multiply(v, new SparseMatrix());
    }


    public <T, R> R mapReduce(MatrixMapReducer<T, R> callback) {
        R res = callback.reduce(null, null);
        for(Integer r : data.keySet()) {
            SparseVector row = data.get(r);
            for(int c : row.indexes()) {
                res = callback.reduce(res, callback.map(r, c, row.get(c)));
            }
        }
        return res;
    }

    @Override
    public void map(MatrixMapper callback) {
        for(Integer r : data.keySet()) {
            SparseVector row = data.get(r);
            for(int c : row.indexes()) {
                callback.map(r, c, row.get(c));
            }
        }
    }

    @Override
    public Matrix transpose() {
        return super.transpose(new SparseMatrix());
    }

    public SparseVector get(int row) {
        SparseVector res = data.get(row);
        if (res == null) {
            res = new SparseVector();
            data.put(row, res);
        }
        return res;
    }


}
