package io.github.eforrest8.rt.camera;

import io.github.eforrest8.rt.geometry.Ray;

public interface Camera {
    Ray getRay(double u, double v);
}
