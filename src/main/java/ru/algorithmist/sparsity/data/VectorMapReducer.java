package ru.algorithmist.sparsity.data;

/**
 * @author Sergey Edunov
 */
public interface VectorMapReducer<T, R> {

    T map(int index, float value);

    R reduce(R current, T value);

}
