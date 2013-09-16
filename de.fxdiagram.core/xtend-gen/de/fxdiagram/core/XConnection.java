package de.fxdiagram.core;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.XConnectionKind;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.anchors.ArrowHead;
import de.fxdiagram.core.anchors.ConnectionRouter;
import de.fxdiagram.core.anchors.TriangleArrowHead;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.extensions.BezierExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.Point2DExtensions;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.DoubleExtensions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@Logging
@SuppressWarnings("all")
public class XConnection extends XShape {
  private Group controlPointGroup = new Function0<Group>() {
    public Group apply() {
      Group _group = new Group();
      return _group;
    }
  }.apply();
  
  private Group shapeGroup = new Function0<Group>() {
    public Group apply() {
      Group _group = new Group();
      return _group;
    }
  }.apply();
  
  private ChangeListener<Number> controlPointListener;
  
  private ConnectionRouter connectionRouter;
  
  public XConnection(final XNode source, final XNode target) {
    this.setNode(this.shapeGroup);
    ObservableList<Node> _children = this.getChildren();
    final Procedure1<Group> _function = new Procedure1<Group>() {
      public void apply(final Group it) {
        it.setVisible(false);
      }
    };
    Group _doubleArrow = ObjectExtensions.<Group>operator_doubleArrow(this.controlPointGroup, _function);
    _children.add(_doubleArrow);
    this.setSource(source);
    this.setTarget(target);
    ObservableList<XConnection> _outgoingConnections = source.getOutgoingConnections();
    boolean _contains = _outgoingConnections.contains(this);
    boolean _not = (!_contains);
    if (_not) {
      ObservableList<XConnection> _outgoingConnections_1 = source.getOutgoingConnections();
      _outgoingConnections_1.add(this);
    }
    ObservableList<XConnection> _incomingConnections = target.getIncomingConnections();
    boolean _contains_1 = _incomingConnections.contains(this);
    boolean _not_1 = (!_contains_1);
    if (_not_1) {
      ObservableList<XConnection> _incomingConnections_1 = target.getIncomingConnections();
      _incomingConnections_1.add(this);
    }
    ConnectionRouter _connectionRouter = new ConnectionRouter(this);
    this.connectionRouter = _connectionRouter;
    TriangleArrowHead _triangleArrowHead = new TriangleArrowHead(this, false);
    this.setTargetArrowHead(_triangleArrowHead);
  }
  
