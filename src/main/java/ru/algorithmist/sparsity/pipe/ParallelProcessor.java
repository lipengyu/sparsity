package ru.algorithmist.sparsity.pipe;

/**
 * @author Sergey Edunov
 */
public interface ParallelProcessor<I,O> extends Processor<I,O> {

    public void setChunks(int n);

    public O initTrain(I input);

    public O initProcess(I input);

    public O initCv(I input);

    public void train(I input, O output, int chunk) throws Exception;

    public void process(I input, O output, int chunk) throws Exception;

    public void cv(I input, O output, int chunk) throws Exception;

}
