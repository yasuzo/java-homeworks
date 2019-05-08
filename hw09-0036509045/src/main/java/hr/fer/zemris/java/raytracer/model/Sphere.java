package hr.fer.zemris.java.raytracer.model;

import java.util.Objects;

/**
 * Model of a sphere.
 *
 * @author Jan Capek
 */
public class Sphere extends GraphicalObject {

    private Point3D center;
    private double radius;

    private double kdr;
    private double kdg;
    private double kdb;

    private double krr;
    private double krg;
    private double krb;
    private double krn;

    /**
     * Constructs a new sphere with given properties.
     *
     * @param center Center coordinates of the sphere.
     * @param radius Sphere's radius.
     * @param kdr    Coefficient of red diffuse reflection intensity.
     * @param kdg    Coefficient of green diffuse reflection intensity.
     * @param kdb    Coefficient of blue diffuse reflection intensity.
     * @param krr    Coefficient of red mirror reflection intensity.
     * @param krg    Coefficient of green mirror reflection intensity.
     * @param krb    Coefficient of blue mirror reflection intensity.
     * @param krn    Shininess factor of mirror reflection.
     * @throws NullPointerException     If given center is {@code null}.
     * @throws IllegalArgumentException If radius < 0.
     */
    public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb, double krr, double krg, double krb, double krn) {
        this.center = Objects.requireNonNull(center);
        if (radius < 0) {
            throw new IllegalArgumentException("Radius cannot be less than 0.");
        }
        this.radius = radius;
        this.kdr = kdr;
        this.kdg = kdg;
        this.kdb = kdb;
        this.krr = krr;
        this.krg = krg;
        this.krb = krb;
        this.krn = krn;
    }

    @Override
    public RayIntersection findClosestRayIntersection(Ray ray) {

        double a = ray.direction.scalarProduct(ray.direction);
        double b = ray.direction.scalarMultiply(2).scalarProduct(ray.start.sub(center));
        Point3D tmp = ray.start.sub(center);
        double c = tmp.scalarProduct(tmp) - radius * radius;

        double discriminant = b * b - 4 * a * c;

//        there are no intersections.
        if(discriminant < 0) {
            return null;
        }

//        calculate t in: ray.start + t * ray.direction
        double t1 = (-b + Math.sqrt(discriminant)) / (2 * a);
        double t2 = (-b - Math.sqrt(discriminant)) / (2 * a);

//        both intersections happened behind the camera
        if(t1 < 0 && t2 < 0) {
            return null;
        }

        double t;
        boolean isOuter;
        if(t1 < 0 || t2 < 0) {
//            one intersection is behind the camera => pick one that is not
            t = t1 < t2 ? t2 : t1;
            isOuter = true;
        } else {
//            both intersections are in front of the camera => pick one that is closer.
            t = t1 < t2 ? t1 : t2;
            isOuter = false;
        }
        Point3D intersection = ray.start.add(ray.direction.scalarMultiply(t));
        return new SphereRayIntersection(intersection, intersection.sub(ray.start).norm(), isOuter);
    }

    /**
     * Sphere intersection object.
     */
    private class SphereRayIntersection extends RayIntersection {

        private Point3D normal;

        /**
         * Constructs a new intersection ray intersection with sphere.
         *
         * @param point Point of intersection.
         * @param distance Distance between ray's origin and intersection point.
         * @param isOuter Flag that indicates if origin of ray is inside the sphere.
         */
        private SphereRayIntersection(Point3D point, double distance, boolean isOuter) {
            super(point, distance, isOuter);
            normal = point.sub(Sphere.this.center).normalize();
        }

        @Override
        public Point3D getNormal() {
            return normal;
        }

        @Override
        public double getKdr() {
            return kdr;
        }

        @Override
        public double getKdg() {
            return kdg;
        }

        @Override
        public double getKdb() {
            return kdb;
        }

        @Override
        public double getKrr() {
            return krr;
        }

        @Override
        public double getKrg() {
            return krg;
        }

        @Override
        public double getKrb() {
            return krb;
        }

        @Override
        public double getKrn() {
            return krn;
        }
    }
}
