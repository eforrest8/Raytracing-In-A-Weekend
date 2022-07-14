package io.github.eforrest8.rt.geometry;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class Hit {
    public Ray r;
    public double t;
    public boolean frontFace;
    public Vector p;
    public Vector normal;

    public Hit(Ray r, Hittable target, double root, Function<Vector, Vector> normalProvider) {
        this.r = r;
        calculate(root, normalProvider);
    }

    private void calculate(double root, Function<Vector, Vector> normalProvider) {
        t = root;
        p = r.at(root);
        setFaceNormal(normalProvider.apply(p));
    }

    private void setFaceNormal(Vector outwardNormal) {
        frontFace = r.direction.dot(outwardNormal) < 0;
        normal = frontFace ? outwardNormal : outwardNormal.negate();
    }
}
