package hr.fer.zemris.java.hw11.jnotepadpp;

import java.util.Objects;
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
    private static ExecutorService getExecutor() {
        if(executor == null) {
            executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        }
        return executor;
    }

    /**
     * Submits given task to be executed.
     *
     * @param task Task that needs to be executed.
     * @throws NullPointerException If the task is {@code null}.
     */
    public static void submit(Runnable task) {
        getExecutor();
        if(executor.isShutdown()) return;
        executor.submit(task);
    }

    /**
     * Shuts down executor service.
     */
    public static void shutdown() {
        if (executor == null) return;
        executor.shutdown();
    }
}
