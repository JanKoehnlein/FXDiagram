package de.fxdiagram.core;

import com.google.common.base.Objects;
import de.fxdiagram.core.AnchorPoints;
import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.core.binding.DoubleExpressionExtensions;
import java.util.List;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polyline;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pair;

@SuppressWarnings("all")
public class XConnection extends Polyline implements XActivatable {
  private XNode _source;
  
  public XNode getSource() {
    return this._source;
  }
  
  public void setSource(final XNode source) {
    this._source = source;
  }
  
  private XNode _target;
  
  public XNode getTarget() {
    return this._target;
  }
  
  public void setTarget(final XNode target) {
    this._target = target;
  }
  
  private XConnectionLabel _label;
  
  public XConnectionLabel getLabel() {
    return this._label;
  }
  
  public void setLabel(final XConnectionLabel label) {
    this._label = label;
  }
  
  private boolean isActive;
  
  public XConnection(final XNode source, final XNode target) {
    this.setSource(source);
    this.setTarget(target);
  }
  
  protected boolean calculatePoints() {
    boolean _xblockexpression = false;
    {
      double shortestDistance = Double.POSITIVE_INFINITY;
      XNode _source = this.getSource();
      AnchorPoints _anchorPoints = _source.getAnchorPoints();
      List<Point2D> _get = _anchorPoints.get();
      Point2D _head = IterableExtensions.<Point2D>head(_get);
      XNode _target = this.getTarget();
      AnchorPoints _anchorPoints_1 = _target.getAnchorPoints();
      List<Point2D> _get_1 = _anchorPoints_1.get();
      Point2D _head_1 = IterableExtensions.<Point2D>head(_get_1);
      Pair<Point2D,Point2D> nearestAnchors = Pair.<Point2D, Point2D>of(_head, _head_1);
      XNode _source_1 = this.getSource();
      AnchorPoints _anchorPoints_2 = _source_1.getAnchorPoints();
      List<Point2D> _get_2 = _anchorPoints_2.get();
      for (final Point2D sourceAnchor : _get_2) {
        XNode _target_1 = this.getTarget();
        AnchorPoints _anchorPoints_3 = _target_1.getAnchorPoints();
        List<Point2D> _get_3 = _anchorPoints_3.get();
        for (final Point2D targetAnchor : _get_3) {
          {
            final double currentDistance = sourceAnchor.distance(targetAnchor);
            boolean _lessThan = (currentDistance < shortestDistance);
            if (_lessThan) {
              shortestDistance = currentDistance;
              Pair<Point2D,Point2D> _mappedTo = Pair.<Point2D, Point2D>of(sourceAnchor, targetAnchor);
              nearestAnchors = _mappedTo;
            }
          }
        }
      }
      ObservableList<Double> _points = this.getPoints();
      Point2D _key = nearestAnchors.getKey();
      double _x = _key.getX();
      Point2D _key_1 = nearestAnchors.getKey();
      double _y = _key_1.getY();
      Point2D _value = nearestAnchors.getValue();
      double _x_1 = _value.getX();
      Point2D _value_1 = nearestAnchors.getValue();
      double _y_1 = _value_1.getY();
      boolean _setAll = _points.setAll(Double.valueOf(_x), Double.valueOf(_y), Double.valueOf(_x_1), Double.valueOf(_y_1));
      _xblockexpression = (_setAll);
    }
    return _xblockexpression;
  }
  
  public void activate() {
    boolean _not = (!this.isActive);
    if (_not) {
      this.doActivate();
    }
    this.isActive = true;
  }
  
  public void doActivate() {
    DoubleProperty _strokeWidthProperty = this.strokeWidthProperty();
    XRootDiagram _rootDiagram = Extensions.getRootDiagram(this);
    DoubleProperty _scaleProperty = _rootDiagram.getScaleProperty();
    DoubleBinding _divide = DoubleExpressionExtensions.operator_divide(1.5, _scaleProperty);
    _strokeWidthProperty.bind(_divide);
    XNode _source = this.getSource();
    AnchorPoints _anchorPoints = _source.getAnchorPoints();
    final ChangeListener<List<Point2D>> _function = new ChangeListener<List<Point2D>>() {
        public void changed(final ObservableValue<? extends List<Point2D>> element, final List<Point2D> oldValue, final List<Point2D> newValue) {
          XConnection.this.calculatePoints();
        }
      };
    _anchorPoints.addListener(_function);
    XNode _target = this.getTarget();
    AnchorPoints _anchorPoints_1 = _target.getAnchorPoints();
    final ChangeListener<List<Point2D>> _function_1 = new ChangeListener<List<Point2D>>() {
        public void changed(final ObservableValue<? extends List<Point2D>> element, final List<Point2D> oldValue, final List<Point2D> newValue) {
          XConnection.this.calculatePoints();
        }
      };
    _anchorPoints_1.addListener(_function_1);
    this.calculatePoints();
    XConnectionLabel _label = this.getLabel();
    boolean _notEquals = (!Objects.equal(_label, null));
    if (_notEquals) {
      XConnectionLabel _label_1 = this.getLabel();
      _label_1.activate();
    }
  }
}
