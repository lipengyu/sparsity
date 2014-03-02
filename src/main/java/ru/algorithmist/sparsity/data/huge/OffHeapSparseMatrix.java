package ru.algorithmist.sparsity.data.huge;

import ru.algorithmist.sparsity.data.BasicMatrix;
import ru.algorithmist.sparsity.data.MatrixMapper;
import ru.algorithmist.sparsity.data.VectorMapper;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;

/**
 * @author Sergey Edunov
 */
public class OffHeapSparseMatrix implements BasicMatrix {

    private OffHeapChunk[] data;
    private int rows;

    private int chunkSize;

    public OffHeapSparseMatrix(int size, int chunkSize) throws IOException {
        this.data = new OffHeapChunk[1 + (size / chunkSize)];
        this.chunkSize = chunkSize;
    }

    @Override
    public void set(int row, int col, float value) {
        OffHeapVector vector = get(row);
        if (vector == null) {
            throw new IllegalStateException("OffHeapVector is not initialized yet");
        }
        vector.set(col, value);
    }

    @Override
    public float get(int row, int col) {
        OffHeapVector vector = get(row);
        if (vector == null) {
            throw new IllegalStateException("OffHeapVector is not initialized yet");
        }
        return vector.get(col);
    }


    @Override
    public OffHeapVector get(int row) {
        OffHeapChunk ch = getOffHeapChunk(row);
        return ch.get(row % chunkSize);
    }

    @Override
    public int rows() {
        return rows;
    }

    @Override
    public void map(final MatrixMapper matrixMapper) {
        MatrixToVectorMapper mapper = new MatrixToVectorMapper(matrixMapper);
        for (int i = 0; i < rows; i++) {
            OffHeapVector row = get(i);
            mapper.row = i;
            row.map(mapper);
        }
    }

    private static class MatrixToVectorMapper implements VectorMapper {
        private int row;
        private MatrixMapper matrixMapper;

        private MatrixToVectorMapper(MatrixMapper matrixMapper) {
            this.matrixMapper = matrixMapper;
        }

        @Override
        public void map(int index, float value) {
            matrixMapper.map(row, index, value);
        }
    }

    public void init(int row, int size) {
        getOffHeapChunk(row).init(row % chunkSize, size);
        if (rows <= row) {
            rows = row + 1;
        }
    }

    public void set(int row, OffHeapVector v) {
        OffHeapChunk ch = getOffHeapChunk(row);
        ch.set(row % chunkSize, v);
        if (row >= rows) {
            rows = row + 1;
        }
    }

    private OffHeapChunk getOffHeapChunk(int row) {
        int chunkIndex = row / chunkSize;

        OffHeapChunk ch = data[chunkIndex];
        try {
            if (ch == null) {
                File tempFile = File.createTempFile("sparsity", ".dat");
                RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");
                tempFile.deleteOnExit();
                FileChannel channel = raf.getChannel();
                MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 1000000000);
                ch = new OffHeapChunk(chunkSize, buffer);
                data[chunkIndex] = ch;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return ch;
    }


}
