package de.fxdiagram.mapping.behavior;

import de.fxdiagram.core.XConnectionLabel;

@SuppressWarnings("all")
public interface AddRemoveAcceptor {
  public abstract void add(final XConnectionLabel label);
  
  public abstract void remove(final XConnectionLabel label);
  
  public abstract void keep(final XConnectionLabel label);
}
