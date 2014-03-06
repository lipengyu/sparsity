package ru.algorithmist.sparsity.data;


import gnu.trove.impl.sync.TSynchronizedIntFloatMap;
import gnu.trove.map.TIntDoubleMap;
import gnu.trove.map.TIntFloatMap;
import gnu.trove.map.hash.TIntDoubleHashMap;
import gnu.trove.map.hash.TIntFloatHashMap;
import gnu.trove.procedure.TIntDoubleProcedure;
import gnu.trove.procedure.TIntFloatProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import ru.algorithmist.sparsity.utils.NumberUtils;
import ru.algorithmist.sparsity.utils.StringFormat;

import java.util.Set;


public final class SparseVector implements Vector {

    private TIntFloatMap data = new TSynchronizedIntFloatMap(new TIntFloatHashMap());
    private int size;

    public synchronized void set(int index, float value) {
        if (index >= size) {
            size = index+1;
        }
        if (NumberUtils.isZero(value)) {
            data.remove(index);
        } else {
            data.put(index, value);
        }
    }

    public synchronized float get(int index) {
        return data.get(index);
    }

    public synchronized void add(int index, float value) {
        data.adjustOrPutValue(index, value, value);
    }

    @Override
    public synchronized Vector slice(int from, int to) {
        SparseVector res = new SparseVector();
        for(int index : data.keys()) {
            if (index >= from && index < to) {
                res.set(index-from, data.get(index));
            }
        }
        return res;
    }

    @Override
    public synchronized float[] toArray() {
        final float[] res = new float[size()];
        data.forEachEntry(new TIntFloatProcedure() {
            @Override
            public boolean execute(int a, float b) {
                res[a] = b;
                return true;
            }
        });
        return res;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public <T, R> R mapReduce(VectorMapReducer<T, R> callback) {
        R res = null;
        for(int r : data.keys()) { //TODO: rework with forEach
            res = callback.reduce(res, callback.map(r, data.get(r)));
        }
        return res;
    }

    @Override
    public void map(final VectorMapper callback) {
        data.forEachEntry(new TIntFloatProcedure() {
            @Override
            public boolean execute(int r, float v) {
                callback.map(r, v);
                return true;
            }
        });
    }

    public int[] indexes() {
        return data.keys();
    }

    public String toString() {
        StringBuilder res = new StringBuilder();
        int cnt = 0;
        for(Integer r : data.keys()){
            if (cnt > 10) break;
            cnt++;
            res.append(r).append(":").append(StringFormat.format(data.get(r))).append("  ");
        }
        res.append("...");
        return res.toString();
    }

    @Override
    public void union(final Vector vector, final VectorVectorMapper callback) {
        if (vector instanceof DenseVector) {
            vector.union(this, new VectorVectorReverseOrderMapper(callback));
            return;
        }
        TIntSet keys = new TIntHashSet();
        keys.addAll(keys());
        keys.addAll(vector.keys());
        keys.forEach(new TIntProcedure() {
            @Override
            public boolean execute(int value) {
                return callback.map(value, get(value), vector.get(value));
            }
        });
    }

    @Override
    public void intersect(final Vector vector, final VectorVectorMapper callback) {
        if (vector instanceof DenseVector) {
            vector.intersect(this, new VectorVectorReverseOrderMapper(callback));
            return;
        }
        TIntSet keys = new TIntHashSet();
        keys.addAll(keys());
        keys.retainAll(vector.keys());
        keys.forEach(new TIntProcedure() {
            @Override
            public boolean execute(int value) {
                return callback.map(value, get(value), vector.get(value));
            }
        });
    }

    @Override
    public int[] keys() {
        return data.keys();
    }

    @Override
    public float l2norm() {
        float res = 0;
        for(float v : data.values()) {
            res += v*v;
        }
        return (float) Math.sqrt(res);
    }
}
