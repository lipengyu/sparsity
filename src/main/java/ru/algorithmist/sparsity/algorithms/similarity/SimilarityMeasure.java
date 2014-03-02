package ru.algorithmist.sparsity.algorithms.similarity;

import ru.algorithmist.sparsity.data.Vector;
import ru.algorithmist.sparsity.utils.VectorUtils;

/**
 * @author Sergey Edunov
 */
public interface SimilarityMeasure {

    public float distance(Vector v1, Vector v2);

}
