package de.fxdiagram.core.command;

import de.fxdiagram.core.command.CommandContext;
import javafx.animation.Animation;

/**
 * Base interface for commands.
 * 
 * FXDiagram extends the command pattern by the idea that everything should be animated
 * in order to give a better user experience. So instead of changing some diagram model,
 * we apply {@link Animations} to the scenegraph directly.
 * 
 * Consider overrding {@link AbstractAnimationCommand} instead.
 */
@SuppressWarnings("all")
public interface AnimationCommand {
  public abstract Animation getExecuteAnimation(final CommandContext context);
  
  public abstract Animation getUndoAnimation(final CommandContext context);
  
  public abstract Animation getRedoAnimation(final CommandContext context);
  
  public abstract boolean clearRedoStackOnExecute();
  
  /**
   * Consider package private. Clients should not override this.
   */
  public abstract void skipViewportRestore();
}
