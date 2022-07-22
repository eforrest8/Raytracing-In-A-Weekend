package io.github.eforrest8.rt.filter;

import io.github.eforrest8.rt.Image;
import io.github.eforrest8.rt.Pixel;

public class PaddingFilter implements Filter {
	
	private final int leftPad;
	private final int upPad;
	private final int rightPad;
	private final int downPad;

	public PaddingFilter(int leftPad, int upPad, int rightPad, int downPad) {
		this.leftPad = leftPad;
		this.upPad = upPad;
		this.rightPad = rightPad;
		this.downPad = downPad;
	}

	@Override
	public Image apply(Image image) {
		Image buffer = new Image(image.width + leftPad + rightPad, image.height + upPad + downPad);
		for (int x = 0; x < buffer.width; x++) {
			for (int y = 0; y < buffer.height; y++) {
				buffer.setPixel(new Pixel(x, y, image.getPixel(
						Math.max(Math.min(x-leftPad, image.width-1), 0),
						Math.max(Math.min(y-upPad, image.height-1), 0))
						.color()));
			}
		}
		return buffer;
	}
	
	public static enum PadMethod {
		BLACK,
		WHITE,
		COPY_EDGE
	}

}
