package inheritanceAndComposition;

import java.util.Random;

/**
 * A wrapper class around Java's built-in {@link Random} class.
 * Demonstrates composition by using an instance of Random to provide random number generation.
 * 
 * This class adds logging output for each generated random value.
 */
public class ComposedRandom {
    private Random random = new Random();

    /**
     * Generates a random integer between 0 (inclusive) and the specified bound (exclusive).
     * Internally uses {@link Random#nextInt(int)} and prints the generated value to the console.
     *
     * @param bound The upper bound (exclusive) for the generated random integer.
     * @return A pseudo-random integer between 0 (inclusive) and {@code bound} (exclusive).
     */
    public int nextInt(int bound) {
        int result = random.nextInt(bound);
        System.out.println("[ComposedRandom] Generated int: " + result);
        return result;
    }

    /**
     * Generates a random double value between 0.0 (inclusive) and 1.0 (exclusive).
     * Internally uses {@link Random#nextDouble()} and prints the generated value to the console.
     *
     * @return A pseudo-random {@code double} value greater than or equal to 0.0 and less than 1.0.
     */
    public double nextDouble() {
        double result = random.nextDouble();
        System.out.println("[ComposedRandom] Generated double: " + result);
        return result;
    }
}