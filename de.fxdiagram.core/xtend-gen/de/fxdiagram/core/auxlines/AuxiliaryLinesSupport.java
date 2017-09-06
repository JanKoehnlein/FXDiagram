package de.fxdiagram.core.auxlines;

import com.google.common.collect.Iterables;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.auxlines.AuxiliaryLine;
import de.fxdiagram.core.auxlines.AuxiliaryLinesCache;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class AuxiliaryLinesSupport {
  private AuxiliaryLinesCache cache;
  
  private Group group = new Group();
  
  public AuxiliaryLinesSupport(final XDiagram diagram) {
    AuxiliaryLinesCache _auxiliaryLinesCache = new AuxiliaryLinesCache(diagram);
    this.cache = _auxiliaryLinesCache;
    ObservableList<Node> _children = diagram.getButtonLayer().getChildren();
    _children.add(this.group);
  }
  
  public void show(final Iterable<? extends XShape> selection) {
    this.group.getChildren().clear();
    final Iterable<XNode> selectedNodes = Iterables.<XNode>filter(selection, XNode.class);
    int _size = IterableExtensions.size(selectedNodes);
    boolean _equals = (_size == 1);
    if (_equals) {
      final Iterable<AuxiliaryLine> lines = this.cache.getAuxiliaryLines(IterableExtensions.<XNode>head(selectedNodes));
      final Consumer<AuxiliaryLine> _function = (AuxiliaryLine it) -> {
        ObservableList<Node> _children = this.group.getChildren();
        Node _createNode = it.createNode();
        _children.add(_createNode);
      };
      lines.forEach(_function);
    } else {
      final Iterable<XControlPoint> selectedControlPoints = Iterables.<XControlPoint>filter(selection, XControlPoint.class);
      int _size_1 = IterableExtensions.size(selectedControlPoints);
      boolean _equals_1 = (_size_1 == 1);
      if (_equals_1) {
        final Iterable<AuxiliaryLine> lines_1 = this.cache.getAuxiliaryLines(IterableExtensions.<XControlPoint>head(selectedControlPoints));
        final Consumer<AuxiliaryLine> _function_1 = (AuxiliaryLine it) -> {
          ObservableList<Node> _children = this.group.getChildren();
          Node _createNode = it.createNode();
          _children.add(_createNode);
        };
        lines_1.forEach(_function_1);
      }
    }
  }
  
  public Point2D getSnappedPosition(final XShape shape, final Point2D newPositionInDiagram) {
    Point2D _switchResult = null;
    boolean _matched = false;
    if (shape instanceof XControlPoint) {
      _matched=true;
      _switchResult = this.cache.getSnappedPosition(((XControlPoint)shape), newPositionInDiagram);
    }
    if (!_matched) {
      if (shape instanceof XNode) {
        _matched=true;
        _switchResult = this.cache.getSnappedPosition(((XNode)shape), newPositionInDiagram);
      }
    }
    if (!_matched) {
      double _layoutX = shape.getLayoutX();
      double _layoutY = shape.getLayoutY();
      _switchResult = new Point2D(_layoutX, _layoutY);
    }
    return _switchResult;
  }
  
  public void hide() {
    this.group.getChildren().clear();
  }
}
