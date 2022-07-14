package io.github.eforrest8.rt.geometry;

import java.util.Optional;

public interface Hittable {
    /**
     *
     * @return An Optional containing a Hit representing the collision,
     * or Optional.empty() if there was no collision in the specified range.
     */
    Optional<Hit> hit(Ray ray, double t_min, double t_max);
}
