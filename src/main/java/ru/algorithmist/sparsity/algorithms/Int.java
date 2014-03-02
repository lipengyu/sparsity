package ru.algorithmist.sparsity.algorithms;

/**
 * @author Sergey Edunov
 */
public class Int<T> {

    private int value;
    private T obj;

    public Int(int value, T obj) {
        this.value = value;
        this.obj = obj;
    }

    public Int(int value) {
        this.value = value;
    }

    public void inc(int delta){
        value+=delta;
    }

    public int getValue() {
        return value;
    }

    public T getObj() {
        return obj;
    }
}
