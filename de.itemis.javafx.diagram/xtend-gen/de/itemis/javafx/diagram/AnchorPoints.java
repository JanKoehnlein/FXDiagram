package de.itemis.javafx.diagram;

import de.itemis.javafx.diagram.Extensions;
import de.itemis.javafx.diagram.XNode;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;

@SuppressWarnings("all")
public class AnchorPoints extends ObjectBinding<List<Point2D>> {
  private XNode host;
  
  public AnchorPoints(final XNode host) {
    DoubleProperty _layoutXProperty = host.layoutXProperty();
    DoubleProperty _layoutYProperty = host.layoutYProperty();
    Node _node = host.getNode();
    ReadOnlyObjectProperty<Bounds> _boundsInLocalProperty = _node.boundsInLocalProperty();
    DoubleProperty _scaleXProperty = host.scaleXProperty();
    DoubleProperty _scaleYProperty = host.scaleYProperty();
    this.bind(_layoutXProperty, _layoutYProperty, _boundsInLocalProperty, _scaleXProperty, _scaleYProperty);
    this.host = host;
  }
  
  protected List<Point2D> computeValue() {
    ArrayList<Point2D> _xblockexpression = null;
    {
      Node _node = this.host==null?(Node)null:this.host.getNode();
      final Bounds bounds = _node==null?(Bounds)null:_node.getBoundsInLocal();
      ArrayList<Point2D> _xifexpression = null;
      boolean _notEquals = ObjectExtensions.operator_notEquals(bounds, null);
      if (_notEquals) {
        ArrayList<Point2D> _xblockexpression_1 = null;
        {
          double _maxX = bounds.getMaxX();
          double _minX = bounds.getMinX();
          double _plus = (_maxX + _minX);
          final double middleX = (_plus / 2);
          double _maxY = bounds.getMaxY();
          double _minY = bounds.getMinY();
          double _plus_1 = (_maxY + _minY);
          final double middleY = (_plus_1 / 2);
          Node _node_1 = this.host.getNode();
          double _minX_1 = bounds.getMinX();
          Point2D _localToRoot = Extensions.localToRoot(_node_1, _minX_1, middleY);
          Node _node_2 = this.host.getNode();
          double _maxX_1 = bounds.getMaxX();
          Point2D _localToRoot_1 = Extensions.localToRoot(_node_2, _maxX_1, middleY);
          Node _node_3 = this.host.getNode();
          double _minY_1 = bounds.getMinY();
          Point2D _localToRoot_2 = Extensions.localToRoot(_node_3, middleX, _minY_1);
          Node _node_4 = this.host.getNode();
          double _maxY_1 = bounds.getMaxY();
          Point2D _localToRoot_3 = Extensions.localToRoot(_node_4, middleX, _maxY_1);
          ArrayList<Point2D> _newArrayList = CollectionLiterals.<Point2D>newArrayList(_localToRoot, _localToRoot_1, _localToRoot_2, _localToRoot_3);
          _xblockexpression_1 = (_newArrayList);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
}
