package il.org.spartan.collections;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/** Set operations implemented as a state-less class, with just static
 * methods. */
public enum Sets {
  ;
  @NotNull
  public static <T, S extends T> Collection<T> addAll(@NotNull final Collection<T> dest, @NotNull final Iterable<S> src) {
    for (final S ¢ : src)
      dest.add(¢);
    return dest;
  }

  @NotNull
  public static <T extends Collection<E>, E> T addAll(@NotNull final T dest, @NotNull final E[] src) {
    for (final E ¢ : src)
      dest.add(¢);
    return dest;
  }

  @NotNull
  public static <E> Set<E> intersection(@NotNull final Collection<E> lhs, @NotNull final Collection<E> rhs) {
    final Set<E> $ = new HashSet<>(lhs);
    $.retainAll(rhs);
    return $;
  }

  @NotNull
  public static <E> Set<E> union(@NotNull final Collection<E> lhs, @NotNull final Collection<E> rhs) {
    final Set<E> $ = new HashSet<>(lhs);
    $.addAll(rhs);
    return $;
  }
}
