package ru.algorithmist.sparsity.data;

/**
 * @author Sergey Edunov
 */
public interface BasicMatrix {

    void set(int row, int col, float value);

    float get(int row, int col);

    Vector get(int row);

    int rows();

    void map(MatrixMapper matrixMapper);
}
