package de.itemis.javafx.diagram;

import de.itemis.javafx.diagram.AnchorPoints;
import de.itemis.javafx.diagram.ShapeContainer;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polyline;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure3;

@SuppressWarnings("all")
public class Connection extends Polyline {
  private ShapeContainer _source;
  
  public ShapeContainer getSource() {
    return this._source;
  }
  
  public void setSource(final ShapeContainer source) {
    this._source = source;
  }
  
  private ShapeContainer _target;
  
  public ShapeContainer getTarget() {
    return this._target;
  }
  
  public void setTarget(final ShapeContainer target) {
    this._target = target;
  }
  
  public Connection(final ShapeContainer source, final ShapeContainer target) {
    this.setSource(source);
    this.setTarget(target);
    final Procedure3<ObservableValue,Object,Object> _function = new Procedure3<ObservableValue,Object,Object>() {
        public void apply(final ObservableValue element, final Object oldValue, final Object newValue) {
          Connection.this.calculatePoints();
        }
      };
    final ChangeListener changeListener = new ChangeListener<Object>() {
        public void changed(ObservableValue<? extends Object> arg0,Object arg1,Object arg2) {
          _function.apply(arg0,arg1,arg2);
        }
    };
    AnchorPoints _anchorPoints = source.getAnchorPoints();
    _anchorPoints.addListener(changeListener);
    AnchorPoints _anchorPoints_1 = target.getAnchorPoints();
    _anchorPoints_1.addListener(changeListener);
    this.calculatePoints();
  }
  
  protected boolean calculatePoints() {
    boolean _xblockexpression = false;
    {
      double shortestDistance = Double.POSITIVE_INFINITY;
      ShapeContainer _source = this.getSource();
      AnchorPoints _anchorPoints = _source.getAnchorPoints();
      List<Point2D> _get = _anchorPoints.get();
      Point2D _head = IterableExtensions.<Point2D>head(_get);
      ShapeContainer _target = this.getTarget();
      AnchorPoints _anchorPoints_1 = _target.getAnchorPoints();
      List<Point2D> _get_1 = _anchorPoints_1.get();
      Point2D _head_1 = IterableExtensions.<Point2D>head(_get_1);
      Pair<Point2D,Point2D> nearestAnchors = Pair.<Point2D, Point2D>of(_head, _head_1);
      ShapeContainer _source_1 = this.getSource();
      AnchorPoints _anchorPoints_2 = _source_1.getAnchorPoints();
      List<Point2D> _get_2 = _anchorPoints_2.get();
      for (final Point2D sourceAnchor : _get_2) {
        ShapeContainer _target_1 = this.getTarget();
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
}
