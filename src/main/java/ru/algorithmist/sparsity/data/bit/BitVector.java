package ru.algorithmist.sparsity.data.bit;

import ru.algorithmist.sparsity.data.Vector;
import ru.algorithmist.sparsity.data.VectorMapReducer;
import ru.algorithmist.sparsity.data.VectorMapper;
import ru.algorithmist.sparsity.data.VectorVectorMapper;

/**
 * @author Sergey Edunov
 */
public class BitVector implements Vector {


    private long[] data;
    private int length;

    public BitVector() {
        data = new long[1];
    }

    public BitVector(int size) {
        data = new long[(int) Math.ceil(size / 64.)];
    }

    private int bucket(int index){
        return index/64;
    }

    @Override
    public void set(int index, float value) {
        if (value > 0) {
            int bucket = bucket(index);
            if (bucket >= data.length) {
                long[] temp = new long[bucket+1];
                System.arraycopy(data, 0, temp, 0, data.length);
                data = temp;
            }
            data[bucket] |= (1L << (index % 64));
        }
        if (index >= length) {
            length = index+1;
        }
    }

    @Override
    public float get(int index) {
        if (index >= length) {
            return 0;
        }
        int bucket = bucket(index);
        return data[bucket] & (1L << (index % 64));
    }

    @Override
    public Vector slice(int from, int to) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float[] toArray() {
        float[] res = new float[length];
        for(int i = 0; i<length; i++) {
            if ((data[bucket(i)] & (1L << (i % 64))) > 0) {
                res[i] = 1;
            }
        }
        return res;
    }

    @Override
    public int size() {
        return length;
    }

    @Override
    public void map(VectorMapper callback) {
        for(int i = 0; i<length; i++) {
            if ((data[bucket(i)] & (1L << (i % 64))) > 0) {
                callback.map(i, 1);
            }
        }
    }

    @Override
    public <T, R> R mapReduce(VectorMapReducer<T, R> callback) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void union(Vector vector, VectorVectorMapper callback) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void intersect(Vector vector, VectorVectorMapper callback) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int[] keys() {
        int[] res = new int[nonZeroElements()];
        int p = 0;
        for(int i = 0; i<length; i++) {
            if ((data[bucket(i)] & (1L << (i % 64))) > 0) {
               res[p++] = i;
            }
        }
        return res;
    }

    @Override
    public float l2norm() {
        return (float) Math.sqrt(nonZeroElements());
    }

    public int nonZeroElements() {
        int res = 0;
        for(long v : data) {
            res += Long.bitCount(v);
        }
        return res;
    }

    public int hammingDistance(BitVector v) {
        int res = 0;
        int i = 0;
        for(; i<Math.min(v.data.length, v.length); i++) {
            res += Long.bitCount(data[i] ^ v.data[i]);
        }
        for(int j = i; j < data.length; j++) {
            res += Long.bitCount(data[j]);
        }
        for(int j = i; j < v.data.length; j++) {
            res += Long.bitCount(v.data[j]);
        }
        return res;
    }
}
