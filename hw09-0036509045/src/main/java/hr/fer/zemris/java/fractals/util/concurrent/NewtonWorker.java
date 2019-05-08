package hr.fer.zemris.java.fractals.util.concurrent;

import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Instances of this class are used to calculate Newton's fractal.
 *
 * @author Jan Capek
 */
public class NewtonWorker implements Callable<short[]> {

    private static final double CONVERGENCE_THRESHOLD = 1e-3;
    private static final double ROOT_THRESHOLD = 1e-2;

    private final int maxIterations;


    private double minRe;
    private double maxRe;
    private double minIm;
    private double maxIm;
    private int width;
    private int height;
    private int minY;
    private int maxY;

    private ComplexRootedPolynomial polynomial;
    private ComplexPolynomial derived;

    /**
     * Constructs a new worker that computes fractals for given range of complex numbers and for given range of raster pixels.
     *
     * @param polynomial Polynomial on which a fractal is based.
     * @param minRe    Minimal real part of a complex number.
     * @param maxRe    Maximal real part of a complex number.
     * @param minIm    Minimal imaginary part of a complex number.
     * @param maxIm    Maximal imaginary part of a complex number.
     * @param width     Plain's width.
     * @param height      Plain's height.
     * @param minY       Minimal y coordinate of a pixel.
     * @param maxY       Maximal y coordinate of a pixel.
     * @throws NullPointerException If given polynomial is {@code null}.
     */
    public NewtonWorker(ComplexRootedPolynomial polynomial,
                        double minRe, double maxRe, double minIm, double maxIm, int width, int height, int minY, int maxY) {
        this.polynomial = Objects.requireNonNull(polynomial);
        derived = polynomial.toComplexPolynom().derive();
        maxIterations = polynomial.toComplexPolynom().order() + 1;
        this.minRe = minRe;
        this.maxRe = maxRe;
        this.minIm = minIm;
        this.maxIm = maxIm;
        this.width = width;
        this.height = height;
        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    public short[] call() throws Exception {
        short[] data = new short[(maxY - minY) * width];
        int offset = 0;

        for (int y = minY; y < maxY; y++) {
            for (int x = 0; x < width; x++) {
                Complex z = mapToComplexPlain(x, y, width, height, minRe, maxRe, minIm, maxIm);
                int iter = 0;
                Complex zOld;
                do{
                    Complex numerator = polynomial.apply(z);
                    Complex denominator = derived.apply(z);
                    zOld = z;
                    z = z.sub(numerator.divide(denominator));
                } while (z.sub(zOld).module() > CONVERGENCE_THRESHOLD && iter < maxIterations);
                int index = polynomial.indexOfClosestRootFor(z, ROOT_THRESHOLD);
                data[offset++] = (short) (index + 1);
            }
        }
        return data;
    }

    /**
     * Maps pixels coordinate to a complex plain.
     *
     * @param x Pixel's x coordinate.
     * @param y Pixel's y coordinate.
     * @param width Plain's width.
     * @param height Plain's height.
     * @param minRe Minimal real part of a complex number.
     * @param maxRe Maximal real part of a complex number.
     * @param minIm Minimal imaginary part of a complex number.
     * @param maxIm Maximal imaginary part of a complex number.
     * @return Mapped complex number.
     */
    private Complex mapToComplexPlain(int x, int y, int width, int height, double minRe, double maxRe, double minIm, double maxIm) {
        double real = x / (width-1.0) * (maxRe - minRe) + minRe;
        double imag = (height-1.0-y) / (height-1) * (maxIm - minIm) + minIm;
        return new Complex(real, imag);
    }
}
