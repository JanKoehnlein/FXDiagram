package de.fxdiagram.core.anchors;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.ArrowHead;
import de.fxdiagram.core.anchors.ControlPointCorrector;
import de.fxdiagram.core.anchors.ManhattanRouter;
import de.fxdiagram.core.anchors.SplineShapeKeeper;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.InitializingListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

@Logging
@SuppressWarnings("all")
public class ConnectionRouter implements XActivatable {
  private XConnection connection;
  
  private ChangeListener<Number> scalarListener;
  
  private ChangeListener<Bounds> boundsListener;
  
  private InitializingListener<XNode> connectionEndListener;
  
  private SplineShapeKeeper shapeKeeper;
  
  private ControlPointCorrector controlPointCorrector;
  
  private ManhattanRouter manhattanRouter;
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private boolean splineShapeKeeperEnabled = false;
  
  private double selfEdgeDist = 60;
  
  public ConnectionRouter(final XConnection connection) {
    this.connection = connection;
    final ChangeListener<Number> _function = (ObservableValue<? extends Number> prop, Number oldVal, Number newVal) -> {
      connection.requestLayout();
    };
    this.scalarListener = _function;
    final ChangeListener<Bounds> _function_1 = (ObservableValue<? extends Bounds> prop, Bounds oldVal, Bounds newVal) -> {
      connection.requestLayout();
    };
    this.boundsListener = _function_1;
    SplineShapeKeeper _splineShapeKeeper = new SplineShapeKeeper(connection);
    this.shapeKeeper = _splineShapeKeeper;
    InitializingListener<XNode> _initializingListener = new InitializingListener<XNode>();
    final Procedure1<InitializingListener<XNode>> _function_2 = (InitializingListener<XNode> it) -> {
      final Procedure1<XNode> _function_3 = (XNode it_1) -> {
        this.bindNode(it_1);
      };
      it.setSet(_function_3);
      final Procedure1<XNode> _function_4 = (XNode it_1) -> {
        this.unbindNode(it_1);
        this.shapeKeeper.reset();
      };
      it.setUnset(_function_4);
    };
    InitializingListener<XNode> _doubleArrow = ObjectExtensions.<InitializingListener<XNode>>operator_doubleArrow(_initializingListener, _function_2);
    this.connectionEndListener = _doubleArrow;
    ControlPointCorrector _controlPointCorrector = new ControlPointCorrector();
    this.controlPointCorrector = _controlPointCorrector;
  }
  
  public ObservableList<XControlPoint> getControlPoints() {
    return this.connection.getControlPoints();
  }
  
  @Override
  public void activate() {
    CoreExtensions.<XNode>addInitializingListener(this.connection.sourceProperty(), this.connectionEndListener);
    CoreExtensions.<XNode>addInitializingListener(this.connection.targetProperty(), this.connectionEndListener);
    this.isActiveProperty.set(true);
    final ChangeListener<XConnection.Kind> _function = (ObservableValue<? extends XConnection.Kind> p, XConnection.Kind o, XConnection.Kind n) -> {
      this.manhattanRouter = null;
    };
    this.connection.kindProperty().addListener(_function);
  }
  
  protected void bindNode(final XNode host) {
    Node current = host.getNode();
    current.layoutBoundsProperty().addListener(this.boundsListener);
    while (((!Objects.equal(current, null)) && (!CoreExtensions.isRootDiagram(current)))) {
      {
        current.boundsInParentProperty().addListener(this.boundsListener);
        current = current.getParent();
      }
    }
  }
  
  protected void unbindNode(final XNode host) {
    Node current = host.getNode();
    current.layoutBoundsProperty().removeListener(this.boundsListener);
    while (((!Objects.equal(current, null)) && (!CoreExtensions.isRootDiagram(current)))) {
      {
        current.boundsInParentProperty().removeListener(this.boundsListener);
        current = current.getParent();
      }
    }
  }
  
  public boolean setSplineShapeKeeperEnabled(final boolean isEnabled) {
    boolean _xblockexpression = false;
    {
      this.shapeKeeper.reset();
      _xblockexpression = this.splineShapeKeeperEnabled = isEnabled;
    }
    return _xblockexpression;
  }
  
