package ru.algorithmist.sparsity.pipe;

import ru.algorithmist.sparsity.data.BasicMatrix;

/**
 * @author Sergey Edunov
 */
public abstract class AbstractParallelProcessor<I, O> extends AbstractProcessor<I, O> implements ParallelProcessor<I, O> {
    @Override
    public O train(I input) throws Exception {
        setChunks(1);
        O output = initTrain(input);
        train(input, output, 0);
        return output;
    }

    @Override
    public O process(I input) throws Exception {
        setChunks(1);
        O output = initProcess(input);
        process(input, output, 0);
        return output;
    }

    @Override
    public void cv(I input, O output, int chunk) throws Exception {
        process(input, output, chunk);
    }

    @Override
    public O initCv(I input) {
        return initProcess(input);
    }
}
