package hr.fer.zemris.java.fractals.util.concurrent;

import java.util.concurrent.ThreadFactory;

/**
 * Thread factory for daemonic threads.
 *
 * @author Jan Capek
 */
public class DaemonicThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    }
}
