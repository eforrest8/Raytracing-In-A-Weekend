package io.github.eforrest8.rt.filter;

import io.github.eforrest8.rt.Image;
import io.github.eforrest8.rt.Pixel;
import io.github.eforrest8.rt.geometry.Vector;

public class ConvolutionFilter implements Filter {
	
	private final double[][] matrix;
	private final double scalar;
	
	public ConvolutionFilter(double[][] matrix, double scalar) {
		this.matrix  = matrix;
		this.scalar = scalar;
	}
	
	public ConvolutionFilter(double[][] matrix) {
		this(matrix, 1);
	}

	@Override
	public Image apply(Image image) {
		Image buffer = new Image(image.width - (matrix.length - 1), image.height - (matrix[0].length - 1));
		for (int x = 0; x < buffer.width; x++) {
			for (int y = 0; y < buffer.height; y++) {
				buffer.setPixel(filterPixel(x, y, image));
			}
		}
		return buffer;
	}

	private Pixel filterPixel(int x, int y, Image image) {
		Vector newColor = new Vector(0,0,0);
		for (int mx = 0; mx < matrix.length; mx++) {
			for (int my = 0; my < matrix[0].length; my++) {
				newColor = newColor.add(image.getPixel(mx + x, my + y).color().multiply(matrix[mx][my]));
			}
		}
		return new Pixel(x, y, newColor.multiply(scalar));
	}

}
