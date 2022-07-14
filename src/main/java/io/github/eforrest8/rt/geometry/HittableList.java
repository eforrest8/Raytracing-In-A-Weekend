package io.github.eforrest8.rt.geometry;

import java.util.LinkedList;
import java.util.Optional;

public class HittableList extends LinkedList<Hittable> implements Hittable {

    public Optional<Hit> getNearestHit(Ray ray, double t_min, double t_max) {
        Hit result = null;
        //double closestSoFar = t_max;
        for (Hittable e : this) {
            Optional<Hit> hit = e.hit(ray, t_min, t_max);
            if (hit.isPresent()) {
                //closestSoFar = hit.get().t;
                result = hit.get();
            }
        }
        return result == null ? Optional.empty() : Optional.of(result);
    }

    @Override
    public Optional<Hit> hit(Ray ray, double t_min, double t_max) {
        Hit result = null;
        double closestSoFar = t_max;
        for (Hittable e : this) {
            Optional<Hit> hit = e.hit(ray, t_min, closestSoFar);
            if (hit.isPresent()) {
                closestSoFar = hit.get().t;
                result = hit.get();
            }
        }
        return result == null ? Optional.empty() : Optional.of(result);
    }

}
