package ru.algorithmist.sparsity.data.huge;

import java.io.IOException;
import java.nio.MappedByteBuffer;

/**
 * @author Sergey Edunov
 */
public class OffHeapChunk {

    private OffHeapVector[] data;
    private MappedByteBuffer buffer;
    private int position;

    public OffHeapChunk(int size, MappedByteBuffer buffer) throws IOException {
        data = new OffHeapVector[size];
        this.buffer = buffer;
    }

    public OffHeapVector get(int row) {
        return data[row];
    }

    public void set(int row, OffHeapVector v) {
        if (data.length <= row) {
            OffHeapVector[] temp = new OffHeapVector[Math.max((int) (data.length * 1.3), row + 1)];
            System.arraycopy(data, 0, temp, 0, data.length);
            data = temp;
        }
        data[row] = v;
    }

    public void init(int pos, int size) {
        data[pos] = new OffHeapVector(buffer, position);
        position += OffHeapVector.PHYSICAL_SIZE * size;
    }
}