  public void growToSize(final int newSize) {
    int _size = this.getControlPoints().size();
    boolean _lessThan = (_size < 2);
    if (_lessThan) {
      this.calculatePoints();
    }
    int _size_1 = this.getControlPoints().size();
    final int nodeDiff = (newSize - _size_1);
    if ((nodeDiff > 0)) {
      final ArrayList<XControlPoint> newControlPoints = CollectionLiterals.<XControlPoint>newArrayList();
      final double delta = (1.0 / (nodeDiff + 1));
      ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
      int _size_2 = this.getControlPoints().size();
      int _minus = (_size_2 - 2);
      final XControlPoint first = _controlPoints.get(_minus);
      final XControlPoint last = IterableExtensions.<XControlPoint>last(this.getControlPoints());
      IntegerRange _upTo = new IntegerRange(1, nodeDiff);
      for (final Integer i : _upTo) {
        {
          XControlPoint _xControlPoint = new XControlPoint();
          final Procedure1<XControlPoint> _function = (XControlPoint it) -> {
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
          };
          final XControlPoint newControlPoint = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint, _function);
          newControlPoint.layoutXProperty().addListener(this.scalarListener);
          newControlPoint.layoutYProperty().addListener(this.scalarListener);
          newControlPoints.add(newControlPoint);
        }
      }
      ObservableList<XControlPoint> _controlPoints_1 = this.getControlPoints();
      int _size_3 = this.getControlPoints().size();
      int _minus_1 = (_size_3 - 1);
      _controlPoints_1.addAll(_minus_1, newControlPoints);
    }
    this.resetPointTypes();
  }
  
  public void shrinkToSize(final int newSize) {
    int _size = this.getControlPoints().size();
    boolean _lessThan = (_size < 2);
    if (_lessThan) {
      this.calculatePoints();
    }
    int _size_1 = this.getControlPoints().size();
    final int nodeDiff = (newSize - _size_1);
    if ((nodeDiff < 0)) {
      final ArrayList<XControlPoint> toBeRemoved = CollectionLiterals.<XControlPoint>newArrayList();
      int _size_2 = this.getControlPoints().size();
      int _minus = (_size_2 - 1);
      int _size_3 = this.getControlPoints().size();
      int _plus = (_size_3 + nodeDiff);
      int _minus_1 = (_plus - 1);
      ExclusiveRange _greaterThanDoubleDot = new ExclusiveRange(_minus, _minus_1, false);
      for (final Integer i : _greaterThanDoubleDot) {
        {
          final XControlPoint removeMe = this.getControlPoints().get((i).intValue());
          removeMe.layoutXProperty().removeListener(this.scalarListener);
          removeMe.layoutYProperty().removeListener(this.scalarListener);
          toBeRemoved.add(removeMe);
        }
      }
      this.getControlPoints().removeAll(toBeRemoved);
    }
    this.resetPointTypes();
  }
  
  protected void resetPointTypes() {
    int _size = this.getControlPoints().size();
    boolean _lessThan = (_size < 2);
    if (_lessThan) {
      return;
    }
    XControlPoint _head = IterableExtensions.<XControlPoint>head(this.getControlPoints());
    _head.setType(XControlPoint.Type.ANCHOR);
    XControlPoint _last = IterableExtensions.<XControlPoint>last(this.getControlPoints());
    _last.setType(XControlPoint.Type.ANCHOR);
    int _size_1 = this.getControlPoints().size();
    int _minus = (_size_1 - 1);
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(1, _minus, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        final XControlPoint currentPoint = this.getControlPoints().get((i).intValue());
        XControlPoint.Type _switchResult = null;
        XConnection.Kind _kind = this.connection.getKind();
        if (_kind != null) {
          switch (_kind) {
            case POLYLINE:
            case RECTILINEAR:
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
  
  public ManhattanRouter getManhattanRouter() {
    ManhattanRouter _elvis = null;
    if (this.manhattanRouter != null) {
      _elvis = this.manhattanRouter;
    } else {
      ManhattanRouter _manhattanRouter = new ManhattanRouter(this.connection);
      ManhattanRouter _manhattanRouter_1 = (this.manhattanRouter = _manhattanRouter);
      _elvis = _manhattanRouter_1;
    }
    return _elvis;
  }
  
  public void calculatePoints() {
    if (((this.getControlPoints().size() < 2) || Objects.equal(this.connection.getKind(), XConnection.Kind.RECTILINEAR))) {
      ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
      for (final XControlPoint controlPoint : _controlPoints) {
        {
          controlPoint.layoutXProperty().removeListener(this.scalarListener);
          controlPoint.layoutYProperty().removeListener(this.scalarListener);
        }
      }
      XNode _source = this.connection.getSource();
      XNode _target = this.connection.getTarget();
      boolean _equals = Objects.equal(_source, _target);
      if (_equals) {
        this.calculateSelfEdge();
      }
    }
    XConnection.Kind _kind = this.connection.getKind();
    boolean _equals_1 = Objects.equal(_kind, XConnection.Kind.RECTILINEAR);
    if (_equals_1) {
      this.getManhattanRouter().calculatePoints();
      return;
    }
    if (((this.splineShapeKeeperEnabled && (!Objects.equal(this.connection.getKind(), XConnection.Kind.POLYLINE))) && (!Objects.equal(this.connection.getKind(), XConnection.Kind.RECTILINEAR)))) {
      this.shapeKeeper.adjustControlPointsToNodeMove();
    }
    final Point2D sourcePoint = this.findClosestSourceAnchor(this.connection.getSource(), true);
    final Point2D targetPoint = this.findClosestTargetAnchor(this.connection.getTarget(), true);
    if (((!Objects.equal(sourcePoint, null)) && (!Objects.equal(targetPoint, null)))) {
      int _size = this.getControlPoints().size();
      boolean _lessThan = (_size < 2);
      if (_lessThan) {
        XControlPoint _xControlPoint = new XControlPoint();
        final Procedure1<XControlPoint> _function = (XControlPoint it) -> {
          it.setLayoutX(sourcePoint.getX());
          it.setLayoutY(sourcePoint.getY());
          it.setType(XControlPoint.Type.ANCHOR);
        };
        XControlPoint _doubleArrow = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint, _function);
        XControlPoint _xControlPoint_1 = new XControlPoint();
        final Procedure1<XControlPoint> _function_1 = (XControlPoint it) -> {
          it.setLayoutX(targetPoint.getX());
          it.setLayoutY(targetPoint.getY());
          it.setType(XControlPoint.Type.ANCHOR);
        };
        XControlPoint _doubleArrow_1 = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint_1, _function_1);
        this.getControlPoints().setAll(
          Collections.<XControlPoint>unmodifiableList(CollectionLiterals.<XControlPoint>newArrayList(_doubleArrow, _doubleArrow_1)));
        XConnection.Kind _kind_1 = this.connection.getKind();
        if (_kind_1 != null) {
          switch (_kind_1) {
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
        ObservableList<XControlPoint> _controlPoints_1 = this.getControlPoints();
        for (final XControlPoint controlPoint_1 : _controlPoints_1) {
          {
            controlPoint_1.layoutXProperty().addListener(this.scalarListener);
            controlPoint_1.layoutYProperty().addListener(this.scalarListener);
          }
        }
      } else {
        XControlPoint _head = IterableExtensions.<XControlPoint>head(this.getControlPoints());
        final Procedure1<XControlPoint> _function_2 = (XControlPoint it) -> {
          CoreExtensions.<Number>setSafely(it.layoutXProperty(), Double.valueOf(sourcePoint.getX()));
          CoreExtensions.<Number>setSafely(it.layoutYProperty(), Double.valueOf(sourcePoint.getY()));
        };
        ObjectExtensions.<XControlPoint>operator_doubleArrow(_head, _function_2);
        XControlPoint _last = IterableExtensions.<XControlPoint>last(this.getControlPoints());
        final Procedure1<XControlPoint> _function_3 = (XControlPoint it) -> {
          CoreExtensions.<Number>setSafely(it.layoutXProperty(), Double.valueOf(targetPoint.getX()));
          CoreExtensions.<Number>setSafely(it.layoutYProperty(), Double.valueOf(targetPoint.getY()));
        };
        ObjectExtensions.<XControlPoint>operator_doubleArrow(_last, _function_3);
      }
      final Consumer<XControlPoint> _function_4 = (XControlPoint it) -> {
        it.update(this.getControlPoints());
      };
      this.getControlPoints().forEach(_function_4);
    }
    this.controlPointCorrector.correctControlPoints(this.connection);
  }
  
  protected boolean calculateSelfEdge() {
    boolean _xblockexpression = false;
    {
      final Bounds nodeSnapBounds = this.connection.getSource().getSnapBounds();
      Bounds _elvis = null;
      Bounds _localToRootDiagram = CoreExtensions.localToRootDiagram(this.connection.getSource(), nodeSnapBounds);
      if (_localToRootDiagram != null) {
        _elvis = _localToRootDiagram;
      } else {
        _elvis = nodeSnapBounds;
      }
      final Bounds boundsInDiagram = _elvis;
      this.getControlPoints().clear();
      ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
      XControlPoint _xControlPoint = new XControlPoint();
      final Procedure1<XControlPoint> _function = (XControlPoint it) -> {
        it.setType(XControlPoint.Type.ANCHOR);
      };
      XControlPoint _doubleArrow = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint, _function);
      _controlPoints.add(_doubleArrow);
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
            ObservableList<XControlPoint> _controlPoints_1 = this.getControlPoints();
            XControlPoint _xControlPoint_1 = new XControlPoint();
            final Procedure1<XControlPoint> _function_1 = (XControlPoint it) -> {
              double _minX = boundsInDiagram.getMinX();
              double _minus = (_minX - deltaX);
              it.setLayoutX(_minus);
              double _minY = boundsInDiagram.getMinY();
              double _plus = (_minY + deltaY);
              it.setLayoutY(_plus);
              it.setType(XControlPoint.Type.INTERPOLATED);
            };
            XControlPoint _doubleArrow_1 = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint_1, _function_1);
            _controlPoints_1.add(_doubleArrow_1);
            ObservableList<XControlPoint> _controlPoints_2 = this.getControlPoints();
            XControlPoint _xControlPoint_2 = new XControlPoint();
            final Procedure1<XControlPoint> _function_2 = (XControlPoint it) -> {
              double _minX = boundsInDiagram.getMinX();
              double _minus = (_minX - deltaX);
              it.setLayoutX(_minus);
              double _minY = boundsInDiagram.getMinY();
              double _minus_1 = (_minY - deltaY);
              it.setLayoutY(_minus_1);
              it.setType(XControlPoint.Type.INTERPOLATED);
            };
            XControlPoint _doubleArrow_2 = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint_2, _function_2);
            _controlPoints_2.add(_doubleArrow_2);
            ObservableList<XControlPoint> _controlPoints_3 = this.getControlPoints();
            XControlPoint _xControlPoint_3 = new XControlPoint();
            final Procedure1<XControlPoint> _function_3 = (XControlPoint it) -> {
              double _minX = boundsInDiagram.getMinX();
              double _plus = (_minX + deltaX);
              it.setLayoutX(_plus);
              double _minY = boundsInDiagram.getMinY();
              double _minus = (_minY - deltaY);
              it.setLayoutY(_minus);
              it.setType(XControlPoint.Type.INTERPOLATED);
            };
            XControlPoint _doubleArrow_3 = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint_3, _function_3);
            _controlPoints_3.add(_doubleArrow_3);
            break;
          case CUBIC_CURVE:
            ObservableList<XControlPoint> _controlPoints_4 = this.getControlPoints();
            XControlPoint _xControlPoint_4 = new XControlPoint();
            final Procedure1<XControlPoint> _function_4 = (XControlPoint it) -> {
              double _minX = boundsInDiagram.getMinX();
              double _minus = (_minX - this.selfEdgeDist);
              it.setLayoutX(_minus);
              double _minY = boundsInDiagram.getMinY();
              double _plus = (_minY + (0.3 * this.selfEdgeDist));
              it.setLayoutY(_plus);
              it.setType(XControlPoint.Type.CONTROL_POINT);
            };
            XControlPoint _doubleArrow_4 = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint_4, _function_4);
            _controlPoints_4.add(_doubleArrow_4);
            ObservableList<XControlPoint> _controlPoints_5 = this.getControlPoints();
            XControlPoint _xControlPoint_5 = new XControlPoint();
            final Procedure1<XControlPoint> _function_5 = (XControlPoint it) -> {
              double _minX = boundsInDiagram.getMinX();
              double _plus = (_minX + (0.3 * this.selfEdgeDist));
              it.setLayoutX(_plus);
              double _minY = boundsInDiagram.getMinY();
              double _minus = (_minY - this.selfEdgeDist);
              it.setLayoutY(_minus);
              it.setType(XControlPoint.Type.CONTROL_POINT);
            };
            XControlPoint _doubleArrow_5 = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint_5, _function_5);
            _controlPoints_5.add(_doubleArrow_5);
            break;
          default:
            break;
        }
      }
      ObservableList<XControlPoint> _controlPoints_6 = this.getControlPoints();
      XControlPoint _xControlPoint_6 = new XControlPoint();
      final Procedure1<XControlPoint> _function_6 = (XControlPoint it) -> {
        it.setType(XControlPoint.Type.ANCHOR);
      };
      XControlPoint _doubleArrow_6 = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint_6, _function_6);
      _xblockexpression = _controlPoints_6.add(_doubleArrow_6);
    }
    return _xblockexpression;
  }
  
  public Point2D findClosestSourceAnchor(final XNode source, final boolean correctArrowHead) {
    Point2D _xblockexpression = null;
    {
      ArrowHead _xifexpression = null;
      if (correctArrowHead) {
        _xifexpression = this.connection.getSourceArrowHead();
      } else {
        _xifexpression = null;
      }
      final ArrowHead arrowHead = _xifexpression;
      Point2D _xifexpression_1 = null;
      int _size = this.getControlPoints().size();
      boolean _lessEqualsThan = (_size <= 2);
      if (_lessEqualsThan) {
        _xifexpression_1 = this.getNearestAnchor(source, this.midPoint(this.connection.getTarget()), arrowHead);
      } else {
        _xifexpression_1 = this.getNearestAnchor(source, this.getControlPoints().get(1), arrowHead);
      }
      _xblockexpression = _xifexpression_1;
    }
    return _xblockexpression;
  }
  
  public Point2D findClosestTargetAnchor(final XNode target, final boolean correctArrowHead) {
    Point2D _xblockexpression = null;
    {
      ArrowHead _xifexpression = null;
      if (correctArrowHead) {
        _xifexpression = this.connection.getTargetArrowHead();
      } else {
        _xifexpression = null;
      }
      final ArrowHead arrowHead = _xifexpression;
      Point2D _xifexpression_1 = null;
      int _size = this.getControlPoints().size();
      boolean _lessEqualsThan = (_size <= 2);
      if (_lessEqualsThan) {
        _xifexpression_1 = this.getNearestAnchor(target, this.midPoint(this.connection.getSource()), arrowHead);
      } else {
        ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
        int _size_1 = this.getControlPoints().size();
        int _minus = (_size_1 - 2);
        _xifexpression_1 = this.getNearestAnchor(target, _controlPoints.get(_minus), arrowHead);
      }
      _xblockexpression = _xifexpression_1;
    }
    return _xblockexpression;
  }
  
  protected Point2D midPoint(final XNode node) {
    return CoreExtensions.localToRootDiagram(node.getNode(), BoundsExtensions.center(node.getNode().getBoundsInLocal()));
  }
  
  protected Point2D getNearestAnchor(final XNode node, final XControlPoint controlPoint, final ArrowHead arrowHead) {
    return this.getNearestAnchor(node, controlPoint.getLayoutX(), controlPoint.getLayoutY(), arrowHead);
  }
  
  public Point2D getNearestAnchor(final XNode node, final Point2D point, final ArrowHead arrowHead) {
    Point2D _xblockexpression = null;
    {
      boolean _equals = Objects.equal(point, null);
      if (_equals) {
        return BoundsExtensions.center(node.getNode().getBoundsInLocal());
      }
      _xblockexpression = this.getNearestAnchor(node, point.getX(), point.getY(), arrowHead);
    }
    return _xblockexpression;
  }
  
  protected Point2D getNearestAnchor(final XNode node, final double x, final double y, final ArrowHead arrowHead) {
    Point2D _xblockexpression = null;
    {
      final Point2D anchor = node.getAnchors().getAnchor(x, y);
      Point2D _xifexpression = null;
      if (((!Objects.equal(anchor, null)) && (!Objects.equal(arrowHead, null)))) {
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
  
  @Pure
  public boolean isSplineShapeKeeperEnabled() {
    return this.splineShapeKeeperEnabled;
  }
}
