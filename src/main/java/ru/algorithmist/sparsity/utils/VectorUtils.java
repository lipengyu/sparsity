package ru.algorithmist.sparsity.utils;

import ru.algorithmist.sparsity.data.Vector;
import ru.algorithmist.sparsity.data.VectorVectorMapper;
import ru.algorithmist.sparsity.data.bit.BitVector;

import java.util.BitSet;

/**
 * @author Sergey Edunov
 */
public class VectorUtils {

    public static float distance(Vector v1, Vector v2){
        VectorDistanceCalc dist = new VectorDistanceCalc();
        v1.union(v2, dist);
        return dist.getRes();
    }

    public static float cosineSimilarity(Vector v1, Vector v2) {
        CosineSimilarityCalc dist = new CosineSimilarityCalc();
        v1.intersect(v2, dist);
        return dist.getRes() / (v1.l2norm() * v2.l2norm());
    }

    public static float cosineSimilarity(BitVector s1, BitVector s2) {
        return (float) Math.cos(s1.hammingDistance(s2) * Math.PI / s1.size());
    }

    private static class CosineSimilarityCalc implements VectorVectorMapper {

        private float res;

        @Override
        public boolean map(int index, float v1, float v2) {
            res += v1*v2;
            return true;
        }

        public float getRes(){
            return res;
        }
    }

    private static class VectorDistanceCalc implements VectorVectorMapper {

        private float res;

        public float getRes() {
            return (float) Math.sqrt(res);
        }

        @Override
        public boolean map(int index, float v1, float v2) {
            res += (v1-v2)*(v1-v2);
            return true;
        }
    }
}
