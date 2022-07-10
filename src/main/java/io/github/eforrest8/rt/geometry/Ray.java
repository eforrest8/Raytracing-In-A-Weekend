package io.github.eforrest8.rt.geometry;

import io.github.eforrest8.rt.RTUtilities;

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

    public Vector rayColor(Hittable world, int depth) {
        if (depth <= 0) {
            return new Vector(0,0,0);
        }

        Optional<HitRecord> optionalHit = world.hit(this, 0.001, Double.POSITIVE_INFINITY);
        if (optionalHit.isPresent()) {
            HitRecord unwrappedHit = optionalHit.get();
            return getDiffuseColor(world, depth, unwrappedHit);
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

    private Vector getDiffuseColor(Hittable world, int depth, HitRecord unwrappedHit) {
        //Vector target = unwrappedHit.p.add(unwrappedHit.normal).add(RTUtilities.randomInUnitSphere()); //lambertian approximation
        Vector target = unwrappedHit.p.add(unwrappedHit.normal).add(RTUtilities.randomUnitVector()); //true lambertian
        return new Ray(unwrappedHit.p, target.subtract(unwrappedHit.p)).rayColor(world, depth - 1).multiply(0.5);
    }
}
