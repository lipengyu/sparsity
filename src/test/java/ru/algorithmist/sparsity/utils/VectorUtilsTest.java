package ru.algorithmist.sparsity.utils;

import org.junit.Test;
import ru.algorithmist.sparsity.data.DenseVector;
import ru.algorithmist.sparsity.data.Vector;
import ru.algorithmist.sparsity.data.bit.BitVector;

import java.util.BitSet;

import static junit.framework.Assert.assertEquals;

/**
 * @author Sergey Edunov
 */
public class VectorUtilsTest {

    @Test
    public void testCosineSimilarity() {
        Vector v1 = new DenseVector(10);
        Vector v2 = new DenseVector(10);
        v1.set(1, 2);
        v2.set(1, 2);
        assertEquals(1f, VectorUtils.cosineSimilarity(v1, v2));

        v1.set(2, 2);
        assertEquals(0.70710677f, VectorUtils.cosineSimilarity(v1, v2));

    }

    @Test
    public void testBSCosineSimilarity() {
        BitVector s1 = new BitVector();
        s1.set(0, 1);
        s1.set(1, 0);
        s1.set(2, 1);

        BitVector s2 = new BitVector();
        s2.set(0, 0);
        s2.set(1, 1);
        s2.set(2, 0);

        System.out.println("Hamming " + s1.hammingDistance(s2));
        System.out.println("Hamming " + s2.hammingDistance(s1));


        assertEquals(1f, VectorUtils.cosineSimilarity(s1, s1));
        assertEquals(1f, VectorUtils.cosineSimilarity(s2, s2));
        assertEquals(-1f, VectorUtils.cosineSimilarity(s2, s1));
    }


}
