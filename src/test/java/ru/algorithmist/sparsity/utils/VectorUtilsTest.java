package ru.algorithmist.sparsity.utils;

import org.junit.Test;
import ru.algorithmist.sparsity.data.DenseVector;
import ru.algorithmist.sparsity.data.Vector;

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


}
