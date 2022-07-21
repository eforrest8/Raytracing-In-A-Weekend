package io.github.eforrest8.rt.sampling;

import io.github.eforrest8.rt.Pixel;
import io.github.eforrest8.rt.geometry.HittableList;

public interface PixelSampler {
    Pixel findPixelColor(int x, int y, HittableList world);
    int samples();
}
