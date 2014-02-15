package de.fxdiagram.core.command;

import javafx.animation.Animation;
import javafx.util.Duration;

@SuppressWarnings("all")
public interface Command {
  public abstract Animation execute(final Duration duration);
  
  public abstract boolean canUndo();
  
  public abstract Animation undo(final Duration duration);
  
  public abstract boolean canRedo();
  
  public abstract Animation redo(final Duration duration);
}
