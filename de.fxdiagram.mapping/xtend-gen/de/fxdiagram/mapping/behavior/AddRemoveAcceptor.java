package de.fxdiagram.mapping.behavior;

import de.fxdiagram.core.XLabel;

@SuppressWarnings("all")
public interface AddRemoveAcceptor {
  public abstract void add(final XLabel label);
  
  public abstract void remove(final XLabel label);
  
  public abstract void keep(final XLabel label);
}
