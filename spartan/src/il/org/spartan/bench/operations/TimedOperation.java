/**
 *
 */
package il.org.spartan.bench.operations;

import il.org.spartan.bench.*;
import org.jetbrains.annotations.NotNull;

/** @author Yossi Gil
 * @since 26/04/2011 */
public abstract class TimedOperation extends NamedOperation {
  /** Instantiate {@link TimedOperation}.
   * @param name */
  public TimedOperation(final String name) {
    super(name);
  }

  @NotNull
  @Override public final Stopwatch call() {
    return run(makeStopWatch());
  }

  @NotNull
  @Override public Stopwatch netTime(final Stopwatch netTime) {
    return run(netTime);
  }

  @Override public Stopwatch netTime(final Stopwatch netTime, final int runs) {
    for (int ¢ = 0; ¢ < runs; ++¢)
      run(netTime);
    return netTime;
  }

  @NotNull
  public abstract Stopwatch run(Stopwatch s);
}
