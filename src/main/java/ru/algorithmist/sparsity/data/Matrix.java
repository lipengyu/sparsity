package ru.algorithmist.sparsity.data;


public interface Matrix extends BasicMatrix {
    int rows();

    int cols();

    Matrix add(Matrix other);

    Matrix multiply(float v);

    <T, R> R mapReduce(MatrixMapReducer<T, R> callback);

    Matrix transpose();

    Matrix dot(Matrix r);

    void add(int row, int col, float value);

    Matrix slice(int r0, int c0, int r1, int c1);
}
