package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.util.concurrent.DaemonicThreadFactory;
import hr.fer.zemris.java.fractals.util.concurrent.NewtonWorker;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Fractal producer that produces Newton's fractal.
 *
 * @author Jan Capek
 */
public class NewtonFractalProducer implements IFractalProducer {

    private ComplexRootedPolynomial polynomial;
    private ExecutorService executor;

    /**
     * Constructs a new producer based on given polynomial.
     *
     * @param polynomial Polynomial on which a producer is based.
     * @throws NullPointerException If given polynomial is {@code null}.
     */
    public NewtonFractalProducer(ComplexRootedPolynomial polynomial) {
        this.polynomial = Objects.requireNonNull(polynomial);
        executor = Executors.newFixedThreadPool(8, new DaemonicThreadFactory());
    }

    @Override
    public void produce(double reMin, double reMax, double imMin, double imMax,
                        int width, int height, long requestNo, IFractalResultObserver observer) {

//        create workers
        int jobNumber = 8 * 8;
        int numberOfRowsPerJob = height / jobNumber;
        List<NewtonWorker> workers = new ArrayList<>(jobNumber);
        for (int i = 0; i < jobNumber - 1; i++) {
            int minY = i * numberOfRowsPerJob;
            int maxY = i * numberOfRowsPerJob + numberOfRowsPerJob;
            NewtonWorker worker = new NewtonWorker(polynomial, reMin, reMax, imMin, imMax, width, height, minY, maxY);
            workers.add(worker);
        }
        int minY = (jobNumber - 1) * numberOfRowsPerJob;
        NewtonWorker worker = new NewtonWorker(polynomial, reMin, reMax, imMin, imMax, width, height, minY, height);
        workers.add(worker);

//        submit workers
        List<Future<short[]>> futures = new ArrayList<>(workers.size());
        for (NewtonWorker w : workers) {
            futures.add(executor.submit(w));
        }

//        fill data array
        short[] data = new short[width * height];
        int offset = 0;
        for (Future<short[]> f : futures) {
            for (short bit : getData(f)) {
                data[offset++] = bit;
            }
        }
//        send data
        observer.acceptResult(data, (short) (polynomial.toComplexPolynom().order() + 1), requestNo);
    }

    /**
     * Waits for data to be returned by given future.
     *
     * @param f Future which needs to return data.
     * @return Data returned by the future.
     * @throws NullPointerException If given future is {@code null}.
     */
    private short[] getData(Future<short[]> f) {
        Objects.requireNonNull(f);
        short[] workerData;
        while (true) {
            try {
                workerData = f.get();
            } catch (InterruptedException e) {
                continue;
            } catch (ExecutionException e) {
                throw new RuntimeException(e.getMessage());
            }
            break;
        }
        return workerData;
    }
}