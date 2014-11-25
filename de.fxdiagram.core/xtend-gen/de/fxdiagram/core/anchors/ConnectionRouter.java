package de.fxdiagram.core.anchors;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.anchors.ArrowHead;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

@Logging
@SuppressWarnings("all")
public class ConnectionRouter implements XActivatable {
  private XConnection connection;
  
  private ChangeListener<Number> scalarListener;
  
  private ChangeListener<Bounds> boundsListener;
  
  private double selfEdgeDist = 60;
  
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
  
  public ObservableList<XControlPoint> getControlPoints() {
    return this.connection.getControlPoints();
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
    while (((!Objects.equal(current, null)) && (!CoreExtensions.isRootDiagram(current)))) {
      {
        ReadOnlyObjectProperty<Bounds> _boundsInParentProperty = current.boundsInParentProperty();
        _boundsInParentProperty.addListener(this.boundsListener);
        Parent _parent = current.getParent();
        current = _parent;
      }
    }
  }
  
  public void growToSize(final int newSize) {
    ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
    int _size = _controlPoints.size();
    boolean _lessThan = (_size < 2);
    if (_lessThan) {
      this.calculatePoints();
    }
    ObservableList<XControlPoint> _controlPoints_1 = this.getControlPoints();
    int _size_1 = _controlPoints_1.size();
    final int nodeDiff = (newSize - _size_1);
    if ((nodeDiff > 0)) {
      final ArrayList<XControlPoint> newControlPoints = CollectionLiterals.<XControlPoint>newArrayList();
      final double delta = (1.0 / (nodeDiff + 1));
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
              double _layoutX = first.getLayoutX();
              double _multiply = ((1 - lambda) * _layoutX);
              double _layoutX_1 = last.getLayoutX();
              double _multiply_1 = (lambda * _layoutX_1);
              double _plus = (_multiply + _multiply_1);
              it.setLayoutX(_plus);
              double _layoutY = first.getLayoutY();
              double _multiply_2 = ((1 - lambda) * _layoutY);
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
      this.resetPointTypes();
    }
  }
  
  public void shrinkToSize(final int newSize) {
    ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
    int _size = _controlPoints.size();
    boolean _lessThan = (_size < 2);
    if (_lessThan) {
      this.calculatePoints();
    }
    ObservableList<XControlPoint> _controlPoints_1 = this.getControlPoints();
    int _size_1 = _controlPoints_1.size();
    final int nodeDiff = (newSize - _size_1);
    if ((nodeDiff < 0)) {
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
      this.resetPointTypes();
    }
  }
  
