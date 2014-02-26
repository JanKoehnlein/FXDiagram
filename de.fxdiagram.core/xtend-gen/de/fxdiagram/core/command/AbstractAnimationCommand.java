package de.fxdiagram.core.command;

import de.fxdiagram.core.command.AnimationQueue;
import de.fxdiagram.core.command.Command;
import de.fxdiagram.core.command.CommandContext;
import javafx.animation.Animation;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@SuppressWarnings("all")
public abstract class AbstractAnimationCommand implements Command {
  public void execute(final CommandContext context) {
    AnimationQueue _animationQueue = context.getAnimationQueue();
    final Function0<Animation> _function = new Function0<Animation>() {
      public Animation apply() {
        return AbstractAnimationCommand.this.createExecuteAnimation(context);
      }
    };
    _animationQueue.enqueue(_function);
  }
  
  public boolean canUndo() {
    return true;
  }
  
  public void undo(final CommandContext context) {
    AnimationQueue _animationQueue = context.getAnimationQueue();
    final Function0<Animation> _function = new Function0<Animation>() {
      public Animation apply() {
        return AbstractAnimationCommand.this.createUndoAnimation(context);
      }
    };
    _animationQueue.enqueue(_function);
  }
  
  public boolean canRedo() {
    return true;
  }
  
  public void redo(final CommandContext context) {
    AnimationQueue _animationQueue = context.getAnimationQueue();
    final Function0<Animation> _function = new Function0<Animation>() {
      public Animation apply() {
        return AbstractAnimationCommand.this.createRedoAnimation(context);
      }
    };
    _animationQueue.enqueue(_function);
  }
  
  public abstract Animation createExecuteAnimation(final CommandContext context);
  
  public abstract Animation createUndoAnimation(final CommandContext context);
  
  public abstract Animation createRedoAnimation(final CommandContext context);
}
