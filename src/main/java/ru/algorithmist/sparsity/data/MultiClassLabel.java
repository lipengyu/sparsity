package ru.algorithmist.sparsity.data;

import java.util.Arrays;

/**
 * @author Sergey Edunov
 */
public class MultiClassLabel {

    private int[] classes;

    public MultiClassLabel(int[] classes){
        this.classes = classes;
    }

    public int[] getClasses() {
        return classes;
    }

    public String toString() {
        return Arrays.toString(classes);
    }
}
