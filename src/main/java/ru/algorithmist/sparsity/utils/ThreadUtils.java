package ru.algorithmist.sparsity.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Sergey Edunov
 */
public class ThreadUtils {

    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static <V> Future<V> schedule(Callable<V> call) {
        return executorService.submit(call);
    }

}
