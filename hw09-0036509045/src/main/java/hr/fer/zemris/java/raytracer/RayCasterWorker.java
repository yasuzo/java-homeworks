package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.*;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("Duplicates")
public class RayCasterWorker extends RecursiveAction {

    private static final int MAX_CALCULATION_ROWS = 10;

    private int width;
    private int height;
    private double horizontal;
    private double vertical;

    private Point3D xAxis;
    private Point3D yAxis;
    private Point3D screenCorner;
    private Point3D eye;

    private Scene scene;

    private short[] red;
    private short[] green;
    private short[] blue;

    private AtomicBoolean cancel;

    private int minY;
    private int maxY;

    /**
     * Constructs a new ray cast worker with given attributes.
     *
     * @param width        Width of the plane.
     * @param height       Height of the plane.
     * @param horizontal   Horizontal width of observed space.
     * @param vertical     Vertical width of observed space.
     * @param xAxis        x axis of the scene.
     * @param yAxis        y axis of the scene.
     * @param screenCorner Location of the corner.
     * @param eye          Location of the observer.
     * @param scene        Scene that is rendered.
     * @param red          Array of red color, one slot for one pixel.
     * @param green        Array of green color, one slot for one pixel.
     * @param blue         Array of blue color, one slot for one pixel.
     * @param cancel       Flag that indicates if calculation should be canceled.
     * @param minY         First row that this worker has to calculate.
     * @param maxY         First row that worker should NOT calculate.
     */
    public RayCasterWorker(int width, int height, double horizontal, double vertical, Point3D xAxis, Point3D yAxis, Point3D screenCorner, Point3D eye,
                           Scene scene, short[] red, short[] green, short[] blue, AtomicBoolean cancel, int minY, int maxY) {
        this.width = width;
        this.height = height;
        this.horizontal = horizontal;
        this.vertical = vertical;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.screenCorner = screenCorner;
        this.eye = eye;
        this.scene = scene;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.cancel = cancel;
        this.minY = minY;
        this.maxY = maxY;
    }

    /**
     * Determines which object should be painted by given ray and fills rgb array with appropriate colors.
     *
     * @param scene Scene that is being rendered.
     * @param ray   Ray of the observer.
     * @param rgb   Array that needs to be filled.
     * @throws NullPointerException     If any of the arguments are {@code null}.
     * @throws IllegalArgumentException If {@code rbg.length != 3}.
     */
    private static void tracer(Scene scene, Ray ray, short[] rgb) {
        Objects.requireNonNull(scene);
        Objects.requireNonNull(ray);
        Objects.requireNonNull(rgb);
        if (rgb.length != 3) {
            throw new IllegalArgumentException("Array size should be 3.");
        }
        RayIntersection closestIntersection = closestOfAnyObject(scene.getObjects(), ray);
        determineColor(scene, closestIntersection, ray.start, rgb);
    }

    /**
     * Determines color of the pixel.
     *
     * @param scene        Scene in which pixel is located.
     * @param intersection Intersection of eye view ray and the closest object.
     * @param view         View point.
     * @param rgb          Array that needs to be filled with color.
     */
    private static void determineColor(Scene scene, RayIntersection intersection, Point3D view, short[] rgb) {
        if (intersection == null) {
            rgb[0] = rgb[1] = rgb[2] = 0;
            return;
        }
        rgb[0] = rgb[1] = rgb[2] = 15;
        for (LightSource s : scene.getLights()) {
            Ray ray = Ray.fromPoints(s.getPoint(), intersection.getPoint());
            RayIntersection lightIntersection = closestOfAnyObject(scene.getObjects(), ray);

//            skip light source if it is blocked by another object
            if (lightIntersection != null && s.getPoint().sub(intersection.getPoint()).norm() - lightIntersection.getDistance() > 1e-10) {
                continue;
            }

//            calculate color factors
            Point3D l = s.getPoint().sub(intersection.getPoint()).normalize().negate();
            Point3D n = intersection.getNormal().normalize();
            Point3D r = l.sub(n.scalarMultiply(2 * n.scalarProduct(l)));
            Point3D v = view.sub(intersection.getPoint()).normalize();
            double lDotN = Math.max(l.negate().scalarProduct(n), 0);
            double rDotV = r.scalarProduct(v);

//            calculate color
            rgb[0] += s.getR() * (intersection.getKdr() * lDotN + intersection.getKrr() * Math.pow(rDotV, intersection.getKrn()));
            rgb[1] += s.getG() * (intersection.getKdg() * lDotN + intersection.getKrg() * Math.pow(rDotV, intersection.getKrn()));
            rgb[2] += s.getB() * (intersection.getKdb() * lDotN + intersection.getKrb() * Math.pow(rDotV, intersection.getKrn()));
        }
    }

    /**
     * Returns closest intersection of ray and any object in the scene.
     *
     * @param objects Objects in the scene.
     * @param ray     Ray whose intersection this will look for.
     * @return Closest intersection or {@code null} if there aren't any.
     * @throws NullPointerException If any of the arguments are {@code null}.
     */
    private static RayIntersection closestOfAnyObject(Collection<GraphicalObject> objects, Ray ray) {
        Objects.requireNonNull(objects);
        Objects.requireNonNull(ray);
        RayIntersection closest = null;
        for (GraphicalObject o : objects) {
            RayIntersection intersection = o.findClosestRayIntersection(ray);
            if (intersection != null && (closest == null || closest.getDistance() > intersection.getDistance())) {
                closest = intersection;
            }
        }
        return closest;
    }

    @Override
    protected void compute() {
        int rowsToExecute = maxY - minY;
        if (rowsToExecute <= MAX_CALCULATION_ROWS) {
            calculate();
            return;
        }
        invokeAll(
                new RayCasterWorker(width, height, horizontal, vertical, xAxis, yAxis, screenCorner, eye, scene,
                        red, green, blue, cancel, minY, minY + rowsToExecute / 2),
                new RayCasterWorker(width, height, horizontal, vertical, xAxis, yAxis, screenCorner, eye, scene,
                        red, green, blue, cancel, minY + rowsToExecute / 2, maxY)
        );
    }

    /**
     * Calculate pixels that this workers has gotten.
     */
    private void calculate() {
        short[] rgb = new short[3];
        int offset = minY * width;

//            determine color for each pixel on the screen starting from upper left corner
        for (int y = minY; y < maxY; y++) {
            if (cancel.get()) {
                return;
            }
            for (int x = 0; x < width; x++) {
                Point3D screenPoint = screenCorner.add(xAxis.scalarMultiply(x / (width - 1.0) * horizontal))
                        .sub(yAxis.scalarMultiply(y / (height - 1.0) * vertical));
                Ray ray = Ray.fromPoints(eye, screenPoint);
                tracer(scene, ray, rgb);
                red[offset] = rgb[0] > 255 ? 255 : rgb[0];
                green[offset] = rgb[1] > 255 ? 255 : rgb[1];
                blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
                offset++;
            }
        }
    }
}
