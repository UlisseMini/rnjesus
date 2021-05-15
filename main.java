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

  private static long findSeedNextInt(int k) {
    // nextInt discards the bottom 16 bits, we shift left to avoid that
    long k_shifted = k << 16;

    return findSeed(k_shifted);
  }

  private static long findSeedNextInt(int k, int step) {
    // nextInt discards the bottom 16 bits, we shift left to avoid that
    long k_shifted = k << 16;

    return findSeed(k_shifted, step);
  }

  private static long findSeed(long k) {
    // solving: a*seed + b = k (mod m)
    // (this effectively inverts the LCG one step)
    long seed = ap*(k - b) & ((1L << 48) - 1);

    return seed;
  }

  // like findSeed, but such that a specific step of nextInt() returns k
  // this can be made O(1) by doing the algebra and simplifying a summation,
  // but this is more readable. (also I want to start simple :p)
  private static long findSeed(long k, int step) {
    long seed = k;
    for (int i = 0; i < step; i++) {
      seed = findSeed(seed);
    }

    return seed;
  }

  public static void main(String[] args) {
    // Find the seed where the first nextInt() call returns 42
    long seed = findSeedNextInt(42, 3);

    seed ^= 0x5DEECE66DL; // invert the xor from initial setSeed

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