  public void doActivate() {
    final ChangeListener<Number> _function = new ChangeListener<Number>() {
      public void changed(final ObservableValue<? extends Number> prop, final Number oldVal, final Number newVal) {
        XConnection.this.updateShapes();
      }
    };
    this.controlPointListener = _function;
    final Procedure1<Change<? extends XControlPoint>> _function_1 = new Procedure1<Change<? extends XControlPoint>>() {
      public void apply(final Change<? extends XControlPoint> it) {
        final ObservableList<? extends XControlPoint> points = it.getList();
        XConnection.this.updateShapes();
        boolean _next = it.next();
        boolean _while = _next;
        while (_while) {
          List<? extends XControlPoint> _addedSubList = it.getAddedSubList();
          final Procedure1<XControlPoint> _function = new Procedure1<XControlPoint>() {
            public void apply(final XControlPoint it) {
              final int index = points.indexOf(it);
              boolean _and = false;
              boolean _notEquals = (index != 0);
              if (!_notEquals) {
                _and = false;
              } else {
                int _size = points.size();
                boolean _notEquals_1 = (index != _size);
                _and = (_notEquals && _notEquals_1);
              }
              if (_and) {
                it.activate();
              }
              DoubleProperty _layoutXProperty = it.layoutXProperty();
              _layoutXProperty.addListener(XConnection.this.controlPointListener);
              DoubleProperty _layoutYProperty = it.layoutYProperty();
              _layoutYProperty.addListener(XConnection.this.controlPointListener);
            }
          };
          IterableExtensions.forEach(_addedSubList, _function);
          boolean _next_1 = it.next();
          _while = _next_1;
        }
        List<? extends XControlPoint> _removed = it.getRemoved();
        final Procedure1<XControlPoint> _function = new Procedure1<XControlPoint>() {
          public void apply(final XControlPoint it) {
            DoubleProperty _layoutXProperty = it.layoutXProperty();
            _layoutXProperty.removeListener(XConnection.this.controlPointListener);
            DoubleProperty _layoutYProperty = it.layoutYProperty();
            _layoutYProperty.removeListener(XConnection.this.controlPointListener);
          }
        };
        IterableExtensions.forEach(_removed, _function);
      }
    };
    final Procedure1<Change<? extends XControlPoint>> listChangeListener = _function_1;
    ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
    _controlPoints.addListener(new ListChangeListener<XControlPoint>() {
        public void onChanged(Change<? extends XControlPoint> arg0) {
          listChangeListener.apply(arg0);
        }
    });
    XConnectionLabel _label = this.getLabel();
    boolean _notEquals = (!Objects.equal(_label, null));
    if (_notEquals) {
      XConnectionLabel _label_1 = this.getLabel();
      _label_1.activate();
    }
    this.connectionRouter.activate();
    this.updateShapes();
    ReadOnlyObjectProperty<Parent> _parentProperty = this.parentProperty();
    final ChangeListener<Parent> _function_2 = new ChangeListener<Parent>() {
      public void changed(final ObservableValue<? extends Parent> property, final Parent oldValue, final Parent newValue) {
        boolean _equals = Objects.equal(newValue, null);
        if (_equals) {
          XNode _source = XConnection.this.getSource();
          ObservableList<XConnection> _outgoingConnections = _source.getOutgoingConnections();
          _outgoingConnections.remove(XConnection.this);
          XNode _target = XConnection.this.getTarget();
          ObservableList<XConnection> _incomingConnections = _target.getIncomingConnections();
          _incomingConnections.remove(XConnection.this);
        }
      }
    };
    _parentProperty.addListener(_function_2);
  }
  
  public void selectionFeedback(final boolean isSelected) {
    if (isSelected) {
      XNode _source = this.getSource();
      _source.toFront();
      XNode _target = this.getTarget();
      _target.toFront();
    }
    this.controlPointGroup.setVisible(isSelected);
  }
  
  public ConnectionRouter getConnectionRouter() {
    return this.connectionRouter;
  }
  
  public ObservableList<XControlPoint> getControlPoints() {
    ObservableList<XControlPoint> _controlPoints = this.connectionRouter.getControlPoints();
    return _controlPoints;
  }
  
