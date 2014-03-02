package ru.algorithmist.sparsity.text;

import java.util.Iterator;

public class TestInputSource implements Iterator<String> {

    private String[] data = new String[] {
            "Partial least squares regression (PLS regression) is a statistical method that bears some relation to principal components regression;",
            "Principal component regression (PCR) is a particular regression analysis technique in statistics that is based on principal component analysis",
            "Statistics is the study of the collection, organization, analysis, interpretation and presentation of data.",
            "In statistics, linear regression is an approach to model the relationship between a scalar dependent variable y and one or more explanatory variables denoted X",
            "In statistics, simple linear regression is the least squares estimator of a linear regression model with a single explanatory variable"
    };

    private int pos = 0;



    @Override
    public boolean hasNext() {
        return pos < data.length;
    }

    @Override
    public String next() {
        return data[pos];
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
