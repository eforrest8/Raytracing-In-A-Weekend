package io.github.eforrest8.rt.geometry;

import io.github.eforrest8.rt.materials.Material;

import java.util.Optional;

public class Sphere implements Hittable {

    public Vector center;
    public double radius;
    public Material material;

    public Sphere(Vector center, double radius, Material material) {
        this.center = center;
        this.radius = radius;
        this.material = material;
    }

    @Override
    public Optional<Hit> hit(Ray ray, double t_min, double t_max) {
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
        
        return Optional.of(new Hit(
                ray,
                root,
                this::getNormal,
                this.material));
    }

    public Vector getNormal(Vector p) {
        return (p.subtract(center)).divide(radius);
    }
}
