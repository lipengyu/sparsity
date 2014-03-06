package ru.algorithmist.sparsity.algorithms;

import ru.algorithmist.sparsity.data.*;
import ru.algorithmist.sparsity.utils.RandomUtils;

/**
 * Based on: T Hastie Very Sparse Random Projections
 * http://www.stanford.edu/~hastie/Papers/Ping/KDD06_rp.pdf
 *
 * @author Sergey Edunov
 */
public class VerySparseRandomProjections  extends RandomProjections {

    private double s;

    public VerySparseRandomProjections(int d, double s) {
        super(d);
        this.s = s;
    }


    /**
     * Lazy initialization for random gaussian matrix. We only add columns when we really need them.
     */
    protected void addRandomCol(SparseMatrix res, int col) {
        float v = (float) Math.sqrt(s);
        for(int i=0; i<d; i++) {
            double prob = RandomUtils.nextDouble();
            if (prob < 1/s) {
                if (prob < 1/(2*s)) {
                    res.set(i, col, v);
                } else {
                    res.set(i, col, -v);
                }
            }
        }
    }


}