  protected void resetPointTypes() {
    ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
    int _size = _controlPoints.size();
    boolean _lessThan = (_size < 2);
    if (_lessThan) {
      return;
    }
    ObservableList<XControlPoint> _controlPoints_1 = this.getControlPoints();
    XControlPoint _head = IterableExtensions.<XControlPoint>head(_controlPoints_1);
    _head.setType(XControlPoint.Type.ANCHOR);
    ObservableList<XControlPoint> _controlPoints_2 = this.getControlPoints();
    XControlPoint _last = IterableExtensions.<XControlPoint>last(_controlPoints_2);
    _last.setType(XControlPoint.Type.ANCHOR);
    ObservableList<XControlPoint> _controlPoints_3 = this.getControlPoints();
    int _size_1 = _controlPoints_3.size();
    int _minus = (_size_1 - 1);
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(1, _minus, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        ObservableList<XControlPoint> _controlPoints_4 = this.getControlPoints();
        final XControlPoint currentPoint = _controlPoints_4.get((i).intValue());
        XControlPoint.Type _switchResult = null;
        XConnection.Kind _kind = this.connection.getKind();
        if (_kind != null) {
          switch (_kind) {
            case POLYLINE:
              _switchResult = XControlPoint.Type.INTERPOLATED;
              break;
            case QUAD_CURVE:
              XControlPoint.Type _xifexpression = null;
              if ((((i).intValue() % 2) == 0)) {
                _xifexpression = XControlPoint.Type.INTERPOLATED;
              } else {
                _xifexpression = XControlPoint.Type.CONTROL_POINT;
              }
              _switchResult = _xifexpression;
              break;
            case CUBIC_CURVE:
              XControlPoint.Type _xifexpression_1 = null;
              if ((((i).intValue() % 3) == 0)) {
                _xifexpression_1 = XControlPoint.Type.INTERPOLATED;
              } else {
                _xifexpression_1 = XControlPoint.Type.CONTROL_POINT;
              }
              _switchResult = _xifexpression_1;
              break;
            default:
              break;
          }
        }
        currentPoint.setType(_switchResult);
      }
    }
  }
  
  public void calculatePoints() {
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
      XNode _source = this.connection.getSource();
      XNode _target = this.connection.getTarget();
      boolean _equals = Objects.equal(_source, _target);
      if (_equals) {
        this.calculateSelfEdge();
      }
    }
    final Pair<Point2D, Point2D> anchors = this.findClosestAnchors();
    boolean _and = false;
    Point2D _key = anchors.getKey();
    boolean _notEquals = (!Objects.equal(_key, null));
    if (!_notEquals) {
      _and = false;
    } else {
      Point2D _value = anchors.getValue();
      boolean _notEquals_1 = (!Objects.equal(_value, null));
      _and = _notEquals_1;
    }
    if (_and) {
      final Point2D sourcePoint = anchors.getKey();
      final Point2D targetPoint = anchors.getValue();
      ObservableList<XControlPoint> _controlPoints_2 = this.getControlPoints();
      int _size_1 = _controlPoints_2.size();
      boolean _lessThan_1 = (_size_1 < 2);
      if (_lessThan_1) {
        ObservableList<XControlPoint> _controlPoints_3 = this.getControlPoints();
        XControlPoint _xControlPoint = new XControlPoint();
        final Procedure1<XControlPoint> _function = new Procedure1<XControlPoint>() {
          public void apply(final XControlPoint it) {
            double _x = sourcePoint.getX();
            it.setLayoutX(_x);
            double _y = sourcePoint.getY();
            it.setLayoutY(_y);
            it.setType(XControlPoint.Type.ANCHOR);
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
            it.setType(XControlPoint.Type.ANCHOR);
          }
        };
        XControlPoint _doubleArrow_1 = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint_1, _function_1);
        _controlPoints_3.setAll(
          Collections.<XControlPoint>unmodifiableList(CollectionLiterals.<XControlPoint>newArrayList(_doubleArrow, _doubleArrow_1)));
        XConnection.Kind _kind = this.connection.getKind();
        if (_kind != null) {
          switch (_kind) {
            case CUBIC_CURVE:
              this.growToSize(4);
              break;
            case QUAD_CURVE:
              this.growToSize(3);
              break;
            default:
              break;
          }
        }
        ObservableList<XControlPoint> _controlPoints_4 = this.getControlPoints();
        for (final XControlPoint controlPoint_1 : _controlPoints_4) {
          {
            DoubleProperty _layoutXProperty = controlPoint_1.layoutXProperty();
            _layoutXProperty.addListener(this.scalarListener);
            DoubleProperty _layoutYProperty = controlPoint_1.layoutYProperty();
            _layoutYProperty.addListener(this.scalarListener);
          }
        }
      } else {
        ObservableList<XControlPoint> _controlPoints_5 = this.getControlPoints();
        XControlPoint _head = IterableExtensions.<XControlPoint>head(_controlPoints_5);
        final Procedure1<XControlPoint> _function_2 = new Procedure1<XControlPoint>() {
          public void apply(final XControlPoint it) {
            double _x = sourcePoint.getX();
            it.setLayoutX(_x);
            double _y = sourcePoint.getY();
            it.setLayoutY(_y);
          }
        };
        ObjectExtensions.<XControlPoint>operator_doubleArrow(_head, _function_2);
        ObservableList<XControlPoint> _controlPoints_6 = this.getControlPoints();
        XControlPoint _last = IterableExtensions.<XControlPoint>last(_controlPoints_6);
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
      ObservableList<XControlPoint> _controlPoints_7 = this.getControlPoints();
      final Consumer<XControlPoint> _function_4 = new Consumer<XControlPoint>() {
        public void accept(final XControlPoint it) {
          ObservableList<XControlPoint> _controlPoints = ConnectionRouter.this.getControlPoints();
          it.update(_controlPoints);
        }
      };
      _controlPoints_7.forEach(_function_4);
    }
  }
  
  protected boolean calculateSelfEdge() {
    boolean _xblockexpression = false;
    {
      XNode _source = this.connection.getSource();
      XNode _source_1 = this.connection.getSource();
      Bounds _snapBounds = _source_1.getSnapBounds();
      final Bounds boundsInDiagram = CoreExtensions.localToRootDiagram(_source, _snapBounds);
      ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
      _controlPoints.clear();
      ObservableList<XControlPoint> _controlPoints_1 = this.getControlPoints();
      XControlPoint _xControlPoint = new XControlPoint();
      final Procedure1<XControlPoint> _function = new Procedure1<XControlPoint>() {
        public void apply(final XControlPoint it) {
          it.setType(XControlPoint.Type.ANCHOR);
        }
      };
      XControlPoint _doubleArrow = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint, _function);
      _controlPoints_1.add(_doubleArrow);
      XConnection.Kind _kind = this.connection.getKind();
      boolean _equals = Objects.equal(_kind, XConnection.Kind.QUAD_CURVE);
      if (_equals) {
        ConnectionRouter.LOG.severe("self-edges cannot be QUAD_CURVEs. Switching to CUBIC_CURVE");
        this.connection.setKind(XConnection.Kind.CUBIC_CURVE);
      }
      XConnection.Kind _kind_1 = this.connection.getKind();
      if (_kind_1 != null) {
        switch (_kind_1) {
          case POLYLINE:
            double _width = boundsInDiagram.getWidth();
            double _multiply = (0.5 * _width);
            final double deltaX = Math.min(this.selfEdgeDist, _multiply);
            double _height = boundsInDiagram.getHeight();
            double _multiply_1 = (0.5 * _height);
            final double deltaY = Math.min(this.selfEdgeDist, _multiply_1);
            ObservableList<XControlPoint> _controlPoints_2 = this.getControlPoints();
            XControlPoint _xControlPoint_1 = new XControlPoint();
            final Procedure1<XControlPoint> _function_1 = new Procedure1<XControlPoint>() {
              public void apply(final XControlPoint it) {
                double _minX = boundsInDiagram.getMinX();
                double _minus = (_minX - deltaX);
                it.setLayoutX(_minus);
                double _minY = boundsInDiagram.getMinY();
                double _plus = (_minY + deltaY);
                it.setLayoutY(_plus);
                it.setType(XControlPoint.Type.INTERPOLATED);
              }
            };
            XControlPoint _doubleArrow_1 = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint_1, _function_1);
            _controlPoints_2.add(_doubleArrow_1);
            ObservableList<XControlPoint> _controlPoints_3 = this.getControlPoints();
            XControlPoint _xControlPoint_2 = new XControlPoint();
            final Procedure1<XControlPoint> _function_2 = new Procedure1<XControlPoint>() {
              public void apply(final XControlPoint it) {
                double _minX = boundsInDiagram.getMinX();
                double _minus = (_minX - deltaX);
                it.setLayoutX(_minus);
                double _minY = boundsInDiagram.getMinY();
                double _minus_1 = (_minY - deltaY);
                it.setLayoutY(_minus_1);
                it.setType(XControlPoint.Type.INTERPOLATED);
              }
            };
            XControlPoint _doubleArrow_2 = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint_2, _function_2);
            _controlPoints_3.add(_doubleArrow_2);
            ObservableList<XControlPoint> _controlPoints_4 = this.getControlPoints();
            XControlPoint _xControlPoint_3 = new XControlPoint();
            final Procedure1<XControlPoint> _function_3 = new Procedure1<XControlPoint>() {
              public void apply(final XControlPoint it) {
                double _minX = boundsInDiagram.getMinX();
                double _plus = (_minX + deltaX);
                it.setLayoutX(_plus);
                double _minY = boundsInDiagram.getMinY();
                double _minus = (_minY - deltaY);
                it.setLayoutY(_minus);
                it.setType(XControlPoint.Type.INTERPOLATED);
              }
            };
            XControlPoint _doubleArrow_3 = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint_3, _function_3);
            _controlPoints_4.add(_doubleArrow_3);
            break;
          case CUBIC_CURVE:
            ObservableList<XControlPoint> _controlPoints_5 = this.getControlPoints();
            XControlPoint _xControlPoint_4 = new XControlPoint();
            final Procedure1<XControlPoint> _function_4 = new Procedure1<XControlPoint>() {
              public void apply(final XControlPoint it) {
                double _minX = boundsInDiagram.getMinX();
                double _minus = (_minX - ConnectionRouter.this.selfEdgeDist);
                it.setLayoutX(_minus);
                double _minY = boundsInDiagram.getMinY();
                double _plus = (_minY + (0.3 * ConnectionRouter.this.selfEdgeDist));
                it.setLayoutY(_plus);
                it.setType(XControlPoint.Type.CONTROL_POINT);
              }
            };
            XControlPoint _doubleArrow_4 = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint_4, _function_4);
            _controlPoints_5.add(_doubleArrow_4);
            ObservableList<XControlPoint> _controlPoints_6 = this.getControlPoints();
            XControlPoint _xControlPoint_5 = new XControlPoint();
            final Procedure1<XControlPoint> _function_5 = new Procedure1<XControlPoint>() {
              public void apply(final XControlPoint it) {
                double _minX = boundsInDiagram.getMinX();
                double _plus = (_minX + (0.3 * ConnectionRouter.this.selfEdgeDist));
                it.setLayoutX(_plus);
                double _minY = boundsInDiagram.getMinY();
                double _minus = (_minY - ConnectionRouter.this.selfEdgeDist);
                it.setLayoutY(_minus);
                it.setType(XControlPoint.Type.CONTROL_POINT);
              }
            };
            XControlPoint _doubleArrow_5 = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint_5, _function_5);
            _controlPoints_6.add(_doubleArrow_5);
            break;
          default:
            break;
        }
      }
      ObservableList<XControlPoint> _controlPoints_7 = this.getControlPoints();
      XControlPoint _xControlPoint_6 = new XControlPoint();
      final Procedure1<XControlPoint> _function_6 = new Procedure1<XControlPoint>() {
        public void apply(final XControlPoint it) {
          it.setType(XControlPoint.Type.ANCHOR);
        }
      };
      XControlPoint _doubleArrow_6 = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint_6, _function_6);
      _xblockexpression = _controlPoints_7.add(_doubleArrow_6);
    }
    return _xblockexpression;
  }
  
  protected Pair<Point2D, Point2D> findClosestAnchors() {
    Pair<Point2D, Point2D> _xifexpression = null;
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
      _xifexpression = Pair.<Point2D, Point2D>of(_nearestAnchor, _nearestAnchor_1);
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
      _xifexpression = Pair.<Point2D, Point2D>of(_nearestAnchor_2, _nearestAnchor_3);
    }
    return _xifexpression;
  }
  
  protected Point2D midPoint(final XNode node) {
    Node _node = node.getNode();
    Bounds _boundsInLocal = _node.getBoundsInLocal();
    Point2D _center = BoundsExtensions.center(_boundsInLocal);
    return CoreExtensions.localToRootDiagram(node, _center);
  }
  
  protected Point2D getNearestAnchor(final XNode node, final XControlPoint controlPoint, final ArrowHead arrowHead) {
    double _layoutX = controlPoint.getLayoutX();
    double _layoutY = controlPoint.getLayoutY();
    return this.getNearestAnchor(node, _layoutX, _layoutY, arrowHead);
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
      _xblockexpression = this.getNearestAnchor(node, _x, _y, arrowHead);
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
        _and = _notEquals_1;
      }
      if (_and) {
        _xifexpression = arrowHead.correctAnchor(x, y, anchor);
      } else {
        _xifexpression = anchor;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.anchors.ConnectionRouter");
    ;
  
  private ReadOnlyBooleanWrapper isActiveProperty = new ReadOnlyBooleanWrapper(this, "isActive");
  
  public boolean getIsActive() {
    return this.isActiveProperty.get();
  }
  
  public ReadOnlyBooleanProperty isActiveProperty() {
    return this.isActiveProperty.getReadOnlyProperty();
  }
}
