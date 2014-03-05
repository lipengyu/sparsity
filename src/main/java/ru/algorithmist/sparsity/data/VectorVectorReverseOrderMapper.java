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
    public boolean map(int index, float v1, float v2) {
        return base.map(index, v2, v1);
    }
}
