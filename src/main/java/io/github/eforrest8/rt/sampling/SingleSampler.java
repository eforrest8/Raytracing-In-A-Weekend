package io.github.eforrest8.rt.sampling;

import io.github.eforrest8.rt.Pixel;
import io.github.eforrest8.rt.camera.Camera;
import io.github.eforrest8.rt.geometry.HittableList;
import io.github.eforrest8.rt.geometry.Ray;

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
    public Pixel findPixelColor(int x, int y, HittableList world) {
        double u = x / (double)(imageWidth - 1);
        double v = y / (double)(imageHeight - 1);
        Ray r = camera.getRay(u, v);
        return new Pixel(x, y, r.rayColor(world, 5));
    }

    @Override
    public int samples() {
        return 1;
    }
}
