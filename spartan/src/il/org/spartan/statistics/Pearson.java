/**
 *
 */
package il.org.spartan.statistics;

import il.org.spartan.xy.*;
import org.jetbrains.annotations.NotNull;

/** @author Yossi Gil
 * @since Apr 21, 2012 */
public enum Pearson {
  ;
  /** Computes the Pearson rho rank correlation coefficient of two data arrays.
   * @param x first data array
   * @param y second data array
   * @return Returns Spearman's rank correlation coefficient for the two arrays,
   *         or NaN if the data has fewer than 2 pairs
   * @throws IllegalArgumentException if the arrays lengths do not match */
  public static double rho(@NotNull final double[] x, @NotNull final double[] y) {
    if (x.length != y.length)
      throw new IllegalArgumentException(x.length + ":" + y.length);
    if (x.length < 2)
      return Double.NaN;
    final WeightedLeastSquares ls = new WeightedLeastSquares();
    ls.feed(x, y);
    return ls.r();
  }
}
