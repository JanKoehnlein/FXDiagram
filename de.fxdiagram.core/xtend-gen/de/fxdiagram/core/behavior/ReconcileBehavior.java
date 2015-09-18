package de.fxdiagram.core.behavior;

import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.behavior.DirtyState;
import de.fxdiagram.core.command.AnimationCommand;

/**
 * A behvaior to compare the shown state of a shape with its current domain model.
 */
@SuppressWarnings("all")
public interface ReconcileBehavior extends Behavior {
  public interface UpdateAcceptor {
    public abstract void delete(final XShape shape);
    
    public abstract void add(final XShape shape);
    
    public abstract void morph(final AnimationCommand command);
  }
  
  public abstract DirtyState getDirtyState();
  
  public abstract void showDirtyState(final DirtyState dirtyState);
  
  public abstract void hideDirtyState();
  
  public abstract void reconcile(final ReconcileBehavior.UpdateAcceptor acceptor);
}
