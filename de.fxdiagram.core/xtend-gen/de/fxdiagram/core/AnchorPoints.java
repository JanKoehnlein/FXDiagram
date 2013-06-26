package de.fxdiagram.core;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.XNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.Observable;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;

@SuppressWarnings("all")
public class AnchorPoints extends ObjectBinding<List<Point2D>> {
  private XNode host;
  
  public AnchorPoints(final XNode host) {
    this.host = host;
    ArrayList<ReadOnlyProperty<? extends Object>> dependencies = CollectionLiterals.<ReadOnlyProperty<? extends Object>>newArrayList();
    Node current = host;
    boolean _dowhile = false;
    do {
      {
        ReadOnlyObjectProperty<Bounds> _boundsInLocalProperty = current.boundsInLocalProperty();
        dependencies.add(_boundsInLocalProperty);
        DoubleProperty _layoutXProperty = current.layoutXProperty();
        dependencies.add(_layoutXProperty);
        DoubleProperty _layoutYProperty = current.layoutYProperty();
        dependencies.add(_layoutYProperty);
        DoubleProperty _scaleXProperty = current.scaleXProperty();
        dependencies.add(_scaleXProperty);
        DoubleProperty _scaleYProperty = current.scaleYProperty();
        dependencies.add(_scaleYProperty);
        DoubleProperty _rotateProperty = current.rotateProperty();
        dependencies.add(_rotateProperty);
        Parent _parent = current.getParent();
        current = _parent;
      }
      boolean _notEquals = (!Objects.equal(current, null));
      _dowhile = _notEquals;
    } while(_dowhile);
    final ArrayList<ReadOnlyProperty<? extends Object>> _converted_dependencies = (ArrayList<ReadOnlyProperty<? extends Object>>)dependencies;
    this.bind(((Observable[])Conversions.unwrapArray(_converted_dependencies, Observable.class)));
  }
  
  protected List<Point2D> computeValue() {
    List<Point2D> _xblockexpression = null;
    {
      Node _node = null;
      if (this.host!=null) {
        _node=this.host.getNode();
      }
      Bounds _boundsInLocal = null;
      if (_node!=null) {
        _boundsInLocal=_node.getBoundsInLocal();
      }
      final Bounds bounds = _boundsInLocal;
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
          _xblockexpression_1 = (Collections.<Point2D>unmodifiableList(Lists.<Point2D>newArrayList(_localToRoot, _localToRoot_1, _localToRoot_2, _localToRoot_3)));
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
}
