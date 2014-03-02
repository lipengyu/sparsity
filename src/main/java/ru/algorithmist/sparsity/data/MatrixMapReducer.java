package ru.algorithmist.sparsity.data;


public interface MatrixMapReducer<T, R> {

    T map(int row, int col, float value);

    R reduce(R current, T value);

}
