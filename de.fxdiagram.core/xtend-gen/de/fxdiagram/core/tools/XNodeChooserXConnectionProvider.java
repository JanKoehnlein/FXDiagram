package de.fxdiagram.core.tools;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XNode;

@SuppressWarnings("all")
public interface XNodeChooserXConnectionProvider {
  public abstract XConnection getConnection(final XNode host, final XNode choice, final Object choiceInfo);
}
