package io.github.eforrest8.rt.materials;

import io.github.eforrest8.rt.RTUtilities;
import io.github.eforrest8.rt.geometry.Hit;
import io.github.eforrest8.rt.geometry.Ray;
import io.github.eforrest8.rt.geometry.Vector;

import java.util.Optional;

public class Lambertian implements Material {

    private final Vector albedo;

    public Lambertian(Vector albedo) {
        this.albedo = albedo;
    }

    @Override
    public Optional<Ray> scatter(Ray r, Hit hit) {
        var scatterDirection = hit.normal.add(RTUtilities.randomUnitVector());
        if (scatterDirection.isNearZero()) {
            scatterDirection = hit.normal;
        }
        return Optional.of(new Ray(hit.p, scatterDirection));
    }

    @Override
    public Vector attenuation(Ray r, Hit hit) {
        return albedo;
    }
}
