package io.github.eforrest8.rt.sampling;

import io.github.eforrest8.rt.geometry.Hittable;
import io.github.eforrest8.rt.geometry.Vector;

public interface PixelSampler {
    Vector findPixelColor(int x, int y, Hittable world);
}
