package de.fxdiagram.core.command;

import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.command.AbstractAnimationCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.extensions.DurationExtensions;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ChangeDiagramCommand extends AbstractAnimationCommand {
  private XDiagram newDiagram;
  
  private XDiagram oldDiagram;
  
  public ChangeDiagramCommand(final XDiagram newDiagram) {
    this.newDiagram = newDiagram;
  }
  
  @Override
  public Animation createExecuteAnimation(final CommandContext context) {
    SequentialTransition _xblockexpression = null;
    {
      XRoot _root = context.getRoot();
      XDiagram _diagram = _root.getDiagram();
      this.oldDiagram = _diagram;
      XRoot _root_1 = context.getRoot();
      Duration _defaultUndoDuration = context.getDefaultUndoDuration();
      _xblockexpression = this.swap(_root_1, this.newDiagram, _defaultUndoDuration);
    }
    return _xblockexpression;
  }
  
  @Override
  public Animation createUndoAnimation(final CommandContext context) {
    XRoot _root = context.getRoot();
    Duration _defaultUndoDuration = context.getDefaultUndoDuration();
    return this.swap(_root, this.oldDiagram, _defaultUndoDuration);
  }
  
  @Override
  public Animation createRedoAnimation(final CommandContext context) {
    XRoot _root = context.getRoot();
    Duration _defaultUndoDuration = context.getDefaultUndoDuration();
    return this.swap(_root, this.newDiagram, _defaultUndoDuration);
  }
  
  protected SequentialTransition swap(final XRoot root, final XDiagram appear, final Duration duration) {
    SequentialTransition _sequentialTransition = new SequentialTransition();
    final Procedure1<SequentialTransition> _function = (SequentialTransition it) -> {
      ObservableList<Animation> _children = it.getChildren();
      FadeTransition _fadeTransition = new FadeTransition();
      final Procedure1<FadeTransition> _function_1 = (FadeTransition it_1) -> {
        XDiagram _diagram = root.getDiagram();
        it_1.setNode(_diagram);
        it_1.setFromValue(1);
        it_1.setToValue(0);
        it_1.setCycleCount(1);
        Duration _xifexpression = null;
        XDiagram _diagram_1 = root.getDiagram();
        ObservableList<XNode> _nodes = _diagram_1.getNodes();
        boolean _isEmpty = _nodes.isEmpty();
        if (_isEmpty) {
          _xifexpression = DurationExtensions.millis(0);
        } else {
          _xifexpression = DurationExtensions.operator_divide(duration, 2);
        }
        it_1.setDuration(_xifexpression);
      };
      FadeTransition _doubleArrow = ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition, _function_1);
      final Procedure1<FadeTransition> _function_2 = (FadeTransition it_1) -> {
        final EventHandler<ActionEvent> _function_3 = (ActionEvent it_2) -> {
          root.setDiagram(appear);
          appear.setOpacity(0);
        };
        it_1.setOnFinished(_function_3);
      };
      FadeTransition _doubleArrow_1 = ObjectExtensions.<FadeTransition>operator_doubleArrow(_doubleArrow, _function_2);
      _children.add(_doubleArrow_1);
      ObservableList<Animation> _children_1 = it.getChildren();
      FadeTransition _fadeTransition_1 = new FadeTransition();
      final Procedure1<FadeTransition> _function_3 = (FadeTransition it_1) -> {
        it_1.setNode(appear);
        it_1.setFromValue(0);
        it_1.setToValue(1);
        it_1.setCycleCount(1);
        Duration _xifexpression = null;
        ObservableList<XNode> _nodes = appear.getNodes();
        boolean _isEmpty = _nodes.isEmpty();
        if (_isEmpty) {
          _xifexpression = DurationExtensions.millis(0);
        } else {
          _xifexpression = DurationExtensions.operator_divide(duration, 2);
        }
        it_1.setDuration(_xifexpression);
      };
      FadeTransition _doubleArrow_2 = ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition_1, _function_3);
      _children_1.add(_doubleArrow_2);
    };
    return ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function);
  }
}
