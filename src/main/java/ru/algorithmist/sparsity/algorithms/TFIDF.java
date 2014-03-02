package ru.algorithmist.sparsity.algorithms;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import ru.algorithmist.sparsity.data.BasicMatrix;
import ru.algorithmist.sparsity.data.MatrixMapper;
import ru.algorithmist.sparsity.data.SparseMatrix;
import ru.algorithmist.sparsity.pipe.AbstractProcessor;
import ru.algorithmist.sparsity.pipe.ProcessingContext;
import ru.algorithmist.sparsity.pipe.Processor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Edunov
 */
public class TFIDF extends AbstractProcessor<BasicMatrix,BasicMatrix> {

    private TIntIntMap docCounts = new TIntIntHashMap();
    private int N;

    @Override
    public BasicMatrix train(BasicMatrix input) throws Exception {
        input.map(new MatrixMapper() {
            @Override
            public void map(int row, int col, float value) {
                docCounts.increment(col);
            }
        });
        N = input.rows();
        return process(input);
    }

    @Override
    public BasicMatrix process(final BasicMatrix input) throws Exception {
        input.map(new MatrixMapper() {
            @Override
            public void map(int row, int col, float value) {
                input.set(row, col, tf((int) value) * idf(col));
            }
        });
        return input;
    }


    private float tf(int x) {
        return (float) Math.log(x + 1);
    }


    private float idf(int col){
        return (float) Math.log( N / (1. + docCounts.get(col)));
    }
}
