package io.github.eforrest8.rt.materials;

import io.github.eforrest8.rt.geometry.Hit;
import io.github.eforrest8.rt.geometry.Ray;
import io.github.eforrest8.rt.geometry.Vector;

import java.util.Optional;

public interface Material {
    Optional<Ray> scatter(Ray r, Hit hit);
    Vector attenuation(Ray r, Hit hit);
}
