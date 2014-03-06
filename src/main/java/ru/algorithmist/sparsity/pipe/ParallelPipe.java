package ru.algorithmist.sparsity.pipe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * @author Sergey Edunov
 */
public class ParallelPipe<I , O> implements Processor<I, O> {

    private List<Processor> processors;
    private ProcessingContext context;
    private ExecutorService executorService;
    private int cores;

    public ParallelPipe(int cores, Processor<I, O> processor){
        processors = new ArrayList<Processor>();
        processors.add(processor);
        context = new ProcessingContext();
        this.cores = cores;
        executorService = new ThreadPoolExecutor(cores, cores, 0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(cores*2));
    }

    private ParallelPipe(int cores, ExecutorService executorService, List<Processor> processors){
        this.processors = processors;
        context = new ProcessingContext();
        this.executorService = executorService;
        this.cores = cores;
    }

    public <O1> ParallelPipe<I, O1> add(Processor<O, O1> processor){
        List<Processor> pr = new ArrayList<Processor>();
        pr.addAll(processors);
        pr.add(processor);
        return new ParallelPipe<I, O1>(cores, executorService, pr);
    }

    @Override
    public O train(I input) throws Exception {
        Object i = input;
        for(final Processor p : processors){
            p.setContext(context);
            context.getProgressReporter().reportProgress(p.getClass().getSimpleName() + " train: ", 0);
            long t0 = System.currentTimeMillis();
            if (p instanceof ParallelProcessor) {
                final ParallelProcessor pp = (ParallelProcessor) p;
                pp.setChunks(cores);
                final Object o = pp.initTrain(i);
                Queue<Future> futures = new LinkedList<Future>();
                for(int c=0; c<cores; c++){
                    final Object finalI = i;
                    final int finalC = c;
                    futures.add(executorService.submit(new Callable<Object>() {
                        @Override
                        public Object call() throws Exception {
                            pp.train(finalI, o, finalC);
                            return null;
                        }
                    }));
                    while(futures.size() > cores * 2) {
                        futures.poll().get();
                    }
                }
                while(futures.size() > 0) {
                    futures.poll().get();
                }
                i = o;
            } else {
                System.err.println(p + " ( " + i.getClass() + " )");
                i = p.train(i);
            }
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
            if (p instanceof ParallelProcessor) {
                final ParallelProcessor pp = (ParallelProcessor) p;
                pp.setChunks(cores);
                final Object o = pp.initProcess(i);
                Queue<Future> futures = new LinkedList<Future>();
                for(int c=0; c<cores; c++){
                    final Object finalI = i;
                    final int finalC = c;
                    futures.add(executorService.submit(new Callable<Object>() {
                        @Override
                        public Object call() throws Exception {
                            pp.process(finalI, o, finalC);
                            return null;
                        }
                    }));
                    while(futures.size() > cores * 2) {
                        futures.poll().get();
                    }
                }
                while(futures.size() > 0) {
                    futures.poll().get();
                }
                i = o;
            } else {
                i = p.process(i);
            }
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

    public void close() {
        executorService.shutdown();
    }
}

