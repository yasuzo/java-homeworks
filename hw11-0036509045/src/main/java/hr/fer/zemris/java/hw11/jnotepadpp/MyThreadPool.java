package hr.fer.zemris.java.hw11.jnotepadpp;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Singleton thread pool.
 *
 * @author Jan Capek
 */
public class MyThreadPool {

    private static ExecutorService executor;

    /**
     * Constructs an executor service and returns it.
     *
     * @return Executor service.
     */
    public static ExecutorService getExecutor() {
        if(executor == null) {
            executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        }
        return executor;
    }

    /**
     * Shuts down executor service.
     */
    public static void shutdown() {
        if (executor == null) return;
        executor.shutdown();
    }
}
