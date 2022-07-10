package io.github.eforrest8.rt.sampling;

import io.github.eforrest8.rt.RTUtilities;
import io.github.eforrest8.rt.camera.Camera;
import io.github.eforrest8.rt.geometry.Hittable;
import io.github.eforrest8.rt.camera.PerspectiveCamera;
import io.github.eforrest8.rt.geometry.Ray;
import io.github.eforrest8.rt.geometry.Vector;

/**
 * PixelSampler which takes only one sample per pixel.
 */
public class SingleSampler implements PixelSampler {
    private final int imageWidth;
    private final int imageHeight;
    private final Camera camera;

    public SingleSampler(Camera camera, int imageWidth, int imageHeight) {
        this.camera = camera;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    @Override
    public Vector findPixelColor(int x, int y, Hittable world) {
        double u = x / (double)(imageWidth - 1);
        double v = y / (double)(imageHeight - 1);
        Ray r = camera.getRay(u, v);
        return scaleColor(r.rayColor(world, 3));
    }

    private Vector scaleColor(Vector color) {
        return new Vector(
                RTUtilities.clamp(color.x(), 0, 0.999),
                RTUtilities.clamp(color.y(), 0, 0.999),
                RTUtilities.clamp(color.z(), 0, 0.999)
        );
    }
}
