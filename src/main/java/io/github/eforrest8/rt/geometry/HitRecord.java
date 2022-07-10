package io.github.eforrest8.rt.geometry;

public class HitRecord {
    public Vector p;
    public Vector normal;
    public double t;
    public boolean frontFace;

    public void setFaceNormal(Ray r, Vector outwardNormal) {
        frontFace = r.direction.dot(outwardNormal) < 0;
        normal = frontFace ? outwardNormal : outwardNormal.negate();
    }
}
