package io.github.eforrest8.rt;

import io.github.eforrest8.rt.camera.Camera;
import io.github.eforrest8.rt.camera.PerspectiveCamera;
import io.github.eforrest8.rt.geometry.HittableList;
import io.github.eforrest8.rt.geometry.Sphere;
import io.github.eforrest8.rt.geometry.Vector;
import io.github.eforrest8.rt.sampling.PixelSampler;
import io.github.eforrest8.rt.sampling.RandomMultiSampler;
import io.github.eforrest8.rt.sampling.SingleSampler;

import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RTRenderer implements Renderer {

    private final ExecutorService executor = Executors.newFixedThreadPool(16);

    //image stuff
    public final double ASPECT_RATIO = 16.0 / 9.0;
    public final int IMAGE_WIDTH = 640;
    public final int IMAGE_HEIGHT = (int)(IMAGE_WIDTH / ASPECT_RATIO);

    // world stuff
    HittableList world = new HittableList();
    Camera camera = new PerspectiveCamera();

    PixelSampler sampler;

    private final int[] pixels = new int[IMAGE_WIDTH*IMAGE_HEIGHT];

    public RTRenderer() {
        world.add(new Sphere(new Vector(0, 0, -1), 0.5));
        world.add(new Sphere(new Vector(0, -100.5, -1), 100));
        world.add(new Sphere(new Vector(1, 0, -1.5), 0.5));
    }

    private void renderPixel(int x, int y, PixelSampler sampler) {
        Vector pixelColor = sampler.findPixelColor(x, y, world);
        int flippedY = IMAGE_HEIGHT - 1 - y;
        pixels[flattenCoords(x, flippedY)] = normalizeColor(pixelColor);
    }

    private int normalizeColor(Vector pixelColor) {
        // gamma correction
        double r = Math.sqrt(pixelColor.x());
        double g = Math.sqrt(pixelColor.y());
        double b = Math.sqrt(pixelColor.z());


        return new Color(
                (int)(r * 256),
                (int)(g * 256),
                (int)(b * 256)).getRGB();
    }

    @Override
    public int[] renderSynchronous() {
        sampler = new SingleSampler(camera, IMAGE_WIDTH, IMAGE_HEIGHT);
        for (int y = 0; y < IMAGE_HEIGHT; y++) {
            for (int x = 0; x < IMAGE_WIDTH; x++) {
                renderPixel(x, y, sampler);
            }
        }
        return pixels;
    }

    @Override
    public void renderAsync() {
        //sampler = new SingleSampler(camera, IMAGE_WIDTH, IMAGE_HEIGHT);
        sampler = new RandomMultiSampler(10, camera, IMAGE_WIDTH, IMAGE_HEIGHT);
        Executors.newSingleThreadExecutor().submit(() -> {
            for (int y = IMAGE_HEIGHT - 1; y >= 0; y--) {
                for (int x = 0; x < IMAGE_WIDTH; x++) {
                    int finalX = x;
                    int finalY = y;
                    executor.submit(() -> renderPixel(finalX, finalY, sampler));
                }
            }
        });
    }

    @Override
    public int[] getAsyncImage() {
        return pixels;
    }

    @Override
    public void cancelAsyncRender() {
        executor.shutdownNow();
    }

    private int flattenCoords(int x, int y) {
        return x + (y * IMAGE_WIDTH);
    }
}
