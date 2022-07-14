package io.github.eforrest8.rt.materials;

import io.github.eforrest8.rt.RTUtilities;
import io.github.eforrest8.rt.geometry.Hit;
import io.github.eforrest8.rt.geometry.Ray;
import io.github.eforrest8.rt.geometry.Vector;

import java.util.Optional;

public class Metal implements Material {
    private final Vector albedo;
    private final double fuzz;

    public Metal(Vector a, double f) {
        albedo = a;
        fuzz = f < 1 ? f : 1;
    }

    @Override
    public Optional<Ray> scatter(Ray r, Hit hit) {
        Vector reflected = r.direction.unitVector().reflect(hit.normal);
        Ray scattered = new Ray(hit.p, reflected.add(RTUtilities.randomInUnitSphere().multiply(fuzz)));
        return (scattered.direction.dot(hit.normal) > 0) ? Optional.of(scattered) : Optional.empty();
    }

    @Override
    public Vector attenuation(Ray r, Hit hit) {
        return albedo;
    }
}
