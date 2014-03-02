package ru.algorithmist.sparsity.data;

/**
 * @author Sergey Edunov
 */
public class VectorVectorReverseOrderMapper implements VectorVectorMapper {

    private VectorVectorMapper base;

    public VectorVectorReverseOrderMapper(VectorVectorMapper base){
        this.base = base;
    }

    @Override
    public void map(int index, float v1, float v2) {
        base.map(index, v2, v1);
    }
}
