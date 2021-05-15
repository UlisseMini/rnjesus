import java.util.Random;

class Main {
  // Useful resources:
  // - https://docs.oracle.com/javase/8/docs/api/java/util/Random.html
  // - http://developer.classpath.org/doc/java/util/Random-source.html
  //
  // Constants used by java RNG (m=2^48, hardcoded for efficency using bitwise ops)
  private static final long a = 0x5DEECE66DL;
  private static final long ap = 246154705703781L; // a inverse mod 2^48
  private static final long b = 0xBL; // also known as 11

  private static long findSeed(int k) {
    // we want a seed such that the first nextInt() call returns k.
    long k_shifted = k << 16;

    // solving: a*seed + b = k (mod m)
    long seed = ap*(k_shifted - b) & ((1L << 48) - 1);

    seed ^= 0x5DEECE66DL; // invert the xor from initial setSeed

    return seed;
  }

  public static void main(String[] args) {
    // Find the seed where the first nextInt() call returns 42
    long seed = findSeed(42);

    System.out.println("seed: " + seed);

    // Get the first value from nextInt manually (for debugging, I like having the code inline)
    long new_seed = (a*(seed^a) + b) & ((1L << 48) - 1);
    int first = (int) (new_seed >>> 16);
    System.out.println("first: " + first);


    System.out.println("\nfirst 10");
    Random rng = new Random(seed);
    for (int i = 0; i < 10; i++) {
      int r = rng.nextInt();

      System.out.print(r);
      // System.out.print("\t");
      // System.out.print(Long.toBinaryString(r));
      System.out.print("\n");
    }
  }
}
