package io.github.eforrest8.rt.geometry;

import java.util.Optional;

public interface Hittable {
    public Optional<HitRecord> hit(Ray ray, double t_min, double t_max);
}
