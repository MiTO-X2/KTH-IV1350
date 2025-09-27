package inheritanceAndComposition;

/**
 * Demonstrates the usage of both {@link InheritedRandom} and {@link ComposedRandom} classes.
 * 
 * This class contains the main method which:
 * - Creates instances of both random number generators,
 * - Calls their methods to generate random integers and doubles,
 * - And outputs the generated values along with informative console messages,
 * allowing comparison of inheritance-based and composition-based designs.
 */
public class ICMain {

    /**
     * The program entry point.
     * 
     * Executes the demonstration of random number generation using both inheritance and composition.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        System.out.println("=== Using InheritedRandom ===");
        InheritedRandom inherited = new InheritedRandom();
        inherited.nextInt(10);
        inherited.nextDouble();

        System.out.println("\n=== Using ComposedRandom ===");
        ComposedRandom composed = new ComposedRandom();
        composed.nextInt(10);
        composed.nextDouble();
    }
}