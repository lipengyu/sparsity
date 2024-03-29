package ru.algorithmist.sparsity.data;

/**
 * @author Sergey Edunov
 */
public class DataFrame<P> {

    private Predictors<P> predictors;
    private BasicMatrix data;
    private int rows;

    public DataFrame(Predictors<P> predictors, BasicMatrix data) {
        assert predictors.size() == data.rows();
        this.predictors = predictors;
        this.data = data;
        this.rows = data.rows();
    }


    public P getPredictor(int row){
        return predictors.get(row);
    }

    public Vector getData(int row) {
        return data.get(row);
    }

    public BasicMatrix getData() {
        return data;
    }

    public Predictors<P> getPredictors() {
        return predictors;
    }

    public int getRows() {
        return rows;
    }

    public String toString() {
        StringBuilder res = new StringBuilder();
        for(int i=0; i<predictors.size() && i<10; i++){
            res.append(predictors.get(i).toString()).append("  :  ");
            res.append(data.get(i).toString());
            res.append("\n");
        }
        return res.toString();
    }


}
