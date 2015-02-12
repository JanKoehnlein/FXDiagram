package de.fxdiagram.core.command;

import de.fxdiagram.core.command.AbstractAnimationCommand;
import de.fxdiagram.core.command.AnimationCommand;
import de.fxdiagram.core.command.CommandContext;
import javafx.animation.Animation;

/**
 * Postpones the delegate command creation until it is executed.
 */
@SuppressWarnings("all")
public abstract class LazyCommand implements AnimationCommand {
  private AbstractAnimationCommand delegate;
  
  protected abstract AbstractAnimationCommand createDelegate();
  
  @Override
  public boolean clearRedoStackOnExecute() {
    boolean _clearRedoStackOnExecute = false;
    if (this.delegate!=null) {
      _clearRedoStackOnExecute=this.delegate.clearRedoStackOnExecute();
    }
    return _clearRedoStackOnExecute;
  }
  
  @Override
  public void skipViewportRestore() {
    if (this.delegate!=null) {
      this.delegate.skipViewportRestore();
    }
  }
  
  @Override
  public Animation getExecuteAnimation(final CommandContext context) {
    Animation _xblockexpression = null;
    {
      AbstractAnimationCommand _createDelegate = this.createDelegate();
      this.delegate = _createDelegate;
      _xblockexpression = this.delegate.getExecuteAnimation(context);
    }
    return _xblockexpression;
  }
  
  @Override
  public Animation getUndoAnimation(final CommandContext context) {
    return this.delegate.getUndoAnimation(context);
  }
  
  @Override
  public Animation getRedoAnimation(final CommandContext context) {
    return this.delegate.getRedoAnimation(context);
  }
}
