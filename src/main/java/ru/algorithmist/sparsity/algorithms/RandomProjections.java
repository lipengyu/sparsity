package ru.algorithmist.sparsity.algorithms;

import ru.algorithmist.sparsity.data.*;
import ru.algorithmist.sparsity.data.bit.BitMatrix;
import ru.algorithmist.sparsity.pipe.AbstractParallelProcessor;
import ru.algorithmist.sparsity.utils.NumberUtils;
import ru.algorithmist.sparsity.utils.RandomUtils;

/**
 * Implementation of Locality Sensitive Hashing (LSH) using random projections.
 *
 * Based on: Charikar, M. S. (2002). Similarity estimation techniques from rounding algorithms
 * http://www.cs.princeton.edu/courses/archive/spr04/cos598B/bib/CharikarEstim.pdf
 *
 * Should probably take a look at this work too. Ella Bingham  Random projection in dimensionality reduction: Applications to image and text data
 * http://users.ics.aalto.fi/ella/publications/randproj_kdd.pdf
 *
 *
 *
 * @author Sergey Edunov
 */
public class RandomProjections extends AbstractParallelProcessor<BasicMatrix, BasicMatrix> {

    protected int d;
    private SparseMatrix RT = new SparseMatrix();

    public RandomProjections(int d) {
        this.d = d;
    }

    @Override
    public BasicMatrix train(BasicMatrix input) throws Exception {
        return project(input);
    }

    private BasicMatrix project(BasicMatrix data) {
        int rows = data.rows();
        final BitMatrix ret = new BitMatrix(data.rows(), d);

        for(int row = 0; row<rows; row++) {
            getContext().getProgressReporter().reportProgress("RandomProjections", row/(double)rows);
            projectRow(data, ret, row);
        }
        return ret;
    }

    private void projectRow(BasicMatrix data, BasicMatrix ret, int row) {
        Vector r = data.get(row);
        synchronized (this) {
            r.map(new VectorMapper() {
                @Override
                public void map(int index, float value) {
                    if (!NumberUtils.isZero(value) && NumberUtils.isZero(RT.get(0, index))){
                        addRandomCol(RT, index);
                    }
                }
            });
        }
        for(int rtvi = 0; rtvi < RT.rows(); rtvi++) {
            final Vector rtv = RT.get(rtvi);
            final float[] holder = new float[1];

            r.intersect(rtv, new VectorVectorMapper() {
                @Override
                public boolean map(int index, float v1, float v2) {
                    holder[0] += v1 * v2;
                    return true;
                }
            });

            ret.set(row, rtvi, holder[0]);
        }
    }

    /**
     * Lazy initialization for random gaussian matrix. We only add columns when we really need them.
     */
    protected void addRandomCol(SparseMatrix res, int col) {
        for(int i=0; i<d; i++) {
            double rnd = RandomUtils.nextGaussian();
            res.set(i, col, (float) rnd);
        }
    }

    @Override
    public BasicMatrix process(BasicMatrix input) throws Exception {
        return project(input);
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
    public BasicMatrix initTrain(BasicMatrix input) {
        return new BitMatrix(input.rows(), d);
    }

    @Override
    public BasicMatrix initProcess(BasicMatrix input) {
        return new BitMatrix(input.rows(), d);
    }

    @Override
    public void train(BasicMatrix input, BasicMatrix output, int chunk) throws Exception {
        project(input, output, chunk);
    }

    @Override
    public void process(BasicMatrix input, BasicMatrix output, int chunk) throws Exception {
        project(input, output, chunk);
    }

    private void project(BasicMatrix data, BasicMatrix ret, int chunk) {
        int rows = data.rows();
        for(int row = chunk; row<rows; row+=chunks) {
            getContext().getProgressReporter().reportProgress("Parallel RandomProjections", row/(double)rows);
            projectRow(data, ret, row);
        }
    }
}
