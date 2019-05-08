package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.IRayTracerAnimator;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

import java.util.concurrent.ForkJoinPool;

/**
 * Animated parallel scene ray casting program.
 *
 * @author Jan Capek
 */
@SuppressWarnings("Duplicates")
public class RayCasterParallel2 {
    public static void main(String[] args) {
        RayTracerViewer.show(
                getIRayTracerProducer(), getIRayTracerAnimator(), 30, 30
        );
    }

    /**
     * @return RayTracer animator.
     */
    private static IRayTracerAnimator getIRayTracerAnimator() {
        return new IRayTracerAnimator() {
            long time;
            @Override
            public void update(long deltaTime) {
                time += deltaTime;
            }
            @Override
            public Point3D getViewUp() { // fixed in time
                return new Point3D(0,0,10);
            }
            @Override
            public Point3D getView() { // fixed in time
                return new Point3D(-2,0,-0.5);
            }
            @Override
            public long getTargetTimeFrameDuration() {
                return 150; // redraw scene each 150 milliseconds
            }
            @Override
            public Point3D getEye() { // changes in time
                double t = (double)time / 10000 * 2 * Math.PI;
                double t2 = (double)time / 5000 * 2 * Math.PI;
                double x = 50*Math.cos(t);
                double y = 50*Math.sin(t);
                double z = 30*Math.sin(t2);
                return new Point3D(x,y,z);
            }
        };
    }

    /**
     * @return Ray tracer producer.
     */
    private static IRayTracerProducer getIRayTracerProducer() {
        return (eye, view, viewUp, horizontal, vertical, width, height, requestNo, observer, cancel) -> {
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
            Scene scene = RayTracerViewer.createPredefinedScene2();

//            create fork-join pool & invoke worker
            ForkJoinPool pool = new ForkJoinPool(8);
            pool.invoke(new RayCasterWorker(width, height, horizontal, vertical, xAxis, yAxis, screenCorner, eye, scene,
                    red, green, blue, cancel, 0, height));
            pool.shutdown();

//            send results
            observer.acceptResult(red, green, blue, requestNo);
        };
    }
}
