package ru.algorithmist.sparsity.pipe;

import ru.algorithmist.sparsity.data.BasicMatrix;
import ru.algorithmist.sparsity.data.DataFrame;

/**
 * @author Sergey Edunov
 */
public class ParallelDataFrameProcessorDecorator<P> extends AbstractParallelProcessor<DataFrame<P>, DataFrame<P>> {

    private ParallelProcessor<BasicMatrix, BasicMatrix> core;

    public ParallelDataFrameProcessorDecorator(ParallelProcessor<BasicMatrix, BasicMatrix> core) {
        this.core = core;
    }

    @Override
    public void setChunks(int n) {
        core.setChunks(n);
    }

    @Override
    public DataFrame<P> initTrain(DataFrame<P> input) {
        BasicMatrix res = core.initTrain(input.getData());
        return new DataFrame<P>(input.getPredictors(), res);
    }

    @Override
    public DataFrame<P> initProcess(DataFrame<P> input) {
        BasicMatrix res = core.initProcess(input.getData());
        return new DataFrame<P>(input.getPredictors(), res);
    }

    @Override
    public void train(DataFrame<P> input, DataFrame<P> output, int chunk) throws Exception {
        core.train(input.getData(), output.getData(), chunk);
    }

    @Override
    public void process(DataFrame<P> input, DataFrame<P> output, int chunk) throws Exception {
        core.process(input.getData(), output.getData(), chunk);
    }

    @Override
    public void setContext(ProcessingContext context) {
        super.setContext(context);
        core.setContext(context);
    }
}
