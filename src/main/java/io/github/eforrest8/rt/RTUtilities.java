package io.github.eforrest8.rt;

import io.github.eforrest8.rt.geometry.Vector;

public class RTUtilities {
    public static double clamp(double n, double min, double max) {
        return Math.min(Math.max(n, min), max);
    }

    public static double randomDouble(double min, double max) {
        return min + (max-min)*Math.random();
    }

    public static Vector randomVec3() {
        return new Vector(Math.random(), Math.random(), Math.random());
    }

    public static Vector randomVec3(double min, double max) {
        return new Vector(randomDouble(min, max), randomDouble(min, max), randomDouble(min, max));
    }

    public static Vector randomInUnitSphere() {
        while (true) {
            Vector p = randomVec3(-1, 1);
            if (p.length_squared() >= 1) continue;
            return p;
        }
    }

    public static Vector randomUnitVector() {
        return randomInUnitSphere().unitVector();
    }

    public static Vector randomInUnitDisc() {
        while (true) {
            Vector p =new Vector(randomDouble(-1,1), randomDouble(-1,1), 0);
            if (p.length_squared() >= 1) continue;
            return p;
        }
    }
}
