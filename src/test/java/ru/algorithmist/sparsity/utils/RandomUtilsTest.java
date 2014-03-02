package ru.algorithmist.sparsity.utils;

import org.junit.Test;
import ru.algorithmist.sparsity.data.Matrix;

import static junit.framework.Assert.*;

public class RandomUtilsTest {

    @Test
    public void testNormal() {
        Matrix n = RandomUtils.normal(10, 20);
        assertEquals(10, n.rows());
        assertEquals(20, n.cols());
    }
}
