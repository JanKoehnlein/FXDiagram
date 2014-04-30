package de.fxdiagram.core.command;

import com.google.common.base.Objects;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.command.AnimationCommand;
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
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public abstract class AbstractAnimationCommand implements AnimationCommand {
  private ViewportMemento fromMemento;
  
  private ViewportMemento toMemento;
  
  public Animation getExecuteAnimation(final CommandContext context) {
    XRoot _root = context.getRoot();
    ViewportTransform _viewportTransform = _root.getViewportTransform();
    ViewportMemento _createMemento = _viewportTransform.createMemento();
    this.fromMemento = _createMemento;
    final Animation animation = this.createExecuteAnimation(context);
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
              ViewportTransform _viewportTransform = _root.getViewportTransform();
              ViewportMemento _createMemento = _viewportTransform.createMemento();
              AbstractAnimationCommand.this.toMemento = _createMemento;
            }
          };
          it.setOnFinished(_function);
        }
      };
      return ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function);
    } else {
      this.toMemento = this.fromMemento;
      return null;
    }
  }
  
  public abstract Animation createExecuteAnimation(final CommandContext context);
  
  public Animation getUndoAnimation(final CommandContext context) {
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
  
  public abstract Animation createUndoAnimation(final CommandContext context);
  
  public Animation getRedoAnimation(final CommandContext context) {
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
  
  public abstract Animation createRedoAnimation(final CommandContext context);
  
  public boolean clearRedoStackOnExecute() {
    return true;
  }
}
