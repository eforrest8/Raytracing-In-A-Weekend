package io.github.eforrest8.rt.filter;

import java.util.Arrays;

import io.github.eforrest8.rt.Image;
import io.github.eforrest8.rt.Pixel;
import io.github.eforrest8.rt.geometry.Vector;

public class MedianNoiseReductionFilter implements Filter {

	@Override
	public Image apply(Image image) {
		Image buffer = new Image(image.width, image.height);
		try {
			for (Pixel pixel : image.pixels) {
				buffer.setPixel(filterPixel(pixel, image));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buffer;
	}

	private Pixel filterPixel(Pixel pixel, Image image) {
		double[] redSorted = image.getAdjacentPixels(pixel).mapToDouble(value -> value.color().x()).toArray();
		Arrays.sort(redSorted);
		double[] greenSorted = image.getAdjacentPixels(pixel).mapToDouble(value -> value.color().y()).toArray();
		Arrays.sort(greenSorted);
		double[] blueSorted = image.getAdjacentPixels(pixel).mapToDouble(value -> value.color().z()).toArray();
		Arrays.sort(blueSorted);
		return new Pixel(pixel.x(), pixel.y(), new Vector(findMedian(redSorted), findMedian(greenSorted), findMedian(blueSorted)));
	}
	
	private double findMedian(double[] sorted) {
		if (sorted.length % 2 == 0) {
			return (sorted[sorted.length / 2] + sorted[(sorted.length / 2) + 1]) / 2; 
		} else {
			return sorted[(sorted.length / 2) + 1];
		}
	}

}
