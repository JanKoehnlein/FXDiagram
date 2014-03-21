package de.fxdiagram.core.command;

import com.google.common.base.Objects;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.command.AnimationQueue;
import de.fxdiagram.core.command.Command;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.viewport.ViewportMemento;
import de.fxdiagram.core.viewport.ViewportTransform;
import de.fxdiagram.core.viewport.ViewportTransition;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public abstract class AbstractAnimationCommand implements Command {
  private ViewportMemento fromMemento;
  
  private ViewportMemento toMemento;
  
  public void execute(final CommandContext context) {
    AnimationQueue _animationQueue = context.getAnimationQueue();
    final Function0<SequentialTransition> _function = new Function0<SequentialTransition>() {
      public SequentialTransition apply() {
        XRoot _root = context.getRoot();
        ViewportTransform _diagramTransform = _root.getDiagramTransform();
        ViewportMemento _createMemento = _diagramTransform.createMemento();
        AbstractAnimationCommand.this.fromMemento = _createMemento;
        final Animation animation = AbstractAnimationCommand.this.createExecuteAnimation(context);
        SequentialTransition _xifexpression = null;
        boolean _notEquals = (!Objects.equal(animation, null));
        if (_notEquals) {
          SequentialTransition _sequentialTransition = new SequentialTransition();
          final Procedure1<SequentialTransition> _function = new Procedure1<SequentialTransition>() {
            public void apply(final SequentialTransition it) {
              ObservableList<Animation> _children = it.getChildren();
              _children.add(animation);
              final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
                public void handle(final ActionEvent it) {
                  XRoot _root = context.getRoot();
                  ViewportTransform _diagramTransform = _root.getDiagramTransform();
                  ViewportMemento _createMemento = _diagramTransform.createMemento();
                  AbstractAnimationCommand.this.toMemento = _createMemento;
                }
              };
              it.setOnFinished(_function);
            }
          };
          _xifexpression = ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function);
        } else {
          SequentialTransition _xblockexpression = null;
          {
            AbstractAnimationCommand.this.toMemento = AbstractAnimationCommand.this.fromMemento;
            _xblockexpression = null;
          }
          _xifexpression = _xblockexpression;
        }
        return _xifexpression;
      }
    };
    _animationQueue.enqueue(_function);
  }
  
  public boolean canUndo() {
    return true;
  }
  
  public void undo(final CommandContext context) {
    AnimationQueue _animationQueue = context.getAnimationQueue();
    final Function0<SequentialTransition> _function = new Function0<SequentialTransition>() {
      public SequentialTransition apply() {
        SequentialTransition _sequentialTransition = new SequentialTransition();
        final Procedure1<SequentialTransition> _function = new Procedure1<SequentialTransition>() {
          public void apply(final SequentialTransition it) {
            ObservableList<Animation> _children = it.getChildren();
            XRoot _root = context.getRoot();
            Duration _defaultUndoDuration = context.getDefaultUndoDuration();
            ViewportTransition _viewportTransition = new ViewportTransition(_root, AbstractAnimationCommand.this.toMemento, _defaultUndoDuration);
            _children.add(_viewportTransition);
            ObservableList<Animation> _children_1 = it.getChildren();
            Animation _createUndoAnimation = AbstractAnimationCommand.this.createUndoAnimation(context);
            _children_1.add(_createUndoAnimation);
          }
        };
        return ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function);
      }
    };
    _animationQueue.enqueue(_function);
  }
  
  public boolean canRedo() {
    return true;
  }
  
  public void redo(final CommandContext context) {
    AnimationQueue _animationQueue = context.getAnimationQueue();
    final Function0<SequentialTransition> _function = new Function0<SequentialTransition>() {
      public SequentialTransition apply() {
        SequentialTransition _sequentialTransition = new SequentialTransition();
        final Procedure1<SequentialTransition> _function = new Procedure1<SequentialTransition>() {
          public void apply(final SequentialTransition it) {
            ObservableList<Animation> _children = it.getChildren();
            XRoot _root = context.getRoot();
            Duration _defaultUndoDuration = context.getDefaultUndoDuration();
            ViewportTransition _viewportTransition = new ViewportTransition(_root, AbstractAnimationCommand.this.fromMemento, _defaultUndoDuration);
            _children.add(_viewportTransition);
            ObservableList<Animation> _children_1 = it.getChildren();
            Animation _createRedoAnimation = AbstractAnimationCommand.this.createRedoAnimation(context);
            _children_1.add(_createRedoAnimation);
          }
        };
        return ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function);
      }
    };
    _animationQueue.enqueue(_function);
  }
  
  public abstract Animation createExecuteAnimation(final CommandContext context);
  
  public abstract Animation createUndoAnimation(final CommandContext context);
  
  public abstract Animation createRedoAnimation(final CommandContext context);
}
