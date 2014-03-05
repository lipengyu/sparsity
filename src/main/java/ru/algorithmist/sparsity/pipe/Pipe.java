package ru.algorithmist.sparsity.pipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Sergey Edunov
 */
public class Pipe <I , O> implements Processor<I, O> {

    private List<Processor> processors;
    private ProcessingContext context;

    public Pipe(Processor<I, O> processor){
        processors = new ArrayList<Processor>();
        processors.add(processor);
        context = new ProcessingContext();
    }

    private Pipe(List<Processor> processors){
        this.processors = processors;
        context = new ProcessingContext();
    }

    public <O1> Pipe<I, O1> add(Processor<O, O1> processor){
        List<Processor> pr = new ArrayList<Processor>();
        pr.addAll(processors);
        pr.add(processor);
        return new Pipe<I, O1>(pr);
    }

    @Override
    public O train(I input) throws Exception {
        Object i = input;
        for(final Processor p : processors){
            p.setContext(context);
            context.getProgressReporter().reportProgress(p.getClass().getSimpleName() + " train: ", 0);
            long t0 = System.currentTimeMillis();
            i = p.train(i);
            long t1 = System.currentTimeMillis();
            context.getProgressReporter().reportProgress(p.getClass().getSimpleName() + " train complete. Time spent " + (t1-t0), 1);
        }
        return (O) i;
    }

    @Override
    public O process(I input) throws Exception {
        Object i = input;
        for(Processor p : processors){
            p.setContext(context);
            context.getProgressReporter().reportProgress(p.getClass().getSimpleName() + " process: ", 0);
            long t0 = System.currentTimeMillis();
            i = p.process(i);
            long t1 = System.currentTimeMillis();
            context.getProgressReporter().reportProgress(p.getClass().getSimpleName() + " process complete. Time spent " + (t1-t0), 1);
        }
        return (O) i;
    }

    @Override
    public O cv(I input) throws Exception {
        Object i = input;
        for(Processor p : processors){
            p.setContext(context);
            context.getProgressReporter().reportProgress(p.getClass().getSimpleName() + " cv: ", 0);
            long t0 = System.currentTimeMillis();
            i = p.cv(i);
            long t1 = System.currentTimeMillis();
            context.getProgressReporter().reportProgress(p.getClass().getSimpleName() + " cv complete. Time spent " + (t1-t0), 1);
        }
        return (O) i;
    }

    @Override
    public void setContext(ProcessingContext context) {
        this.context = context;
    }

    public ProcessingContext getContext() {
        return context;
    }

    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("Pipe [");
        for(Processor p : processors) {
            res.append(p.toString());
        }
        res.append("]");
        return res.toString();
    }
}
