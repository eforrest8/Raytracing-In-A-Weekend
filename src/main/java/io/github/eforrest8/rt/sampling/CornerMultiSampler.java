package io.github.eforrest8.rt.sampling;

import io.github.eforrest8.rt.camera.Camera;
import io.github.eforrest8.rt.geometry.HittableList;
import io.github.eforrest8.rt.geometry.Ray;
import io.github.eforrest8.rt.geometry.Vector;

import java.util.LinkedList;

public class CornerMultiSampler implements PixelSampler {

    private final int imageWidth;
    private final int imageHeight;
    private final Vector[][] samples;
    private final Camera camera;

    public CornerMultiSampler(int imageWidth, int imageHeight, Camera camera) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.camera = camera;
        samples = new Vector[imageWidth][imageHeight];
    }

    @Override
    public Vector findPixelColor(int x, int y, HittableList world) {
        LinkedList<int[]> toBeProcessed = new LinkedList<>();
        if (samples[x][y] == null) {
            toBeProcessed.add(new int[]{x, y});
        } if (samples[x+1][y] == null) {
            toBeProcessed.add(new int[]{x+1, y});
        } if (samples[x][y+1] == null) {
            toBeProcessed.add(new int[]{x, y+1});
        } if (samples[x+1][y+1] == null) {
            toBeProcessed.add(new int[]{x+1, y+1});
        }

        for (int[] pixel : toBeProcessed) {
            double u = pixel[0] / (double)(imageWidth - 1);
            double v = pixel[1] / (double)(imageHeight - 1);
            Ray r = camera.getRay(u, v);
            samples[pixel[0]][pixel[1]] = r.rayColor(world, 50);
        }

        Vector pixelColor = new Vector(0,0,0);
        pixelColor = pixelColor.add(samples[x][y])
                .add(samples[x+1][y])
                .add(samples[x][y+1])
                .add(samples[x+1][y+1]);
        return pixelColor;
    }

    @Override
    public int samples() {
        return 4;
    }
}
