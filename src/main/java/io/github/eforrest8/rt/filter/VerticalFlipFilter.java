package io.github.eforrest8.rt.filter;

import io.github.eforrest8.rt.Image;
import io.github.eforrest8.rt.Pixel;

public class VerticalFlipFilter implements Filter {
	public Image apply(Image image) {
		Image buffer = new Image(image.width, image.height);
		for (Pixel pixel : image.pixels) {
			buffer.setPixel(filterPixel(pixel, image));
		}
		return buffer;
	}
	
	private Pixel filterPixel(Pixel pixel, Image image) {
		return new Pixel(
				pixel.x(),
				image.height - 1 - pixel.y(),
				pixel.color());
	}
}
