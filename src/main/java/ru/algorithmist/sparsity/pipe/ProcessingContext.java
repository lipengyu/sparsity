package ru.algorithmist.sparsity.pipe;

import ru.algorithmist.sparsity.utils.StringFormat;

/**
 * @author Sergey Edunov
 */
public class ProcessingContext {

    private ProgressReporter reporter = new ProgressReporter() {

        private String prevWarning = "";
        private long lastMessage;
        private long firstMessage;
        private String lastKey;
        private boolean disabled = false;

        @Override
        public void reportProgress(String name, double progress) {
            if (disabled) return;
            long curTime = System.currentTimeMillis();
            if (name.equals(lastKey)) {
                if (lastMessage > curTime - 1000) {
                    return;
                }
            }
            lastMessage = curTime;
            if (!name.equals(lastKey)) {
                lastKey = name;
                firstMessage = curTime;
            }
            long timeSpent = (curTime - firstMessage);
            if (progress < 1 & progress > 1e-5) { //we can estimate remaining time
                System.out.println(name + " [" + StringFormat.formatTime(timeSpent) + " / " + StringFormat.formatTime((long)((1-progress) * timeSpent / progress)) + " remaining] " + progress);
            } else {
                System.out.println(name + " [" + StringFormat.formatTime(timeSpent) + "] " + progress);
            }
        }

        @Override
        public void reportWarning(String s) {
            if (disabled) return;
            if (!s.equals(prevWarning)) {
                System.out.println("WARN: " + s);
            } else {
                System.out.print(".");
            }
            prevWarning = s;
        }

        @Override
        public void disable() {
            disabled = true;
        }

        @Override
        public void enable() {
            disabled = false;
        }
    };

    public ProgressReporter getProgressReporter() {
        return reporter;
    }
}