  public void updateShapes() {
    int remainder = (-1);
    XConnectionKind _kind = this.getKind();
    final XConnectionKind getKind = _kind;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(getKind,XConnectionKind.CUBIC_CURVE)) {
        _matched=true;
        ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
        int _size = _controlPoints.size();
        int _minus = (_size - 1);
        int _modulo = (_minus % 3);
        remainder = _modulo;
        boolean _equals = (remainder == 0);
        if (_equals) {
          ObservableList<XControlPoint> _controlPoints_1 = this.getControlPoints();
          int _size_1 = _controlPoints_1.size();
          int _minus_1 = (_size_1 - 1);
          final int numSegments = (_minus_1 / 3);
          ObservableList<Node> _children = this.shapeGroup.getChildren();
          Iterable<CubicCurve> _filter = Iterables.<CubicCurve>filter(_children, CubicCurve.class);
          final List<CubicCurve> curves = IterableExtensions.<CubicCurve>toList(_filter);
          int _size_2 = curves.size();
          boolean _greaterThan = (_size_2 > numSegments);
          boolean _while = _greaterThan;
          while (_while) {
            CubicCurve _last = IterableExtensions.<CubicCurve>last(curves);
            curves.remove(_last);
            int _size_3 = curves.size();
            boolean _greaterThan_1 = (_size_3 > numSegments);
            _while = _greaterThan_1;
          }
          int _size_3 = curves.size();
          boolean _lessThan = (_size_3 < numSegments);
          boolean _while_1 = _lessThan;
          while (_while_1) {
            CubicCurve _cubicCurve = new CubicCurve();
            final Procedure1<CubicCurve> _function = new Procedure1<CubicCurve>() {
              public void apply(final CubicCurve it) {
                it.setFill(null);
                it.setStroke(Color.BLACK);
              }
            };
            CubicCurve _doubleArrow = ObjectExtensions.<CubicCurve>operator_doubleArrow(_cubicCurve, _function);
            curves.add(_doubleArrow);
            int _size_4 = curves.size();
            boolean _lessThan_1 = (_size_4 < numSegments);
            _while_1 = _lessThan_1;
          }
          ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, numSegments, true);
          for (final Integer i : _doubleDotLessThan) {
            {
              final CubicCurve curve = curves.get((i).intValue());
              final int offset = ((i).intValue() * 3);
              ObservableList<XControlPoint> _controlPoints_2 = this.getControlPoints();
              XControlPoint _get = _controlPoints_2.get(offset);
              double _layoutX = _get.getLayoutX();
              curve.setStartX(_layoutX);
              ObservableList<XControlPoint> _controlPoints_3 = this.getControlPoints();
              XControlPoint _get_1 = _controlPoints_3.get(offset);
              double _layoutY = _get_1.getLayoutY();
              curve.setStartY(_layoutY);
              ObservableList<XControlPoint> _controlPoints_4 = this.getControlPoints();
              int _plus = (offset + 1);
              XControlPoint _get_2 = _controlPoints_4.get(_plus);
              double _layoutX_1 = _get_2.getLayoutX();
              curve.setControlX1(_layoutX_1);
              ObservableList<XControlPoint> _controlPoints_5 = this.getControlPoints();
              int _plus_1 = (offset + 1);
              XControlPoint _get_3 = _controlPoints_5.get(_plus_1);
              double _layoutY_1 = _get_3.getLayoutY();
              curve.setControlY1(_layoutY_1);
              ObservableList<XControlPoint> _controlPoints_6 = this.getControlPoints();
              int _plus_2 = (offset + 2);
              XControlPoint _get_4 = _controlPoints_6.get(_plus_2);
              double _layoutX_2 = _get_4.getLayoutX();
              curve.setControlX2(_layoutX_2);
              ObservableList<XControlPoint> _controlPoints_7 = this.getControlPoints();
              int _plus_3 = (offset + 2);
              XControlPoint _get_5 = _controlPoints_7.get(_plus_3);
              double _layoutY_2 = _get_5.getLayoutY();
              curve.setControlY2(_layoutY_2);
              ObservableList<XControlPoint> _controlPoints_8 = this.getControlPoints();
              int _plus_4 = (offset + 3);
              XControlPoint _get_6 = _controlPoints_8.get(_plus_4);
              double _layoutX_3 = _get_6.getLayoutX();
              curve.setEndX(_layoutX_3);
              ObservableList<XControlPoint> _controlPoints_9 = this.getControlPoints();
              int _plus_5 = (offset + 3);
              XControlPoint _get_7 = _controlPoints_9.get(_plus_5);
              double _layoutY_3 = _get_7.getLayoutY();
              curve.setEndY(_layoutY_3);
            }
          }
          this.setShapes(curves);
        }
      }
    }
    if (!_matched) {
      if (Objects.equal(getKind,XConnectionKind.QUAD_CURVE)) {
        _matched=true;
        ObservableList<XControlPoint> _controlPoints_2 = this.getControlPoints();
        int _size_4 = _controlPoints_2.size();
        int _minus_2 = (_size_4 - 1);
        int _modulo_1 = (_minus_2 % 2);
        remainder = _modulo_1;
        boolean _equals_1 = (remainder == 0);
        if (_equals_1) {
          ObservableList<XControlPoint> _controlPoints_3 = this.getControlPoints();
          int _size_5 = _controlPoints_3.size();
          int _minus_3 = (_size_5 - 1);
          final int numSegments_1 = (_minus_3 / 2);
          ObservableList<Node> _children_1 = this.shapeGroup.getChildren();
          Iterable<QuadCurve> _filter_1 = Iterables.<QuadCurve>filter(_children_1, QuadCurve.class);
          final List<QuadCurve> curves_1 = IterableExtensions.<QuadCurve>toList(_filter_1);
          int _size_6 = curves_1.size();
          boolean _greaterThan_1 = (_size_6 > numSegments_1);
          boolean _while_2 = _greaterThan_1;
          while (_while_2) {
            QuadCurve _last = IterableExtensions.<QuadCurve>last(curves_1);
            curves_1.remove(_last);
            int _size_7 = curves_1.size();
            boolean _greaterThan_2 = (_size_7 > numSegments_1);
            _while_2 = _greaterThan_2;
          }
          int _size_7 = curves_1.size();
          boolean _lessThan_1 = (_size_7 < numSegments_1);
          boolean _while_3 = _lessThan_1;
          while (_while_3) {
            QuadCurve _quadCurve = new QuadCurve();
            final Procedure1<QuadCurve> _function = new Procedure1<QuadCurve>() {
              public void apply(final QuadCurve it) {
                it.setFill(null);
                it.setStroke(Color.BLACK);
              }
            };
            QuadCurve _doubleArrow = ObjectExtensions.<QuadCurve>operator_doubleArrow(_quadCurve, _function);
            curves_1.add(_doubleArrow);
            int _size_8 = curves_1.size();
            boolean _lessThan_2 = (_size_8 < numSegments_1);
            _while_3 = _lessThan_2;
          }
          ExclusiveRange _doubleDotLessThan_1 = new ExclusiveRange(0, numSegments_1, true);
          for (final Integer i_1 : _doubleDotLessThan_1) {
            {
              final QuadCurve curve = curves_1.get((i_1).intValue());
              final int offset = ((i_1).intValue() * 2);
              ObservableList<XControlPoint> _controlPoints_4 = this.getControlPoints();
              XControlPoint _get = _controlPoints_4.get(offset);
              double _layoutX = _get.getLayoutX();
              curve.setStartX(_layoutX);
              ObservableList<XControlPoint> _controlPoints_5 = this.getControlPoints();
              XControlPoint _get_1 = _controlPoints_5.get(offset);
              double _layoutY = _get_1.getLayoutY();
              curve.setStartY(_layoutY);
              ObservableList<XControlPoint> _controlPoints_6 = this.getControlPoints();
              int _plus = (offset + 1);
              XControlPoint _get_2 = _controlPoints_6.get(_plus);
              double _layoutX_1 = _get_2.getLayoutX();
              curve.setControlX(_layoutX_1);
              ObservableList<XControlPoint> _controlPoints_7 = this.getControlPoints();
              int _plus_1 = (offset + 1);
              XControlPoint _get_3 = _controlPoints_7.get(_plus_1);
              double _layoutY_1 = _get_3.getLayoutY();
              curve.setControlY(_layoutY_1);
              ObservableList<XControlPoint> _controlPoints_8 = this.getControlPoints();
              int _plus_2 = (offset + 2);
              XControlPoint _get_4 = _controlPoints_8.get(_plus_2);
              double _layoutX_2 = _get_4.getLayoutX();
              curve.setEndX(_layoutX_2);
              ObservableList<XControlPoint> _controlPoints_9 = this.getControlPoints();
              int _plus_3 = (offset + 2);
              XControlPoint _get_5 = _controlPoints_9.get(_plus_3);
              double _layoutY_2 = _get_5.getLayoutY();
              curve.setEndY(_layoutY_2);
            }
          }
          this.setShapes(curves_1);
        }
      }
    }
    boolean _notEquals = (remainder != 0);
    if (_notEquals) {
      Polyline _elvis = null;
      ObservableList<Node> _children_2 = this.shapeGroup.getChildren();
      Iterable<Polyline> _filter_2 = Iterables.<Polyline>filter(_children_2, Polyline.class);
      Polyline _head = IterableExtensions.<Polyline>head(_filter_2);
      if (_head != null) {
        _elvis = _head;
      } else {
        Polyline _polyline = new Polyline();
        _elvis = ObjectExtensions.<Polyline>operator_elvis(_head, _polyline);
      }
      final Procedure1<Polyline> _function = new Procedure1<Polyline>() {
        public void apply(final Polyline it) {
          it.setStroke(Color.BLACK);
        }
      };
      final Polyline polyline = ObjectExtensions.<Polyline>operator_doubleArrow(_elvis, _function);
      ObservableList<Double> _points = polyline.getPoints();
      ObservableList<XControlPoint> _controlPoints_4 = this.getControlPoints();
      final Function1<XControlPoint,List<Double>> _function_1 = new Function1<XControlPoint,List<Double>>() {
        public List<Double> apply(final XControlPoint it) {
          double _layoutX = it.getLayoutX();
          double _layoutY = it.getLayoutY();
          return Collections.<Double>unmodifiableList(Lists.<Double>newArrayList(_layoutX, _layoutY));
        }
      };
      List<List<Double>> _map = ListExtensions.<XControlPoint, List<Double>>map(_controlPoints_4, _function_1);
      Iterable<Double> _flatten = Iterables.<Double>concat(_map);
      _points.setAll(((Double[])Conversions.unwrapArray(_flatten, Double.class)));
      this.setShapes(Collections.<Polyline>unmodifiableList(Lists.<Polyline>newArrayList(polyline)));
    }
    ObservableList<Node> _children_3 = this.controlPointGroup.getChildren();
    ObservableList<XControlPoint> _controlPoints_5 = this.connectionRouter.getControlPoints();
    _children_3.setAll(_controlPoints_5);
  }
  
  protected void setShapes(final List<? extends Shape> shapes) {
    ObservableList<Node> _children = this.shapeGroup.getChildren();
    _children.setAll(shapes);
    XNode _source = this.getSource();
    double _strokeWidth = this.getStrokeWidth();
    double _strokeWidth_1 = this.getStrokeWidth();
    BoundingBox _boundingBox = new BoundingBox(0, 0, _strokeWidth, _strokeWidth_1);
    final Bounds strokeBoundsInRoot = CoreExtensions.localToRootDiagram(_source, _boundingBox);
    double _width = strokeBoundsInRoot.getWidth();
    double _height = strokeBoundsInRoot.getHeight();
    double _plus = (_width + _height);
    final double strokeInRoot = (0.5 * _plus);
    final Procedure1<Shape> _function = new Procedure1<Shape>() {
      public void apply(final Shape it) {
        Paint _stroke = XConnection.this.getStroke();
        it.setStroke(_stroke);
        it.setStrokeLineCap(StrokeLineCap.ROUND);
        it.setStrokeWidth(strokeInRoot);
      }
    };
    IterableExtensions.forEach(shapes, _function);
  }
  
  public boolean isSelectable() {
    boolean _isActive = this.getIsActive();
    return _isActive;
  }
  
  public MoveBehavior<? extends XShape> getMoveBehavior() {
    return null;
  }
  
  public void layoutChildren() {
    super.layoutChildren();
    this.connectionRouter.calculatePoints();
    XConnectionLabel _label = this.getLabel();
    if (_label!=null) {
      ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
      _label.place(_controlPoints);
    }
    ArrowHead _sourceArrowHead = this.getSourceArrowHead();
    if (_sourceArrowHead!=null) {
      _sourceArrowHead.place();
    }
    ArrowHead _targetArrowHead = this.getTargetArrowHead();
    if (_targetArrowHead!=null) {
      _targetArrowHead.place();
    }
  }
  
  public Point2D at(final double t) {
    Point2D _xblockexpression = null;
    {
      boolean _or = false;
      boolean _lessThan = (t < 0);
      if (_lessThan) {
        _or = true;
      } else {
        boolean _greaterThan = (t > 1);
        _or = (_lessThan || _greaterThan);
      }
      if (_or) {
        IllegalArgumentException _illegalArgumentException = new IllegalArgumentException("Argument must be between 0 and 1");
        throw _illegalArgumentException;
      }
      boolean _equals = (t == 1);
      if (_equals) {
        ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
        XControlPoint _last = IterableExtensions.<XControlPoint>last(_controlPoints);
        double _layoutX = _last.getLayoutX();
        ObservableList<XControlPoint> _controlPoints_1 = this.getControlPoints();
        XControlPoint _last_1 = IterableExtensions.<XControlPoint>last(_controlPoints_1);
        double _layoutY = _last_1.getLayoutY();
        Point2D _point2D = new Point2D(_layoutX, _layoutY);
        return _point2D;
      }
      Point2D _switchResult = null;
      XConnectionKind _kind = this.getKind();
      final XConnectionKind getKind = _kind;
      boolean _matched = false;
      if (!_matched) {
        if (Objects.equal(getKind,XConnectionKind.CUBIC_CURVE)) {
          _matched=true;
          Point2D _xblockexpression_1 = null;
          {
            ObservableList<Node> _children = this.shapeGroup.getChildren();
            final Iterable<CubicCurve> curves = Iterables.<CubicCurve>filter(_children, CubicCurve.class);
            int _size = IterableExtensions.size(curves);
            final double segment = (t * _size);
            final int index = ((int) segment);
            final CubicCurve curve = ((CubicCurve[])Conversions.unwrapArray(curves, CubicCurve.class))[index];
            double _minus = (segment - index);
            int _size_1 = IterableExtensions.size(curves);
            double _multiply = (_minus * _size_1);
            Point2D _at = BezierExtensions.at(curve, _multiply);
            _xblockexpression_1 = (_at);
          }
          _switchResult = _xblockexpression_1;
        }
      }
      if (!_matched) {
        if (Objects.equal(getKind,XConnectionKind.QUAD_CURVE)) {
          _matched=true;
          Point2D _xblockexpression_2 = null;
          {
            ObservableList<Node> _children = this.shapeGroup.getChildren();
            final Iterable<QuadCurve> curves = Iterables.<QuadCurve>filter(_children, QuadCurve.class);
            int _size = IterableExtensions.size(curves);
            final double segment = (t * _size);
            final int index = ((int) segment);
            final QuadCurve curve = ((QuadCurve[])Conversions.unwrapArray(curves, QuadCurve.class))[index];
            double _minus = (segment - index);
            int _size_1 = IterableExtensions.size(curves);
            double _multiply = (_minus * _size_1);
            Point2D _at = BezierExtensions.at(curve, _multiply);
            _xblockexpression_2 = (_at);
          }
          _switchResult = _xblockexpression_2;
        }
      }
      if (!_matched) {
        if (Objects.equal(getKind,XConnectionKind.POLYLINE)) {
          _matched=true;
          Point2D _xblockexpression_3 = null;
          {
            ObservableList<Node> _children = this.shapeGroup.getChildren();
            Iterable<Polyline> _filter = Iterables.<Polyline>filter(_children, Polyline.class);
            final Polyline line = IterableExtensions.<Polyline>head(_filter);
            ObservableList<Double> _points = line.getPoints();
            int _size = _points.size();
            int _divide = (_size / 2);
            final int numSegments = (_divide - 1);
            final double segment = (t * numSegments);
            final int index = ((int) segment);
            ObservableList<Double> _points_1 = line.getPoints();
            Double _get = _points_1.get(index);
            ObservableList<Double> _points_2 = line.getPoints();
            int _plus = (index + 1);
            Double _get_1 = _points_2.get(_plus);
            ObservableList<Double> _points_3 = line.getPoints();
            int _plus_1 = (index + 2);
            Double _get_2 = _points_3.get(_plus_1);
            ObservableList<Double> _points_4 = line.getPoints();
            int _plus_2 = (index + 3);
            Double _get_3 = _points_4.get(_plus_2);
            double _minus = (segment - index);
            double _multiply = (_minus * numSegments);
            Point2D _linear = Point2DExtensions.linear((_get).doubleValue(), (_get_1).doubleValue(), (_get_2).doubleValue(), (_get_3).doubleValue(), _multiply);
            _xblockexpression_3 = (_linear);
          }
          _switchResult = _xblockexpression_3;
        }
      }
      _xblockexpression = (_switchResult);
    }
    return _xblockexpression;
  }
  
  public Point2D derivativeAt(final double t) {
    Point2D _xblockexpression = null;
    {
      boolean _or = false;
      boolean _lessThan = (t < 0);
      if (_lessThan) {
        _or = true;
      } else {
        boolean _greaterThan = (t > 1);
        _or = (_lessThan || _greaterThan);
      }
      if (_or) {
        IllegalArgumentException _illegalArgumentException = new IllegalArgumentException("Argument must be between 0 and 1");
        throw _illegalArgumentException;
      }
      Point2D _switchResult = null;
      XConnectionKind _kind = this.getKind();
      final XConnectionKind getKind = _kind;
      boolean _matched = false;
      if (!_matched) {
        if (Objects.equal(getKind,XConnectionKind.CUBIC_CURVE)) {
          _matched=true;
          Point2D _xblockexpression_1 = null;
          {
            ObservableList<Node> _children = this.shapeGroup.getChildren();
            final Iterable<CubicCurve> curves = Iterables.<CubicCurve>filter(_children, CubicCurve.class);
            boolean _equals = (t == 1);
            if (_equals) {
              CubicCurve _last = IterableExtensions.<CubicCurve>last(curves);
              return BezierExtensions.derivativeAt(_last, 1);
            }
            int _size = IterableExtensions.size(curves);
            final double segment = (t * _size);
            final int index = ((int) segment);
            final CubicCurve curve = ((CubicCurve[])Conversions.unwrapArray(curves, CubicCurve.class))[index];
            double _minus = (segment - index);
            int _size_1 = IterableExtensions.size(curves);
            double _multiply = (_minus * _size_1);
            Point2D _derivativeAt = BezierExtensions.derivativeAt(curve, _multiply);
            _xblockexpression_1 = (_derivativeAt);
          }
          _switchResult = _xblockexpression_1;
        }
      }
      if (!_matched) {
        if (Objects.equal(getKind,XConnectionKind.QUAD_CURVE)) {
          _matched=true;
          Point2D _xblockexpression_2 = null;
          {
            ObservableList<Node> _children = this.shapeGroup.getChildren();
            final Iterable<QuadCurve> curves = Iterables.<QuadCurve>filter(_children, QuadCurve.class);
            boolean _equals = (t == 1);
            if (_equals) {
              QuadCurve _last = IterableExtensions.<QuadCurve>last(curves);
              return BezierExtensions.derivativeAt(_last, 1);
            }
            int _size = IterableExtensions.size(curves);
            final double segment = (t * _size);
            final int index = ((int) segment);
            final QuadCurve curve = ((QuadCurve[])Conversions.unwrapArray(curves, QuadCurve.class))[index];
            double _minus = (segment - index);
            int _size_1 = IterableExtensions.size(curves);
            double _multiply = (_minus * _size_1);
            Point2D _derivativeAt = BezierExtensions.derivativeAt(curve, _multiply);
            _xblockexpression_2 = (_derivativeAt);
          }
          _switchResult = _xblockexpression_2;
        }
      }
      if (!_matched) {
        if (Objects.equal(getKind,XConnectionKind.POLYLINE)) {
          _matched=true;
          Point2D _xblockexpression_3 = null;
          {
            ObservableList<Node> _children = this.shapeGroup.getChildren();
            Iterable<Polyline> _filter = Iterables.<Polyline>filter(_children, Polyline.class);
            final Polyline line = IterableExtensions.<Polyline>head(_filter);
            ObservableList<Double> _points = line.getPoints();
            int _size = _points.size();
            int _divide = (_size / 2);
            final int numSegments = (_divide - 1);
            double _xifexpression = (double) 0;
            boolean _equals = (t == 1);
            if (_equals) {
              double _divide_1 = (0.5 / numSegments);
              double _minus = (numSegments - _divide_1);
              _xifexpression = _minus;
            } else {
              double _multiply = (t * numSegments);
              _xifexpression = _multiply;
            }
            final double segment = _xifexpression;
            final int index = ((int) segment);
            ObservableList<Double> _points_1 = line.getPoints();
            int _plus = (index + 2);
            Double _get = _points_1.get(_plus);
            ObservableList<Double> _points_2 = line.getPoints();
            Double _get_1 = _points_2.get(index);
            double _minus_1 = DoubleExtensions.operator_minus(_get, _get_1);
            ObservableList<Double> _points_3 = line.getPoints();
            int _plus_1 = (index + 3);
            Double _get_2 = _points_3.get(_plus_1);
            ObservableList<Double> _points_4 = line.getPoints();
            int _plus_2 = (index + 1);
            Double _get_3 = _points_4.get(_plus_2);
            double _minus_2 = DoubleExtensions.operator_minus(_get_2, _get_3);
            Point2D _point2D = new Point2D(_minus_1, _minus_2);
            _xblockexpression_3 = (_point2D);
          }
          _switchResult = _xblockexpression_3;
        }
      }
      _xblockexpression = (_switchResult);
    }
    return _xblockexpression;
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.XConnection");
    ;
  
  private SimpleObjectProperty<XNode> sourceProperty = new SimpleObjectProperty<XNode>(this, "source");
  
  public XNode getSource() {
    return this.sourceProperty.get();
  }
  
  public void setSource(final XNode source) {
    this.sourceProperty.set(source);
  }
  
  public ObjectProperty<XNode> sourceProperty() {
    return this.sourceProperty;
  }
  
  private SimpleObjectProperty<XNode> targetProperty = new SimpleObjectProperty<XNode>(this, "target");
  
  public XNode getTarget() {
    return this.targetProperty.get();
  }
  
  public void setTarget(final XNode target) {
    this.targetProperty.set(target);
  }
  
  public ObjectProperty<XNode> targetProperty() {
    return this.targetProperty;
  }
  
  private SimpleObjectProperty<XConnectionLabel> labelProperty = new SimpleObjectProperty<XConnectionLabel>(this, "label");
  
  public XConnectionLabel getLabel() {
    return this.labelProperty.get();
  }
  
  public void setLabel(final XConnectionLabel label) {
    this.labelProperty.set(label);
  }
  
  public ObjectProperty<XConnectionLabel> labelProperty() {
    return this.labelProperty;
  }
  
  private SimpleObjectProperty<ArrowHead> sourceArrowHeadProperty = new SimpleObjectProperty<ArrowHead>(this, "sourceArrowHead");
  
  public ArrowHead getSourceArrowHead() {
    return this.sourceArrowHeadProperty.get();
  }
  
  public void setSourceArrowHead(final ArrowHead sourceArrowHead) {
    this.sourceArrowHeadProperty.set(sourceArrowHead);
  }
  
  public ObjectProperty<ArrowHead> sourceArrowHeadProperty() {
    return this.sourceArrowHeadProperty;
  }
  
  private SimpleObjectProperty<ArrowHead> targetArrowHeadProperty = new SimpleObjectProperty<ArrowHead>(this, "targetArrowHead");
  
  public ArrowHead getTargetArrowHead() {
    return this.targetArrowHeadProperty.get();
  }
  
  public void setTargetArrowHead(final ArrowHead targetArrowHead) {
    this.targetArrowHeadProperty.set(targetArrowHead);
  }
  
  public ObjectProperty<ArrowHead> targetArrowHeadProperty() {
    return this.targetArrowHeadProperty;
  }
  
  private SimpleObjectProperty<XConnectionKind> kindProperty = new SimpleObjectProperty<XConnectionKind>(this, "kind",_initKind());
  
  private static final XConnectionKind _initKind() {
    return XConnectionKind.POLYLINE;
  }
  
  public XConnectionKind getKind() {
    return this.kindProperty.get();
  }
  
  public void setKind(final XConnectionKind kind) {
    this.kindProperty.set(kind);
  }
  
  public ObjectProperty<XConnectionKind> kindProperty() {
    return this.kindProperty;
  }
  
  private SimpleDoubleProperty strokeWidthProperty = new SimpleDoubleProperty(this, "strokeWidth",_initStrokeWidth());
  
  private static final double _initStrokeWidth() {
    return 2.0;
  }
  
  public double getStrokeWidth() {
    return this.strokeWidthProperty.get();
  }
  
  public void setStrokeWidth(final double strokeWidth) {
    this.strokeWidthProperty.set(strokeWidth);
  }
  
  public DoubleProperty strokeWidthProperty() {
    return this.strokeWidthProperty;
  }
  
  private SimpleObjectProperty<Paint> strokeProperty = new SimpleObjectProperty<Paint>(this, "stroke",_initStroke());
  
  private static final Paint _initStroke() {
    return Color.BLACK;
  }
  
  public Paint getStroke() {
    return this.strokeProperty.get();
  }
  
  public void setStroke(final Paint stroke) {
    this.strokeProperty.set(stroke);
  }
  
  public ObjectProperty<Paint> strokeProperty() {
    return this.strokeProperty;
  }
}
