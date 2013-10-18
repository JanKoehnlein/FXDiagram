package de.fxdiagram.core.behavior;

import de.fxdiagram.core.XActivatable;

@SuppressWarnings("all")
public interface Behavior extends XActivatable {
  public abstract Class<? extends Behavior> getBehaviorKey();
}
