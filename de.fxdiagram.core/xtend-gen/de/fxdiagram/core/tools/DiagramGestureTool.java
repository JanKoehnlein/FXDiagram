package de.fxdiagram.core.tools;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.tools.XDiagramTool;
import de.fxdiagram.core.viewport.ViewportTransform;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.transform.Rotate;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class DiagramGestureTool implements XDiagramTool {
  public static class ZoomContext {
    @Accessors
    private double previousScale = 1;
    
    @Accessors
    private Point2D pivotInDiagram;
    
    public ZoomContext(final Point2D pivotInDiagram) {
      this.pivotInDiagram = pivotInDiagram;
    }
    
    @Pure
    public double getPreviousScale() {
      return this.previousScale;
    }
    
    public void setPreviousScale(final double previousScale) {
      this.previousScale = previousScale;
    }
    
    @Pure
    public Point2D getPivotInDiagram() {
      return this.pivotInDiagram;
    }
    
    public void setPivotInDiagram(final Point2D pivotInDiagram) {
      this.pivotInDiagram = pivotInDiagram;
    }
  }
  
  private XRoot root;
  
  private DiagramGestureTool.ZoomContext zoomContext;
  
  private EventHandler<ZoomEvent> zoomStartHandler;
  
  private EventHandler<ZoomEvent> zoomHandler;
  
  private EventHandler<ScrollEvent> scrollHandler;
  
  private EventHandler<RotateEvent> rotateHandler;
  
  public DiagramGestureTool(final XRoot root) {
    this.root = root;
    final EventHandler<ZoomEvent> _function = new EventHandler<ZoomEvent>() {
      @Override
      public void handle(final ZoomEvent it) {
        XDiagram _diagram = root.getDiagram();
        double _sceneX = it.getSceneX();
        double _sceneY = it.getSceneY();
        Point2D _sceneToLocal = _diagram.sceneToLocal(_sceneX, _sceneY);
        DiagramGestureTool.ZoomContext _zoomContext = new DiagramGestureTool.ZoomContext(_sceneToLocal);
        DiagramGestureTool.this.zoomContext = _zoomContext;
      }
    };
    this.zoomStartHandler = _function;
    final EventHandler<ZoomEvent> _function_1 = new EventHandler<ZoomEvent>() {
      @Override
      public void handle(final ZoomEvent it) {
        double _totalZoomFactor = it.getTotalZoomFactor();
        final double scale = (_totalZoomFactor / DiagramGestureTool.this.zoomContext.previousScale);
        ViewportTransform _viewportTransform = root.getViewportTransform();
        _viewportTransform.scaleRelative(scale);
        XDiagram _diagram = root.getDiagram();
        final Point2D pivotInScene = _diagram.localToScene(DiagramGestureTool.this.zoomContext.pivotInDiagram);
        ViewportTransform _viewportTransform_1 = root.getViewportTransform();
        double _sceneX = it.getSceneX();
        double _x = pivotInScene.getX();
        double _minus = (_sceneX - _x);
        double _sceneY = it.getSceneY();
        double _y = pivotInScene.getY();
        double _minus_1 = (_sceneY - _y);
        _viewportTransform_1.translateRelative(_minus, _minus_1);
        double _totalZoomFactor_1 = it.getTotalZoomFactor();
        DiagramGestureTool.this.zoomContext.previousScale = _totalZoomFactor_1;
      }
    };
    this.zoomHandler = _function_1;
    final EventHandler<ScrollEvent> _function_2 = new EventHandler<ScrollEvent>() {
      @Override
      public void handle(final ScrollEvent it) {
        ViewportTransform _viewportTransform = root.getViewportTransform();
        double _deltaX = it.getDeltaX();
        double _deltaY = it.getDeltaY();
        _viewportTransform.translateRelative(_deltaX, _deltaY);
      }
    };
    this.scrollHandler = _function_2;
    final EventHandler<RotateEvent> _function_3 = new EventHandler<RotateEvent>() {
      @Override
      public void handle(final RotateEvent it) {
        Iterable<XShape> selection = root.getCurrentSelection();
        Iterable<XShape> _xifexpression = null;
        boolean _isEmpty = IterableExtensions.isEmpty(selection);
        if (_isEmpty) {
          XDiagram _diagram = root.getDiagram();
          ObservableList<XNode> _nodes = _diagram.getNodes();
          XDiagram _diagram_1 = root.getDiagram();
          ObservableList<XConnection> _connections = _diagram_1.getConnections();
          final Function1<XConnection, Iterable<XControlPoint>> _function = new Function1<XConnection, Iterable<XControlPoint>>() {
            @Override
            public Iterable<XControlPoint> apply(final XConnection it) {
              return DiagramGestureTool.this.getNonAnchorPoints(it);
            }
          };
          List<Iterable<XControlPoint>> _map = ListExtensions.<XConnection, Iterable<XControlPoint>>map(_connections, _function);
          Iterable<XControlPoint> _flatten = Iterables.<XControlPoint>concat(_map);
          _xifexpression = Iterables.<XShape>concat(_nodes, _flatten);
        } else {
          Iterable<XShape> _xblockexpression = null;
          {
            Iterable<XNode> _filter = Iterables.<XNode>filter(selection, XNode.class);
            final Set<XNode> nodes = IterableExtensions.<XNode>toSet(_filter);
            Iterable<XConnection> _filter_1 = Iterables.<XConnection>filter(selection, XConnection.class);
            final Function1<XNode, Iterable<XConnection>> _function_1 = new Function1<XNode, Iterable<XConnection>>() {
              @Override
              public Iterable<XConnection> apply(final XNode it) {
                ObservableList<XConnection> _outgoingConnections = it.getOutgoingConnections();
                final Function1<XConnection, Boolean> _function = new Function1<XConnection, Boolean>() {
                  @Override
                  public Boolean apply(final XConnection it) {
                    XNode _target = it.getTarget();
                    return Boolean.valueOf(nodes.contains(_target));
                  }
                };
                Iterable<XConnection> _filter = IterableExtensions.<XConnection>filter(_outgoingConnections, _function);
                ObservableList<XConnection> _incomingConnections = it.getIncomingConnections();
                final Function1<XConnection, Boolean> _function_1 = new Function1<XConnection, Boolean>() {
                  @Override
                  public Boolean apply(final XConnection it) {
                    XNode _source = it.getSource();
                    return Boolean.valueOf(nodes.contains(_source));
                  }
                };
                Iterable<XConnection> _filter_1 = IterableExtensions.<XConnection>filter(_incomingConnections, _function_1);
                return Iterables.<XConnection>concat(_filter, _filter_1);
              }
            };
            Iterable<Iterable<XConnection>> _map_1 = IterableExtensions.<XNode, Iterable<XConnection>>map(nodes, _function_1);
            Iterable<XConnection> _flatten_1 = Iterables.<XConnection>concat(_map_1);
            final Iterable<XConnection> connections = Iterables.<XConnection>concat(_filter_1, _flatten_1);
            final Function1<XConnection, Iterable<XControlPoint>> _function_2 = new Function1<XConnection, Iterable<XControlPoint>>() {
              @Override
              public Iterable<XControlPoint> apply(final XConnection it) {
                return DiagramGestureTool.this.getNonAnchorPoints(it);
              }
            };
            Iterable<Iterable<XControlPoint>> _map_2 = IterableExtensions.<XConnection, Iterable<XControlPoint>>map(connections, _function_2);
            Iterable<XControlPoint> _flatten_2 = Iterables.<XControlPoint>concat(_map_2);
            Iterable<XShape> _plus = Iterables.<XShape>concat(nodes, _flatten_2);
            Iterable<XControlPoint> _filter_2 = Iterables.<XControlPoint>filter(selection, XControlPoint.class);
            _xblockexpression = Iterables.<XShape>concat(_plus, _filter_2);
          }
          _xifexpression = _xblockexpression;
        }
        final Set<XShape> rotateSet = IterableExtensions.<XShape>toSet(_xifexpression);
        XDiagram _diagram_2 = root.getDiagram();
        double _sceneX = it.getSceneX();
        double _sceneY = it.getSceneY();
        final Point2D pivot = _diagram_2.sceneToLocal(_sceneX, _sceneY);
        double _angle = it.getAngle();
        double _x = pivot.getX();
        double _y = pivot.getY();
        final Rotate rotate = new Rotate(_angle, _x, _y);
        final Consumer<XShape> _function_1 = new Consumer<XShape>() {
          @Override
          public void accept(final XShape it) {
            final Dimension2D offset = DiagramGestureTool.this.getRotateOffset(((XShape)it));
            double _layoutX = ((XShape)it).getLayoutX();
            double _width = offset.getWidth();
            double _plus = (_layoutX + _width);
            double _layoutY = ((XShape)it).getLayoutY();
            double _height = offset.getHeight();
            double _plus_1 = (_layoutY + _height);
            final Point2D newPosition = rotate.transform(_plus, _plus_1);
            double _x = newPosition.getX();
            double _width_1 = offset.getWidth();
            double _minus = (_x - _width_1);
            ((XShape)it).setLayoutX(_minus);
            double _y = newPosition.getY();
            double _height_1 = offset.getHeight();
            double _minus_1 = (_y - _height_1);
            ((XShape)it).setLayoutY(_minus_1);
          }
        };
        rotateSet.forEach(_function_1);
      }
    };
    this.rotateHandler = _function_3;
  }
  
  protected Iterable<XControlPoint> getNonAnchorPoints(final XConnection it) {
    ObservableList<XControlPoint> _controlPoints = it.getControlPoints();
    final Function1<XControlPoint, Boolean> _function = new Function1<XControlPoint, Boolean>() {
      @Override
      public Boolean apply(final XControlPoint it) {
        XControlPoint.Type _type = it.getType();
        return Boolean.valueOf((!Objects.equal(_type, XControlPoint.Type.ANCHOR)));
      }
    };
    return IterableExtensions.<XControlPoint>filter(_controlPoints, _function);
  }
  
  protected Dimension2D getRotateOffset(final XShape it) {
    Dimension2D _xifexpression = null;
    if ((it instanceof XControlPoint)) {
      _xifexpression = new Dimension2D(0, 0);
    } else {
      Bounds _layoutBounds = it.getLayoutBounds();
      double _width = _layoutBounds.getWidth();
      double _multiply = (0.5 * _width);
      Bounds _layoutBounds_1 = it.getLayoutBounds();
      double _height = _layoutBounds_1.getHeight();
      double _multiply_1 = (0.5 * _height);
      _xifexpression = new Dimension2D(_multiply, _multiply_1);
    }
    return _xifexpression;
  }
  
  @Override
  public boolean activate() {
    boolean _xblockexpression = false;
    {
      final Scene scene = this.root.getScene();
      scene.<ZoomEvent>addEventHandler(ZoomEvent.ZOOM_STARTED, this.zoomStartHandler);
      scene.<ZoomEvent>addEventHandler(ZoomEvent.ZOOM, this.zoomHandler);
      scene.<ZoomEvent>addEventHandler(ZoomEvent.ZOOM_FINISHED, this.zoomHandler);
      scene.<ScrollEvent>addEventHandler(ScrollEvent.SCROLL, this.scrollHandler);
      scene.<ScrollEvent>addEventHandler(ScrollEvent.SCROLL_FINISHED, this.scrollHandler);
      scene.<RotateEvent>addEventHandler(RotateEvent.ROTATION_STARTED, this.rotateHandler);
      scene.<RotateEvent>addEventHandler(RotateEvent.ROTATE, this.rotateHandler);
      scene.<RotateEvent>addEventHandler(RotateEvent.ROTATION_FINISHED, this.rotateHandler);
      _xblockexpression = true;
    }
    return _xblockexpression;
  }
  
  @Override
  public boolean deactivate() {
    boolean _xblockexpression = false;
    {
      final Scene scene = this.root.getScene();
      scene.<ZoomEvent>removeEventHandler(ZoomEvent.ZOOM_STARTED, this.zoomStartHandler);
      scene.<ZoomEvent>removeEventHandler(ZoomEvent.ZOOM, this.zoomHandler);
      scene.<ZoomEvent>removeEventHandler(ZoomEvent.ZOOM_FINISHED, this.zoomHandler);
      scene.<ScrollEvent>removeEventHandler(ScrollEvent.SCROLL, this.scrollHandler);
      scene.<ScrollEvent>removeEventHandler(ScrollEvent.SCROLL_FINISHED, this.scrollHandler);
      scene.<RotateEvent>removeEventHandler(RotateEvent.ROTATION_STARTED, this.rotateHandler);
      scene.<RotateEvent>removeEventHandler(RotateEvent.ROTATE, this.rotateHandler);
      scene.<RotateEvent>removeEventHandler(RotateEvent.ROTATION_FINISHED, this.rotateHandler);
      _xblockexpression = true;
    }
    return _xblockexpression;
  }
}
