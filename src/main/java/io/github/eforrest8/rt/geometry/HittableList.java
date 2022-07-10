package io.github.eforrest8.rt.geometry;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class HittableList extends LinkedList<Hittable> implements Hittable, List<Hittable> {

    @Override
    public Optional<HitRecord> hit(Ray ray, double t_min, double t_max) {
        Optional<HitRecord> result = Optional.empty();
        double closestSoFar = t_max;

        for (Hittable e : this) {
            Optional<HitRecord> optionalHitRecord = e.hit(ray, t_min, closestSoFar);
            if (optionalHitRecord.isPresent()) {
                HitRecord unwrapped = optionalHitRecord.get();
                closestSoFar = unwrapped.t;
                result = optionalHitRecord; //if things break, this might be the culprit
            }
        }

        return result;
    }
}
