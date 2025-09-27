package inheritanceAndComposition;

import java.util.Random;

/**
 * A subclass of Java's built-in {@link Random} class.
 * 
 * Demonstrates inheritance by extending {@code Random} and overriding its methods to include
 * logging of generated random values.
 */
public class InheritedRandom extends Random {

    /**
     * Generates a random integer between 0 (inclusive) and the specified bound (exclusive).
     * Overrides {@link Random#nextInt(int)} to add logging of the generated value.
     *
     * @param bound The upper bound (exclusive) for the generated random integer.
     * @return A pseudo-random integer between 0 (inclusive) and {@code bound} (exclusive).
     */
    @Override
    public int nextInt(int bound) {
        int result = super.nextInt(bound);
        System.out.println("[InheritedRandom] Generated int: " + result);
        return result;
    }

    /**
     * Generates a random double value between 0.0 (inclusive) and 1.0 (exclusive).
     * Overrides {@link Random#nextDouble()} to add logging of the generated value.
     *
     * @return A pseudo-random {@code double} value greater than or equal to 0.0 and less than 1.0.
     */
    @Override
    public double nextDouble() {
        double result = super.nextDouble();
        System.out.println("[InheritedRandom] Generated double: " + result);
        return result;
    }
}