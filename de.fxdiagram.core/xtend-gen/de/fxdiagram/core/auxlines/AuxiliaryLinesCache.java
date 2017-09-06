package de.fxdiagram.core.auxlines;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XDiagramContainer;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.auxlines.AuxiliaryLine;
import de.fxdiagram.core.auxlines.AuxiliaryLineMap;
import de.fxdiagram.core.auxlines.NodeLine;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.InitializingListListener;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import de.fxdiagram.core.extensions.Point2DExtensions;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Consumer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AuxiliaryLinesCache {
  private InitializingListListener<XNode> nodesListener;
  
  private InitializingListListener<XConnection> connectionsListener;
  
  private InitializingListListener<XControlPoint> controlPointsListener;
  
  private Map<XShape, ChangeListener<Number>> shape2scalarListener = CollectionLiterals.<XShape, ChangeListener<Number>>newHashMap();
  
  private Map<XNode, ChangeListener<Bounds>> node2boundsListener = CollectionLiterals.<XNode, ChangeListener<Bounds>>newHashMap();
  
  private AuxiliaryLineMap<Bounds> leftMap = new AuxiliaryLineMap<Bounds>();
  
  private AuxiliaryLineMap<Bounds> centerXMap = new AuxiliaryLineMap<Bounds>();
  
  private AuxiliaryLineMap<Bounds> rightMap = new AuxiliaryLineMap<Bounds>();
  
  private AuxiliaryLineMap<Bounds> topMap = new AuxiliaryLineMap<Bounds>();
  
  private AuxiliaryLineMap<Bounds> centerYMap = new AuxiliaryLineMap<Bounds>();
  
  private AuxiliaryLineMap<Bounds> bottomMap = new AuxiliaryLineMap<Bounds>();
  
  private double magnetDist = 10;
  
  public AuxiliaryLinesCache(final XDiagram diagram) {
    InitializingListListener<XNode> _initializingListListener = new InitializingListListener<XNode>();
    final Procedure1<InitializingListListener<XNode>> _function = (InitializingListListener<XNode> it) -> {
      final Procedure1<XNode> _function_1 = (XNode it_1) -> {
        this.watchNode(it_1);
      };
      it.setAdd(_function_1);
      final Procedure1<XNode> _function_2 = (XNode it_1) -> {
        this.unwatchNode(it_1);
      };
      it.setRemove(_function_2);
    };
    InitializingListListener<XNode> _doubleArrow = ObjectExtensions.<InitializingListListener<XNode>>operator_doubleArrow(_initializingListListener, _function);
    this.nodesListener = _doubleArrow;
    InitializingListListener<XConnection> _initializingListListener_1 = new InitializingListListener<XConnection>();
    final Procedure1<InitializingListListener<XConnection>> _function_1 = (InitializingListListener<XConnection> it) -> {
      final Procedure1<XConnection> _function_2 = (XConnection it_1) -> {
        this.watchConnection(it_1);
      };
      it.setAdd(_function_2);
      final Procedure1<XConnection> _function_3 = (XConnection it_1) -> {
        this.unwatchConnection(it_1);
      };
      it.setRemove(_function_3);
    };
    InitializingListListener<XConnection> _doubleArrow_1 = ObjectExtensions.<InitializingListListener<XConnection>>operator_doubleArrow(_initializingListListener_1, _function_1);
    this.connectionsListener = _doubleArrow_1;
    InitializingListListener<XControlPoint> _initializingListListener_2 = new InitializingListListener<XControlPoint>();
    final Procedure1<InitializingListListener<XControlPoint>> _function_2 = (InitializingListListener<XControlPoint> it) -> {
      final Procedure1<XControlPoint> _function_3 = (XControlPoint it_1) -> {
        this.watchControlPoint(it_1);
      };
      it.setAdd(_function_3);
      final Procedure1<XControlPoint> _function_4 = (XControlPoint it_1) -> {
        this.unwatchControlPoint(it_1);
      };
      it.setRemove(_function_4);
    };
    InitializingListListener<XControlPoint> _doubleArrow_2 = ObjectExtensions.<InitializingListListener<XControlPoint>>operator_doubleArrow(_initializingListListener_2, _function_2);
    this.controlPointsListener = _doubleArrow_2;
    this.watchDiagram(diagram);
  }
  
  public Iterable<AuxiliaryLine> getAuxiliaryLines(final XNode node) {
    Iterable<AuxiliaryLine> _xblockexpression = null;
    {
      final Bounds boundsInDiagram = CoreExtensions.localToRootDiagram(node, node.getSnapBounds());
      Iterable<AuxiliaryLine> _atLeastTwo = this.atLeastTwo(this.leftMap.getByPosition(boundsInDiagram.getMinX()));
      double _minX = boundsInDiagram.getMinX();
      double _maxX = boundsInDiagram.getMaxX();
      double _plus = (_minX + _maxX);
      double _multiply = (0.5 * _plus);
      Iterable<AuxiliaryLine> _atLeastTwo_1 = this.atLeastTwo(this.centerXMap.getByPosition(_multiply));
      Iterable<AuxiliaryLine> _plus_1 = Iterables.<AuxiliaryLine>concat(_atLeastTwo, _atLeastTwo_1);
      Iterable<AuxiliaryLine> _atLeastTwo_2 = this.atLeastTwo(this.rightMap.getByPosition(boundsInDiagram.getMaxX()));
      Iterable<AuxiliaryLine> _plus_2 = Iterables.<AuxiliaryLine>concat(_plus_1, _atLeastTwo_2);
      Iterable<AuxiliaryLine> _atLeastTwo_3 = this.atLeastTwo(this.topMap.getByPosition(boundsInDiagram.getMinY()));
      Iterable<AuxiliaryLine> _plus_3 = Iterables.<AuxiliaryLine>concat(_plus_2, _atLeastTwo_3);
      double _minY = boundsInDiagram.getMinY();
      double _maxY = boundsInDiagram.getMaxY();
      double _plus_4 = (_minY + _maxY);
      double _multiply_1 = (0.5 * _plus_4);
      Iterable<AuxiliaryLine> _atLeastTwo_4 = this.atLeastTwo(this.centerYMap.getByPosition(_multiply_1));
      Iterable<AuxiliaryLine> _plus_5 = Iterables.<AuxiliaryLine>concat(_plus_3, _atLeastTwo_4);
      Iterable<AuxiliaryLine> _atLeastTwo_5 = this.atLeastTwo(this.bottomMap.getByPosition(boundsInDiagram.getMaxY()));
      _xblockexpression = Iterables.<AuxiliaryLine>concat(_plus_5, _atLeastTwo_5);
    }
    return _xblockexpression;
  }
  
  public Iterable<AuxiliaryLine> getAuxiliaryLines(final XControlPoint point) {
    Iterable<AuxiliaryLine> _xblockexpression = null;
    {
      final Point2D centerInDiagram = CoreExtensions.localToRootDiagram(point, BoundsExtensions.center(point.getBoundsInLocal()));
      Iterable<AuxiliaryLine> _atLeastTwo = this.atLeastTwo(this.centerXMap.getByPosition(centerInDiagram.getX()));
      Iterable<AuxiliaryLine> _atLeastTwo_1 = this.atLeastTwo(this.centerYMap.getByPosition(centerInDiagram.getY()));
      _xblockexpression = Iterables.<AuxiliaryLine>concat(_atLeastTwo, _atLeastTwo_1);
    }
    return _xblockexpression;
  }
  
  protected Iterable<AuxiliaryLine> atLeastTwo(final Iterable<AuxiliaryLine> lines) {
    Iterable<AuxiliaryLine> _xifexpression = null;
    int _size = IterableExtensions.size(lines);
    boolean _lessThan = (_size < 2);
    if (_lessThan) {
      _xifexpression = CollectionLiterals.<AuxiliaryLine>emptyList();
    } else {
      _xifexpression = lines;
    }
    return _xifexpression;
  }
  
  public Point2D getSnappedPosition(final XNode node, final Point2D newPositionInDiagram) {
    final Bounds boundsInDiagram = CoreExtensions.localToDiagram(node, node.getSnapBounds());
    double _x = newPositionInDiagram.getX();
    double _layoutX = node.getLayoutX();
    double _minus = (_x - _layoutX);
    double _minX = boundsInDiagram.getMinX();
    double _plus = (_minus + _minX);
    double _y = newPositionInDiagram.getY();
    double _layoutY = node.getLayoutY();
    double _minus_1 = (_y - _layoutY);
    double _minY = boundsInDiagram.getMinY();
    double _plus_1 = (_minus_1 + _minY);
    double _width = boundsInDiagram.getWidth();
    double _height = boundsInDiagram.getHeight();
    final BoundingBox newBoundsInDiagram = new BoundingBox(_plus, _plus_1, _width, _height);
    final Bounds boundsInRootDiagram = CoreExtensions.localToRootDiagram(CoreExtensions.getDiagram(node), newBoundsInDiagram);
    final HashSet<XShape> excluded = CollectionLiterals.<XShape>newHashSet();
    excluded.add(node);
    final Consumer<XConnection> _function = (XConnection it) -> {
      if ((Objects.equal(it.getKind(), XConnection.Kind.RECTILINEAR) && (it.getControlPoints().size() > 2))) {
        XControlPoint _get = it.getControlPoints().get(1);
        excluded.add(_get);
      }
    };
    node.getOutgoingConnections().forEach(_function);
    final Consumer<XConnection> _function_1 = (XConnection it) -> {
      if ((Objects.equal(it.getKind(), XConnection.Kind.RECTILINEAR) && (it.getControlPoints().size() > 2))) {
        ObservableList<XControlPoint> _controlPoints = it.getControlPoints();
        int _size = it.getControlPoints().size();
        int _minus_2 = (_size - 2);
        XControlPoint _get = _controlPoints.get(_minus_2);
        excluded.add(_get);
      }
    };
    node.getIncomingConnections().forEach(_function_1);
    double dx = this.magnetDist;
    double _minX_1 = boundsInRootDiagram.getMinX();
    double _maxX = boundsInRootDiagram.getMaxX();
    double _plus_2 = (_minX_1 + _maxX);
    double _multiply = (0.5 * _plus_2);
    dx = this.centerXMap.getNearestLineDelta(_multiply, dx, excluded);
    dx = this.leftMap.getNearestLineDelta(boundsInRootDiagram.getMinX(), dx, excluded);
    dx = this.rightMap.getNearestLineDelta(boundsInRootDiagram.getMaxX(), dx, excluded);
    double dy = this.magnetDist;
    double _minY_1 = boundsInRootDiagram.getMinY();
    double _maxY = boundsInRootDiagram.getMaxY();
    double _plus_3 = (_minY_1 + _maxY);
    double _multiply_1 = (0.5 * _plus_3);
    dy = this.centerYMap.getNearestLineDelta(_multiply_1, dy, excluded);
    dy = this.topMap.getNearestLineDelta(boundsInRootDiagram.getMinY(), dy, excluded);
    dy = this.bottomMap.getNearestLineDelta(boundsInRootDiagram.getMaxY(), dy, excluded);
    return this.toLocal(dx, dy, newPositionInDiagram, node);
  }
  
  public Point2D getSnappedPosition(final XControlPoint point, final Point2D newPositionInDiagram) {
    final Point2D centerInDiagram = CoreExtensions.localToRootDiagram(point.getParent(), newPositionInDiagram);
    final double dx = this.centerXMap.getNearestLineDelta(centerInDiagram.getX(), this.magnetDist, Collections.<XShape>unmodifiableSet(CollectionLiterals.<XShape>newHashSet(point)));
    final double dy = this.centerYMap.getNearestLineDelta(centerInDiagram.getY(), this.magnetDist, Collections.<XShape>unmodifiableSet(CollectionLiterals.<XShape>newHashSet(point)));
    return this.toLocal(dx, dy, newPositionInDiagram, point);
  }
  
  protected Point2D toLocal(final double dx, final double dy, final Point2D newPointInDiagram, final XShape shape) {
    double _xifexpression = (double) 0;
    if ((dx >= this.magnetDist)) {
      _xifexpression = 0;
    } else {
      _xifexpression = dx;
    }
    final double deltaX = _xifexpression;
    double _xifexpression_1 = (double) 0;
    if ((dy >= this.magnetDist)) {
      _xifexpression_1 = 0;
    } else {
      _xifexpression_1 = dy;
    }
    final double deltaY = _xifexpression_1;
    final Point2D delta = new Point2D(deltaX, deltaY);
    double _norm = Point2DExtensions.norm(delta);
    boolean _lessThan = (_norm < NumberExpressionExtensions.EPSILON);
    if (_lessThan) {
      return newPointInDiagram;
    }
    final Point2D deltaLocal = shape.getParent().sceneToLocal(CoreExtensions.getRootDiagram(shape).localToScene(delta));
    double _x = newPointInDiagram.getX();
    double _x_1 = deltaLocal.getX();
    double _plus = (_x + _x_1);
    double _y = newPointInDiagram.getY();
    double _y_1 = deltaLocal.getY();
    double _plus_1 = (_y + _y_1);
    return new Point2D(_plus, _plus_1);
  }
  
  protected void watchDiagram(final XDiagram diagram) {
    CoreExtensions.<XNode>addInitializingListener(diagram.getNodes(), this.nodesListener);
    CoreExtensions.<XConnection>addInitializingListener(diagram.getConnections(), this.connectionsListener);
  }
  
  protected void updateDiagram(final XDiagram diagram) {
    final Consumer<XNode> _function = (XNode it) -> {
      this.updateNode(it);
    };
    diagram.getNodes().forEach(_function);
    final Function1<XConnection, ObservableList<XControlPoint>> _function_1 = (XConnection it) -> {
      return it.getControlPoints();
    };
    final Consumer<XControlPoint> _function_2 = (XControlPoint it) -> {
      this.updateControlPoint(it);
    };
    Iterables.<XControlPoint>concat(ListExtensions.<XConnection, ObservableList<XControlPoint>>map(diagram.getConnections(), _function_1)).forEach(_function_2);
  }
  
  protected void unwatchDiagram(final XDiagram diagram) {
    CoreExtensions.<XNode>removeInitializingListener(diagram.getNodes(), this.nodesListener);
    CoreExtensions.<XConnection>removeInitializingListener(diagram.getConnections(), this.connectionsListener);
  }
  
  protected void watchNode(final XNode node) {
    final ChangeListener<Number> _function = (ObservableValue<? extends Number> scalar, Number oldValue, Number newValue) -> {
      this.updateNode(node);
      if ((node instanceof XDiagramContainer)) {
        boolean _isInnerDiagramActive = ((XDiagramContainer)node).isInnerDiagramActive();
        if (_isInnerDiagramActive) {
          this.updateDiagram(((XDiagramContainer)node).getInnerDiagram());
        }
      }
    };
    final ChangeListener<Number> scalarListener = _function;
    final ChangeListener<Bounds> _function_1 = (ObservableValue<? extends Bounds> scalar, Bounds oldValue, Bounds newValue) -> {
      this.updateNode(node);
      if ((node instanceof XDiagramContainer)) {
        boolean _isInnerDiagramActive = ((XDiagramContainer)node).isInnerDiagramActive();
        if (_isInnerDiagramActive) {
          this.updateDiagram(((XDiagramContainer)node).getInnerDiagram());
        }
      }
    };
    final ChangeListener<Bounds> boundsListener = _function_1;
    node.layoutXProperty().addListener(scalarListener);
    node.layoutYProperty().addListener(scalarListener);
    node.boundsInLocalProperty().addListener(boundsListener);
    this.shape2scalarListener.put(node, scalarListener);
    this.node2boundsListener.put(node, boundsListener);
    this.updateNode(node);
    if ((node instanceof XDiagramContainer)) {
      boolean _isInnerDiagramActive = ((XDiagramContainer)node).isInnerDiagramActive();
      if (_isInnerDiagramActive) {
        this.watchDiagram(((XDiagramContainer)node).getInnerDiagram());
      }
    }
  }
  
  protected void updateNode(final XNode node) {
    final Bounds boundsInDiagram = CoreExtensions.localToRootDiagram(node, node.getSnapBounds());
    double _minX = boundsInDiagram.getMinX();
    NodeLine _nodeLine = new NodeLine(_minX, 
      Orientation.VERTICAL, node, boundsInDiagram);
    this.leftMap.add(_nodeLine);
    double _minX_1 = boundsInDiagram.getMinX();
    double _maxX = boundsInDiagram.getMaxX();
    double _plus = (_minX_1 + _maxX);
    double _multiply = (0.5 * _plus);
    NodeLine _nodeLine_1 = new NodeLine(_multiply, 
      Orientation.VERTICAL, node, boundsInDiagram);
    this.centerXMap.add(_nodeLine_1);
    double _maxX_1 = boundsInDiagram.getMaxX();
    NodeLine _nodeLine_2 = new NodeLine(_maxX_1, 
      Orientation.VERTICAL, node, boundsInDiagram);
    this.rightMap.add(_nodeLine_2);
    double _minY = boundsInDiagram.getMinY();
    NodeLine _nodeLine_3 = new NodeLine(_minY, 
      Orientation.HORIZONTAL, node, boundsInDiagram);
    this.topMap.add(_nodeLine_3);
    double _minY_1 = boundsInDiagram.getMinY();
    double _maxY = boundsInDiagram.getMaxY();
    double _plus_1 = (_minY_1 + _maxY);
    double _multiply_1 = (0.5 * _plus_1);
    NodeLine _nodeLine_4 = new NodeLine(_multiply_1, 
      Orientation.HORIZONTAL, node, boundsInDiagram);
    this.centerYMap.add(_nodeLine_4);
    double _maxY_1 = boundsInDiagram.getMaxY();
    NodeLine _nodeLine_5 = new NodeLine(_maxY_1, 
      Orientation.HORIZONTAL, node, boundsInDiagram);
    this.bottomMap.add(_nodeLine_5);
  }
  
  protected void unwatchNode(final XNode node) {
    if ((node instanceof XDiagramContainer)) {
      boolean _isInnerDiagramActive = ((XDiagramContainer)node).isInnerDiagramActive();
      if (_isInnerDiagramActive) {
        this.unwatchDiagram(((XDiagramContainer)node).getInnerDiagram());
      }
    }
    this.leftMap.removeByShape(node);
    this.centerXMap.removeByShape(node);
    this.rightMap.removeByShape(node);
    this.topMap.removeByShape(node);
    this.centerYMap.removeByShape(node);
    this.bottomMap.removeByShape(node);
    final ChangeListener<Bounds> boundsListener = this.node2boundsListener.remove(node);
    node.boundsInLocalProperty().removeListener(boundsListener);
    final ChangeListener<Number> scalarListener = this.shape2scalarListener.remove(node);
    node.layoutXProperty().removeListener(scalarListener);
    node.layoutYProperty().removeListener(scalarListener);
  }
  
  protected void watchConnection(final XConnection connection) {
    CoreExtensions.<XControlPoint>addInitializingListener(connection.getControlPoints(), this.controlPointsListener);
  }
  
  protected void unwatchConnection(final XConnection connection) {
    CoreExtensions.<XControlPoint>removeInitializingListener(connection.getControlPoints(), this.controlPointsListener);
  }
  
  protected void watchControlPoint(final XControlPoint controlPoint) {
    final ChangeListener<Number> _function = (ObservableValue<? extends Number> scalar, Number oldValue, Number newValue) -> {
      this.updateControlPoint(controlPoint);
    };
    final ChangeListener<Number> scalarListener = _function;
    controlPoint.layoutXProperty().addListener(scalarListener);
    controlPoint.layoutYProperty().addListener(scalarListener);
    final ChangeListener<XControlPoint.Type> _function_1 = (ObservableValue<? extends XControlPoint.Type> p, XControlPoint.Type o, XControlPoint.Type n) -> {
      boolean _equals = Objects.equal(n, XControlPoint.Type.ANCHOR);
      if (_equals) {
        this.centerXMap.removeByShape(controlPoint);
      } else {
        this.updateControlPoint(controlPoint);
      }
    };
    controlPoint.typeProperty().addListener(_function_1);
    this.shape2scalarListener.put(controlPoint, scalarListener);
    this.updateControlPoint(controlPoint);
  }
  
  protected void updateControlPoint(final XControlPoint point) {
    XControlPoint.Type _type = point.getType();
    boolean _notEquals = (!Objects.equal(_type, XControlPoint.Type.ANCHOR));
    if (_notEquals) {
      final Bounds boundsInDiagram = CoreExtensions.localToRootDiagram(point, point.getBoundsInLocal());
      double _x = BoundsExtensions.center(boundsInDiagram).getX();
      NodeLine _nodeLine = new NodeLine(_x, 
        Orientation.VERTICAL, point, boundsInDiagram);
      this.centerXMap.add(_nodeLine);
      double _y = BoundsExtensions.center(boundsInDiagram).getY();
      NodeLine _nodeLine_1 = new NodeLine(_y, 
        Orientation.HORIZONTAL, point, boundsInDiagram);
      this.centerYMap.add(_nodeLine_1);
    }
  }
  
  protected void unwatchControlPoint(final XControlPoint point) {
    this.centerXMap.removeByShape(point);
    this.centerYMap.removeByShape(point);
    final ChangeListener<Number> scalarListener = this.shape2scalarListener.remove(point);
    boolean _notEquals = (!Objects.equal(scalarListener, null));
    if (_notEquals) {
      point.layoutXProperty().removeListener(scalarListener);
      point.layoutYProperty().removeListener(scalarListener);
    }
  }
}
