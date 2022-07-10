package io.github.eforrest8.rt.camera;

import io.github.eforrest8.rt.geometry.Ray;
import io.github.eforrest8.rt.geometry.Vector;

public class PerspectiveCamera implements Camera {
    public final double ASPECT_RATIO = 16.0 / 9.0;
    public double viewportHeight = 2.0;
    public double viewportWidth = ASPECT_RATIO * viewportHeight;
    public double focalLength = 1.0;
    private final Vector origin = new Vector(0d, 0d, 0d);
    private final Vector horizontal = new Vector(viewportWidth, 0d, 0d);
    private final Vector vertical = new Vector(0d, viewportHeight, 0d);
    private final Vector lowerLeftCorner = origin
            .subtract(horizontal.divide(2))
            .subtract(vertical.divide(2))
            .subtract(new Vector(0d, 0d, focalLength));

    public Ray getRay(double u, double v) {
        return new Ray(origin, lowerLeftCorner.add(horizontal.multiply(u)).add(vertical.multiply(v)).subtract(origin));
    }
}
