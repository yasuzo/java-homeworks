package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.*;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

import java.util.Collection;
import java.util.Objects;

/**
 * Ray caster rendering program.
 *
 * @author Jan Capek
 */
public class RayCaster {

    public static void main(String[] args) {
        RayTracerViewer.show(getIRayTracerProducer(),
                new Point3D(10, 0, 0),
                new Point3D(0, 0, 0),
                new Point3D(0, 0, 10),
                20, 20);
    }

    /**
     * @return Ray tracer producer.
     */
    private static IRayTracerProducer getIRayTracerProducer() {
        return (eye, view, viewUp, horizontal, vertical, width, height, requestNo, observer, cancel) -> {
            System.out.println("Započinjem izračune...");
            short[] red = new short[width * height];
            short[] green = new short[width * height];
            short[] blue = new short[width * height];
//            calculate axis
            Point3D normalizedViewUp = viewUp.normalize();
            Point3D zAxis = view.sub(eye).normalize();
            Point3D yAxis = normalizedViewUp.sub(zAxis.scalarMultiply(zAxis.scalarProduct(normalizedViewUp))).normalize();
            Point3D xAxis = zAxis.vectorProduct(yAxis);
            Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal / 2.0)).add(yAxis.scalarMultiply(vertical / 2.0));
//            create scene
            Scene scene = RayTracerViewer.createPredefinedScene();

            short[] rgb = new short[3];
            int offset = 0;

//            determine color for each pixel on the screen starting from upper left corner
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (cancel.get()) {
                        return;
                    }
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
            System.out.println("Izračuni gotovi...");
            observer.acceptResult(red, green, blue, requestNo);
            System.out.println("Dojava gotova...");
        };
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
}
