package de.fxdiagram.core.anchors;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionKind;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XControlPointType;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.anchors.ArrowHead;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import java.util.ArrayList;
import java.util.Collections;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.IntegerRange;
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
    ReadOnlyObjectProperty<Bounds> _layoutBoundsProperty = current.layoutBoundsProperty();
    _layoutBoundsProperty.addListener(this.boundsListener);
    boolean _and = false;
    boolean _notEquals = (!Objects.equal(current, null));
    if (!_notEquals) {
      _and = false;
    } else {
      boolean _isRootDiagram = CoreExtensions.isRootDiagram(current);
      boolean _not = (!_isRootDiagram);
      _and = (_notEquals && _not);
    }
    boolean _while = _and;
    while (_while) {
      {
        ReadOnlyObjectProperty<Bounds> _boundsInParentProperty = current.boundsInParentProperty();
        _boundsInParentProperty.addListener(this.boundsListener);
        Parent _parent = current.getParent();
        current = _parent;
      }
      boolean _and_1 = false;
      boolean _notEquals_1 = (!Objects.equal(current, null));
      if (!_notEquals_1) {
        _and_1 = false;
      } else {
        boolean _isRootDiagram_1 = CoreExtensions.isRootDiagram(current);
        boolean _not_1 = (!_isRootDiagram_1);
        _and_1 = (_notEquals_1 && _not_1);
      }
      _while = _and_1;
    }
  }
  
  public MoveBehavior<XControlPoint> growToSize(final int newSize) {
    MoveBehavior<XControlPoint> _xblockexpression = null;
    {
      ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
      int _size = _controlPoints.size();
      boolean _lessThan = (_size < 2);
      if (_lessThan) {
        this.calculatePoints();
      }
      ObservableList<XControlPoint> _controlPoints_1 = this.getControlPoints();
      int _size_1 = _controlPoints_1.size();
      final int nodeDiff = (newSize - _size_1);
      MoveBehavior<XControlPoint> _xifexpression = null;
      boolean _greaterThan = (nodeDiff > 0);
      if (_greaterThan) {
        MoveBehavior<XControlPoint> _xblockexpression_1 = null;
        {
          final ArrayList<XControlPoint> newControlPoints = CollectionLiterals.<XControlPoint>newArrayList();
          int _plus = (nodeDiff + 1);
          final double delta = (1.0 / _plus);
          ObservableList<XControlPoint> _controlPoints_2 = this.getControlPoints();
          final XControlPoint first = IterableExtensions.<XControlPoint>head(_controlPoints_2);
          ObservableList<XControlPoint> _controlPoints_3 = this.getControlPoints();
          final XControlPoint last = IterableExtensions.<XControlPoint>last(_controlPoints_3);
          IntegerRange _upTo = new IntegerRange(1, nodeDiff);
          for (final Integer i : _upTo) {
            {
              XControlPoint _xControlPoint = new XControlPoint();
              final Procedure1<XControlPoint> _function = new Procedure1<XControlPoint>() {
                public void apply(final XControlPoint it) {
                  final double lambda = (delta * (i).intValue());
                  double _minus = (1 - lambda);
                  double _layoutX = first.getLayoutX();
                  double _multiply = (_minus * _layoutX);
                  double _layoutX_1 = last.getLayoutX();
                  double _multiply_1 = (lambda * _layoutX_1);
                  double _plus = (_multiply + _multiply_1);
                  it.setLayoutX(_plus);
                  double _minus_1 = (1 - lambda);
                  double _layoutY = first.getLayoutY();
                  double _multiply_2 = (_minus_1 * _layoutY);
                  double _layoutY_1 = last.getLayoutY();
                  double _multiply_3 = (lambda * _layoutY_1);
                  double _plus_1 = (_multiply_2 + _multiply_3);
                  it.setLayoutY(_plus_1);
                }
              };
              final XControlPoint newControlPoint = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint, _function);
              DoubleProperty _layoutXProperty = newControlPoint.layoutXProperty();
              _layoutXProperty.addListener(this.scalarListener);
              DoubleProperty _layoutYProperty = newControlPoint.layoutYProperty();
              _layoutYProperty.addListener(this.scalarListener);
              newControlPoints.add(newControlPoint);
            }
          }
          ObservableList<XControlPoint> _controlPoints_4 = this.getControlPoints();
          ObservableList<XControlPoint> _controlPoints_5 = this.getControlPoints();
          int _size_2 = _controlPoints_5.size();
          int _minus = (_size_2 - 1);
          _controlPoints_4.addAll(_minus, newControlPoints);
          MoveBehavior<XControlPoint> _resetPointTypes = this.resetPointTypes();
          _xblockexpression_1 = (_resetPointTypes);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  public MoveBehavior<XControlPoint> shrinkToSize(final int newSize) {
    MoveBehavior<XControlPoint> _xblockexpression = null;
    {
      ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
      int _size = _controlPoints.size();
      boolean _lessThan = (_size < 2);
      if (_lessThan) {
        this.calculatePoints();
      }
      ObservableList<XControlPoint> _controlPoints_1 = this.getControlPoints();
      int _size_1 = _controlPoints_1.size();
      final int nodeDiff = (newSize - _size_1);
      MoveBehavior<XControlPoint> _xifexpression = null;
      boolean _lessThan_1 = (nodeDiff < 0);
      if (_lessThan_1) {
        MoveBehavior<XControlPoint> _xblockexpression_1 = null;
        {
          final ArrayList<XControlPoint> toBeRemoved = CollectionLiterals.<XControlPoint>newArrayList();
          ObservableList<XControlPoint> _controlPoints_2 = this.getControlPoints();
          int _size_2 = _controlPoints_2.size();
          int _minus = (_size_2 - 1);
          ObservableList<XControlPoint> _controlPoints_3 = this.getControlPoints();
          int _size_3 = _controlPoints_3.size();
          int _plus = (_size_3 + nodeDiff);
          int _minus_1 = (_plus - 1);
          ExclusiveRange _greaterThanDoubleDot = new ExclusiveRange(_minus, _minus_1, false);
          for (final Integer i : _greaterThanDoubleDot) {
            {
              ObservableList<XControlPoint> _controlPoints_4 = this.getControlPoints();
              final XControlPoint removeMe = _controlPoints_4.get((i).intValue());
              DoubleProperty _layoutXProperty = removeMe.layoutXProperty();
              _layoutXProperty.removeListener(this.scalarListener);
              DoubleProperty _layoutYProperty = removeMe.layoutYProperty();
              _layoutYProperty.removeListener(this.scalarListener);
              toBeRemoved.add(removeMe);
            }
          }
          ObservableList<XControlPoint> _controlPoints_4 = this.getControlPoints();
          _controlPoints_4.removeAll(toBeRemoved);
          MoveBehavior<XControlPoint> _resetPointTypes = this.resetPointTypes();
          _xblockexpression_1 = (_resetPointTypes);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  protected MoveBehavior<XControlPoint> resetPointTypes() {
    ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
    int _size = _controlPoints.size();
    boolean _lessThan = (_size < 2);
    if (_lessThan) {
      ObservableList<XControlPoint> _controlPoints_1 = this.getControlPoints();
      XControlPoint _head = IterableExtensions.<XControlPoint>head(_controlPoints_1);
      return _head.setType(XControlPointType.ANCHOR);
    }
    ObservableList<XControlPoint> _controlPoints_2 = this.getControlPoints();
    XControlPoint _last = IterableExtensions.<XControlPoint>last(_controlPoints_2);
    _last.setType(XControlPointType.ANCHOR);
    ObservableList<XControlPoint> _controlPoints_3 = this.getControlPoints();
    int _size_1 = _controlPoints_3.size();
    int _minus = (_size_1 - 1);
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(1, _minus, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        ObservableList<XControlPoint> _controlPoints_4 = this.getControlPoints();
        final XControlPoint currentPoint = _controlPoints_4.get((i).intValue());
        XControlPointType _switchResult = null;
        XConnectionKind _kind = this.connection.getKind();
        final XConnectionKind _switchValue = _kind;
        boolean _matched = false;
        if (!_matched) {
          if (Objects.equal(_switchValue,XConnectionKind.POLYLINE)) {
            _matched=true;
            _switchResult = XControlPointType.INTERPOLATED;
          }
        }
        if (!_matched) {
          if (Objects.equal(_switchValue,XConnectionKind.QUAD_CURVE)) {
            _matched=true;
            XControlPointType _xifexpression = null;
            int _modulo = ((i).intValue() % 2);
            boolean _equals = (_modulo == 0);
            if (_equals) {
              _xifexpression = XControlPointType.INTERPOLATED;
            } else {
              _xifexpression = XControlPointType.CONTROL_POINT;
            }
            _switchResult = _xifexpression;
          }
        }
        if (!_matched) {
          if (Objects.equal(_switchValue,XConnectionKind.CUBIC_CURVE)) {
            _matched=true;
            XControlPointType _xifexpression_1 = null;
            int _modulo_1 = ((i).intValue() % 3);
            boolean _equals_1 = (_modulo_1 == 0);
            if (_equals_1) {
              _xifexpression_1 = XControlPointType.INTERPOLATED;
            } else {
              _xifexpression_1 = XControlPointType.CONTROL_POINT;
            }
            _switchResult = _xifexpression_1;
          }
        }
        currentPoint.setType(_switchResult);
      }
    }
    return null;
  }
  
  public void calculatePoints() {
    final Pair<Point2D,Point2D> anchors = this.findClosestAnchors();
    boolean _and = false;
    Point2D _key = anchors.getKey();
    boolean _notEquals = (!Objects.equal(_key, null));
    if (!_notEquals) {
      _and = false;
    } else {
      Point2D _value = anchors.getValue();
      boolean _notEquals_1 = (!Objects.equal(_value, null));
      _and = (_notEquals && _notEquals_1);
    }
    if (_and) {
      final Point2D sourcePoint = anchors.getKey();
      final Point2D targetPoint = anchors.getValue();
      ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
      int _size = _controlPoints.size();
      boolean _lessThan = (_size < 2);
      if (_lessThan) {
        ObservableList<XControlPoint> _controlPoints_1 = this.getControlPoints();
        for (final XControlPoint controlPoint : _controlPoints_1) {
          {
            DoubleProperty _layoutXProperty = controlPoint.layoutXProperty();
            _layoutXProperty.removeListener(this.scalarListener);
            DoubleProperty _layoutYProperty = controlPoint.layoutYProperty();
            _layoutYProperty.removeListener(this.scalarListener);
          }
        }
        ObservableList<XControlPoint> _controlPoints_2 = this.getControlPoints();
        XControlPoint _xControlPoint = new XControlPoint();
        final Procedure1<XControlPoint> _function = new Procedure1<XControlPoint>() {
          public void apply(final XControlPoint it) {
            double _x = sourcePoint.getX();
            it.setLayoutX(_x);
            double _y = sourcePoint.getY();
            it.setLayoutY(_y);
            it.setType(XControlPointType.ANCHOR);
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
            it.setType(XControlPointType.ANCHOR);
          }
        };
        XControlPoint _doubleArrow_1 = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint_1, _function_1);
        _controlPoints_2.setAll(
          Collections.<XControlPoint>unmodifiableList(Lists.<XControlPoint>newArrayList(_doubleArrow, _doubleArrow_1)));
        XConnectionKind _kind = this.connection.getKind();
        final XConnectionKind _switchValue = _kind;
        boolean _matched = false;
        if (!_matched) {
          if (Objects.equal(_switchValue,XConnectionKind.CUBIC_CURVE)) {
            _matched=true;
            this.growToSize(4);
          }
        }
        if (!_matched) {
          if (Objects.equal(_switchValue,XConnectionKind.QUAD_CURVE)) {
            _matched=true;
            this.growToSize(3);
          }
        }
        ObservableList<XControlPoint> _controlPoints_3 = this.getControlPoints();
        for (final XControlPoint controlPoint_1 : _controlPoints_3) {
          {
            DoubleProperty _layoutXProperty = controlPoint_1.layoutXProperty();
            _layoutXProperty.addListener(this.scalarListener);
            DoubleProperty _layoutYProperty = controlPoint_1.layoutYProperty();
            _layoutYProperty.addListener(this.scalarListener);
          }
        }
      } else {
        ObservableList<XControlPoint> _controlPoints_4 = this.getControlPoints();
        XControlPoint _head = IterableExtensions.<XControlPoint>head(_controlPoints_4);
        final Procedure1<XControlPoint> _function_2 = new Procedure1<XControlPoint>() {
          public void apply(final XControlPoint it) {
            double _x = sourcePoint.getX();
            it.setLayoutX(_x);
            double _y = sourcePoint.getY();
            it.setLayoutY(_y);
          }
        };
        ObjectExtensions.<XControlPoint>operator_doubleArrow(_head, _function_2);
        ObservableList<XControlPoint> _controlPoints_5 = this.getControlPoints();
        XControlPoint _last = IterableExtensions.<XControlPoint>last(_controlPoints_5);
        final Procedure1<XControlPoint> _function_3 = new Procedure1<XControlPoint>() {
          public void apply(final XControlPoint it) {
            double _x = targetPoint.getX();
            it.setLayoutX(_x);
            double _y = targetPoint.getY();
            it.setLayoutY(_y);
          }
        };
        ObjectExtensions.<XControlPoint>operator_doubleArrow(_last, _function_3);
      }
      ObservableList<XControlPoint> _controlPoints_6 = this.getControlPoints();
      final Procedure1<XControlPoint> _function_4 = new Procedure1<XControlPoint>() {
        public void apply(final XControlPoint it) {
          ObservableList<XControlPoint> _controlPoints = ConnectionRouter.this.getControlPoints();
          it.update(_controlPoints);
        }
      };
      IterableExtensions.<XControlPoint>forEach(_controlPoints_6, _function_4);
    }
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
      ArrowHead _sourceArrowHead = this.connection.getSourceArrowHead();
      Point2D _nearestAnchor = this.getNearestAnchor(_source, _midPoint, _sourceArrowHead);
      XNode _target_1 = this.connection.getTarget();
      XNode _source_1 = this.connection.getSource();
      Point2D _midPoint_1 = this.midPoint(_source_1);
      ArrowHead _targetArrowHead = this.connection.getTargetArrowHead();
      Point2D _nearestAnchor_1 = this.getNearestAnchor(_target_1, _midPoint_1, _targetArrowHead);
      Pair<Point2D,Point2D> _mappedTo = Pair.<Point2D, Point2D>of(_nearestAnchor, _nearestAnchor_1);
      _xifexpression = _mappedTo;
    } else {
      XNode _source_2 = this.connection.getSource();
      ObservableList<XControlPoint> _controlPoints_1 = this.getControlPoints();
      XControlPoint _get = _controlPoints_1.get(1);
      ArrowHead _sourceArrowHead_1 = this.connection.getSourceArrowHead();
      Point2D _nearestAnchor_2 = this.getNearestAnchor(_source_2, _get, _sourceArrowHead_1);
      XNode _target_2 = this.connection.getTarget();
      ObservableList<XControlPoint> _controlPoints_2 = this.getControlPoints();
      ObservableList<XControlPoint> _controlPoints_3 = this.getControlPoints();
      int _size_1 = _controlPoints_3.size();
      int _minus = (_size_1 - 2);
      XControlPoint _get_1 = _controlPoints_2.get(_minus);
      ArrowHead _targetArrowHead_1 = this.connection.getTargetArrowHead();
      Point2D _nearestAnchor_3 = this.getNearestAnchor(_target_2, _get_1, _targetArrowHead_1);
      Pair<Point2D,Point2D> _mappedTo_1 = Pair.<Point2D, Point2D>of(_nearestAnchor_2, _nearestAnchor_3);
      _xifexpression = _mappedTo_1;
    }
    return _xifexpression;
  }
  
  protected Point2D midPoint(final XNode node) {
    Bounds _boundsInLocal = node.getBoundsInLocal();
    Point2D _center = BoundsExtensions.center(_boundsInLocal);
    Point2D _localToRootDiagram = CoreExtensions.localToRootDiagram(node, _center);
    return _localToRootDiagram;
  }
  
  protected Point2D getNearestAnchor(final XNode node, final XControlPoint controlPoint, final ArrowHead arrowHead) {
    double _layoutX = controlPoint.getLayoutX();
    double _layoutY = controlPoint.getLayoutY();
    Point2D _nearestAnchor = this.getNearestAnchor(node, _layoutX, _layoutY, arrowHead);
    return _nearestAnchor;
  }
  
  protected Point2D getNearestAnchor(final XNode node, final Point2D point, final ArrowHead arrowHead) {
    Point2D _xblockexpression = null;
    {
      boolean _equals = Objects.equal(point, null);
      if (_equals) {
        return null;
      }
      double _x = point.getX();
      double _y = point.getY();
      Point2D _nearestAnchor = this.getNearestAnchor(node, _x, _y, arrowHead);
      _xblockexpression = (_nearestAnchor);
    }
    return _xblockexpression;
  }
  
  protected Point2D getNearestAnchor(final XNode node, final double x, final double y, final ArrowHead arrowHead) {
    Point2D _xblockexpression = null;
    {
      Anchors _anchors = node.getAnchors();
      final Point2D anchor = _anchors.getAnchor(x, y);
      Point2D _xifexpression = null;
      boolean _and = false;
      boolean _notEquals = (!Objects.equal(anchor, null));
      if (!_notEquals) {
        _and = false;
      } else {
        boolean _notEquals_1 = (!Objects.equal(arrowHead, null));
        _and = (_notEquals && _notEquals_1);
      }
      if (_and) {
        Point2D _correctAnchor = arrowHead.correctAnchor(x, y, anchor);
        _xifexpression = _correctAnchor;
      } else {
        _xifexpression = anchor;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  private ReadOnlyListWrapper<XControlPoint> controlPointsProperty = new ReadOnlyListWrapper<XControlPoint>(this, "controlPoints",_initControlPoints());
  
  private static final ObservableList<XControlPoint> _initControlPoints() {
    ObservableList<XControlPoint> _observableArrayList = FXCollections.<XControlPoint>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<XControlPoint> getControlPoints() {
    return this.controlPointsProperty.get();
  }
  
  public ReadOnlyListProperty<XControlPoint> controlPointsProperty() {
    return this.controlPointsProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyBooleanWrapper isActiveProperty = new ReadOnlyBooleanWrapper(this, "isActive");
  
  public boolean getIsActive() {
    return this.isActiveProperty.get();
  }
  
  public ReadOnlyBooleanProperty isActiveProperty() {
    return this.isActiveProperty.getReadOnlyProperty();
  }
}
