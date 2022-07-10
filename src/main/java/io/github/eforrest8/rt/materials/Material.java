package io.github.eforrest8.rt.materials;

import io.github.eforrest8.rt.geometry.HitRecord;
import io.github.eforrest8.rt.geometry.Ray;
import io.github.eforrest8.rt.geometry.Vector;

import java.util.Optional;

public interface Material {
    public Optional<HitRecord> scatter(Ray r, Vector colorAttenuation, Ray scattered);
}
