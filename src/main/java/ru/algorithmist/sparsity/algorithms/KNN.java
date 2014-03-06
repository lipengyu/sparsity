package ru.algorithmist.sparsity.algorithms;

import ru.algorithmist.sparsity.algorithms.similarity.SimilarityMeasure;
import ru.algorithmist.sparsity.data.*;
import ru.algorithmist.sparsity.pipe.AbstractParallelProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Sergey Edunov
 */
public class KNN<P> extends AbstractParallelProcessor<DataFrame<P>, Predictors<Collection<P>>> {

    private Predictors<P> predictors;
    private BasicMatrix data;
    private int n;
    private SimilarityMeasure similarityMeasure;

    public KNN(int n, SimilarityMeasure similarityMeasure){
        this.n = n;
        this.similarityMeasure = similarityMeasure;
    }

    @Override
    public Predictors<Collection<P>> train(DataFrame<P> input) throws Exception {
        Predictors<Collection<P>> res = new Predictors<Collection<P>>(input.getRows());
        train(input, res, 0);
        return res;
    }

    @Override
    public Predictors<Collection<P>> process(final DataFrame<P> input) throws Exception {
        int rows = input.getRows();
        Predictors<Collection<P>> res = new Predictors<Collection<P>>(rows);
        for(int i=0; i<rows; i++){
            getContext().getProgressReporter().reportProgress("KNN", i/(double)rows);
            res.set(i, closest(input.getData(i)));
        }
        return res;
    }

    @Override
    public Predictors<Collection<P>> cv(DataFrame<P> input) throws Exception {
        return train(input);
    }

    private Collection<P> closest(Vector v){
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


    /**
     * Parallel implementation below...
     */

    private int chunks;

    @Override
    public void setChunks(int n) {
        chunks = n;
    }

    @Override
    public Predictors<Collection<P>> initTrain(DataFrame<P> input) {
        return new Predictors<Collection<P>>(input.getRows());
    }

    @Override
    public Predictors<Collection<P>> initProcess(DataFrame<P> input) {
        return initTrain(input);
    }

    @Override
    public void train(DataFrame<P> input, Predictors<Collection<P>> output, int chunk) throws Exception {
        if (chunk == 0) { //This is not parallel (doesn't seem to worth it anyway
            this.predictors = input.getPredictors();
            this.data = input.getData();

            for(int i=0; i<predictors.size(); i++){
                Collection<P> pl = Arrays.asList(predictors.get(i));
                output.set(i,  pl);
            }
        }
    }

    @Override
    public void process(DataFrame<P> input, Predictors<Collection<P>> output, int chunk) throws Exception {
        int rows = input.getRows();
        for(int i=chunk; i<rows; i+=chunks){
            getContext().getProgressReporter().reportProgress("Parallel KNN", i/(double)rows);
            output.set(i, closest(input.getData(i)));
        }
    }
}
