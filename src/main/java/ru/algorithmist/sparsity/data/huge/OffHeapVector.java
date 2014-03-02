package ru.algorithmist.sparsity.data.huge;

import ru.algorithmist.sparsity.data.*;
import ru.algorithmist.sparsity.utils.StringFormat;

import java.nio.MappedByteBuffer;
import java.util.Arrays;

/**
 * @author Sergey Edunov
 */
public class OffHeapVector implements Vector{

    public static final int INDEX_PHYSICAL_SIZE = 4;
    public static final int PHYSICAL_SIZE = 4 + INDEX_PHYSICAL_SIZE;

    private int fillLevel;
    private MappedByteBuffer buffer;
    private int position;
    private float l2norm = Float.NaN;

    public OffHeapVector(MappedByteBuffer buffer, int position){
        this.fillLevel = 0;
        this.buffer = buffer;
        this.position = position;
    }

    public void set(int index, float value){
        l2norm = Float.NaN;
        int idx = find(index);
        if (idx >= 0) {
            setValue(idx, value);
        } else {
            idx = - idx - 1;
            for(int i = idx; i<=fillLevel; i++) {
                int tindex = index(i);
                setIndex(i, index);
                index = tindex;
                float tvalue = value(i);
                setValue(i, value);
                value = tvalue;
            }
            this.fillLevel++;
        }
    }

    private int index(int position) {
        return buffer.getInt(this.position + position*PHYSICAL_SIZE);
    }

    private void setIndex(int position, int value) {
        buffer.putInt(this.position + position * PHYSICAL_SIZE, value);
    }

    private float value(int position) {
        return buffer.getFloat(this.position + position * PHYSICAL_SIZE + INDEX_PHYSICAL_SIZE);
    }

    private void setValue(int position, float value) {
        buffer.putFloat(this.position + position * PHYSICAL_SIZE + INDEX_PHYSICAL_SIZE, value);
    }


    @Override
    public float get(int index) {
        int idx = find(index);
        if (idx < 0) {
            return 0;
        }
        return value(idx);
    }

    @Override
    public Vector slice(int from, int to) {

        SparseVector res = new SparseVector();
        int fi = find(from);
        if (fi < 0 ) {
            fi = - fi - 1;
        }
        int ti = find(to);
        if (ti < 0) {
            ti = - ti - 1;
        }
        for(int idx = fi; idx<ti; idx++) {
            res.set(index(idx)-from, value(idx));
        }
        return res;
    }

    @Override
    public float[] toArray() {
        float[] res = new float[size()];
        for(int i=0; i<fillLevel; i++){
            res[index(i)] = value(i);
        }
        return res;
    }

    @Override
    public int size() {
        if (fillLevel == 0) return 0;
        return index(fillLevel-1)+1;
    }

    @Override
    public <T, R> R mapReduce(VectorMapReducer<T, R> callback) {
        R current = null;
        for(int i=0; i<fillLevel; i++){
            current = callback.reduce(current, callback.map(index(i), value(i)));
        }
        return current;
    }

    @Override
    public void map(VectorMapper callback) {
        for(int i=0; i<fillLevel; i++){
            callback.map(index(i), value(i));
        }
    }

    @Override
    public void union(Vector vector, VectorVectorMapper callback) {
        if (vector instanceof DenseVector || vector instanceof SparseVector) {
            vector.union(this, new VectorVectorReverseOrderMapper(callback));
        }
        OffHeapVector v = (OffHeapVector)vector;
        int p1 = 0;
        int p2 = 0;
        for(; p1<fillLevel && p2 < v.fillLevel; ){
            if (index(p1) == v.index(p2)) {
                callback.map(index(p1), value(p1), v.value(p2));
                p1++;
                p2++;
            } else if (index(p1) < v.index(p2)) {
                callback.map(index(p1), value(p1), 0);
                p1++;
            } else {
                callback.map(v.index(p2), 0, v.value(p2));
                p2++;
            }
        }
        for(; p1 < fillLevel; p1++) {
            callback.map(index(p1), value(p1), 0);
        }
        for(; p2 < v.fillLevel; p2++) {
            callback.map(v.index(p2), 0, v.value(p2));
        }

    }

    @Override
    public void intersect(Vector vector, VectorVectorMapper callback) {
        if (vector instanceof DenseVector || vector instanceof SparseVector) {
            vector.intersect(this, new VectorVectorReverseOrderMapper(callback));
        }
        OffHeapVector v = (OffHeapVector)vector;
        int p1 = 0;
        int p2 = 0;
        for(; p1<fillLevel && p2 < v.fillLevel; ){
            if (index(p1) == v.index(p2)) {
                callback.map(index(p1), value(p1), v.value(p2));
                p1++;
                p2++;
            } else if (index(p1) < v.index(p2)) {
                p1++;
            } else {
                p2++;
            }
        }
    }

    @Override
    public int[] keys() {
        int[] res = new int[fillLevel];
        for(int i=0; i<fillLevel; i++) {
            res[i] = index(i);
        }
        return res;
    }

    @Override
    public float l2norm() {
        if (Float.isNaN(l2norm)) {
            l2norm = 0;
            for(int i=0; i<fillLevel; i++){
                float v = value(i);
                l2norm += v*v;
            }
            l2norm = (float) Math.sqrt(l2norm);
        }
        return l2norm;
    }

    private int find(int index){
        int low = 0;
        int high = fillLevel - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midVal = index(mid);

            if (midVal < index)
                low = mid + 1;
            else if (midVal > index)
                high = mid - 1;
            else
                return mid;
        }
        return -(low + 1);
    }


    public String toString() {
        StringBuilder res = new StringBuilder();
        for(int i=0; i<fillLevel && i<10; i++){
            res.append(index(i)).append(":").append(StringFormat.format(value(i))).append("  ");
        }
        res.append("...");
        return res.toString();
    }

}
