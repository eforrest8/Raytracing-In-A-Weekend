package io.github.eforrest8.rt.camera;

import io.github.eforrest8.rt.geometry.Ray;
import io.github.eforrest8.rt.geometry.Vector;

public class PerspectiveCamera implements Camera {
    public double viewportHeight;
    public double viewportWidth;
    private final Vector origin;
    private final Vector horizontal;
    private final Vector vertical;
    private final Vector lowerLeftCorner;

    public PerspectiveCamera(Vector lookfrom, Vector lookat, Vector vup, double vfov, double aspectRatio) {
        var theta = Math.toRadians(vfov);
        var h = Math.tan(theta/2);
        viewportHeight = 2.0 * h;
        viewportWidth = aspectRatio * viewportHeight;

        Vector w = lookfrom.subtract(lookat).unitVector();
        Vector u = vup.cross(w).unitVector();
        Vector v = w.cross(u);

        origin = lookfrom;
        horizontal = u.multiply(viewportWidth);
        vertical = v.multiply(viewportHeight);
        lowerLeftCorner = origin
                .subtract(horizontal.divide(2))
                .subtract(vertical.divide(2))
                .subtract(w);
    }

    public Ray getRay(double s, double t) {
        return new Ray(origin, lowerLeftCorner.add(horizontal.multiply(s)).add(vertical.multiply(t)).subtract(origin));
    }
}
