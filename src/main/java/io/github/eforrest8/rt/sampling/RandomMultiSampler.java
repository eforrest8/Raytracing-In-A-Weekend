package io.github.eforrest8.rt.sampling;

import io.github.eforrest8.rt.camera.Camera;
import io.github.eforrest8.rt.geometry.HittableList;
import io.github.eforrest8.rt.geometry.Ray;
import io.github.eforrest8.rt.geometry.Vector;

public class RandomMultiSampler implements PixelSampler {
    private final int numberOfSamples;
    private final Camera camera;
    private final int imageWidth;
    private final int imageHeight;

    public RandomMultiSampler(int numberOfSamples, Camera camera, int imageWidth, int imageHeight) {
        this.numberOfSamples = numberOfSamples;
        this.camera = camera;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    @Override
    public Vector findPixelColor(int x, int y, HittableList world) {
        Vector pixelColor = new Vector(0,0,0);
        for (int s = 0; s < numberOfSamples; s++) {
            double u = (x + Math.random()) / (double)(imageWidth - 1);
            double v = (y + Math.random()) / (double)(imageHeight - 1);
            Ray r = camera.getRay(u, v);
            pixelColor = pixelColor.add(r.rayColor(world, 50));
        }
        return pixelColor;
    }

    @Override
    public int samples() {
        return numberOfSamples;
    }
}
