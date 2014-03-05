package ru.algorithmist.sparsity.algorithms.similarity;

import ru.algorithmist.sparsity.data.Vector;
import ru.algorithmist.sparsity.data.bit.BitVector;
import ru.algorithmist.sparsity.utils.VectorUtils;

import java.util.BitSet;

/**
 * @author Sergey Edunov
 */
public class RPCosineSimilarity implements SimilarityMeasure{
    @Override
    public float distance(Vector v1, Vector v2) {
        return 1 - VectorUtils.cosineSimilarity((BitVector)v1, (BitVector)v2);
    }
}
