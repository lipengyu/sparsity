package ru.algorithmist.sparsity.algorithms;


import cern.colt.matrix.linalg.QRDecomposition;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import ru.algorithmist.sparsity.data.DenseVector;
import ru.algorithmist.sparsity.data.Matrix;
import ru.algorithmist.sparsity.data.Vector;
import ru.algorithmist.sparsity.utils.ColtUtils;
import ru.algorithmist.sparsity.utils.CommonsUtils;
import ru.algorithmist.sparsity.utils.RandomUtils;

public class RandomizedSVD {

    private Matrix V, U;
    private Vector S;


    public RandomizedSVD(Matrix a, int nComponents, int nIter) {
        boolean transpose = a.rows() < a.cols();
        if (transpose) {
            a = a.transpose();
        }

        nComponents = Math.min(nComponents, a.cols());
        Matrix q = randomizedRangeFinder(a, nComponents + 10, nIter);

        Matrix b = q.transpose().dot(a);

        SingularValueDecomposition svd = new SingularValueDecomposition(CommonsUtils.matrix(b));
        double [] s = svd.getSingularValues();
        Matrix uHat = CommonsUtils.matrix(svd.getU());
        Matrix u = q.dot(uHat);

        Matrix v = CommonsUtils.matrix(svd.getV());

        V = v.slice(0, 0, v.rows(), Math.min(nComponents, s.length));
        S = new DenseVector(s).slice(0, Math.min(nComponents, s.length));
        U = u.slice(0, 0, u.rows(), nComponents);

        if (transpose) {
            Matrix t = V.transpose();
            V = U.transpose();
            U = t.transpose();
        }
    }

    private static Matrix randomizedRangeFinder(Matrix a, int size, int nIter) {
        Matrix r = RandomUtils.normal(a.cols(), size);


        Matrix y = a.dot(r);
        for(int i=0; i<nIter; i++) {
            y = a.dot(a.transpose().dot(y));
        }

        QRDecomposition decomposition = new QRDecomposition(ColtUtils.matrix(y));
        return ColtUtils.matrix(decomposition.getQ());
    }

    public Matrix getV() {
        return V;
    }

    public Vector getS() {
        return S;
    }

    public Matrix getU() {
        return U;
    }
}
