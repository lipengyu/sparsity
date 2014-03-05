package ru.algorithmist.sparsity.data.huge;

import org.junit.Test;
import ru.algorithmist.sparsity.data.Vector;
import ru.algorithmist.sparsity.utils.VectorUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

import static junit.framework.Assert.assertEquals;

/**
 * @author Sergey Edunov
 */
public class OffHeapVectorTest {

    private MappedByteBuffer createBuffer() throws IOException {
        File tempFile = File.createTempFile("sparsity", ".dat");
        RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");
        tempFile.deleteOnExit();
        FileChannel channel = raf.getChannel();
        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 10000);
        return buffer;
    }

    @Test
    public void testSlice() throws IOException {
        Vector vector = new OffHeapVector(createBuffer(), 10);
        vector.set(3, 2);
        vector.set(4, 3);
        Vector s = vector.slice(0, 4);
        assertEquals(4, s.size());


        Vector s1 = vector.slice(2, 4);
        assertEquals(2, s1.size());
    }

    @Test
    public void testToArray() throws IOException {
        Vector vector = new OffHeapVector(createBuffer(), 10);
        vector.set(3, 2);
        vector.set(4, 3);
        float [] res = vector.toArray();
        assertEquals(2f, res[3]);
        assertEquals(3f, res[4]);
    }

    @Test
    public void testDistance() throws IOException {
        OffHeapVector v1 = new OffHeapVector(createBuffer(), 10);
        v1.set(1, 1);
        v1.set(3, 1);

        OffHeapVector v2 = new OffHeapVector(createBuffer(), 10);
        v2.set(3, 1);
        v2.set(4, 1);
        double d = VectorUtils.distance(v1, v2);
        assertEquals(2, d*d, 0.00001);

    }

    @Test
    public void testL2Norm() throws IOException {
        Vector vector = new OffHeapVector(createBuffer(), 10);
        vector.set(3, 2);
        vector.set(4, 3);

        assertEquals(3.605551f, vector.l2norm(), 0.01);

        vector.set(5, 4);
        assertEquals(5.385165f, vector.l2norm(), 0.01);

        vector.set(3, 0);
        assertEquals(5f, vector.l2norm(), 0.01);

    }
}
