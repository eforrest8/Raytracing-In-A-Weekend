package io.github.eforrest8.rt.geometry;

import java.util.Optional;

public class Sphere implements Hittable {

    public Vector center;
    public double radius;

    public Sphere(Vector center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    public Optional<HitRecord> hit(Ray ray, double t_min, double t_max) {
        Vector oc = ray.origin.subtract(center);
        double a = ray.direction.length_squared();
        double half_b = oc.dot(ray.direction);
        double c = oc.length_squared() - radius * radius;

        double discriminant = half_b * half_b - a * c;
        if (discriminant < 0) {
            return Optional.empty();
        }
        double sqrtd = Math.sqrt(discriminant);

        //find the nearest root that lies in the acceptable range
        double root = (-half_b - sqrtd) / a;
        if (root < t_min || root > t_max) {
            root = (-half_b + sqrtd) / a;
            if (root < t_min || root > t_max) {
                return Optional.empty();
            }
        }

        HitRecord rec = new HitRecord();
        rec.t = root;
        rec.p = ray.at(root);
        Vector outwardNormal = (rec.p.subtract(center)).divide(radius);
        rec.setFaceNormal(ray, outwardNormal);
        return Optional.of(rec);
    }
}
