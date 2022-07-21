package io.github.eforrest8.rt;

import java.util.Arrays;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

public class Image {
	public Pixel[] pixels;
	public int width;
	public int height;
	
	public Image(Pixel[] pixels, int width, int height) {
		this.pixels = Arrays.copyOf(pixels, pixels.length);
		this.width = width;
		this.height = height;
	}
	public Image(int width, int height) {
		this.pixels = new Pixel[width * height];
		this.width = width;
		this.height = height;
	}
	
	public Pixel getPixel(int x, int y) {
		int pixelIndex = x + y * width;
		return pixels[pixelIndex];
	}
	
	public Stream<Pixel> getAdjacentPixels(int x, int y) {
		Builder<Pixel> builder = Stream.builder();
		if (x > 0 && y > 0) {
			builder.add(getPixel(x-1, y-1));
		}
		if (x > 0) {
			builder.add(getPixel(x-1, y));
		}
		if (y > 0) {
			builder.add(getPixel(x, y-1));
		}
		if (x < width - 1) {
			builder.add(getPixel(x+1, y));
		}
		if (y < height - 1) {
			builder.add(getPixel(x, y+1));
		}
		if (x < width - 1 && y < height - 1) {
			builder.add(getPixel(x+1, y+1));
		}
		if (x < width - 1 && y > 0) {
			builder.add(getPixel(x+1, y-1));
		}
		if (x > 0 && y < height - 1) {
			builder.add(getPixel(x-1, y+1));
		}
		return builder.build();
	}
	
	public void setPixel(Pixel value) {
		int pixelIndex = flattenCoords(value.x(), value.y());
		pixels[pixelIndex] = value;
	}
	
	public int flattenCoords(int x, int y) {
        return x + (y * width);
    }
	
	public int[] getAsIntArray() {
		return Arrays.stream(pixels).mapToInt(Pixel::getColorAsInt).toArray();
	}
	public Stream<Pixel> getAdjacentPixels(Pixel pixel) {
		return getAdjacentPixels(pixel.x(), pixel.y());
	}
	
}
