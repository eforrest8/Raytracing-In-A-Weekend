package io.github.eforrest8.rt.geometry;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.function.DoubleBinaryOperator;
import java.util.stream.DoubleStream;

public class Vector {

    private final double[] elements;

    public int arrayLength() {return elements.length;}

    public double x() {
        return get(0);
    }

    public double y() {
        return get(1);
    }

    public double z() {
        return get(2);
    }

    private Vector(double ...elements) {
        this(elements.length, elements);
    }

    private Vector(int length, double ...elements) {
        this.elements = Arrays.copyOf(elements, length);
    }

    public Vector (double x, double y, double z) {
        elements = new double[]{x, y, z};
    }

    public Vector negate() {
        return new Vector(
                doubleStream().map(e -> -e).toArray()
        );
    }

    public double get(int i) {
        return elements[i];
    }

    public DoubleStream doubleStream() {
        return Arrays.stream(elements);
    }

    public Vector applyEach(Vector v, DoubleBinaryOperator consumer) {
        if (arrayLength() != v.arrayLength()) {
            throw new InvalidParameterException("Vector lengths do not match!");
        }
        double[] result = new double[arrayLength()];
        for (int i = 0; i < arrayLength(); i++) {
            result[i] = consumer.applyAsDouble(get(i), v.get(i));
        }
        return new Vector(result);
    }

    public <T extends Vector> Vector add(T v) {
        return applyEach(v, Double::sum);
    }

    public <T extends Vector> Vector subtract(T v) {
        return applyEach(v, (l, r) -> l - r);
    }

    public <T extends Vector> Vector multiply(T v) {
        return applyEach(v, (l, r) -> l * r);
    }

    public Vector multiply(double t) {
        return new Vector(
                Arrays.stream(elements).map(e -> e * t).toArray()
        );
    }

    public Vector divide(double t) {
        return new Vector(
                Arrays.stream(elements).map(e -> e / t).toArray()
        );
    }

    public double length() {
        return Math.sqrt(length_squared());
    }

    public double length_squared() {
        return Arrays.stream(elements).reduce(0, (l, r) -> l + (r * r));
    }

    public <T extends Vector> double dot(T v) {
        return multiply(v).doubleStream().sum();
    }

    public <T extends Vector> Vector cross(T v) {
        if (arrayLength() != 3 || v.arrayLength() != 3) {
            throw new InvalidParameterException("cross product only defined for vectors of length 3");
        }
        return new Vector(
                get(1) * v.get(2) - get(2) * v.get(1),
                get(2) * v.get(0) - get(0) * v.get(2),
                get(0) * v.get(1) - get(1) * v.get(0)
        );
    }

    public Vector unitVector() {
        return divide(length());
    }

    @Override
    public String toString() {
        return Arrays.toString(elements);
    }

    public boolean isNearZero() {
        double s = 1e-8;
        return (Math.abs(get(0)) < s) && (Math.abs(get(1)) < s) && (Math.abs(get(2)) < s);
    }

    public Vector reflect(Vector normal) {
        return this.subtract(normal.multiply(this.dot(normal)).multiply(2));
    }

    public Vector refract(Vector n, double etaIOverEtaT) {
        var cosTheta = Math.min(negate().dot(n), 1.0);
        Vector rOutPerp = (this.add(n.multiply(cosTheta))).multiply(etaIOverEtaT);
        Vector rOutParallel = n.multiply(-Math.sqrt(Math.abs(1.0 - rOutPerp.length_squared())));
        return rOutPerp.add(rOutParallel);
    }
    
    public boolean equals(Vector other) {
    	return x() == other.x() && y() == other.y() && z() == other.z();
    }
}
