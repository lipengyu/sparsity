package ru.algorithmist.sparsity.pipe;

/**
 * @author Sergey Edunov
 */
public abstract class AbstractProcessor<I, O> implements Processor<I, O> {

    private ProcessingContext context;

    @Override
    public void setContext(ProcessingContext context) {
        this.context = context;
    }

    protected ProcessingContext getContext() {
        return context;
    }

    @Override
    public O cv(I input) throws Exception {
        return process(input);
    }
}
