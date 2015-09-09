package de.fxdiagram.core.behavior;

import de.fxdiagram.core.XShape;
import de.fxdiagram.core.command.AnimationCommand;

@SuppressWarnings("all")
public interface UpdateAcceptor {
  public abstract void delete(final XShape shape);
  
  public abstract void add(final XShape shape);
  
  public abstract void morph(final AnimationCommand command);
}
