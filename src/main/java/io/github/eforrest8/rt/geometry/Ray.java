package io.github.eforrest8.rt.geometry;

import java.util.Optional;

public class Ray {
    public final Vector origin;
    public final Vector direction;

    public Ray(Vector origin, Vector direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Vector at(double t) {
        return origin.add(direction.multiply(t));
    }

    public Vector rayColor(Hittable target, int depth) {
        if (depth <= 0) {
            return new Vector(0,0,0);
        }

        Optional<Hit> hit = target.hit(this, 0.001, Double.POSITIVE_INFINITY);
        if (hit.isPresent()) {
            return getDiffuseColor(depth, hit.get(), target);
        }

        return getBackgroundColor();
    }

    private Vector getBackgroundColor() {
        Vector unitDirection = direction.unitVector();
        double t = 0.5 * (unitDirection.y() + 1.0);
        Vector white = new Vector(1, 1, 1).multiply(1.0 - t);
        Vector blue = new Vector(0.5, 0.7, 1.0).multiply(t);
        return white.add(blue);
    }

    private Vector getDiffuseColor(int depth, Hit hit, Hittable world) {
        //Vector target = hit.p.add(hit.normal).add(RTUtilities.randomInUnitSphere()); //lambertian approximation
        Optional<Ray> target = hit.material.scatter(this, hit);
        if (target.isEmpty()) {
            return new Vector(0,0,0);
        }
        return target.get().rayColor(world, depth - 1).multiply(hit.material.attenuation(this, hit));
    }
}
