package ru.algorithmist.sparsity.pipe;

import ru.algorithmist.sparsity.data.BasicMatrix;
import ru.algorithmist.sparsity.data.DataFrame;
import ru.algorithmist.sparsity.data.SparseMatrix;

/**
 * @author Sergey Edunov
 */
public class DataFrameProcessorDecorator<P> extends AbstractProcessor<DataFrame<P>, DataFrame<P>>{

    private Processor<BasicMatrix, BasicMatrix> core;

    public DataFrameProcessorDecorator(Processor<BasicMatrix, BasicMatrix> core){
        this.core = core;
    }

    @Override
    public DataFrame<P> train(DataFrame<P> input) throws Exception {
        BasicMatrix res = core.train(input.getData());
        return new DataFrame<P>(input.getPredictors(), res);
    }

    @Override
    public DataFrame<P> process(DataFrame<P> input) throws Exception {
        BasicMatrix res = core.process(input.getData());
        return new DataFrame<P>(input.getPredictors(), res);
    }
}
