package io.github.eforrest8.rational;

public class Rational extends Number {

    private final int numerator;
    private final int denominator;

    private Rational(int numerator, int denominator) {
        int gcd = findGCD(numerator, denominator);
        this.numerator = numerator / gcd;
        this.denominator = denominator / gcd;
    }

    // this method graciously stolen from a stackoverflow answer
    private int findGCD(int u, int v) {
        int temp;
        while (0 != v) {
            temp = v;
            v = u % v;
            u = temp;
        }
        return u;
    }

    public Rational add(Rational addend) {
        return new Rational(numerator + addend.numerator, denominator + addend.denominator);
    }

    public Rational subtract(Rational subtrahend) {
        return new Rational(numerator - subtrahend.numerator, denominator - subtrahend.denominator);
    }

    public Rational multiply(Rational factor) {
        return new Rational(numerator * factor.numerator, denominator * factor.denominator);
    }

    public Rational divide(Rational dividend) {
        return multiply(new Rational(dividend.denominator, dividend.numerator));
    }

    @Override
    public int intValue() {
        return numerator/denominator;
    }

    @Override
    public long longValue() {
        return intValue();
    }

    @Override
    public float floatValue() {
        return (float)numerator/(float)denominator;
    }

    @Override
    public double doubleValue() {
        return (double)numerator/(double)denominator;
    }
}
