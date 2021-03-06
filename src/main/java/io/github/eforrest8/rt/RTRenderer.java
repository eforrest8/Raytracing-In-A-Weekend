package io.github.eforrest8.rt;

import io.github.eforrest8.rt.camera.Camera;
import io.github.eforrest8.rt.camera.PerspectiveCamera;
import io.github.eforrest8.rt.geometry.HittableList;
import io.github.eforrest8.rt.geometry.Sphere;
import io.github.eforrest8.rt.geometry.Vector;
import io.github.eforrest8.rt.materials.Dielectric;
import io.github.eforrest8.rt.materials.Lambertian;
import io.github.eforrest8.rt.materials.Material;
import io.github.eforrest8.rt.materials.Metal;
import io.github.eforrest8.rt.sampling.PixelSampler;
import io.github.eforrest8.rt.sampling.RandomMultiSampler;
import io.github.eforrest8.rt.sampling.SingleSampler;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RTRenderer implements Renderer {

    private final ExecutorService executor = Executors.newFixedThreadPool(16);

    //image stuff
    public final double ASPECT_RATIO = 16.0 / 9.0;
    public final int IMAGE_WIDTH = 640;
    public final int IMAGE_HEIGHT = (int)(IMAGE_WIDTH / ASPECT_RATIO);

    // world stuff
    double R = Math.cos(Math.PI/4);
    HittableList fovWorld = new HittableList();

    HittableList world = new HittableList();

    // camera stuff
    Vector lookfrom = new Vector(3,3,2);
    Vector lookat = new Vector(0,0,-1);
    Camera camera = new PerspectiveCamera(
            lookfrom,
            lookat,
            new Vector(0,1,0),
            20,
            ASPECT_RATIO,
            2.0,
            (lookfrom.subtract(lookat)).length());

    PixelSampler sampler;

    private final int[] pixels = new int[IMAGE_WIDTH*IMAGE_HEIGHT];

    public RTRenderer() {
        var groundMaterial = new Lambertian(new Vector(0.8, 0.8, 0.0));
        var centerMaterial = new Lambertian(new Vector(0.1, 0.2, 0.5));
        var leftMaterial = new Dielectric(1.5);
        var rightMaterial = new Metal(new Vector(0.8, 0.6, 0.2), 0.0);

        world.add(new Sphere(new Vector(0, -100.5, -1), 100, groundMaterial));
        world.add(new Sphere(new Vector(0, 0, -1), 0.5, centerMaterial));
        world.add(new Sphere(new Vector(-1, 0, -1), 0.5, leftMaterial));
        world.add(new Sphere(new Vector(-1, 0, -1), -0.4, leftMaterial));
        world.add(new Sphere(new Vector(1, 0, -1), 0.5, rightMaterial));

        Material fovLeft = new Lambertian(new Vector(0,0,1));
        Material fovRight = new Lambertian(new Vector(1,0,0));
        fovWorld.add(new Sphere(new Vector(-R,0,-1), R, fovLeft));
        fovWorld.add(new Sphere(new Vector(R,0,-1), R, fovRight));
    }

    private Pixel renderPixel(int x, int y, PixelSampler sampler) {
        return sampler.findPixelColor(x, y, world);
    }

    private int normalizeColor(Vector pixelColor) {
        // gamma correction
        double scale = 1.0 / sampler.samples();
        double r = Math.sqrt(scale * pixelColor.x());
        double g = Math.sqrt(scale * pixelColor.y());
        double b = Math.sqrt(scale * pixelColor.z());

        return new Color(
                (int)(RTUtilities.clamp(r, 0, 0.999) * 256),
                (int)(RTUtilities.clamp(g, 0, 0.999) * 256),
                (int)(RTUtilities.clamp(b, 0, 0.999) * 256)).getRGB();
    }

    @Override
    public CompletableFuture<Image> render() {
        //sampler = new SingleSampler(camera, IMAGE_WIDTH, IMAGE_HEIGHT);
        sampler = new RandomMultiSampler(4, camera, IMAGE_WIDTH, IMAGE_HEIGHT);
        return CompletableFuture.supplyAsync(this::renderImage);
        /*return Executors.newSingleThreadExecutor().submit(() -> {
            for (int y = IMAGE_HEIGHT - 1; y >= 0; y--) {
                for (int x = 0; x < IMAGE_WIDTH; x++) {
                    int finalX = x;
                    int finalY = y;
                    executor.submit(() -> renderPixel(finalX, finalY, sampler));
                }
            }
        });*/
    }
    
    private Image renderImage() {
		Image buffer = new Image(IMAGE_WIDTH, IMAGE_HEIGHT);
		List<Future<Pixel>> futurePixels = new LinkedList<>();
		for (int y = IMAGE_HEIGHT - 1; y >= 0; y--) {
		    for (int x = 0; x < IMAGE_WIDTH; x++) {
		        int finalX = x;
		        int finalY = y;
		        futurePixels.add(executor.submit(() -> renderPixel(finalX, finalY, sampler)));
		    }
		}
		for (Future<Pixel> pixel : futurePixels) {
			try {
				buffer.setPixel(pixel.get());
			} catch (InterruptedException | ExecutionException ignored) {}
		}
		return buffer;
	}

    private int flattenCoords(int x, int y) {
        return x + (y * IMAGE_WIDTH);
    }
}
