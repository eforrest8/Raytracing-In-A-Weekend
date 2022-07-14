package io.github.eforrest8.rt.geometry;

import io.github.eforrest8.rt.materials.Material;

import java.util.function.Function;

public class Hit {
    public Ray r;
    public double t;
    public boolean frontFace;
    public Vector p;
    public Vector normal;
    public Material material;

    public Hit(Ray r, double root, Function<Vector, Vector> normalProvider, Material material) {
        this.r = r;
        this.material = material;
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
