package de.fxdiagram.core.command;

import com.google.common.base.Objects;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.command.AnimationCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.viewport.ViewportMemento;
import de.fxdiagram.core.viewport.ViewportTransition;
import javafx.animation.Animation;
import javafx.util.Duration;

@SuppressWarnings("all")
public abstract class ViewportCommand implements AnimationCommand {
  private ViewportMemento fromMemento;
  
  private ViewportMemento toMemento;
  
  @Override
  public boolean clearRedoStackOnExecute() {
    return false;
  }
  
  @Override
  public void skipViewportRestore() {
  }
  
  public abstract ViewportTransition createViewportTransiton(final CommandContext context);
  
  @Override
  public Animation getExecuteAnimation(final CommandContext context) {
    ViewportTransition _xblockexpression = null;
    {
      final ViewportTransition transition = this.createViewportTransiton(context);
      ViewportTransition _xifexpression = null;
      boolean _notEquals = (!Objects.equal(transition, null));
      if (_notEquals) {
        ViewportTransition _xblockexpression_1 = null;
        {
          this.fromMemento = transition.getFrom();
          this.toMemento = transition.getTo();
          _xblockexpression_1 = transition;
        }
        _xifexpression = _xblockexpression_1;
      } else {
        _xifexpression = null;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  @Override
  public Animation getUndoAnimation(final CommandContext context) {
    XRoot _root = context.getRoot();
    Duration _defaultUndoDuration = context.getDefaultUndoDuration();
    return new ViewportTransition(_root, this.fromMemento, _defaultUndoDuration);
  }
  
  @Override
  public Animation getRedoAnimation(final CommandContext context) {
    XRoot _root = context.getRoot();
    Duration _defaultUndoDuration = context.getDefaultUndoDuration();
    return new ViewportTransition(_root, this.toMemento, _defaultUndoDuration);
  }
}
