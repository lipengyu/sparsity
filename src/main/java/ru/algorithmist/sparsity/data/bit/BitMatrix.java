package ru.algorithmist.sparsity.data.bit;

import ru.algorithmist.sparsity.data.BasicMatrix;
import ru.algorithmist.sparsity.data.MatrixMapper;
import ru.algorithmist.sparsity.data.Vector;
import ru.algorithmist.sparsity.data.VectorMapper;

/**
 * @author Sergey Edunov
 */
public class BitMatrix implements BasicMatrix {

    private BitVector[] data;

    public BitMatrix(int rows) {
        this.data = new BitVector[rows];
        for(int i = 0; i<rows; i++) {
            data[i] = new BitVector();
        }
    }

    public BitMatrix(int rows, int cols) {
        this.data = new BitVector[rows];
        for(int i = 0; i<rows; i++) {
            data[i] = new BitVector(cols);
        }
    }

    @Override
    public void set(int row, int col, float value) {
        BitVector v = data[row];
        v.set(col, value);
    }

    @Override
    public float get(int row, int col) {
        return data[row].get(col);
    }

    @Override
    public Vector get(int row) {
        return data[row];
    }

    @Override
    public int rows() {
        return data.length;
    }

    @Override
    public void map(final MatrixMapper matrixMapper) {
        MatrixToVectorMapper mapper = new MatrixToVectorMapper(matrixMapper);
        for (int i = 0; i<data.length; i++) {
            BitVector row = data[i];
            mapper.row = i;
            if (row != null) {
                row.map(mapper);
            }
        }
    }

    private static class MatrixToVectorMapper implements VectorMapper {
        private int row;
        private MatrixMapper matrixMapper;

        private MatrixToVectorMapper(MatrixMapper matrixMapper) {
            this.matrixMapper = matrixMapper;
        }

        @Override
        public void map(int index, float value) {
            matrixMapper.map(row, index, value);
        }
    }
}
