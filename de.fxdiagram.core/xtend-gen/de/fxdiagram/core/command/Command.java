package de.fxdiagram.core.command;

import de.fxdiagram.core.command.CommandContext;

@SuppressWarnings("all")
public interface Command {
  public abstract void execute(final CommandContext context);
  
  public abstract boolean canUndo();
  
  public abstract void undo(final CommandContext context);
  
  public abstract boolean canRedo();
  
  public abstract void redo(final CommandContext context);
}
