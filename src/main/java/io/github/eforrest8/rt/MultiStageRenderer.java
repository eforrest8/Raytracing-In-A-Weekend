package io.github.eforrest8.rt;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import io.github.eforrest8.rt.camera.Camera;
import io.github.eforrest8.rt.camera.PerspectiveCamera;
import io.github.eforrest8.rt.filter.BilinearUpscaleFilter;
import io.github.eforrest8.rt.filter.ConvolutionFilter;
import io.github.eforrest8.rt.filter.GammaCorrectionFilter;
import io.github.eforrest8.rt.filter.GreyscaleFilter;
import io.github.eforrest8.rt.filter.MedianNoiseReductionFilter;
import io.github.eforrest8.rt.filter.PaddingFilter;
import io.github.eforrest8.rt.filter.VerticalFlipFilter;
import io.github.eforrest8.rt.geometry.HittableList;
import io.github.eforrest8.rt.geometry.Sphere;
import io.github.eforrest8.rt.geometry.Vector;
import io.github.eforrest8.rt.materials.Dielectric;
import io.github.eforrest8.rt.materials.Lambertian;
import io.github.eforrest8.rt.materials.Metal;
import io.github.eforrest8.rt.sampling.PixelSampler;
import io.github.eforrest8.rt.sampling.RandomMultiSampler;
import io.github.eforrest8.rt.sampling.SingleSampler;

public class MultiStageRenderer implements Renderer {

    //image stuff
    public final double ASPECT_RATIO = 16.0 / 9.0;
    public final int IMAGE_WIDTH = 640;
    public final int IMAGE_HEIGHT = (int)(IMAGE_WIDTH / ASPECT_RATIO);

    // world stuff
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
            0.5,
            (lookfrom.subtract(lookat)).length());

    private final Pixel[] pixels = new Pixel[IMAGE_WIDTH*IMAGE_HEIGHT];

    public MultiStageRenderer() {
        var groundMaterial = new Lambertian(new Vector(0.8, 0.8, 0.0));
        var centerMaterial = new Lambertian(new Vector(0.1, 0.2, 0.5));
        var leftMaterial = new Dielectric(1.5);
        var rightMaterial = new Metal(new Vector(0.8, 0.6, 0.2), 0.0);

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
        PixelSampler maskSampler = new SingleSampler(camera, IMAGE_WIDTH/8, IMAGE_HEIGHT/8);
        PixelSampler sampler = new RandomMultiSampler(4, camera, IMAGE_WIDTH, IMAGE_HEIGHT);
        Filter gammaCorrect = new GammaCorrectionFilter();
        Filter vertFlip = new VerticalFlipFilter();
        Filter denoise = new MedianNoiseReductionFilter();
        Filter greyscale = new GreyscaleFilter();
        Filter preConvolvePad = new PaddingFilter(1, 1, 1, 1);
        //Filter sobelConvolve = new ConvolutionFilter(new double[][] {{1,2,1},{0,0,0},{-1,-2,-1}});
        Filter laplace = new ConvolutionFilter(new double[][] {{1,1,1},{1,-8,1},{1,1,1}});
        //Filter boxblur = new ConvolutionFilter(new double[][] {{1,1,1},{1,1,1},{1,1,1}}, 1.0/9.0);
        Filter upscale = new BilinearUpscaleFilter(IMAGE_HEIGHT, IMAGE_WIDTH);
        CompletableFuture<Image> result = CompletableFuture
        		.supplyAsync(() -> renderImage(IMAGE_HEIGHT/8, IMAGE_WIDTH/8, maskSampler))
        		.thenApply(vertFlip::apply)
    			.thenApply(gammaCorrect::apply)
    			.thenApply(denoise::apply)
    			.thenApply(greyscale::apply)
    			.thenApply(preConvolvePad::apply)
    			.thenApply(laplace::apply)
    			.thenApply(upscale::apply);
        return result.join();
    }

	private Image renderImage(int height, int width, PixelSampler sampler) {
		Image buffer = new Image(width, height);
		List<Future<Pixel>> futurePixels = new LinkedList<>();
		for (int y = height - 1; y >= 0; y--) {
		    for (int x = 0; x < width; x++) {
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
