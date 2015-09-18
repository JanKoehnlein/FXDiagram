package de.fxdiagram.mapping.reconcile;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.ConnectionRouter;
import de.fxdiagram.core.command.AbstractAnimationCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.command.EmptyTransition;
import de.fxdiagram.core.extensions.CoreExtensions;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.animation.SequentialTransition;
import javafx.beans.property.DoubleProperty;
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
    Duration _defaultExecuteDuration = context.getDefaultExecuteDuration();
    return this.morph(this.newNode, _defaultExecuteDuration);
  }
  
  @Override
  public Animation createUndoAnimation(final CommandContext context) {
    Duration _defaultUndoDuration = context.getDefaultUndoDuration();
    return this.morph(this.oldNode, _defaultUndoDuration);
  }
  
  @Override
  public Animation createRedoAnimation(final CommandContext context) {
    Duration _defaultUndoDuration = context.getDefaultUndoDuration();
    return this.morph(this.newNode, _defaultUndoDuration);
  }
  
  protected SequentialTransition morph(final XNode nodeAfterMorph, final Duration duration) {
    SequentialTransition _xblockexpression = null;
    {
      Point2D _xifexpression = null;
      if (this.isSource) {
        ConnectionRouter _connectionRouter = this.connection.getConnectionRouter();
        XNode _source = this.connection.getSource();
        _xifexpression = _connectionRouter.findClosestSourceAnchor(_source, false);
      } else {
        ConnectionRouter _connectionRouter_1 = this.connection.getConnectionRouter();
        XNode _target = this.connection.getTarget();
        _xifexpression = _connectionRouter_1.findClosestTargetAnchor(_target, false);
      }
      final Point2D from = _xifexpression;
      Point2D _xifexpression_1 = null;
      if (this.isSource) {
        ConnectionRouter _connectionRouter_2 = this.connection.getConnectionRouter();
        _xifexpression_1 = _connectionRouter_2.findClosestSourceAnchor(nodeAfterMorph, false);
      } else {
        ConnectionRouter _connectionRouter_3 = this.connection.getConnectionRouter();
        _xifexpression_1 = _connectionRouter_3.findClosestTargetAnchor(nodeAfterMorph, false);
      }
      final Point2D to = _xifexpression_1;
      Group _group = new Group();
      final Procedure1<Group> _function = (Group it) -> {
        double _x = from.getX();
        it.setTranslateX(_x);
        double _y = from.getY();
        it.setTranslateY(_y);
      };
      final Group dummy = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
      final Procedure1<XNode> _function_1 = (XNode it) -> {
        DoubleProperty _layoutXProperty = it.layoutXProperty();
        DoubleProperty _translateXProperty = dummy.translateXProperty();
        _layoutXProperty.bind(_translateXProperty);
        DoubleProperty _layoutYProperty = it.layoutYProperty();
        DoubleProperty _translateYProperty = dummy.translateYProperty();
        _layoutYProperty.bind(_translateYProperty);
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
            XDiagram _diagram = CoreExtensions.getDiagram(this.connection);
            ObservableList<XNode> _nodes = _diagram.getNodes();
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
        XDiagram _diagram = CoreExtensions.getDiagram(this.connection);
        ObservableList<XNode> _nodes = _diagram.getNodes();
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
