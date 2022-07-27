package io.github.eforrest8.rt.filter;

import java.util.LinkedList;
import java.util.List;

import io.github.eforrest8.rt.Filter;
import io.github.eforrest8.rt.Image;
import io.github.eforrest8.rt.Pixel;
import io.github.eforrest8.rt.geometry.Vector;

public class InpaintingFilter implements Filter {

	private Image image;
	private final Vector marker = new Vector(0,0,0); 

	@Override
	public Image apply(Image image) {
		this.image = image;
		Image buffer = new Image(image.width, image.height);
		for (int x = 0; x < image.width; x++) {
			for (int y = 0; y < image.height; y++) {
				if (image.getPixel(x, y) == null || image.getPixel(x, y).color().equals(marker)) {
					buffer.setPixel(calculatePixel(x, y));
				} else {
					buffer.setPixel(image.getPixel(x, y));
				}
			}
		}
		return buffer; //note: not all pixels are actually interpolated yet!!!!
	}
	
	private Pixel calculatePixel(int x, int y) {
		List<Pixel[]> pairs = findCrossingPairs(x, y);
		if (pairs.isEmpty()) {
			return new Pixel(x, y, new Vector(0,0,0));
		}
		Vector sumColor = pairs.stream()
				.map(pair -> {
					double dlong = Math.sqrt(Math.pow(pair[0].x()-pair[1].x(), 2) + Math.pow(pair[0].y()-pair[1].y(), 2));
					double dshort = Math.sqrt(Math.pow(pair[0].x()-x, 2) + Math.pow(pair[0].y()-y, 2));
					double t = dshort/dlong;
					return new Vector(
							lerp(pair[0].color().x(), pair[1].color().x(), t),
							lerp(pair[0].color().y(), pair[1].color().y(), t),
							lerp(pair[0].color().z(), pair[1].color().z(), t));
				}) // convert pairs to interpolated color
				.reduce(new Vector(0,0,0), Vector::add);
		return new Pixel(x, y, sumColor.divide(pairs.size()));
	}
	
	private double lerp(double min, double max, double t) {
		return ((1-t) * min) + (t * max);
	}
	
	private List<Pixel[]> findCrossingPairs(int x, int y) {
		List<Pixel[]> pairs = new LinkedList<>();
		newpixel: for (double y0 = y-2; y0 <= y+2; y0 += 0.5) {
			double vx = x - 1;
			double vy = y - y0;
			double vlen = Math.sqrt(vx*vx + vy*vy);
			double ux = vx/vlen;
			double uy = vy/vlen;
			for (int d = 0; pointIsInImage((int)Math.floor(x + d*ux), (int)Math.floor(y + d*uy), image); d--) {
				Pixel pixel = image.getPixel((int)Math.floor(x + d*ux), (int)Math.floor(y + d*uy));
				if (pixel == null || pixel.color().equals(marker)) {
					continue;
				}
				for (int d1 = 0; pointIsInImage((int)Math.floor(x + d1*ux), (int)Math.floor(y + d1*uy), image); d1++) {
					Pixel opposite = image.getPixel((int)Math.floor(x + d1*ux), (int)Math.floor(y + d1*uy));
					if (opposite == null || opposite.color().equals(marker)) {
						continue;
					}
					pairs.add(new Pixel[] {pixel, opposite});
					continue newpixel;
				}
			}
		}
		return pairs;
	}
	
	private boolean pointIsInImage(int x, int y, Image image) {
		return x >= 0 && y >= 0 && x < image.width && y < image.height;
	}
}
