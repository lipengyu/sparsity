package ru.algorithmist.sparsity.data;

import ru.algorithmist.sparsity.utils.StringFormat;

public class DenseVector implements Vector {

    private float [] data;

    public DenseVector(int size) {
        data = new float[size];
    }

    public DenseVector(double[] data) {
        this.data = new float[data.length];
        for(int i=0; i<data.length; i++){
            this.data[i] = (float) data[i];
        }
    }


    public DenseVector(float[] data) {
        this.data = new float[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
    }

    @Override
    public void set(int index, float value) {
        data[index] = value;
    }

    @Override
    public float get(int index) {
        if (index >= data.length){
            return 0;
        }
        return data[index];
    }

    @Override
    public Vector slice(int from, int to) {
        DenseVector res = new DenseVector(to-from);
        System.arraycopy(data, from, res.data, 0, to-from);
        return res;
    }

    @Override
    public float[] toArray() {
        float [] res = new float[data.length];
        System.arraycopy(data, 0, res, 0, data.length);
        return res;
    }

    @Override
    public int size() {
        return data.length;
    }

    @Override
    public <T, R> R mapReduce(VectorMapReducer<T, R> callback) {
        R res = null;
        for(int r = 0; r < size(); r++) {
            res = callback.reduce(res, callback.map(r, data[r]));
        }
        return res;
    }

    @Override
    public void map(VectorMapper callback) {
        for(int r = 0; r < size(); r++) {
            callback.map(r, data[r]);
        }
    }

    @Override
    public void union(Vector vector, VectorVectorMapper callback) {
        int size = Math.max(size(), vector.size());
        for(int i=0; i<size; i++) {
            float v1 = get(i);
            float v2 = vector.get(i);
            if (v1 != 0 || v2!= 0) {
                callback.map(i, v1, v2);
            }
        }
    }

    @Override
    public void intersect(Vector vector, VectorVectorMapper callback) {
        int size = Math.min(size(), vector.size());
        for(int i=0; i<size; i++) {
            float v1 = get(i);
            float v2 = vector.get(i);
            if (v1 != 0 && v2!= 0) {
                callback.map(i, v1, v2);
            }
        }
    }

    @Override
    public int[] keys() {
        int[] res = new int[size()];
        for(int i=0; i<res.length; i++){
            res[i] = i;
        }
        return res;
    }

    @Override
    public float l2norm() {
        float res = 0;
        for(int r = 0; r < size(); r++) {
            res += data[r]*data[r];
        }
        return (float) Math.sqrt(res);
    }

    public String toString() {
        StringBuilder res = new StringBuilder();
        for(int i=0; i<10 && i < data.length; i++){
            res.append(StringFormat.format(data[i])).append(" ");
        }
        if (data.length > 10) {
            res.append("...");
        }
        return res.toString();
    }
}
