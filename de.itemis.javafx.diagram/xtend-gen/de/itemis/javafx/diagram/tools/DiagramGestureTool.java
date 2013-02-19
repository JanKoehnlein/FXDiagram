package de.itemis.javafx.diagram.tools;

import de.itemis.javafx.diagram.XRootDiagram;
import de.itemis.javafx.diagram.tools.ZoomContext;
import de.itemis.javafx.diagram.transform.TransformExtensions;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

@SuppressWarnings("all")
public class DiagramGestureTool {
  private ZoomContext zoomContext;
  
  private Affine diagramTransform;
  
  public DiagramGestureTool(final XRootDiagram diagram) {
    final Scene scene = diagram.getScene();
    Affine _affine = new Affine();
    this.diagramTransform = _affine;
    ObservableList<Transform> _transforms = diagram.getTransforms();
    _transforms.clear();
    ObservableList<Transform> _transforms_1 = diagram.getTransforms();
    _transforms_1.add(this.diagramTransform);
    final EventHandler<ZoomEvent> _function = new EventHandler<ZoomEvent>() {
        public void handle(final ZoomEvent it) {
          double _sceneX = it.getSceneX();
          double _sceneY = it.getSceneY();
          Point2D _sceneToLocal = diagram.sceneToLocal(_sceneX, _sceneY);
          ZoomContext _zoomContext = new ZoomContext(_sceneToLocal);
          DiagramGestureTool.this.zoomContext = _zoomContext;
        }
      };
    scene.setOnZoomStarted(_function);
    final EventHandler<ZoomEvent> _function_1 = new EventHandler<ZoomEvent>() {
        public void handle(final ZoomEvent it) {
          double _totalZoomFactor = it.getTotalZoomFactor();
          double _previousScale = DiagramGestureTool.this.zoomContext.getPreviousScale();
          final double scale = (_totalZoomFactor / _previousScale);
          DoubleProperty _scaleProperty = diagram.getScaleProperty();
          DoubleProperty _scaleProperty_1 = diagram.getScaleProperty();
          double _get = _scaleProperty_1.get();
          double _multiply = (scale * _get);
          _scaleProperty.set(_multiply);
          TransformExtensions.scale(DiagramGestureTool.this.diagramTransform, scale, scale);
          Point2D _pivotInDiagram = DiagramGestureTool.this.zoomContext.getPivotInDiagram();
          final Point2D pivotInScene = diagram.localToScene(_pivotInDiagram);
          double _sceneX = it.getSceneX();
          double _x = pivotInScene.getX();
          double _minus = (_sceneX - _x);
          double _sceneY = it.getSceneY();
          double _y = pivotInScene.getY();
          double _minus_1 = (_sceneY - _y);
          TransformExtensions.translate(DiagramGestureTool.this.diagramTransform, _minus, _minus_1);
          double _totalZoomFactor_1 = it.getTotalZoomFactor();
          DiagramGestureTool.this.zoomContext.setPreviousScale(_totalZoomFactor_1);
        }
      };
    final EventHandler<ZoomEvent> zoomHandler = _function_1;
    scene.setOnZoom(zoomHandler);
    scene.setOnZoomFinished(zoomHandler);
    final EventHandler<ScrollEvent> _function_2 = new EventHandler<ScrollEvent>() {
        public void handle(final ScrollEvent it) {
          double _deltaX = it.getDeltaX();
          double _deltaY = it.getDeltaY();
          TransformExtensions.translate(DiagramGestureTool.this.diagramTransform, _deltaX, _deltaY);
        }
      };
    final EventHandler<ScrollEvent> scrollHandler = _function_2;
    scene.setOnScrollStarted(scrollHandler);
    scene.setOnScroll(scrollHandler);
    scene.setOnScrollFinished(scrollHandler);
    final EventHandler<RotateEvent> _function_3 = new EventHandler<RotateEvent>() {
        public void handle(final RotateEvent it) {
          boolean _isShortcutDown = it.isShortcutDown();
          if (_isShortcutDown) {
            double _angle = it.getAngle();
            double _sceneX = it.getSceneX();
            double _sceneY = it.getSceneY();
            TransformExtensions.rotate(DiagramGestureTool.this.diagramTransform, _angle, _sceneX, _sceneY);
          }
        }
      };
    final EventHandler<RotateEvent> rotateHandler = _function_3;
    scene.setOnRotationStarted(rotateHandler);
    scene.setOnRotate(rotateHandler);
    scene.setOnRotationFinished(rotateHandler);
  }
}
