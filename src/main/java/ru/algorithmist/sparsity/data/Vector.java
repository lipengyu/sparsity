package ru.algorithmist.sparsity.data;


public interface Vector {

    void set(int index, float value);

    float get(int index);

    Vector slice(int from, int to);

    float[] toArray();

    int size();

    <T, R> R mapReduce(VectorMapReducer<T, R> callback);

    void map(VectorMapper callback);

    void union(Vector vector, VectorVectorMapper callback);

    void intersect(Vector vector, VectorVectorMapper callback);

    int[] keys();

    float l2norm();
}
