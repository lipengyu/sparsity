package ru.algorithmist.sparsity.pipe;

/**
 * @author Sergey Edunov
 */
public class ProcessingContext {

    private ProgressReporter reporter = new ProgressReporter() {

        private String prevWarning = "";
        @Override
        public void reportProgress(String name, double progress) {
            System.out.println(name + " " + progress);
        }

        @Override
        public void reportWarning(String s) {
            if (!s.equals(prevWarning)) {
                System.out.println("WARN: " + s);
            } else {
                System.out.print(".");
            }
            prevWarning = s;
        }
    };

    public ProgressReporter getProgressReporter() {
        return reporter;
    }
}
