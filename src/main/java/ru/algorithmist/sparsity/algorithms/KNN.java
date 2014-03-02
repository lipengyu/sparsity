package ru.algorithmist.sparsity.algorithms;

import ru.algorithmist.sparsity.algorithms.similarity.SimilarityMeasure;
import ru.algorithmist.sparsity.data.*;
import ru.algorithmist.sparsity.pipe.AbstractProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Sergey Edunov
 */
public class KNN<P> extends AbstractProcessor<DataFrame<P>, List<List<P>>> {

    private List<P> predictors;
    private BasicMatrix data;
    private int n;
    private SimilarityMeasure similarityMeasure;

    public KNN(int n, SimilarityMeasure similarityMeasure){
        this.n = n;
        this.similarityMeasure = similarityMeasure;
    }

    @Override
    public List<List<P>> train(DataFrame<P> input) throws Exception {
        this.predictors = input.getPredictors();
        this.data = input.getData();
        List<List<P>> res = new ArrayList<List<P>>();
        for(P p : predictors){
            List<P> pl = new ArrayList<P>(1);
            pl.add(p);
            res.add(pl);
        }
        return res;
    }

    @Override
    public List<List<P>> process(final DataFrame<P> input) throws Exception {
        List<List<P>> res = new ArrayList<List<P>>();
//        List<Future<List<P>>> futures = new ArrayList<Future<List<P>>>();
        int rows = input.getRows();
        for(int i=0; i<input.getRows(); i++){
            final int row = i;
            getContext().getProgressReporter().reportProgress("KNN", i/(double)rows);
//            futures.add(ThreadUtils.schedule(new Callable<List<P>>() {
//                @Override
//                public List<P> call() throws Exception {
//                    return closest(input.getData().get(row));
//                }
//            }));
            res.add(closest(input.getData().get(row)));
        }
//        for(int i=0; i<futures.size(); i++) {
//            Future<List<P>> future = futures.get(i);
//            getContext().getProgressReporter().reportProgress("KNN", i/(double)rows);
//            res.add(future.get());
//        }
        return res;
    }

    @Override
    public List<List<P>> cv(DataFrame<P> input) throws Exception {
        return train(input);
    }

    private List<P> closest(Vector v){
        IndexedDouble[] distances = new IndexedDouble[predictors.size()];
        for(int i=0; i<distances.length; i++){
            Vector dv = data.get(i);
            distances[i] = new IndexedDouble(similarityMeasure.distance(dv, v), i);
        }
        Arrays.sort(distances);
        List<P> res = new ArrayList<P>(n);
        for(int i=0; i<n; i++){
            res.add(predictors.get(distances[i].pos));
        }
        return res;
    }


    private static class IndexedDouble implements Comparable<IndexedDouble>{
        private double value;
        private int pos;

        private IndexedDouble(double value, int pos) {
            this.value = value;
            this.pos = pos;
        }

        @Override
        public int compareTo(IndexedDouble o) {
            return Double.compare(value, o.value);
        }

        public String toString() {
            return pos + ":" + value;
        }
    }
}
