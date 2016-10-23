// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.utils;

import il.org.spartan.*;
import il.org.spartan.streotypes.*;
import org.jetbrains.annotations.Nullable;

/** A class to manage printing a {@link String} exactly once. In the first
 * invocation of {@link #toString()}, the initial value is returned. In all
 * subsequent invocations, the empty string is returned.
 * @see Separator
 * @author Yossi Gil
 * @since 21/08/2007 */
@Instantiable public class Once {
  @Nullable
  private String value;

  public Once(final String value) {
    this.value = defaults.to(value, "");
  }

  @Nullable
  @Override public String toString() {
    final String $ = value;
    value = null;
    return $;
  }
}
