package de.fxdiagram.core.behavior;

import de.fxdiagram.core.behavior.Behavior;

@SuppressWarnings("all")
public interface NavigationBehavior extends Behavior {
  public abstract boolean next();
  
  public abstract boolean previous();
}
