package de.itemis.javafx.diagram.tools;

import de.itemis.javafx.diagram.XRootDiagram;
import de.itemis.javafx.diagram.tools.ZoomContext;
import de.itemis.javafx.diagram.transform.TransformExtensions;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

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
    final Procedure1<ZoomEvent> _function = new Procedure1<ZoomEvent>() {
        public void apply(final ZoomEvent it) {
          double _sceneX = it.getSceneX();
          double _sceneY = it.getSceneY();
          Point2D _sceneToLocal = diagram.sceneToLocal(_sceneX, _sceneY);
          ZoomContext _zoomContext = new ZoomContext(_sceneToLocal);
          DiagramGestureTool.this.zoomContext = _zoomContext;
        }
      };
    scene.setOnZoomStarted(new EventHandler<ZoomEvent>() {
        public void handle(ZoomEvent arg0) {
          _function.apply(arg0);
        }
    });
    final Procedure1<ZoomEvent> _function_1 = new Procedure1<ZoomEvent>() {
        public void apply(final ZoomEvent it) {
          double _totalZoomFactor = it.getTotalZoomFactor();
          double _previousScale = DiagramGestureTool.this.zoomContext.getPreviousScale();
          final double scale = (_totalZoomFactor / _previousScale);
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
    final EventHandler<ZoomEvent> zoomHandler = new EventHandler<ZoomEvent>() {
        public void handle(ZoomEvent arg0) {
          _function_1.apply(arg0);
        }
    };
    scene.setOnZoom(zoomHandler);
    scene.setOnZoomFinished(zoomHandler);
    final Procedure1<ScrollEvent> _function_2 = new Procedure1<ScrollEvent>() {
        public void apply(final ScrollEvent it) {
          double _deltaX = it.getDeltaX();
          double _deltaY = it.getDeltaY();
          TransformExtensions.translate(DiagramGestureTool.this.diagramTransform, _deltaX, _deltaY);
        }
      };
    final EventHandler<ScrollEvent> scrollHandler = new EventHandler<ScrollEvent>() {
        public void handle(ScrollEvent arg0) {
          _function_2.apply(arg0);
        }
    };
    scene.setOnScrollStarted(scrollHandler);
    scene.setOnScroll(scrollHandler);
    scene.setOnScrollFinished(scrollHandler);
    final Procedure1<RotateEvent> _function_3 = new Procedure1<RotateEvent>() {
        public void apply(final RotateEvent it) {
          boolean _isShortcutDown = it.isShortcutDown();
          if (_isShortcutDown) {
            double _angle = it.getAngle();
            double _sceneX = it.getSceneX();
            double _sceneY = it.getSceneY();
            TransformExtensions.rotate(DiagramGestureTool.this.diagramTransform, _angle, _sceneX, _sceneY);
          }
        }
      };
    final EventHandler<RotateEvent> rotateHandler = new EventHandler<RotateEvent>() {
        public void handle(RotateEvent arg0) {
          _function_3.apply(arg0);
        }
    };
    scene.setOnRotationStarted(rotateHandler);
    scene.setOnRotate(rotateHandler);
    scene.setOnRotationFinished(rotateHandler);
  }
}
