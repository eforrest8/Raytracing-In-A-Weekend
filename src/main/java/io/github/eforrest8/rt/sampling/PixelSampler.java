package io.github.eforrest8.rt.sampling;

import io.github.eforrest8.rt.geometry.HittableList;
import io.github.eforrest8.rt.geometry.Vector;

public interface PixelSampler {
    Vector findPixelColor(int x, int y, HittableList world);
    int samples();
}
