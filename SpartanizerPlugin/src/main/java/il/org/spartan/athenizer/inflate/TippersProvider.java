package il.org.spartan.athenizer.inflate;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.tipping.*;

public abstract class TippersProvider {
  public abstract <N extends ASTNode> Tipper<N> getTipper(N n);
}
