package ru.algorithmist.sparsity.data;

import org.junit.Test;

import static junit.framework.Assert.*;


public class SparseMatrixTest {

    @Test
    public void testDefault() {
        SparseMatrix a = new SparseMatrix();
        a.set(1, 1, 4);
        assertEquals(0f, a.get(0, 0));
    }

    @Test
    public void testInitialization() {
        SparseMatrix a = new SparseMatrix();
        a.set(0, 0, 1);
        a.set(0, 1, 2);
        a.set(1, 0, 3);
        a.set(1, 1, 4);
        assertEquals(2, a.rows());
        assertEquals(2, a.cols());
    }

    @Test
    public void testAdd() {
        SparseMatrix a = new SparseMatrix();
        a.set(0, 0, 1);
        a.set(0, 1, 2);
        a.set(1, 0, 3);
        a.set(1, 1, 4);

        SparseMatrix b = new SparseMatrix();
        b.set(0, 0, 5);
        b.set(1, 1, 6);
        b.set(2, 2, 7);

        Matrix c = a.add(b);

        assertEquals(3, c.rows());
        assertEquals(3, c.cols());
        assertEquals(6f, c.get(0, 0));
    }

    @Test
    public void testTranspose() {
        SparseMatrix a = new SparseMatrix();
        a.set(0, 1, 2);
        a.set(1, 0, 3);
        a.set(0, 2, 4);

        Matrix t = a.transpose();
        assertEquals(a.rows(), t.cols());
        assertEquals(a.cols(), t.rows());
        assertEquals(a.get(0, 1), t.get(1, 0));
        assertEquals(a.get(1, 0), t.get(0, 1));
        assertEquals(a.get(0, 2), t.get(2, 0));
    }

    //2x1 * 1x2 = 2x2
    @Test
    public void testMultiply() {
        SparseMatrix a = new SparseMatrix();
        a.set(0, 0, 1);
        a.set(1, 0, 2);

        SparseMatrix b = new SparseMatrix();
        b.set(0, 0, 3);
        b.set(0, 1, 4);

        Matrix res = a.dot(b);
        assertEquals(2, res.cols());
        assertEquals(2, res.rows());
        assertEquals(3f, res.get(0, 0));
        assertEquals(4f ,res.get(0, 1));
        assertEquals(6f, res.get(1, 0));
        assertEquals(8f, res.get(1, 1));
    }

    //1x2 * 2x1 = 1x1
    @Test
    public void testMultiply1() {
        SparseMatrix a = new SparseMatrix();
        a.set(0, 0, 1);
        a.set(1, 0, 2);

        SparseMatrix b = new SparseMatrix();
        b.set(0, 0, 3);
        b.set(0, 1, 4);

        Matrix res = b.dot(a);
        assertEquals(1, res.cols());
        assertEquals(1, res.rows());
        assertEquals(11f, res.get(0, 0));
    }

    @Test
    public void testMultiplyDense() {
        SparseMatrix a = new SparseMatrix();
        a.set(0, 0, 1);
        a.set(1, 0, 2);

        DenseMatrix b = new DenseMatrix(1, 2);
        b.set(0, 0, 3);
        b.set(0, 1, 4);

        Matrix res = a.dot(b);
        assertEquals(2, res.cols());
        assertEquals(2, res.rows());
        assertEquals(3f, res.get(0, 0));
        assertEquals(4f ,res.get(0, 1));
        assertEquals(6f, res.get(1, 0));
        assertEquals(8f, res.get(1, 1));
    }

    @Test
    public void testSlice() {

        SparseMatrix a = new SparseMatrix();
        a.set(0, 0, 1);
        a.set(0, 1, 2);
        a.set(0, 2, 3);
        a.set(1, 0, 4);
        a.set(1, 1, 5);
        a.set(1, 2, 6);
        a.set(2, 0, 7);
        a.set(2, 1, 8);
        a.set(2, 2, 9);

        Matrix b = a.slice(1, 1, 3, 3);
        assertEquals(2, b.rows());
        assertEquals(2, b.cols());

        assertEquals(5f, b.get(0, 0));
        assertEquals(9f, b.get(1, 1));
    }

    @Test
    public void testSliceException() {
        SparseMatrix a = new SparseMatrix();
        try{
            a.slice(0, 0, 20, 10);
            fail("Exception must be thrown");
        }catch (IllegalArgumentException e){
        }
    }

    @Test
    public void testMultiplyByNumber() {
        SparseMatrix a = new SparseMatrix();
        a.set(3, 4, 1f);
        assertEquals(5f, a.multiply(5).get(3, 4));
    }

}
