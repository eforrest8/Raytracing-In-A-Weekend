package io.github.eforrest8.rt.filter;

import io.github.eforrest8.rt.Image;
import io.github.eforrest8.rt.Pixel;
import io.github.eforrest8.rt.geometry.Vector;

public class GreyscaleFilter implements Filter {

	@Override
	public Image apply(Image image) {
		Image buffer = new Image(image.width, image.height);
		for (Pixel pixel : image.pixels) {
			double luminance = pixel.color().x()*0.2126 + pixel.color().y()*0.7152 + pixel.color().z()*0.0722; 
			buffer.setPixel(new Pixel(pixel.x(), pixel.y(), new Vector(luminance, luminance, luminance)));
		}
		return buffer;
	}

}
