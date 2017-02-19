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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
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
      Bounds _snapBounds = node.getSnapBounds();
      final Bounds boundsInDiagram = CoreExtensions.localToRootDiagram(node, _snapBounds);
      double _minX = boundsInDiagram.getMinX();
      Collection<AuxiliaryLine> _byPosition = this.leftMap.getByPosition(_minX);
      Iterable<AuxiliaryLine> _atLeastTwo = this.atLeastTwo(_byPosition);
      double _minX_1 = boundsInDiagram.getMinX();
      double _maxX = boundsInDiagram.getMaxX();
      double _plus = (_minX_1 + _maxX);
      double _multiply = (0.5 * _plus);
      Collection<AuxiliaryLine> _byPosition_1 = this.centerXMap.getByPosition(_multiply);
      Iterable<AuxiliaryLine> _atLeastTwo_1 = this.atLeastTwo(_byPosition_1);
      Iterable<AuxiliaryLine> _plus_1 = Iterables.<AuxiliaryLine>concat(_atLeastTwo, _atLeastTwo_1);
      double _maxX_1 = boundsInDiagram.getMaxX();
      Collection<AuxiliaryLine> _byPosition_2 = this.rightMap.getByPosition(_maxX_1);
      Iterable<AuxiliaryLine> _atLeastTwo_2 = this.atLeastTwo(_byPosition_2);
      Iterable<AuxiliaryLine> _plus_2 = Iterables.<AuxiliaryLine>concat(_plus_1, _atLeastTwo_2);
      double _minY = boundsInDiagram.getMinY();
      Collection<AuxiliaryLine> _byPosition_3 = this.topMap.getByPosition(_minY);
      Iterable<AuxiliaryLine> _atLeastTwo_3 = this.atLeastTwo(_byPosition_3);
      Iterable<AuxiliaryLine> _plus_3 = Iterables.<AuxiliaryLine>concat(_plus_2, _atLeastTwo_3);
      double _minY_1 = boundsInDiagram.getMinY();
      double _maxY = boundsInDiagram.getMaxY();
      double _plus_4 = (_minY_1 + _maxY);
      double _multiply_1 = (0.5 * _plus_4);
      Collection<AuxiliaryLine> _byPosition_4 = this.centerYMap.getByPosition(_multiply_1);
      Iterable<AuxiliaryLine> _atLeastTwo_4 = this.atLeastTwo(_byPosition_4);
      Iterable<AuxiliaryLine> _plus_5 = Iterables.<AuxiliaryLine>concat(_plus_3, _atLeastTwo_4);
      double _maxY_1 = boundsInDiagram.getMaxY();
      Collection<AuxiliaryLine> _byPosition_5 = this.bottomMap.getByPosition(_maxY_1);
      Iterable<AuxiliaryLine> _atLeastTwo_5 = this.atLeastTwo(_byPosition_5);
      _xblockexpression = Iterables.<AuxiliaryLine>concat(_plus_5, _atLeastTwo_5);
    }
    return _xblockexpression;
  }
  
  public Iterable<AuxiliaryLine> getAuxiliaryLines(final XControlPoint point) {
    Iterable<AuxiliaryLine> _xblockexpression = null;
    {
      Bounds _boundsInLocal = point.getBoundsInLocal();
      Point2D _center = BoundsExtensions.center(_boundsInLocal);
      final Point2D centerInDiagram = CoreExtensions.localToRootDiagram(point, _center);
      double _x = centerInDiagram.getX();
      Collection<AuxiliaryLine> _byPosition = this.centerXMap.getByPosition(_x);
      Iterable<AuxiliaryLine> _atLeastTwo = this.atLeastTwo(_byPosition);
      double _y = centerInDiagram.getY();
      Collection<AuxiliaryLine> _byPosition_1 = this.centerYMap.getByPosition(_y);
      Iterable<AuxiliaryLine> _atLeastTwo_1 = this.atLeastTwo(_byPosition_1);
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
    Bounds _snapBounds = node.getSnapBounds();
    final Bounds boundsInDiagram = CoreExtensions.localToDiagram(node, _snapBounds);
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
    XDiagram _diagram = CoreExtensions.getDiagram(node);
    final Bounds boundsInRootDiagram = CoreExtensions.localToRootDiagram(_diagram, newBoundsInDiagram);
    final HashSet<XShape> excluded = CollectionLiterals.<XShape>newHashSet();
    excluded.add(node);
    ObservableList<XConnection> _outgoingConnections = node.getOutgoingConnections();
    final Consumer<XConnection> _function = (XConnection it) -> {
      if ((Objects.equal(it.getKind(), XConnection.Kind.RECTILINEAR) && (it.getControlPoints().size() > 2))) {
        ObservableList<XControlPoint> _controlPoints = it.getControlPoints();
        XControlPoint _get = _controlPoints.get(1);
        excluded.add(_get);
      }
    };
    _outgoingConnections.forEach(_function);
    ObservableList<XConnection> _incomingConnections = node.getIncomingConnections();
    final Consumer<XConnection> _function_1 = (XConnection it) -> {
      if ((Objects.equal(it.getKind(), XConnection.Kind.RECTILINEAR) && (it.getControlPoints().size() > 2))) {
        ObservableList<XControlPoint> _controlPoints = it.getControlPoints();
        ObservableList<XControlPoint> _controlPoints_1 = it.getControlPoints();
        int _size = _controlPoints_1.size();
        int _minus_2 = (_size - 2);
        XControlPoint _get = _controlPoints.get(_minus_2);
        excluded.add(_get);
      }
    };
    _incomingConnections.forEach(_function_1);
    double dx = this.magnetDist;
    double _minX_1 = boundsInRootDiagram.getMinX();
    double _maxX = boundsInRootDiagram.getMaxX();
    double _plus_2 = (_minX_1 + _maxX);
    double _multiply = (0.5 * _plus_2);
    double _nearestLineDelta = this.centerXMap.getNearestLineDelta(_multiply, dx, excluded);
    dx = _nearestLineDelta;
    double _minX_2 = boundsInRootDiagram.getMinX();
    double _nearestLineDelta_1 = this.leftMap.getNearestLineDelta(_minX_2, dx, excluded);
    dx = _nearestLineDelta_1;
    double _maxX_1 = boundsInRootDiagram.getMaxX();
    double _nearestLineDelta_2 = this.rightMap.getNearestLineDelta(_maxX_1, dx, excluded);
    dx = _nearestLineDelta_2;
    double dy = this.magnetDist;
    double _minY_1 = boundsInRootDiagram.getMinY();
    double _maxY = boundsInRootDiagram.getMaxY();
    double _plus_3 = (_minY_1 + _maxY);
    double _multiply_1 = (0.5 * _plus_3);
    double _nearestLineDelta_3 = this.centerYMap.getNearestLineDelta(_multiply_1, dy, excluded);
    dy = _nearestLineDelta_3;
    double _minY_2 = boundsInRootDiagram.getMinY();
    double _nearestLineDelta_4 = this.topMap.getNearestLineDelta(_minY_2, dy, excluded);
    dy = _nearestLineDelta_4;
    double _maxY_1 = boundsInRootDiagram.getMaxY();
    double _nearestLineDelta_5 = this.bottomMap.getNearestLineDelta(_maxY_1, dy, excluded);
    dy = _nearestLineDelta_5;
    return this.toLocal(dx, dy, newPositionInDiagram, node);
  }
  
  public Point2D getSnappedPosition(final XControlPoint point, final Point2D newPositionInDiagram) {
    Parent _parent = point.getParent();
    final Point2D centerInDiagram = CoreExtensions.localToRootDiagram(_parent, newPositionInDiagram);
    double _x = centerInDiagram.getX();
    final double dx = this.centerXMap.getNearestLineDelta(_x, this.magnetDist, Collections.<XShape>unmodifiableSet(CollectionLiterals.<XShape>newHashSet(point)));
    double _y = centerInDiagram.getY();
    final double dy = this.centerYMap.getNearestLineDelta(_y, this.magnetDist, Collections.<XShape>unmodifiableSet(CollectionLiterals.<XShape>newHashSet(point)));
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
    Parent _parent = shape.getParent();
    XDiagram _rootDiagram = CoreExtensions.getRootDiagram(shape);
    Point2D _localToScene = _rootDiagram.localToScene(delta);
    final Point2D deltaLocal = _parent.sceneToLocal(_localToScene);
    double _x = newPointInDiagram.getX();
    double _x_1 = deltaLocal.getX();
    double _plus = (_x + _x_1);
    double _y = newPointInDiagram.getY();
    double _y_1 = deltaLocal.getY();
    double _plus_1 = (_y + _y_1);
    return new Point2D(_plus, _plus_1);
  }
  
  protected void watchDiagram(final XDiagram diagram) {
    ObservableList<XNode> _nodes = diagram.getNodes();
    CoreExtensions.<XNode>addInitializingListener(_nodes, this.nodesListener);
    ObservableList<XConnection> _connections = diagram.getConnections();
    CoreExtensions.<XConnection>addInitializingListener(_connections, this.connectionsListener);
  }
  
  protected void updateDiagram(final XDiagram diagram) {
    ObservableList<XNode> _nodes = diagram.getNodes();
    final Consumer<XNode> _function = (XNode it) -> {
      this.updateNode(it);
    };
    _nodes.forEach(_function);
    ObservableList<XConnection> _connections = diagram.getConnections();
    final Function1<XConnection, ObservableList<XControlPoint>> _function_1 = (XConnection it) -> {
      return it.getControlPoints();
    };
    List<ObservableList<XControlPoint>> _map = ListExtensions.<XConnection, ObservableList<XControlPoint>>map(_connections, _function_1);
    Iterable<XControlPoint> _flatten = Iterables.<XControlPoint>concat(_map);
    final Consumer<XControlPoint> _function_2 = (XControlPoint it) -> {
      this.updateControlPoint(it);
    };
    _flatten.forEach(_function_2);
  }
  
  protected void unwatchDiagram(final XDiagram diagram) {
    ObservableList<XNode> _nodes = diagram.getNodes();
    CoreExtensions.<XNode>removeInitializingListener(_nodes, this.nodesListener);
    ObservableList<XConnection> _connections = diagram.getConnections();
    CoreExtensions.<XConnection>removeInitializingListener(_connections, this.connectionsListener);
  }
  
  protected void watchNode(final XNode node) {
    final ChangeListener<Number> _function = (ObservableValue<? extends Number> scalar, Number oldValue, Number newValue) -> {
      this.updateNode(node);
      if ((node instanceof XDiagramContainer)) {
        boolean _isInnerDiagramActive = ((XDiagramContainer)node).isInnerDiagramActive();
        if (_isInnerDiagramActive) {
          XDiagram _innerDiagram = ((XDiagramContainer)node).getInnerDiagram();
          this.updateDiagram(_innerDiagram);
        }
      }
    };
    final ChangeListener<Number> scalarListener = _function;
    final ChangeListener<Bounds> _function_1 = (ObservableValue<? extends Bounds> scalar, Bounds oldValue, Bounds newValue) -> {
      this.updateNode(node);
      if ((node instanceof XDiagramContainer)) {
        boolean _isInnerDiagramActive = ((XDiagramContainer)node).isInnerDiagramActive();
        if (_isInnerDiagramActive) {
          XDiagram _innerDiagram = ((XDiagramContainer)node).getInnerDiagram();
          this.updateDiagram(_innerDiagram);
        }
      }
    };
    final ChangeListener<Bounds> boundsListener = _function_1;
    DoubleProperty _layoutXProperty = node.layoutXProperty();
    _layoutXProperty.addListener(scalarListener);
    DoubleProperty _layoutYProperty = node.layoutYProperty();
    _layoutYProperty.addListener(scalarListener);
    ReadOnlyObjectProperty<Bounds> _boundsInLocalProperty = node.boundsInLocalProperty();
    _boundsInLocalProperty.addListener(boundsListener);
    this.shape2scalarListener.put(node, scalarListener);
    this.node2boundsListener.put(node, boundsListener);
    this.updateNode(node);
    if ((node instanceof XDiagramContainer)) {
      boolean _isInnerDiagramActive = ((XDiagramContainer)node).isInnerDiagramActive();
      if (_isInnerDiagramActive) {
        XDiagram _innerDiagram = ((XDiagramContainer)node).getInnerDiagram();
        this.watchDiagram(_innerDiagram);
      }
    }
  }
  
  protected void updateNode(final XNode node) {
    Bounds _snapBounds = node.getSnapBounds();
    final Bounds boundsInDiagram = CoreExtensions.localToRootDiagram(node, _snapBounds);
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
        XDiagram _innerDiagram = ((XDiagramContainer)node).getInnerDiagram();
        this.unwatchDiagram(_innerDiagram);
      }
    }
    this.leftMap.removeByShape(node);
    this.centerXMap.removeByShape(node);
    this.rightMap.removeByShape(node);
    this.topMap.removeByShape(node);
    this.centerYMap.removeByShape(node);
    this.bottomMap.removeByShape(node);
    final ChangeListener<Bounds> boundsListener = this.node2boundsListener.remove(node);
    ReadOnlyObjectProperty<Bounds> _boundsInLocalProperty = node.boundsInLocalProperty();
    _boundsInLocalProperty.removeListener(boundsListener);
    final ChangeListener<Number> scalarListener = this.shape2scalarListener.remove(node);
    DoubleProperty _layoutXProperty = node.layoutXProperty();
    _layoutXProperty.removeListener(scalarListener);
    DoubleProperty _layoutYProperty = node.layoutYProperty();
    _layoutYProperty.removeListener(scalarListener);
  }
  
  protected void watchConnection(final XConnection connection) {
    ObservableList<XControlPoint> _controlPoints = connection.getControlPoints();
    CoreExtensions.<XControlPoint>addInitializingListener(_controlPoints, this.controlPointsListener);
  }
  
  protected void unwatchConnection(final XConnection connection) {
    ObservableList<XControlPoint> _controlPoints = connection.getControlPoints();
    CoreExtensions.<XControlPoint>removeInitializingListener(_controlPoints, this.controlPointsListener);
  }
  
  protected void watchControlPoint(final XControlPoint controlPoint) {
    final ChangeListener<Number> _function = (ObservableValue<? extends Number> scalar, Number oldValue, Number newValue) -> {
      this.updateControlPoint(controlPoint);
    };
    final ChangeListener<Number> scalarListener = _function;
    DoubleProperty _layoutXProperty = controlPoint.layoutXProperty();
    _layoutXProperty.addListener(scalarListener);
    DoubleProperty _layoutYProperty = controlPoint.layoutYProperty();
    _layoutYProperty.addListener(scalarListener);
    ObjectProperty<XControlPoint.Type> _typeProperty = controlPoint.typeProperty();
    final ChangeListener<XControlPoint.Type> _function_1 = (ObservableValue<? extends XControlPoint.Type> p, XControlPoint.Type o, XControlPoint.Type n) -> {
      boolean _equals = Objects.equal(n, XControlPoint.Type.ANCHOR);
      if (_equals) {
        this.centerXMap.removeByShape(controlPoint);
      } else {
        this.updateControlPoint(controlPoint);
      }
    };
    _typeProperty.addListener(_function_1);
    this.shape2scalarListener.put(controlPoint, scalarListener);
    this.updateControlPoint(controlPoint);
  }
  
  protected void updateControlPoint(final XControlPoint point) {
    XControlPoint.Type _type = point.getType();
    boolean _notEquals = (!Objects.equal(_type, XControlPoint.Type.ANCHOR));
    if (_notEquals) {
      Bounds _boundsInLocal = point.getBoundsInLocal();
      final Bounds boundsInDiagram = CoreExtensions.localToRootDiagram(point, _boundsInLocal);
      Point2D _center = BoundsExtensions.center(boundsInDiagram);
      double _x = _center.getX();
      NodeLine _nodeLine = new NodeLine(_x, 
        Orientation.VERTICAL, point, boundsInDiagram);
      this.centerXMap.add(_nodeLine);
      Point2D _center_1 = BoundsExtensions.center(boundsInDiagram);
      double _y = _center_1.getY();
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
      DoubleProperty _layoutXProperty = point.layoutXProperty();
      _layoutXProperty.removeListener(scalarListener);
      DoubleProperty _layoutYProperty = point.layoutYProperty();
      _layoutYProperty.removeListener(scalarListener);
    }
  }
}
