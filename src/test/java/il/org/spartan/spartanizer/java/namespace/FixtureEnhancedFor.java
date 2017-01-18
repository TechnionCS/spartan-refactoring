package il.org.spartan.spartanizer.java.namespace;

import il.org.spartan.spartanizer.meta.*;

/** Fixture for testing enhanced for loops
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-01 */
public class FixtureEnhancedFor extends ReflectiveTester {
  {
    final int i = hashCode();
    for (@knows({ "carrot", "i" }) final int carrot : toString().toCharArray()) {
      @knows({ "carrot", "i" }) final int cherry = 2 * carrot * (carrot - 21) + 2;
      if (hashCode() != hashCode())
        q(i * cherry + carrot * cherry);
    }
    final int j = i * hashCode();
    for (@knows({ "banana", "i", "j" }) final int banana : toString().toCharArray()) {
      @knows("banana") final int cherry = 2 * banana * (banana - 21) + 2;
      if (hashCode() == hashCode() + j)
        q(i * cherry + banana * cherry);
    }
    for (@knows("banana") final int banana : toString().toCharArray()) {
      @knows("banana") final int cherry = 2 * banana * (banana - 21) + 2;
      if (hashCode() != hashCode())
        q(i * cherry + banana * cherry);
    }
    for (@knows({ "carrot", "i", "j" }) final int carrot : toString().toCharArray()) {
      @knows("carrot") final int cherry = 2 * carrot * (carrot - 21) + 2;
      if (hashCode() != hashCode())
        q(i * cherry + carrot * cherry);
    }
    for (@knows({ "carrot", "i", "j" }) final int carrot : toString().toCharArray())
      for (@knows({ "banana", "carrot", "i", "j" }) final int banana : toString().toCharArray()) {
        @knows("banana") final int cherry = 2 * carrot + 2 * banana * (banana - 21);
        if (hashCode() == hashCode() + cherry)
          q(i * cherry + banana * cherry);
      }
  }

  private void q(final int ¢) {
    q(¢);
  }
}
