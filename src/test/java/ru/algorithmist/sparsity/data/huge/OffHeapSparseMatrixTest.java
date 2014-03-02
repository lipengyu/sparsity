package ru.algorithmist.sparsity.data.huge;

import org.junit.Test;
import ru.algorithmist.sparsity.algorithms.Int;
import ru.algorithmist.sparsity.data.MatrixMapper;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

/**
 * @author Sergey Edunov
 */
public class OffHeapSparseMatrixTest {

    @Test
    public void testSimple() throws IOException {
        OffHeapSparseMatrix matrix = new OffHeapSparseMatrix(100, 10);
        matrix.init(0, 1);
        matrix.set(0, 0, 0.1f);
        assertEquals(0.1f, matrix.get(0, 0));
    }

    @Test
    public void testSimple2x2() throws IOException {
        OffHeapSparseMatrix matrix = new OffHeapSparseMatrix(100, 10);
        matrix.init(0, 2);
        matrix.init(1, 2);
        matrix.set(0, 0, 0.1f);
        matrix.set(1, 0, 0.2f);
        matrix.set(0, 1, 0.3f);
        matrix.set(1, 1, 0.4f);
        assertEquals(0.1f, matrix.get(0, 0));
        assertEquals(0.2f, matrix.get(1, 0));
        assertEquals(0.3f, matrix.get(0, 1));
        assertEquals(0.4f, matrix.get(1, 1));
    }

    @Test
    public void testBigMatrix() throws IOException {
        OffHeapSparseMatrix matrix = new OffHeapSparseMatrix(100, 10);
        for(int i=0; i<100; i++){
            matrix.init(i, 10);
            for(int j=0; j<10; j++){
                matrix.set(i, j*7+4, 1);
            }
        }
        final Int counter = new Int(0);
        matrix.map(new MatrixMapper() {
            @Override
            public void map(int row, int col, float value) {
                counter.inc((int) value);
            }
        });

        assertEquals(1000, counter.getValue());

    }
}
