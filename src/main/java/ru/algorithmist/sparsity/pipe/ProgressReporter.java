package ru.algorithmist.sparsity.pipe;

/**
 * @author Sergey Edunov
 */
public interface ProgressReporter {

    public void reportProgress(String name, double progress);

    public void reportWarning(String s);
}
