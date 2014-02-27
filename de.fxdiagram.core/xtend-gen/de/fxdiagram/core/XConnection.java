package de.fxdiagram.core;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XConnectionKind;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.anchors.ArrowHead;
import de.fxdiagram.core.anchors.ConnectionRouter;
import de.fxdiagram.core.anchors.TriangleArrowHead;
import de.fxdiagram.core.extensions.BezierExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.InitializingListListener;
import de.fxdiagram.core.extensions.Point2DExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.StringDescriptor;
import de.fxdiagram.core.model.XModelProvider;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@Logging
@ModelNode({ "domainObject", "source", "target", "kind", "controlPoints", "labels", "sourceArrowHead", "targetArrowHead" })
@SuppressWarnings("all")
public class XConnection extends XShape implements XModelProvider {
  private Group controlPointGroup = new Group();
  
  private Group shapeGroup = new Group();
  
  private ChangeListener<Number> controlPointListener;
  
  public XConnection() {
    TriangleArrowHead _triangleArrowHead = new TriangleArrowHead(this, false);
    this.setTargetArrowHead(_triangleArrowHead);
  }
  
  public XConnection(final DomainObjectDescriptor domainObject) {
    this();
    this.domainObjectProperty.set(domainObject);
  }
  
  public XConnection(final XNode source, final XNode target, final DomainObjectDescriptor domainObject) {
    this(domainObject);
    this.setSource(source);
    this.setTarget(target);
  }
  
  public XConnection(final XNode source, final XNode target) {
    this(source, target, new StringDescriptor(((source.getName() + "->") + target.getName())));
  }
  
  public void setSource(final XNode source) {
    this.sourceProperty.set(source);
    ObservableList<XConnection> _outgoingConnections = source.getOutgoingConnections();
    boolean _contains = _outgoingConnections.contains(this);
    boolean _not = (!_contains);
    if (_not) {
      ObservableList<XConnection> _outgoingConnections_1 = source.getOutgoingConnections();
      _outgoingConnections_1.add(this);
    }
  }
  
  public void setTarget(final XNode target) {
    this.targetProperty.set(target);
    ObservableList<XConnection> _incomingConnections = target.getIncomingConnections();
    boolean _contains = _incomingConnections.contains(this);
    boolean _not = (!_contains);
    if (_not) {
      ObservableList<XConnection> _incomingConnections_1 = target.getIncomingConnections();
      _incomingConnections_1.add(this);
    }
  }
  
  protected Node createNode() {
    Group _xblockexpression = null;
    {
      final Group node = this.shapeGroup;
      ObservableList<Node> _children = this.getChildren();
      final Procedure1<Group> _function = new Procedure1<Group>() {
        public void apply(final Group it) {
          it.setVisible(false);
        }
      };
      Group _doubleArrow = ObjectExtensions.<Group>operator_doubleArrow(this.controlPointGroup, _function);
      _children.add(_doubleArrow);
      ConnectionRouter _connectionRouter = new ConnectionRouter(this);
      this.setConnectionRouter(_connectionRouter);
      ConnectionRouter _connectionRouter_1 = this.getConnectionRouter();
      _connectionRouter_1.calculatePoints();
      _xblockexpression = node;
    }
    return _xblockexpression;
  }
  
