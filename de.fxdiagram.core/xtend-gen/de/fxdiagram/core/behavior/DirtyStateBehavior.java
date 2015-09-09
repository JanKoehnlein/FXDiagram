package de.fxdiagram.core.behavior;

import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.behavior.DirtyState;
import de.fxdiagram.core.behavior.UpdateAcceptor;

@SuppressWarnings("all")
public interface DirtyStateBehavior extends Behavior {
  public abstract DirtyState getDirtyState();
  
  public abstract void showDirtyState(final DirtyState state);
  
  public abstract void update(final UpdateAcceptor acceptor);
}
