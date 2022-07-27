package io.github.eforrest8.rt;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import io.github.eforrest8.rt.geometry.HittableList;
import io.github.eforrest8.rt.geometry.Sphere;
import io.github.eforrest8.rt.geometry.Vector;
import io.github.eforrest8.rt.materials.Dielectric;
import io.github.eforrest8.rt.materials.Lambertian;
import io.github.eforrest8.rt.materials.Metal;
import io.github.eforrest8.rt.sampling.PixelSampler;

public class RTRenderer implements Renderer {

    //image stuff
    public final double ASPECT_RATIO;
    public final int IMAGE_WIDTH;
    public final int IMAGE_HEIGHT;
    public final PixelSampler sampler;

    // world stuff
    HittableList world = new HittableList();

    public RTRenderer(int height, int width, PixelSampler sampler) {
    	IMAGE_HEIGHT = height;
    	IMAGE_WIDTH = width;
    	ASPECT_RATIO = width/(double)height;
    	this.sampler = sampler;
    	
        var groundMaterial = new Lambertian(new Vector(0.8, 0.8, 0.0));
        var centerMaterial = new Lambertian(new Vector(0.1, 0.2, 0.5));
        var leftMaterial = new Dielectric(1.5);
        var rightMaterial = new Metal(new Vector(0.8, 0.6, 0.2), 0.1);

        world.add(new Sphere(new Vector(0, -100.5, -1), 100, groundMaterial));
        world.add(new Sphere(new Vector(0, 0, -1), 0.5, centerMaterial));
        world.add(new Sphere(new Vector(-1, 0, -1), 0.5, leftMaterial));
        world.add(new Sphere(new Vector(-1, 0, -1), -0.4, leftMaterial));
        world.add(new Sphere(new Vector(1, 0, -1), 0.5, rightMaterial));
    }

    private Pixel renderPixel(int x, int y, PixelSampler sampler) {
        return sampler.findPixelColor(x, y, world);
    }

    @Override
    public Image render() {
    	Image buffer = new Image(IMAGE_WIDTH, IMAGE_HEIGHT);
		List<Future<Pixel>> futurePixels = new LinkedList<>();
		for (int y = IMAGE_HEIGHT - 1; y >= 0; y--) {
		    for (int x = 0; x < IMAGE_WIDTH; x++) {
		        int finalX = x;
		        int finalY = y;
		        futurePixels.add(ForkJoinPool.commonPool().submit(() -> renderPixel(finalX, finalY, sampler)));
		    }
		}
		for (Future<Pixel> pixel : futurePixels) {
			try {
				buffer.setPixel(pixel.get());
			} catch (InterruptedException | ExecutionException ignored) {}
		}
		return buffer;
	}
}
