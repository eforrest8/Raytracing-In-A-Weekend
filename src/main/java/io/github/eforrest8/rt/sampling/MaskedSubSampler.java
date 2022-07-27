package io.github.eforrest8.rt.sampling;

import io.github.eforrest8.rt.Image;
import io.github.eforrest8.rt.Pixel;
import io.github.eforrest8.rt.camera.Camera;
import io.github.eforrest8.rt.geometry.HittableList;
import io.github.eforrest8.rt.geometry.Ray;
import io.github.eforrest8.rt.geometry.Vector;

public class MaskedSubSampler implements PixelSampler {

	private final int numberOfSamples;
    private final Camera camera;
    private final int imageWidth;
    private final int imageHeight;
	private final Image mask;

    public MaskedSubSampler(int numberOfSamples, Camera camera, int imageWidth, int imageHeight, Image mask) {
        this.numberOfSamples = numberOfSamples;
        this.camera = camera;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
		this.mask = mask;
    }

    @Override
    public Pixel findPixelColor(int x, int y, HittableList world) {
        Vector pixelColor = new Vector(0,0,0);
        if (x == 0 || y == 0 || x == imageWidth-1 || y == imageHeight-1) {
        } else if (Math.random() + 0.05 > mask.getPixel(x, y).color().x()*0.8) {
        	return new Pixel(x, y, pixelColor);
        }
        for (int s = 0; s < numberOfSamples; s++) {
            double u = (x + Math.random()) / (double)(imageWidth - 1);
            double v = (y + Math.random()) / (double)(imageHeight - 1);
            Ray r = camera.getRay(u, v);
            pixelColor = pixelColor.add(r.rayColor(world, 50));
        }
        pixelColor = scaleColor(pixelColor);
        return new Pixel(x, y, pixelColor);
    }
    
    private Vector scaleColor(Vector color) {
    	double scale = 1.0 / numberOfSamples;
    	return new Vector(
    			color.x() * scale,
    			color.y() * scale,
    			color.z() * scale
    			);
    }

    @Override
    public int samples() {
        return numberOfSamples;
    }

}
