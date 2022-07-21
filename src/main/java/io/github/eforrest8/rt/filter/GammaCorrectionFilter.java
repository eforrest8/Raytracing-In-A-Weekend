package io.github.eforrest8.rt.filter;

import io.github.eforrest8.rt.Image;
import io.github.eforrest8.rt.Pixel;
import io.github.eforrest8.rt.geometry.Vector;

public class GammaCorrectionFilter implements Filter {
	public Image apply(Image image) {
		for (Pixel pixel : image.pixels) {
			image.setPixel(filterPixel(pixel));
		}
		return image;
	}
	
	private Pixel filterPixel(Pixel pixel) {
		return new Pixel(pixel.x(), pixel.y(), new Vector(
				Math.sqrt(pixel.color().x()),
				Math.sqrt(pixel.color().y()),
				Math.sqrt(pixel.color().z()))
				);
	}
}
