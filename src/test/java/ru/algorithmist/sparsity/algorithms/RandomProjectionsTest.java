package ru.algorithmist.sparsity.algorithms;

import org.junit.Test;
import ru.algorithmist.sparsity.data.BasicMatrix;
import ru.algorithmist.sparsity.data.SparseMatrix;
import ru.algorithmist.sparsity.data.bit.BitMatrix;
import ru.algorithmist.sparsity.data.bit.BitVector;
import ru.algorithmist.sparsity.pipe.ProcessingContext;
import ru.algorithmist.sparsity.utils.VectorUtils;

import java.util.Arrays;
import java.util.BitSet;

import static junit.framework.Assert.*;


/**
 * @author Sergey Edunov
 */
public class RandomProjectionsTest {

    @Test
    public void testSameText() throws Exception {
        RandomProjections rp = new RandomProjections(5);
        rp.setContext(new ProcessingContext());
        SparseMatrix train = new SparseMatrix();
        train.set(0, 0, 1);
        train.set(0, 1, 1);
        train.set(0, 2, 1);

        SparseMatrix test = new SparseMatrix();
        test.set(0, 0, 1);
        test.set(0, 1, 1);
        test.set(0, 2, 1);

        BasicMatrix tr = rp.train(train);
        BasicMatrix ts = rp.process(test);

        assertEquals(1f, VectorUtils.cosineSimilarity((BitVector)tr.get(0), (BitVector)ts.get(0)));
    }

    @Test
    public void testDifferentTexts() throws Exception {
        int size = 5000;
        RandomProjections rp = new RandomProjections(size);
        rp.setContext(new ProcessingContext());
        SparseMatrix train = new SparseMatrix();
        train.set(0, 0, 1);
        train.set(0, 1, 1);
        train.set(0, 2, 1);

        SparseMatrix test = new SparseMatrix();
        test.set(0, 0, 1);
        test.set(0, 3, 1);
        test.set(0, 4, 1);

        BasicMatrix tr = rp.train(train);
        BasicMatrix ts = rp.process(test);


        float similarity = VectorUtils.cosineSimilarity((BitVector)tr.get(0), (BitVector)ts.get(0));

        double exact = VectorUtils.cosineSimilarity(train.get(0), test.get(0));
        assertEquals("Similarity is out of bounds " + similarity + " vs " + exact, exact, similarity, 0.1);
    }

}
