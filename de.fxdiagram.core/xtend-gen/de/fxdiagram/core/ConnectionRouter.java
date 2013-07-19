package de.fxdiagram.core;

import com.google.common.base.Objects;
import de.fxdiagram.core.Anchors;
import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRootDiagram;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ConnectionRouter implements XActivatable {
  private XConnection connection;
  
  private ChangeListener<Number> scalarListener;
  
  private ChangeListener<Bounds> boundsListener;
  
  public ConnectionRouter(final XConnection connection) {
    this.connection = connection;
    final ChangeListener<Number> _function = new ChangeListener<Number>() {
        public void changed(final ObservableValue<? extends Number> prop, final Number oldVal, final Number newVal) {
          connection.requestLayout();
        }
      };
    this.scalarListener = _function;
    final ChangeListener<Bounds> _function_1 = new ChangeListener<Bounds>() {
        public void changed(final ObservableValue<? extends Bounds> prop, final Bounds oldVal, final Bounds newVal) {
          connection.requestLayout();
        }
      };
    this.boundsListener = _function_1;
  }
  
  public void activate() {
    boolean _isActive = this.getIsActive();
    boolean _not = (!_isActive);
    if (_not) {
      XNode _source = this.connection.getSource();
      this.bindNode(_source);
      XNode _target = this.connection.getTarget();
      this.bindNode(_target);
    }
    this.isActiveProperty.set(true);
  }
  
  protected void bindNode(final XNode host) {
    Node current = host.getNode();
    boolean _dowhile = false;
    do {
      {
        ReadOnlyObjectProperty<Bounds> _boundsInLocalProperty = current.boundsInLocalProperty();
        _boundsInLocalProperty.addListener(this.boundsListener);
        DoubleProperty _layoutXProperty = current.layoutXProperty();
        _layoutXProperty.addListener(this.scalarListener);
        DoubleProperty _layoutYProperty = current.layoutYProperty();
        _layoutYProperty.addListener(this.scalarListener);
        DoubleProperty _scaleXProperty = current.scaleXProperty();
        _scaleXProperty.addListener(this.scalarListener);
        DoubleProperty _scaleYProperty = current.scaleYProperty();
        _scaleYProperty.addListener(this.scalarListener);
        DoubleProperty _rotateProperty = current.rotateProperty();
        _rotateProperty.addListener(this.scalarListener);
        Parent _parent = current.getParent();
        current = _parent;
      }
      boolean _and = false;
      boolean _notEquals = (!Objects.equal(current, null));
      if (!_notEquals) {
        _and = false;
      } else {
        boolean _not = (!(current instanceof XRootDiagram));
        _and = (_notEquals && _not);
      }
      _dowhile = _and;
    } while(_dowhile);
  }
  
  protected Object calculatePoints() {
    Object _xblockexpression = null;
    {
      final Pair<Point2D,Point2D> anchors = this.findClosestAnchors();
      final Point2D sourcePoint = anchors.getKey();
      final Point2D targetPoint = anchors.getValue();
      Object _xifexpression = null;
      ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
      int _size = _controlPoints.size();
      boolean _lessThan = (_size < 2);
      if (_lessThan) {
        ObservableList<XControlPoint> _controlPoints_1 = this.getControlPoints();
        XControlPoint _xControlPoint = new XControlPoint();
        final Procedure1<XControlPoint> _function = new Procedure1<XControlPoint>() {
            public void apply(final XControlPoint it) {
              double _x = sourcePoint.getX();
              it.setLayoutX(_x);
              double _y = sourcePoint.getY();
              it.setLayoutY(_y);
              it.setMovable(false);
            }
          };
        XControlPoint _doubleArrow = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint, _function);
        XControlPoint _xControlPoint_1 = new XControlPoint();
        final Procedure1<XControlPoint> _function_1 = new Procedure1<XControlPoint>() {
            public void apply(final XControlPoint it) {
              double _x = targetPoint.getX();
              it.setLayoutX(_x);
              double _y = targetPoint.getY();
              it.setLayoutY(_y);
              it.setMovable(false);
            }
          };
        XControlPoint _doubleArrow_1 = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint_1, _function_1);
        boolean _setAll = _controlPoints_1.setAll(
          new XControlPoint[] { _doubleArrow, _doubleArrow_1 });
        _xifexpression = Boolean.valueOf(_setAll);
      } else {
        XControlPoint _xblockexpression_1 = null;
        {
          ObservableList<XControlPoint> _controlPoints_2 = this.getControlPoints();
          XControlPoint _head = IterableExtensions.<XControlPoint>head(_controlPoints_2);
          final Procedure1<XControlPoint> _function_2 = new Procedure1<XControlPoint>() {
              public void apply(final XControlPoint it) {
                double _x = sourcePoint.getX();
                it.setLayoutX(_x);
                double _y = sourcePoint.getY();
                it.setLayoutY(_y);
                it.setMovable(false);
              }
            };
          ObjectExtensions.<XControlPoint>operator_doubleArrow(_head, _function_2);
          ObservableList<XControlPoint> _controlPoints_3 = this.getControlPoints();
          XControlPoint _last = IterableExtensions.<XControlPoint>last(_controlPoints_3);
          final Procedure1<XControlPoint> _function_3 = new Procedure1<XControlPoint>() {
              public void apply(final XControlPoint it) {
                double _x = targetPoint.getX();
                it.setLayoutX(_x);
                double _y = targetPoint.getY();
                it.setLayoutY(_y);
                it.setMovable(false);
              }
            };
          XControlPoint _doubleArrow_2 = ObjectExtensions.<XControlPoint>operator_doubleArrow(_last, _function_3);
          _xblockexpression_1 = (_doubleArrow_2);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  protected Pair<Point2D,Point2D> findClosestAnchors() {
    Pair<Point2D,Point2D> _xifexpression = null;
    ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
    int _size = _controlPoints.size();
    boolean _lessEqualsThan = (_size <= 2);
    if (_lessEqualsThan) {
      XNode _source = this.connection.getSource();
      XNode _target = this.connection.getTarget();
      Point2D _midPoint = this.midPoint(_target);
      Point2D _nearestAnchor = this.getNearestAnchor(_source, _midPoint);
      XNode _target_1 = this.connection.getTarget();
      XNode _source_1 = this.connection.getSource();
      Point2D _midPoint_1 = this.midPoint(_source_1);
      Point2D _nearestAnchor_1 = this.getNearestAnchor(_target_1, _midPoint_1);
      Pair<Point2D,Point2D> _mappedTo = Pair.<Point2D, Point2D>of(_nearestAnchor, _nearestAnchor_1);
      _xifexpression = _mappedTo;
    } else {
      XNode _source_2 = this.connection.getSource();
      ObservableList<XControlPoint> _controlPoints_1 = this.getControlPoints();
      XControlPoint _get = _controlPoints_1.get(1);
      Point2D _nearestAnchor_2 = this.getNearestAnchor(_source_2, _get);
      XNode _target_2 = this.connection.getTarget();
      ObservableList<XControlPoint> _controlPoints_2 = this.getControlPoints();
      ObservableList<XControlPoint> _controlPoints_3 = this.getControlPoints();
      int _size_1 = _controlPoints_3.size();
      int _minus = (_size_1 - 2);
      XControlPoint _get_1 = _controlPoints_2.get(_minus);
      Point2D _nearestAnchor_3 = this.getNearestAnchor(_target_2, _get_1);
      Pair<Point2D,Point2D> _mappedTo_1 = Pair.<Point2D, Point2D>of(_nearestAnchor_2, _nearestAnchor_3);
      _xifexpression = _mappedTo_1;
    }
    return _xifexpression;
  }
  
  protected Point2D midPoint(final XNode node) {
    Bounds _boundsInLocal = node.getBoundsInLocal();
    double _minX = _boundsInLocal.getMinX();
    Bounds _boundsInLocal_1 = node.getBoundsInLocal();
    double _maxX = _boundsInLocal_1.getMaxX();
    double _plus = (_minX + _maxX);
    double _multiply = (0.5 * _plus);
    Bounds _boundsInLocal_2 = node.getBoundsInLocal();
    double _minY = _boundsInLocal_2.getMinY();
    Bounds _boundsInLocal_3 = node.getBoundsInLocal();
    double _maxY = _boundsInLocal_3.getMaxY();
    double _plus_1 = (_minY + _maxY);
    double _multiply_1 = (0.5 * _plus_1);
    Point2D _localToRootDiagram = Extensions.localToRootDiagram(node, _multiply, _multiply_1);
    return _localToRootDiagram;
  }
  
  protected Point2D getNearestAnchor(final XNode node, final XControlPoint controlPoint) {
    Anchors _anchors = node.getAnchors();
    double _layoutX = controlPoint.getLayoutX();
    double _layoutY = controlPoint.getLayoutY();
    Point2D _anchor = _anchors.getAnchor(_layoutX, _layoutY);
    return _anchor;
  }
  
  protected Point2D getNearestAnchor(final XNode node, final Point2D point) {
    Anchors _anchors = node.getAnchors();
    double _x = point.getX();
    double _y = point.getY();
    Point2D _anchor = _anchors.getAnchor(_x, _y);
    return _anchor;
  }
  
  private SimpleListProperty<XControlPoint> controlPointsProperty = new SimpleListProperty<XControlPoint>(this, "controlPoints",_initControlPoints());
  
  private static final ObservableList<XControlPoint> _initControlPoints() {
    ObservableList<XControlPoint> _observableArrayList = FXCollections.<XControlPoint>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<XControlPoint> getControlPoints() {
    return this.controlPointsProperty.get();
    
  }
  
  public ListProperty<XControlPoint> controlPointsProperty() {
    return this.controlPointsProperty;
    
  }
  
  private ReadOnlyBooleanWrapper isActiveProperty = new ReadOnlyBooleanWrapper(this, "isActive");
  
  public boolean getIsActive() {
    return this.isActiveProperty.get();
    
  }
  
  public ReadOnlyBooleanProperty isActiveProperty() {
    return this.isActiveProperty.getReadOnlyProperty();
    
  }
}
