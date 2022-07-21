package io.github.eforrest8.rt.filter;

import io.github.eforrest8.rt.Image;
import io.github.eforrest8.rt.Pixel;
import io.github.eforrest8.rt.geometry.Vector;

public class EdgeDetectionFilter implements Filter {
	
	public Image apply(Image image) {
		Image buffer = new Image(image.width, image.height);
		for (Pixel pixel : image.pixels) {
				buffer.setPixel(filterPixel(pixel, image));
		}
		return buffer;
	}

	private Pixel filterPixel(Pixel pixel, Image image) {
		double maxdif = 0;
		for (Pixel adj : image.getAdjacentPixels(pixel).toList()) {
			maxdif = Math.max(Math.max(Math.max(adj.color().x(), adj.color().y()), adj.color().z()), maxdif);
		}
		return new Pixel(pixel.x(), pixel.y(), new Vector(maxdif, maxdif, maxdif));
	}
}