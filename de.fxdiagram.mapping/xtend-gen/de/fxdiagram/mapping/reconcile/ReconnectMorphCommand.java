package de.fxdiagram.mapping.reconcile;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.command.AbstractAnimationCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.command.EmptyTransition;
import de.fxdiagram.core.extensions.CoreExtensions;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.animation.SequentialTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.util.Duration;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@FinalFieldsConstructor
@SuppressWarnings("all")
public class ReconnectMorphCommand extends AbstractAnimationCommand {
  private final XConnection connection;
  
  private final XNode oldNode;
  
  private final XNode newNode;
  
  private final boolean isSource;
  
  @Override
  public Animation createExecuteAnimation(final CommandContext context) {
    return this.morph(this.newNode, context.getDefaultExecuteDuration());
  }
  
  @Override
  public Animation createUndoAnimation(final CommandContext context) {
    return this.morph(this.oldNode, context.getDefaultUndoDuration());
  }
  
  @Override
  public Animation createRedoAnimation(final CommandContext context) {
    return this.morph(this.newNode, context.getDefaultUndoDuration());
  }
  
  protected SequentialTransition morph(final XNode nodeAfterMorph, final Duration duration) {
    SequentialTransition _xblockexpression = null;
    {
      Point2D _xifexpression = null;
      if (this.isSource) {
        _xifexpression = this.connection.getConnectionRouter().findClosestSourceAnchor(this.connection.getSource(), false);
      } else {
        _xifexpression = this.connection.getConnectionRouter().findClosestTargetAnchor(this.connection.getTarget(), false);
      }
      final Point2D from = _xifexpression;
      Point2D _xifexpression_1 = null;
      if (this.isSource) {
        _xifexpression_1 = this.connection.getConnectionRouter().findClosestSourceAnchor(nodeAfterMorph, false);
      } else {
        _xifexpression_1 = this.connection.getConnectionRouter().findClosestTargetAnchor(nodeAfterMorph, false);
      }
      final Point2D to = _xifexpression_1;
      Group _group = new Group();
      final Procedure1<Group> _function = (Group it) -> {
        it.setTranslateX(from.getX());
        it.setTranslateY(from.getY());
      };
      final Group dummy = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
      final Procedure1<XNode> _function_1 = (XNode it) -> {
        it.layoutXProperty().bind(dummy.translateXProperty());
        it.layoutYProperty().bind(dummy.translateYProperty());
      };
      final XNode dummyNode = ObjectExtensions.<XNode>operator_doubleArrow(new XNode() {
        @Override
        protected Node createNode() {
          return new Circle(0);
        }
      }, _function_1);
      SequentialTransition _sequentialTransition = new SequentialTransition();
      final Procedure1<SequentialTransition> _function_2 = (SequentialTransition it) -> {
        ObservableList<Animation> _children = it.getChildren();
        EmptyTransition _emptyTransition = new EmptyTransition();
        final Procedure1<EmptyTransition> _function_3 = (EmptyTransition it_1) -> {
          final EventHandler<ActionEvent> _function_4 = (ActionEvent it_2) -> {
            ObservableList<XNode> _nodes = CoreExtensions.getDiagram(this.connection).getNodes();
            _nodes.add(dummyNode);
            if (this.isSource) {
              this.connection.setSource(dummyNode);
            } else {
              this.connection.setTarget(dummyNode);
            }
          };
          it_1.setOnFinished(_function_4);
        };
        EmptyTransition _doubleArrow = ObjectExtensions.<EmptyTransition>operator_doubleArrow(_emptyTransition, _function_3);
        _children.add(_doubleArrow);
        ObservableList<Animation> _children_1 = it.getChildren();
        PathTransition _createPathTransition = this.createPathTransition(from, to, dummyNode, dummy, nodeAfterMorph, duration);
        _children_1.add(_createPathTransition);
      };
      _xblockexpression = ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function_2);
    }
    return _xblockexpression;
  }
  
  protected PathTransition createPathTransition(final Point2D from, final Point2D to, final XNode dummyNode, final Node dummy, final XNode nodeAfterMorph, final Duration duration) {
    PathTransition _pathTransition = new PathTransition();
    final Procedure1<PathTransition> _function = (PathTransition it) -> {
      it.setNode(dummy);
      it.setDuration(duration);
      it.setCycleCount(1);
      Path _path = new Path();
      final Procedure1<Path> _function_1 = (Path it_1) -> {
        ObservableList<PathElement> _elements = it_1.getElements();
        double _x = from.getX();
        double _y = from.getY();
        MoveTo _moveTo = new MoveTo(_x, _y);
        _elements.add(_moveTo);
        ObservableList<PathElement> _elements_1 = it_1.getElements();
        double _x_1 = to.getX();
        double _y_1 = to.getY();
        LineTo _lineTo = new LineTo(_x_1, _y_1);
        _elements_1.add(_lineTo);
      };
      Path _doubleArrow = ObjectExtensions.<Path>operator_doubleArrow(_path, _function_1);
      it.setPath(_doubleArrow);
      final EventHandler<ActionEvent> _function_2 = (ActionEvent it_1) -> {
        if (this.isSource) {
          this.connection.setSource(nodeAfterMorph);
        } else {
          this.connection.setTarget(nodeAfterMorph);
        }
        ObservableList<XNode> _nodes = CoreExtensions.getDiagram(this.connection).getNodes();
        _nodes.remove(dummyNode);
      };
      it.setOnFinished(_function_2);
    };
    return ObjectExtensions.<PathTransition>operator_doubleArrow(_pathTransition, _function);
  }
  
  public ReconnectMorphCommand(final XConnection connection, final XNode oldNode, final XNode newNode, final boolean isSource) {
    super();
    this.connection = connection;
    this.oldNode = oldNode;
    this.newNode = newNode;
    this.isSource = isSource;
  }
}