  public void doActivate() {
    Paint _stroke = this.getStroke();
    boolean _equals = Objects.equal(_stroke, null);
    if (_equals) {
      XDiagram _diagram = CoreExtensions.getDiagram(this);
      Paint _connectionPaint = _diagram.getConnectionPaint();
      this.setStroke(_connectionPaint);
    }
    final ChangeListener<Number> _function = new ChangeListener<Number>() {
      public void changed(final ObservableValue<? extends Number> prop, final Number oldVal, final Number newVal) {
        XConnection.this.updateShapes();
      }
    };
    this.controlPointListener = _function;
    ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
    InitializingListListener<XControlPoint> _initializingListListener = new InitializingListListener<XControlPoint>();
    final Procedure1<InitializingListListener<XControlPoint>> _function_1 = new Procedure1<InitializingListListener<XControlPoint>>() {
      public void apply(final InitializingListListener<XControlPoint> it) {
        final Procedure1<ListChangeListener.Change<? extends XControlPoint>> _function = new Procedure1<ListChangeListener.Change<? extends XControlPoint>>() {
          public void apply(final ListChangeListener.Change<? extends XControlPoint> it) {
            XConnection.this.updateShapes();
          }
        };
        it.setChange(_function);
        final Procedure1<XControlPoint> _function_1 = new Procedure1<XControlPoint>() {
          public void apply(final XControlPoint it) {
            ObservableList<XControlPoint> _controlPoints = XConnection.this.getControlPoints();
            final int index = _controlPoints.indexOf(it);
            boolean _or = false;
            if ((index == 0)) {
              _or = true;
            } else {
              ObservableList<XControlPoint> _controlPoints_1 = XConnection.this.getControlPoints();
              int _size = _controlPoints_1.size();
              int _minus = (_size - 1);
              boolean _equals = (index == _minus);
              _or = _equals;
            }
            if (_or) {
              it.initializeGraphics();
            } else {
              it.activate();
            }
            DoubleProperty _layoutXProperty = it.layoutXProperty();
            _layoutXProperty.addListener(XConnection.this.controlPointListener);
            DoubleProperty _layoutYProperty = it.layoutYProperty();
            _layoutYProperty.addListener(XConnection.this.controlPointListener);
          }
        };
        it.setAdd(_function_1);
        final Procedure1<XControlPoint> _function_2 = new Procedure1<XControlPoint>() {
          public void apply(final XControlPoint it) {
            DoubleProperty _layoutXProperty = it.layoutXProperty();
            _layoutXProperty.removeListener(XConnection.this.controlPointListener);
            DoubleProperty _layoutYProperty = it.layoutYProperty();
            _layoutYProperty.removeListener(XConnection.this.controlPointListener);
          }
        };
        it.setRemove(_function_2);
      }
    };
    InitializingListListener<XControlPoint> _doubleArrow = ObjectExtensions.<InitializingListListener<XControlPoint>>operator_doubleArrow(_initializingListListener, _function_1);
    CoreExtensions.<XControlPoint>addInitializingListener(_controlPoints, _doubleArrow);
    ObservableList<XConnectionLabel> _labels = this.getLabels();
    final Procedure1<XConnectionLabel> _function_2 = new Procedure1<XConnectionLabel>() {
      public void apply(final XConnectionLabel it) {
        it.activate();
      }
    };
    IterableExtensions.<XConnectionLabel>forEach(_labels, _function_2);
    ConnectionRouter _connectionRouter = this.getConnectionRouter();
    _connectionRouter.activate();
    this.updateShapes();
    ReadOnlyObjectProperty<Parent> _parentProperty = this.parentProperty();
    final ChangeListener<Parent> _function_3 = new ChangeListener<Parent>() {
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
    _parentProperty.addListener(_function_3);
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
  
  public void updateShapes() {
    int remainder = (-1);
    XConnectionKind _kind = this.getKind();
    if (_kind != null) {
      switch (_kind) {
        case CUBIC_CURVE:
          ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
          int _size = _controlPoints.size();
          int _minus = (_size - 1);
          int _modulo = (_minus % 3);
          remainder = _modulo;
          if ((remainder == 0)) {
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
                XControlPoint _get_2 = _controlPoints_4.get((offset + 1));
                double _layoutX_1 = _get_2.getLayoutX();
                curve.setControlX1(_layoutX_1);
                ObservableList<XControlPoint> _controlPoints_5 = this.getControlPoints();
                XControlPoint _get_3 = _controlPoints_5.get((offset + 1));
                double _layoutY_1 = _get_3.getLayoutY();
                curve.setControlY1(_layoutY_1);
                ObservableList<XControlPoint> _controlPoints_6 = this.getControlPoints();
                XControlPoint _get_4 = _controlPoints_6.get((offset + 2));
                double _layoutX_2 = _get_4.getLayoutX();
                curve.setControlX2(_layoutX_2);
                ObservableList<XControlPoint> _controlPoints_7 = this.getControlPoints();
                XControlPoint _get_5 = _controlPoints_7.get((offset + 2));
                double _layoutY_2 = _get_5.getLayoutY();
                curve.setControlY2(_layoutY_2);
                ObservableList<XControlPoint> _controlPoints_8 = this.getControlPoints();
                XControlPoint _get_6 = _controlPoints_8.get((offset + 3));
                double _layoutX_3 = _get_6.getLayoutX();
                curve.setEndX(_layoutX_3);
                ObservableList<XControlPoint> _controlPoints_9 = this.getControlPoints();
                XControlPoint _get_7 = _controlPoints_9.get((offset + 3));
                double _layoutY_3 = _get_7.getLayoutY();
                curve.setEndY(_layoutY_3);
              }
            }
            this.setShapes(curves);
          }
          break;
        case QUAD_CURVE:
          ObservableList<XControlPoint> _controlPoints_2 = this.getControlPoints();
          int _size_4 = _controlPoints_2.size();
          int _minus_2 = (_size_4 - 1);
          int _modulo_1 = (_minus_2 % 2);
          remainder = _modulo_1;
          if ((remainder == 0)) {
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
                XControlPoint _get_2 = _controlPoints_6.get((offset + 1));
                double _layoutX_1 = _get_2.getLayoutX();
                curve.setControlX(_layoutX_1);
                ObservableList<XControlPoint> _controlPoints_7 = this.getControlPoints();
                XControlPoint _get_3 = _controlPoints_7.get((offset + 1));
                double _layoutY_1 = _get_3.getLayoutY();
                curve.setControlY(_layoutY_1);
                ObservableList<XControlPoint> _controlPoints_8 = this.getControlPoints();
                XControlPoint _get_4 = _controlPoints_8.get((offset + 2));
                double _layoutX_2 = _get_4.getLayoutX();
                curve.setEndX(_layoutX_2);
                ObservableList<XControlPoint> _controlPoints_9 = this.getControlPoints();
                XControlPoint _get_5 = _controlPoints_9.get((offset + 2));
                double _layoutY_2 = _get_5.getLayoutY();
                curve.setEndY(_layoutY_2);
              }
            }
            this.setShapes(curves_1);
          }
          break;
        default:
          break;
      }
    }
    if ((remainder != 0)) {
      Polyline _elvis = null;
      ObservableList<Node> _children_2 = this.shapeGroup.getChildren();
      Iterable<Polyline> _filter_2 = Iterables.<Polyline>filter(_children_2, Polyline.class);
      Polyline _head = IterableExtensions.<Polyline>head(_filter_2);
      if (_head != null) {
        _elvis = _head;
      } else {
        Polyline _polyline = new Polyline();
        _elvis = _polyline;
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
    ObservableList<XControlPoint> _controlPoints_5 = this.getControlPoints();
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
    return this.getIsActive();
  }
  
  public void layoutChildren() {
    super.layoutChildren();
    ConnectionRouter _connectionRouter = this.getConnectionRouter();
    _connectionRouter.calculatePoints();
    try {
      ObservableList<XConnectionLabel> _labels = this.getLabels();
      final Procedure1<XConnectionLabel> _function = new Procedure1<XConnectionLabel>() {
        public void apply(final XConnectionLabel it) {
          ObservableList<XControlPoint> _controlPoints = XConnection.this.getControlPoints();
          it.place(_controlPoints);
        }
      };
      IterableExtensions.<XConnectionLabel>forEach(_labels, _function);
      ArrowHead _sourceArrowHead = this.getSourceArrowHead();
      if (_sourceArrowHead!=null) {
        _sourceArrowHead.place();
      }
      ArrowHead _targetArrowHead = this.getTargetArrowHead();
      if (_targetArrowHead!=null) {
        _targetArrowHead.place();
      }
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception exc = (Exception)_t;
        String _message = exc.getMessage();
        String _plus = ("Exception in XConnection.layoutChildren() " + _message);
        XConnection.LOG.severe(_plus);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  public Point2D at(final double t) {
    Point2D _xblockexpression = null;
    {
      if (((t < 0) || (t > 1))) {
        throw new IllegalArgumentException("Argument must be between 0 and 1");
      }
      if ((t == 1)) {
        ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
        XControlPoint _last = IterableExtensions.<XControlPoint>last(_controlPoints);
        double _layoutX = _last.getLayoutX();
        ObservableList<XControlPoint> _controlPoints_1 = this.getControlPoints();
        XControlPoint _last_1 = IterableExtensions.<XControlPoint>last(_controlPoints_1);
        double _layoutY = _last_1.getLayoutY();
        return new Point2D(_layoutX, _layoutY);
      }
      Point2D _switchResult = null;
      XConnectionKind _kind = this.getKind();
      if (_kind != null) {
        switch (_kind) {
          case CUBIC_CURVE:
            Point2D _xblockexpression_1 = null;
            {
              ObservableList<Node> _children = this.shapeGroup.getChildren();
              final Iterable<CubicCurve> curves = Iterables.<CubicCurve>filter(_children, CubicCurve.class);
              int _size = IterableExtensions.size(curves);
              final double segment = (t * _size);
              final int index = ((int) segment);
              final CubicCurve curve = ((CubicCurve[])Conversions.unwrapArray(curves, CubicCurve.class))[index];
              _xblockexpression_1 = BezierExtensions.at(curve, (segment - index));
            }
            _switchResult = _xblockexpression_1;
            break;
          case QUAD_CURVE:
            Point2D _xblockexpression_2 = null;
            {
              ObservableList<Node> _children = this.shapeGroup.getChildren();
              final Iterable<QuadCurve> curves = Iterables.<QuadCurve>filter(_children, QuadCurve.class);
              int _size = IterableExtensions.size(curves);
              final double segment = (t * _size);
              final int index = ((int) segment);
              final QuadCurve curve = ((QuadCurve[])Conversions.unwrapArray(curves, QuadCurve.class))[index];
              _xblockexpression_2 = BezierExtensions.at(curve, (segment - index));
            }
            _switchResult = _xblockexpression_2;
            break;
          case POLYLINE:
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
              Double _get_1 = _points_2.get((index + 1));
              ObservableList<Double> _points_3 = line.getPoints();
              Double _get_2 = _points_3.get((index + 2));
              ObservableList<Double> _points_4 = line.getPoints();
              Double _get_3 = _points_4.get((index + 3));
              _xblockexpression_3 = Point2DExtensions.linear((_get).doubleValue(), (_get_1).doubleValue(), (_get_2).doubleValue(), (_get_3).doubleValue(), 
                (segment - index));
            }
            _switchResult = _xblockexpression_3;
            break;
          default:
            break;
        }
      }
      _xblockexpression = _switchResult;
    }
    return _xblockexpression;
  }
  
  public Point2D derivativeAt(final double t) {
    Point2D _xblockexpression = null;
    {
      if (((t < 0) || (t > 1))) {
        throw new IllegalArgumentException("Argument must be between 0 and 1");
      }
      Point2D _switchResult = null;
      XConnectionKind _kind = this.getKind();
      if (_kind != null) {
        switch (_kind) {
          case CUBIC_CURVE:
            Point2D _xblockexpression_1 = null;
            {
              ObservableList<Node> _children = this.shapeGroup.getChildren();
              final Iterable<CubicCurve> curves = Iterables.<CubicCurve>filter(_children, CubicCurve.class);
              if ((t == 1)) {
                CubicCurve _last = IterableExtensions.<CubicCurve>last(curves);
                return BezierExtensions.derivativeAt(_last, 1);
              }
              int _size = IterableExtensions.size(curves);
              final double segment = (t * _size);
              final int index = ((int) segment);
              final CubicCurve curve = ((CubicCurve[])Conversions.unwrapArray(curves, CubicCurve.class))[index];
              _xblockexpression_1 = BezierExtensions.derivativeAt(curve, (segment - index));
            }
            _switchResult = _xblockexpression_1;
            break;
          case QUAD_CURVE:
            Point2D _xblockexpression_2 = null;
            {
              ObservableList<Node> _children = this.shapeGroup.getChildren();
              final Iterable<QuadCurve> curves = Iterables.<QuadCurve>filter(_children, QuadCurve.class);
              if ((t == 1)) {
                QuadCurve _last = IterableExtensions.<QuadCurve>last(curves);
                return BezierExtensions.derivativeAt(_last, 1);
              }
              int _size = IterableExtensions.size(curves);
              final double segment = (t * _size);
              final int index = ((int) segment);
              final QuadCurve curve = ((QuadCurve[])Conversions.unwrapArray(curves, QuadCurve.class))[index];
              _xblockexpression_2 = BezierExtensions.derivativeAt(curve, (segment - index));
            }
            _switchResult = _xblockexpression_2;
            break;
          case POLYLINE:
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
              if ((t == 1)) {
                ObservableList<Double> _points_1 = line.getPoints();
                int _size_1 = _points_1.size();
                _xifexpression = (_size_1 - 4);
              } else {
                _xifexpression = (t * numSegments);
              }
              final double segment = _xifexpression;
              final int index = ((int) segment);
              ObservableList<Double> _points_2 = line.getPoints();
              Double _get = _points_2.get((index + 2));
              ObservableList<Double> _points_3 = line.getPoints();
              Double _get_1 = _points_3.get(index);
              double _minus = DoubleExtensions.operator_minus(_get, _get_1);
              ObservableList<Double> _points_4 = line.getPoints();
              Double _get_2 = _points_4.get((index + 3));
              ObservableList<Double> _points_5 = line.getPoints();
              Double _get_3 = _points_5.get((index + 1));
              double _minus_1 = DoubleExtensions.operator_minus(_get_2, _get_3);
              _xblockexpression_3 = new Point2D(_minus, _minus_1);
            }
            _switchResult = _xblockexpression_3;
            break;
          default:
            break;
        }
      }
      _xblockexpression = _switchResult;
    }
    return _xblockexpression;
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.XConnection");
    ;
  
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(domainObjectProperty, DomainObjectDescriptor.class);
    modelElement.addProperty(sourceProperty, XNode.class);
    modelElement.addProperty(targetProperty, XNode.class);
    modelElement.addProperty(kindProperty, XConnectionKind.class);
    modelElement.addProperty(controlPointsProperty, XControlPoint.class);
    modelElement.addProperty(labelsProperty, XConnectionLabel.class);
    modelElement.addProperty(sourceArrowHeadProperty, ArrowHead.class);
    modelElement.addProperty(targetArrowHeadProperty, ArrowHead.class);
  }
  
  private ReadOnlyObjectWrapper<XNode> sourceProperty = new ReadOnlyObjectWrapper<XNode>(this, "source");
  
  public XNode getSource() {
    return this.sourceProperty.get();
  }
  
  public ReadOnlyObjectProperty<XNode> sourceProperty() {
    return this.sourceProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyObjectWrapper<XNode> targetProperty = new ReadOnlyObjectWrapper<XNode>(this, "target");
  
  public XNode getTarget() {
    return this.targetProperty.get();
  }
  
  public ReadOnlyObjectProperty<XNode> targetProperty() {
    return this.targetProperty.getReadOnlyProperty();
  }
  
  private SimpleListProperty<XConnectionLabel> labelsProperty = new SimpleListProperty<XConnectionLabel>(this, "labels",_initLabels());
  
  private static final ObservableList<XConnectionLabel> _initLabels() {
    ObservableList<XConnectionLabel> _observableArrayList = FXCollections.<XConnectionLabel>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<XConnectionLabel> getLabels() {
    return this.labelsProperty.get();
  }
  
  public ListProperty<XConnectionLabel> labelsProperty() {
    return this.labelsProperty;
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
  
  private SimpleObjectProperty<Paint> strokeProperty = new SimpleObjectProperty<Paint>(this, "stroke");
  
  public Paint getStroke() {
    return this.strokeProperty.get();
  }
  
  public void setStroke(final Paint stroke) {
    this.strokeProperty.set(stroke);
  }
  
  public ObjectProperty<Paint> strokeProperty() {
    return this.strokeProperty;
  }
  
  private ReadOnlyObjectWrapper<DomainObjectDescriptor> domainObjectProperty = new ReadOnlyObjectWrapper<DomainObjectDescriptor>(this, "domainObject");
  
  public DomainObjectDescriptor getDomainObject() {
    return this.domainObjectProperty.get();
  }
  
  public ReadOnlyObjectProperty<DomainObjectDescriptor> domainObjectProperty() {
    return this.domainObjectProperty.getReadOnlyProperty();
  }
  
  private SimpleObjectProperty<ConnectionRouter> connectionRouterProperty = new SimpleObjectProperty<ConnectionRouter>(this, "connectionRouter");
  
  public ConnectionRouter getConnectionRouter() {
    return this.connectionRouterProperty.get();
  }
  
  public void setConnectionRouter(final ConnectionRouter connectionRouter) {
    this.connectionRouterProperty.set(connectionRouter);
  }
  
  public ObjectProperty<ConnectionRouter> connectionRouterProperty() {
    return this.connectionRouterProperty;
  }
}
