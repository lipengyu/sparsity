package ru.algorithmist.sparsity.pipe;

/**
 * @author Sergey Edunov
 */
public interface Processor<I,O> {

    public O train(I input) throws Exception;

    public O process(I input) throws Exception;

    public void setContext(ProcessingContext context);

}
