package ru.algorithmist.sparsity.data;

/**
 * @author Sergey Edunov
 */
public interface Transformer<I, O> {

    public O transform(I input);

}
