package io.github.eforrest8.rt.filter;

import io.github.eforrest8.rt.Filter;
import io.github.eforrest8.rt.Image;
import io.github.eforrest8.rt.Pixel;

public class NearestUpscaleFilter implements Filter {

	private final int height;
	private final int width;
	
	public NearestUpscaleFilter(int height, int width) {
		this.height = height;
		this.width = width;
	}

	@Override
	public Image apply(Image image) {
		Image buffer = new Image(width, height);
		double xscale = image.width / (double)buffer.width;
		double yscale = image.height / (double)buffer.height;
		for (int x = 0; x < buffer.width; x++) {
			for (int y = 0; y < buffer.height; y++) {
				buffer.setPixel(new Pixel(x, y, image.getPixel((int)Math.floor(x*xscale), (int)Math.floor(y*yscale)).color()));
			}
		}
		return buffer;
	}

}
