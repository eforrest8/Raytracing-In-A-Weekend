package io.github.eforrest8.rt.camera;

import io.github.eforrest8.rt.RTUtilities;
import io.github.eforrest8.rt.geometry.Ray;
import io.github.eforrest8.rt.geometry.Vector;

public class PerspectiveCamera implements Camera {
    private final double lensRadius;
    private final Vector u;
    private final Vector v;
    public double viewportHeight;
    public double viewportWidth;
    private final Vector origin;
    private final Vector horizontal;
    private final Vector vertical;
    private final Vector lowerLeftCorner;

    public PerspectiveCamera(
            Vector lookfrom,
            Vector lookat,
            Vector vup,
            double vfov,
            double aspectRatio,
            double aperture,
            double focusDist
    ) {
        var theta = Math.toRadians(vfov);
        var h = Math.tan(theta/2);
        viewportHeight = 2.0 * h;
        viewportWidth = aspectRatio * viewportHeight;

        Vector w = lookfrom.subtract(lookat).unitVector();
        u = vup.cross(w).unitVector();
        v = w.cross(u);

        origin = lookfrom;
        horizontal = u.multiply(viewportWidth * focusDist);
        vertical = v.multiply(viewportHeight * focusDist);
        lowerLeftCorner = origin
                .subtract(horizontal.divide(2))
                .subtract(vertical.divide(2))
                .subtract(w.multiply(focusDist));

        lensRadius = aperture / 2;
    }

    public Ray getRay(double s, double t) {
        Vector rd = RTUtilities.randomInUnitDisc().multiply(lensRadius);
        Vector offset = u.multiply(rd.x()).add(v.multiply(rd.y()));
        return new Ray(
                origin.add(offset),
                lowerLeftCorner.add(horizontal.multiply(s)).add(vertical.multiply(t)).subtract(origin).subtract(offset));
    }
}
