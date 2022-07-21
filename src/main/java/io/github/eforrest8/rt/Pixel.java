package io.github.eforrest8.rt;

import java.awt.Color;

import io.github.eforrest8.rt.geometry.Vector;

public record Pixel(int x, int y, Vector color) {
	public int getColorAsInt() {
		return new Color(
                (int)(RTUtilities.clamp(color().x(), 0, 0.999) * 256),
                (int)(RTUtilities.clamp(color().y(), 0, 0.999) * 256),
                (int)(RTUtilities.clamp(color().z(), 0, 0.999) * 256)).getRGB();
	}
}
