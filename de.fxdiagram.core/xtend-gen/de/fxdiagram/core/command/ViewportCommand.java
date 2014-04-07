package de.fxdiagram.core.command;

import com.google.common.base.Objects;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.command.AnimationQueue;
import de.fxdiagram.core.command.Command;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.viewport.ViewportMemento;
import de.fxdiagram.core.viewport.ViewportTransition;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@SuppressWarnings("all")
public class ViewportCommand implements Command {
  private ViewportMemento fromMemento;
  
  private ViewportMemento toMemento;
  
  private Function0<? extends ViewportTransition> transitionFactory;
  
  public ViewportCommand(final Function0<? extends ViewportTransition> transitionFactory) {
    this.transitionFactory = transitionFactory;
  }
  
  public void execute(final CommandContext context) {
    final ViewportTransition transition = this.transitionFactory.apply();
    boolean _notEquals = (!Objects.equal(transition, null));
    if (_notEquals) {
      ViewportMemento _from = transition.getFrom();
      this.fromMemento = _from;
      ViewportMemento _to = transition.getTo();
      this.toMemento = _to;
      AnimationQueue _animationQueue = context.getAnimationQueue();
      final Function0<ViewportTransition> _function = new Function0<ViewportTransition>() {
        public ViewportTransition apply() {
          return transition;
        }
      };
      _animationQueue.enqueue(_function);
    }
  }
  
  public boolean canUndo() {
    return (!Objects.equal(this.fromMemento, null));
  }
  
  public void undo(final CommandContext context) {
    AnimationQueue _animationQueue = context.getAnimationQueue();
    final Function0<ViewportTransition> _function = new Function0<ViewportTransition>() {
      public ViewportTransition apply() {
        XRoot _root = context.getRoot();
        Duration _defaultUndoDuration = context.getDefaultUndoDuration();
        return new ViewportTransition(_root, ViewportCommand.this.fromMemento, _defaultUndoDuration);
      }
    };
    _animationQueue.enqueue(_function);
  }
  
  public boolean canRedo() {
    return (!Objects.equal(this.toMemento, null));
  }
  
  public void redo(final CommandContext context) {
    AnimationQueue _animationQueue = context.getAnimationQueue();
    final Function0<ViewportTransition> _function = new Function0<ViewportTransition>() {
      public ViewportTransition apply() {
        XRoot _root = context.getRoot();
        Duration _defaultUndoDuration = context.getDefaultUndoDuration();
        return new ViewportTransition(_root, ViewportCommand.this.toMemento, _defaultUndoDuration);
      }
    };
    _animationQueue.enqueue(_function);
  }
}
