package de.fxdiagram.core.tools;

import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.tools.XDiagramTool;
import de.fxdiagram.core.viewport.ViewportTransform;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import org.eclipse.xtend.lib.Property;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class DiagramGestureTool implements XDiagramTool {
  public static class ZoomContext {
    @Property
    private double _previousScale = 1;
    
    @Property
    private Point2D _pivotInDiagram;
    
    public ZoomContext(final Point2D pivotInDiagram) {
      this.setPivotInDiagram(pivotInDiagram);
    }
    
    @Pure
    public double getPreviousScale() {
      return this._previousScale;
    }
    
    public void setPreviousScale(final double previousScale) {
      this._previousScale = previousScale;
    }
    
    @Pure
    public Point2D getPivotInDiagram() {
      return this._pivotInDiagram;
    }
    
    public void setPivotInDiagram(final Point2D pivotInDiagram) {
      this._pivotInDiagram = pivotInDiagram;
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
      public void handle(final ZoomEvent it) {
        double _totalZoomFactor = it.getTotalZoomFactor();
        double _previousScale = DiagramGestureTool.this.zoomContext.getPreviousScale();
        final double scale = (_totalZoomFactor / _previousScale);
        ViewportTransform _viewportTransform = root.getViewportTransform();
        _viewportTransform.scaleRelative(scale);
        XDiagram _diagram = root.getDiagram();
        Point2D _pivotInDiagram = DiagramGestureTool.this.zoomContext.getPivotInDiagram();
        final Point2D pivotInScene = _diagram.localToScene(_pivotInDiagram);
        ViewportTransform _viewportTransform_1 = root.getViewportTransform();
        double _sceneX = it.getSceneX();
        double _x = pivotInScene.getX();
        double _minus = (_sceneX - _x);
        double _sceneY = it.getSceneY();
        double _y = pivotInScene.getY();
        double _minus_1 = (_sceneY - _y);
        _viewportTransform_1.translateRelative(_minus, _minus_1);
        double _totalZoomFactor_1 = it.getTotalZoomFactor();
        DiagramGestureTool.this.zoomContext.setPreviousScale(_totalZoomFactor_1);
      }
    };
    this.zoomHandler = _function_1;
    final EventHandler<ScrollEvent> _function_2 = new EventHandler<ScrollEvent>() {
      public void handle(final ScrollEvent it) {
        ViewportTransform _viewportTransform = root.getViewportTransform();
        double _deltaX = it.getDeltaX();
        double _deltaY = it.getDeltaY();
        _viewportTransform.translateRelative(_deltaX, _deltaY);
      }
    };
    this.scrollHandler = _function_2;
    final EventHandler<RotateEvent> _function_3 = new EventHandler<RotateEvent>() {
      public void handle(final RotateEvent it) {
        boolean _isShortcutDown = it.isShortcutDown();
        if (_isShortcutDown) {
          ViewportTransform _viewportTransform = root.getViewportTransform();
          double _angle = it.getAngle();
          double _minus = (-_angle);
          double _sceneX = it.getSceneX();
          double _sceneY = it.getSceneY();
          _viewportTransform.rotateRelative(_minus, _sceneX, _sceneY);
        }
      }
    };
    this.rotateHandler = _function_3;
  }
  
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
