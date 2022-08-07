package io.github.eforrest8.rt;

import io.github.eforrest8.rt.camera.Camera;
import io.github.eforrest8.rt.camera.PerspectiveCamera;
import io.github.eforrest8.rt.filter.*;
import io.github.eforrest8.rt.geometry.Vector;
import io.github.eforrest8.rt.sampling.MaskedSubSampler;
import io.github.eforrest8.rt.sampling.PixelSampler;
import io.github.eforrest8.rt.sampling.SingleSampler;

import javax.swing.*;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class SimpleDisplay extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final int IMAGE_WIDTH = 640;
	private static final int IMAGE_HEIGHT = 360;
	private static final double ASPECT_RATIO = IMAGE_WIDTH / (double)IMAGE_HEIGHT;

    public SimpleDisplay() {
        int[] pixels = new int[IMAGE_WIDTH * IMAGE_HEIGHT];
        Arrays.fill(pixels, 0xffaa00aa);
        MemoryImageSource imageSource = new MemoryImageSource(IMAGE_WIDTH, IMAGE_HEIGHT, ColorModel.getRGBdefault(), pixels, 0, IMAGE_WIDTH);
        imageSource.setAnimated(true);
        java.awt.Image image = createImage(imageSource);
        JLabel label = new JLabel(new ImageIcon(image));
        add(label);
        RenderPipeline pipeline = configurePipeline();
        
    	System.out.println("starting render at " + System.currentTimeMillis());
    	io.github.eforrest8.rt.Image render = pipeline.get();
    	System.out.println("render finished at " + System.currentTimeMillis());
    	imageSource.newPixels(
                render.getAsIntArray(),
                ColorModel.getRGBdefault(),
                0,
                IMAGE_WIDTH);
    }
    
    private RenderPipeline configurePipeline() {
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
    	PixelSampler maskSampler = new SingleSampler(camera, IMAGE_WIDTH/8, IMAGE_HEIGHT/8);
        Renderer maskRenderer = new RTRenderer(IMAGE_HEIGHT/8, IMAGE_WIDTH/8, maskSampler);
        Filter gammaCorrect = new GammaCorrectionFilter();
        Filter vertFlip = new VerticalFlipFilter();
        Filter denoise = new MedianNoiseReductionFilter();
        Filter greyscale = new GreyscaleFilter();
        Filter preConvolvePad = new PaddingFilter(1, 1, 1, 1);
        //Filter sobelConvolve = new ConvolutionFilter(new double[][] {{1,2,1},{0,0,0},{-1,-2,-1}});
        Filter laplace = new ConvolutionFilter(new double[][] {{1,1,1},{1,-8,1},{1,1,1}});
        //Filter boxblur = new ConvolutionFilter(new double[][] {{1,1,1},{1,1,1},{1,1,1}}, 1.0/9.0);
        Filter upscale = new BilinearUpscaleFilter(IMAGE_HEIGHT, IMAGE_WIDTH);
        Image mask = upscale.apply(laplace.apply(preConvolvePad.apply(greyscale.apply(denoise.apply(gammaCorrect.apply(maskRenderer.render()))))));
        PixelSampler sampler = new MaskedSubSampler(4, camera, IMAGE_WIDTH, IMAGE_HEIGHT, mask);
        //PixelSampler rsubsampler = new RandomSubSampler(4, camera, IMAGE_WIDTH, IMAGE_HEIGHT);
        Renderer renderer = new RTRenderer(IMAGE_HEIGHT, IMAGE_WIDTH, sampler);
        Filter inpaint = new InpaintingFilter();
        
    	return new RenderPipeline(CompletableFuture
        		.supplyAsync(renderer::render)
        		.thenApply(vertFlip::apply)
    			.thenApply(gammaCorrect::apply)
    			.thenApply(inpaint::apply));
    }

}
