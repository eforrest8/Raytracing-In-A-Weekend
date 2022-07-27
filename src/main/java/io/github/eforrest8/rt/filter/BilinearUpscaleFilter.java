package io.github.eforrest8.rt.filter;

import io.github.eforrest8.rt.Filter;
import io.github.eforrest8.rt.Image;
import io.github.eforrest8.rt.Pixel;
import io.github.eforrest8.rt.RTUtilities;
import io.github.eforrest8.rt.geometry.Vector;

public class BilinearUpscaleFilter implements Filter {
	
	private final int height;
	private final int width;
	
	public BilinearUpscaleFilter(int height, int width) {
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
				double scaledx = (x*xscale) + 0.001;
				double scaledy = (y*yscale) + 0.001;
				double x1 = Math.floor(scaledx);
				double x2 = Math.ceil(scaledx);
				double y1 = Math.floor(scaledy);
				double y2 = Math.ceil(scaledy);
				double intermediate = (x2-x1)*(y2-y1);
				double a11 = (x2-scaledx)*(y2-scaledy)/intermediate;
				double a12 = (x2-scaledx)*(scaledy-y1)/intermediate;
				double a21 = (scaledx-x1)*(y2-scaledy)/intermediate;
				double a22 = (scaledx-x1)*(scaledy-y1)/intermediate;
				Vector color = image.getPixel((int)RTUtilities.clamp(x1, 0, image.width-1), (int)RTUtilities.clamp(y1, 0, image.height-1)).color().multiply(a11)
						.add(image.getPixel((int)RTUtilities.clamp(x1, 0, image.width-1), (int)RTUtilities.clamp(y2, 0, image.height-1)).color().multiply(a12))
						.add(image.getPixel((int)RTUtilities.clamp(x2, 0, image.width-1), (int)RTUtilities.clamp(y1, 0, image.height-1)).color().multiply(a21))
						.add(image.getPixel((int)RTUtilities.clamp(x2, 0, image.width-1), (int)RTUtilities.clamp(y2, 0, image.height-1)).color().multiply(a22));
				buffer.setPixel(new Pixel(x, y, color));
			}
		}
		return buffer;
	}

}
