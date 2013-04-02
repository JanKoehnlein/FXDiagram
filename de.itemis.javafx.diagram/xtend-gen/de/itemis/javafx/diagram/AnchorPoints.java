package de.itemis.javafx.diagram;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import de.itemis.javafx.diagram.Extensions;
import de.itemis.javafx.diagram.XNode;
import java.util.List;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;

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
    List<Point2D> _xblockexpression = null;
    {
      Node _node = this.host==null?(Node)null:this.host.getNode();
      final Bounds bounds = _node==null?(Bounds)null:_node.getBoundsInLocal();
      List<Point2D> _xifexpression = null;
      boolean _notEquals = (!Objects.equal(bounds, null));
      if (_notEquals) {
        List<Point2D> _xblockexpression_1 = null;
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
          List<Point2D> _xlistliteral = null;
          Builder<Point2D> _builder = ImmutableList.builder();
          _builder.add(_localToRoot);
          _builder.add(_localToRoot_1);
          _builder.add(_localToRoot_2);
          _builder.add(_localToRoot_3);
          _xlistliteral = _builder.build();
          _xblockexpression_1 = (_xlistliteral);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
}
