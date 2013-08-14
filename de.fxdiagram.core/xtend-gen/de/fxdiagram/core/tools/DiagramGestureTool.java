package de.fxdiagram.core.tools;

import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.core.geometry.TransformExtensions;
import de.fxdiagram.core.tools.XDiagramTool;
import de.fxdiagram.core.tools.ZoomContext;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.transform.Affine;

@SuppressWarnings("all")
public class DiagramGestureTool implements XDiagramTool {
  private XRoot root;
  
  private ZoomContext zoomContext;
  
  private EventHandler<ZoomEvent> zoomStartHandler;
  
  private EventHandler<ZoomEvent> zoomHandler;
  
  private EventHandler<ScrollEvent> scrollHandler;
  
  private EventHandler<RotateEvent> rotateHandler;
  
  public DiagramGestureTool(final XRoot root) {
    this.root = root;
    final EventHandler<ZoomEvent> _function = new EventHandler<ZoomEvent>() {
      public void handle(final ZoomEvent it) {
        XRootDiagram _diagram = root.getDiagram();
        double _sceneX = it.getSceneX();
        double _sceneY = it.getSceneY();
        Point2D _sceneToLocal = _diagram.sceneToLocal(_sceneX, _sceneY);
        ZoomContext _zoomContext = new ZoomContext(_sceneToLocal);
        DiagramGestureTool.this.zoomContext = _zoomContext;
      }
    };
    this.zoomStartHandler = _function;
    final EventHandler<ZoomEvent> _function_1 = new EventHandler<ZoomEvent>() {
      public void handle(final ZoomEvent it) {
        double _totalZoomFactor = it.getTotalZoomFactor();
        double _previousScale = DiagramGestureTool.this.zoomContext.getPreviousScale();
        double _divide = (_totalZoomFactor / _previousScale);
        final double scale = Math.max(_divide, XRootDiagram.MIN_SCALE);
        XRootDiagram _diagram = root.getDiagram();
        DoubleProperty _scaleProperty = _diagram.scaleProperty();
        double _get = _scaleProperty.get();
        final double newScale = (scale * _get);
        XRootDiagram _diagram_1 = root.getDiagram();
        _diagram_1.setScale(newScale);
        XRootDiagram _diagram_2 = root.getDiagram();
        Affine _canvasTransform = _diagram_2.getCanvasTransform();
        TransformExtensions.scale(_canvasTransform, scale, scale);
        XRootDiagram _diagram_3 = root.getDiagram();
        Point2D _pivotInDiagram = DiagramGestureTool.this.zoomContext.getPivotInDiagram();
        final Point2D pivotInScene = _diagram_3.localToScene(_pivotInDiagram);
        XRootDiagram _diagram_4 = root.getDiagram();
        Affine _canvasTransform_1 = _diagram_4.getCanvasTransform();
        double _sceneX = it.getSceneX();
        double _x = pivotInScene.getX();
        double _minus = (_sceneX - _x);
        double _sceneY = it.getSceneY();
        double _y = pivotInScene.getY();
        double _minus_1 = (_sceneY - _y);
        TransformExtensions.translate(_canvasTransform_1, _minus, _minus_1);
        double _totalZoomFactor_1 = it.getTotalZoomFactor();
        DiagramGestureTool.this.zoomContext.setPreviousScale(_totalZoomFactor_1);
      }
    };
    this.zoomHandler = _function_1;
    final EventHandler<ScrollEvent> _function_2 = new EventHandler<ScrollEvent>() {
      public void handle(final ScrollEvent it) {
        XRootDiagram _diagram = root.getDiagram();
        Affine _canvasTransform = _diagram.getCanvasTransform();
        double _deltaX = it.getDeltaX();
        double _deltaY = it.getDeltaY();
        TransformExtensions.translate(_canvasTransform, _deltaX, _deltaY);
      }
    };
    this.scrollHandler = _function_2;
    final EventHandler<RotateEvent> _function_3 = new EventHandler<RotateEvent>() {
      public void handle(final RotateEvent it) {
        boolean _isShortcutDown = it.isShortcutDown();
        if (_isShortcutDown) {
          XRootDiagram _diagram = root.getDiagram();
          Affine _canvasTransform = _diagram.getCanvasTransform();
          double _angle = it.getAngle();
          double _sceneX = it.getSceneX();
          double _sceneY = it.getSceneY();
          TransformExtensions.rotate(_canvasTransform, _angle, _sceneX, _sceneY);
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
      _xblockexpression = (true);
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
      _xblockexpression = (true);
    }
    return _xblockexpression;
  }
}
