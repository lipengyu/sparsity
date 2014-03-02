package ru.algorithmist.sparsity.algorithms.similarity;

import ru.algorithmist.sparsity.data.Vector;
import ru.algorithmist.sparsity.utils.VectorUtils;

/**
 * @author Sergey Edunov
 */
public class CosineSimilarity implements SimilarityMeasure {
    @Override
    public float distance(Vector v1, Vector v2) {
        return VectorUtils.cosineSimilarity(v1, v2);
    }
}
