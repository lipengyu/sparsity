package ru.algorithmist.sparsity.data;

import org.junit.Test;

import static junit.framework.Assert.*;

public class DenseVectorTest {

    @Test
    public void testSlice() {
        Vector vector = new DenseVector(10);
        vector.set(3, 2);
        vector.set(4, 3);
        Vector s = vector.slice(0, 4);
        assertEquals(4, s.size());


        Vector s1 = vector.slice(2, 4);
        assertEquals(2, s1.size());
    }

    @Test
    public void testToArray() {
        Vector vector = new DenseVector(10);
        vector.set(3, 2);
        vector.set(4, 3);
        float [] res = vector.toArray();
        assertEquals(2f, res[3]);
        assertEquals(3f, res[4]);
    }

    @Test
    public void testL2Norm() {
        Vector vector = new DenseVector(10);
        vector.set(3, 2);
        vector.set(4, 3);

        assertEquals(3.605551f, vector.l2norm(), 0.01);

        vector.set(5, 4);
        assertEquals(5.385165f, vector.l2norm(), 0.01);

        vector.set(3, 0);
        assertEquals(5f, vector.l2norm(), 0.01);

    }



}

