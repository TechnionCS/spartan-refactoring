/**
 *
 */
package il.org.spartan.bench;

import org.jetbrains.annotations.NotNull;

public class RunRecordAccumulator extends AbstractRunRecord {
  public void add(@NotNull final RunRecord ¢) {
    runs += ¢.runs;
    netTime += ¢.netTime;
    grossTime += ¢.grossTime;
  }
}