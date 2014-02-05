package de.fxdiagram.core.tools;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.model.DomainObjectHandle;

@SuppressWarnings("all")
public interface ChooserConnectionProvider {
  public abstract XConnection getConnection(final XNode host, final XNode choice, final DomainObjectHandle choiceInfo);
}
