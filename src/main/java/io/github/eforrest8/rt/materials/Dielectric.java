package io.github.eforrest8.rt.materials;

import io.github.eforrest8.rt.geometry.Hit;
import io.github.eforrest8.rt.geometry.Ray;
import io.github.eforrest8.rt.geometry.Vector;

import java.util.Optional;

public class Dielectric implements Material {

    private final double refractionIndex;

    public Dielectric(double refractionIndex) {
        this.refractionIndex = refractionIndex;
    }

    @Override
    public Optional<Ray> scatter(Ray r, Hit hit) {
        double refractionRatio = hit.frontFace ? (1.0 / refractionIndex) : refractionIndex;
        Vector unitDirection = r.direction.unitVector();
        double cosTheta = Math.min(unitDirection.negate().dot(hit.normal), 1.0);
        double sinTheta = Math.sqrt(1.0 - cosTheta*cosTheta);
        boolean cannotRefract = (refractionRatio * sinTheta) > 1.0;
        Vector direction;
        if (cannotRefract || reflectance(cosTheta, refractionRatio) > Math.random()) {
            direction = unitDirection.reflect(hit.normal);
        } else {
            direction = unitDirection.refract(hit.normal, refractionRatio);
        }
        return Optional.of(new Ray(hit.p, direction));
    }

    @Override
    public Vector attenuation(Ray r, Hit hit) {
        return new Vector(1, 1, 1);
    }

    private double reflectance(double cosine, double refractionIndex) {
        // Schlick's approximation for reflectance
        double r0 = (1-refractionIndex) / (1+refractionIndex);
        r0 = r0*r0;
        return r0 + (1-r0)*Math.pow((1-cosine), 5);
    }
}
