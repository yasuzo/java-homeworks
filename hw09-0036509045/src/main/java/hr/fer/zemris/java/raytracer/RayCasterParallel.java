package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

import java.util.concurrent.ForkJoinPool;

@SuppressWarnings("Duplicates")
public class RayCasterParallel {
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

//            create fork-join pool & invoke worker
            ForkJoinPool pool = new ForkJoinPool(8);
            pool.invoke(new RayCasterWorker(width, height, horizontal, vertical, xAxis, yAxis, screenCorner, eye, scene,
                    red, green, blue, cancel, 0, height));
            pool.shutdown();

//            send results
            System.out.println("Izračuni gotovi...");
            observer.acceptResult(red, green, blue, requestNo);
            System.out.println("Dojava gotova...");
        };
    }

}
