package ru.algorithmist.sparsity.utils;


import org.junit.Test;
import ru.algorithmist.sparsity.data.Matrix;
import ru.algorithmist.sparsity.data.SparseMatrix;

import static junit.framework.Assert.*;

public class CommonsUtilsTest {

    @Test
    public void testMatrix() {
        SparseMatrix sm = new SparseMatrix();
        sm.add(0, 0, 1);
        sm.add(2, 2, 2);

        Matrix nm = CommonsUtils.matrix(CommonsUtils.matrix(sm));

        assertEquals(3, nm.rows());
        assertEquals(3, nm.cols());
        assertEquals(1f, nm.get(0, 0));
        assertEquals(2f, nm.get(2, 2));
        assertEquals(0f, nm.get(1, 1));
    }
}
