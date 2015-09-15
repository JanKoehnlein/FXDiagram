package de.fxdiagram.core;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XDomainObjectShape;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.ArrowHead;
import de.fxdiagram.core.anchors.ConnectionRouter;
import de.fxdiagram.core.anchors.TriangleArrowHead;
import de.fxdiagram.core.extensions.BezierExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.DoubleExpressionExtensions;
import de.fxdiagram.core.extensions.InitializingListListener;
import de.fxdiagram.core.extensions.Point2DExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.StringDescriptor;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
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
import javafx.scene.paint.Paint;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.DoubleExtensions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * A line connecting two {@link XNode}s.
 * 
 * A {@link XConnection} is always directed, i.e. it has a dedicated {@link #source} and {@link #target}.
 * These properties are automatically kept in sync with their counterparts {@link XNode#outgoingConnections}
 * and {@link XNode#incomingConnections}.
 * 
 * Independent of {@link #source} and {@link #target}, it can have {@link ArrowHead}s at the respective ends.
 * It may also have a {@link XconnectionLabel}. The actual shape of a connection is determined by its
 * {@link ConnectionRouter} and its {@link Kind}.
 * 
 * A connection can refer to a {@link #domainObject} as its underlying semantic element.
 * 
 * Clients usually don't extend this class, but configure its label and appearance properties.
 */
@Logging
@ModelNode({ "source", "target", "kind", "controlPoints", "labels", "sourceArrowHead", "targetArrowHead" })
@SuppressWarnings("all")
public class XConnection extends XDomainObjectShape {
  public enum Kind {
    POLYLINE,
    
    QUAD_CURVE,
    
    CUBIC_CURVE;
  }
  
  private Group controlPointGroup = new Group();
  
  private Group shapeGroup = new Group();
  
  private ChangeListener<Number> controlPointListener;
  
  public XConnection() {
    TriangleArrowHead _triangleArrowHead = new TriangleArrowHead(this, false);
    this.setTargetArrowHead(_triangleArrowHead);
    this.addSourceTargetListeners();
  }
  
  public XConnection(final DomainObjectDescriptor domainObject) {
    super(domainObject);
    this.addSourceTargetListeners();
    TriangleArrowHead _triangleArrowHead = new TriangleArrowHead(this, false);
    this.setTargetArrowHead(_triangleArrowHead);
  }
  
  public XConnection(final XNode source, final XNode target, final DomainObjectDescriptor domainObject) {
    this(domainObject);
    this.sourceProperty.set(source);
    this.targetProperty.set(target);
  }
  
  public XConnection(final XNode source, final XNode target) {
    this(source, target, new StringDescriptor(((source.getName() + "->") + target.getName())));
  }
  
  protected void addSourceTargetListeners() {
    final ChangeListener<XNode> _function = (ObservableValue<? extends XNode> p, XNode oldSource, XNode newSource) -> {
      ObservableList<XConnection> _outgoingConnections = null;
      if (oldSource!=null) {
        _outgoingConnections=oldSource.getOutgoingConnections();
      }
      if (_outgoingConnections!=null) {
        _outgoingConnections.remove(this);
      }
      boolean _and = false;
      boolean _notEquals = (!Objects.equal(newSource, null));
      if (!_notEquals) {
        _and = false;
      } else {
        ObservableList<XConnection> _outgoingConnections_1 = newSource.getOutgoingConnections();
        boolean _contains = _outgoingConnections_1.contains(this);
        boolean _not = (!_contains);
        _and = _not;
      }
      if (_and) {
        ObservableList<XConnection> _outgoingConnections_2 = newSource.getOutgoingConnections();
        _outgoingConnections_2.add(this);
      }
      this.setNeedsLayout(true);
    };
    this.sourceProperty.addListener(_function);
    final ChangeListener<XNode> _function_1 = (ObservableValue<? extends XNode> p, XNode oldTarget, XNode newTarget) -> {
      ObservableList<XConnection> _incomingConnections = null;
      if (oldTarget!=null) {
        _incomingConnections=oldTarget.getIncomingConnections();
      }
      if (_incomingConnections!=null) {
        _incomingConnections.remove(this);
      }
      boolean _and = false;
      boolean _notEquals = (!Objects.equal(newTarget, null));
      if (!_notEquals) {
        _and = false;
      } else {
        ObservableList<XConnection> _incomingConnections_1 = newTarget.getIncomingConnections();
        boolean _contains = _incomingConnections_1.contains(this);
        boolean _not = (!_contains);
        _and = _not;
      }
      if (_and) {
        ObservableList<XConnection> _incomingConnections_2 = newTarget.getIncomingConnections();
        _incomingConnections_2.add(this);
      }
      this.setNeedsLayout(true);
    };
    this.targetProperty.addListener(_function_1);
  }
  
  @Override
  protected Node createNode() {
    Group _xblockexpression = null;
    {
      final Group node = this.shapeGroup;
      ObservableList<Node> _children = this.getChildren();
      final Procedure1<Group> _function = (Group it) -> {
        it.setVisible(false);
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
  
  @Override
  public void doActivate() {
    Paint _stroke = this.getStroke();
    boolean _equals = Objects.equal(_stroke, null);
    if (_equals) {
      XDiagram _diagram = CoreExtensions.getDiagram(this);
      Paint _connectionPaint = _diagram.getConnectionPaint();
      this.setStroke(_connectionPaint);
    }
    final ChangeListener<Number> _function = (ObservableValue<? extends Number> prop, Number oldVal, Number newVal) -> {
      this.updateShapes();
    };
    this.controlPointListener = _function;
    ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
    InitializingListListener<XControlPoint> _initializingListListener = new InitializingListListener<XControlPoint>();
    final Procedure1<InitializingListListener<XControlPoint>> _function_1 = (InitializingListListener<XControlPoint> it) -> {
      final Procedure1<ListChangeListener.Change<? extends XControlPoint>> _function_2 = (ListChangeListener.Change<? extends XControlPoint> it_1) -> {
        this.updateShapes();
      };
      it.setChange(_function_2);
      final Procedure1<XControlPoint> _function_3 = (XControlPoint it_1) -> {
        ObservableList<XControlPoint> _controlPoints_1 = this.getControlPoints();
        final int index = _controlPoints_1.indexOf(it_1);
        boolean _or = false;
        if ((index == 0)) {
          _or = true;
        } else {
          ObservableList<XControlPoint> _controlPoints_2 = this.getControlPoints();
          int _size = _controlPoints_2.size();
          int _minus = (_size - 1);
          boolean _equals_1 = (index == _minus);
          _or = _equals_1;
        }
        if (_or) {
          it_1.initializeGraphics();
        } else {
          it_1.activate();
        }
        DoubleProperty _layoutXProperty = it_1.layoutXProperty();
        _layoutXProperty.addListener(this.controlPointListener);
        DoubleProperty _layoutYProperty = it_1.layoutYProperty();
        _layoutYProperty.addListener(this.controlPointListener);
      };
      it.setAdd(_function_3);
      final Procedure1<XControlPoint> _function_4 = (XControlPoint it_1) -> {
        DoubleProperty _layoutXProperty = it_1.layoutXProperty();
        _layoutXProperty.removeListener(this.controlPointListener);
        DoubleProperty _layoutYProperty = it_1.layoutYProperty();
        _layoutYProperty.removeListener(this.controlPointListener);
      };
      it.setRemove(_function_4);
    };
    InitializingListListener<XControlPoint> _doubleArrow = ObjectExtensions.<InitializingListListener<XControlPoint>>operator_doubleArrow(_initializingListListener, _function_1);
    CoreExtensions.<XControlPoint>addInitializingListener(_controlPoints, _doubleArrow);
    ObservableList<XConnectionLabel> _labels = this.getLabels();
    final Consumer<XConnectionLabel> _function_2 = (XConnectionLabel it) -> {
      it.activate();
    };
    _labels.forEach(_function_2);
    ConnectionRouter _connectionRouter = this.getConnectionRouter();
    _connectionRouter.activate();
    this.updateShapes();
    ReadOnlyObjectProperty<Parent> _parentProperty = this.parentProperty();
    final ChangeListener<Parent> _function_3 = (ObservableValue<? extends Parent> property, Parent oldValue, Parent newValue) -> {
      boolean _equals_1 = Objects.equal(newValue, null);
      if (_equals_1) {
        XNode _source = this.getSource();
        ObservableList<XConnection> _outgoingConnections = _source.getOutgoingConnections();
        _outgoingConnections.remove(this);
        XNode _target = this.getTarget();
        ObservableList<XConnection> _incomingConnections = _target.getIncomingConnections();
        _incomingConnections.remove(this);
      }
    };
    _parentProperty.addListener(_function_3);
  }
  
  @Override
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
    XConnection.Kind _kind = this.getKind();
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
            while ((curves.size() > numSegments)) {
              CubicCurve _last = IterableExtensions.<CubicCurve>last(curves);
              curves.remove(_last);
            }
            while ((curves.size() < numSegments)) {
              CubicCurve _cubicCurve = new CubicCurve();
              curves.add(_cubicCurve);
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
          int _size_2 = _controlPoints_2.size();
          int _minus_2 = (_size_2 - 1);
          int _modulo_1 = (_minus_2 % 2);
          remainder = _modulo_1;
          if ((remainder == 0)) {
            ObservableList<XControlPoint> _controlPoints_3 = this.getControlPoints();
            int _size_3 = _controlPoints_3.size();
            int _minus_3 = (_size_3 - 1);
            final int numSegments_1 = (_minus_3 / 2);
            ObservableList<Node> _children_1 = this.shapeGroup.getChildren();
            Iterable<QuadCurve> _filter_1 = Iterables.<QuadCurve>filter(_children_1, QuadCurve.class);
            final List<QuadCurve> curves_1 = IterableExtensions.<QuadCurve>toList(_filter_1);
            while ((curves_1.size() > numSegments_1)) {
              QuadCurve _last = IterableExtensions.<QuadCurve>last(curves_1);
              curves_1.remove(_last);
            }
            while ((curves_1.size() < numSegments_1)) {
              QuadCurve _quadCurve = new QuadCurve();
              curves_1.add(_quadCurve);
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
      final Polyline polyline = _elvis;
      ObservableList<Double> _points = polyline.getPoints();
      ObservableList<XControlPoint> _controlPoints_4 = this.getControlPoints();
      final Function1<XControlPoint, List<Double>> _function = (XControlPoint it) -> {
        double _layoutX = it.getLayoutX();
        double _layoutY = it.getLayoutY();
        return Collections.<Double>unmodifiableList(CollectionLiterals.<Double>newArrayList(Double.valueOf(_layoutX), Double.valueOf(_layoutY)));
      };
      List<List<Double>> _map = ListExtensions.<XControlPoint, List<Double>>map(_controlPoints_4, _function);
      Iterable<Double> _flatten = Iterables.<Double>concat(_map);
      _points.setAll(((Double[])Conversions.unwrapArray(_flatten, Double.class)));
      this.setShapes(Collections.<Shape>unmodifiableList(CollectionLiterals.<Shape>newArrayList(polyline)));
    }
    ObservableList<Node> _children_3 = this.controlPointGroup.getChildren();
    ObservableList<XControlPoint> _controlPoints_5 = this.getControlPoints();
    _children_3.setAll(_controlPoints_5);
  }
  
  protected void setShapes(final List<? extends Shape> shapes) {
    ObservableList<Node> _children = this.shapeGroup.getChildren();
    _children.setAll(shapes);
    XDiagram _diagram = CoreExtensions.getDiagram(this);
    boolean _equals = Objects.equal(_diagram, null);
    if (_equals) {
      return;
    }
    XNode _source = this.getSource();
    double _strokeWidth = this.getStrokeWidth();
    double _strokeWidth_1 = this.getStrokeWidth();
    BoundingBox _boundingBox = new BoundingBox(0, 0, _strokeWidth, _strokeWidth_1);
    final Bounds strokeBoundsInRoot = CoreExtensions.localToRootDiagram(_source, _boundingBox);
    double _width = strokeBoundsInRoot.getWidth();
    double _height = strokeBoundsInRoot.getHeight();
    double _plus = (_width + _height);
    final double strokeInRoot = (0.5 * _plus);
    double _strokeWidth_2 = this.getStrokeWidth();
    final double strokeScale = (_strokeWidth_2 / strokeInRoot);
    final Consumer<Shape> _function = (Shape shape) -> {
      shape.setFill(null);
      shape.setStrokeLineCap(StrokeLineCap.ROUND);
      DoubleProperty _strokeWidthProperty = shape.strokeWidthProperty();
      DoubleBinding _multiply = DoubleExpressionExtensions.operator_multiply(this.strokeWidthProperty, strokeScale);
      _strokeWidthProperty.bind(_multiply);
      DoubleProperty _opacityProperty = shape.opacityProperty();
      DoubleProperty _opacityProperty_1 = this.opacityProperty();
      _opacityProperty.bind(_opacityProperty_1);
      ObservableList<Double> _strokeDashArray = shape.getStrokeDashArray();
      ObservableList<Double> _strokeDashArray_1 = this.getStrokeDashArray();
      _strokeDashArray.setAll(_strokeDashArray_1);
      ObjectProperty<Paint> _strokeProperty = shape.strokeProperty();
      _strokeProperty.bind(this.strokeProperty);
      double _strokeDashOffset = this.getStrokeDashOffset();
      shape.setStrokeDashOffset(_strokeDashOffset);
    };
    shapes.forEach(_function);
  }
  
  @Override
  public boolean isSelectable() {
    return this.getIsActive();
  }
  
  @Override
  public void layoutChildren() {
    super.layoutChildren();
    try {
      ConnectionRouter _connectionRouter = this.getConnectionRouter();
      _connectionRouter.calculatePoints();
      ObservableList<XConnectionLabel> _labels = this.getLabels();
      final Consumer<XConnectionLabel> _function = (XConnectionLabel it) -> {
        it.place(false);
      };
      _labels.forEach(_function);
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
        Class<? extends Exception> _class = exc.getClass();
        String _simpleName = _class.getSimpleName();
        String _plus = (_simpleName + " in XConnection.layoutChildren() ");
        String _message = exc.getMessage();
        String _plus_1 = (_plus + _message);
        XConnection.LOG.severe(_plus_1);
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
      XConnection.Kind _kind = this.getKind();
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
      XConnection.Kind _kind = this.getKind();
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
    super.populate(modelElement);
    modelElement.addProperty(sourceProperty, XNode.class);
    modelElement.addProperty(targetProperty, XNode.class);
    modelElement.addProperty(kindProperty, XConnection.Kind.class);
    modelElement.addProperty(controlPointsProperty, XControlPoint.class);
    modelElement.addProperty(labelsProperty, XConnectionLabel.class);
    modelElement.addProperty(sourceArrowHeadProperty, ArrowHead.class);
    modelElement.addProperty(targetArrowHeadProperty, ArrowHead.class);
  }
  
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
  
  private SimpleObjectProperty<XConnection.Kind> kindProperty = new SimpleObjectProperty<XConnection.Kind>(this, "kind",_initKind());
  
  private static final XConnection.Kind _initKind() {
    return XConnection.Kind.POLYLINE;
  }
  
  public XConnection.Kind getKind() {
    return this.kindProperty.get();
  }
  
  public void setKind(final XConnection.Kind kind) {
    this.kindProperty.set(kind);
  }
  
  public ObjectProperty<XConnection.Kind> kindProperty() {
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
  
  private SimpleDoubleProperty strokeDashOffsetProperty = new SimpleDoubleProperty(this, "strokeDashOffset",_initStrokeDashOffset());
  
  private static final double _initStrokeDashOffset() {
    return 0.0;
  }
  
  public double getStrokeDashOffset() {
    return this.strokeDashOffsetProperty.get();
  }
  
  public void setStrokeDashOffset(final double strokeDashOffset) {
    this.strokeDashOffsetProperty.set(strokeDashOffset);
  }
  
  public DoubleProperty strokeDashOffsetProperty() {
    return this.strokeDashOffsetProperty;
  }
  
  private SimpleListProperty<Double> strokeDashArrayProperty = new SimpleListProperty<Double>(this, "strokeDashArray",_initStrokeDashArray());
  
  private static final ObservableList<Double> _initStrokeDashArray() {
    ObservableList<Double> _observableArrayList = FXCollections.<Double>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<Double> getStrokeDashArray() {
    return this.strokeDashArrayProperty.get();
  }
  
  public ListProperty<Double> strokeDashArrayProperty() {
    return this.strokeDashArrayProperty;
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
