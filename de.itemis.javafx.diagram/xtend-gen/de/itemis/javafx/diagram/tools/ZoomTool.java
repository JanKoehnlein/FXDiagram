package de.itemis.javafx.diagram.tools;

import de.itemis.javafx.diagram.XRootDiagram;
import de.itemis.javafx.diagram.tools.ZoomContext;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ZoomTool {
  private ZoomContext zoomContext;
  
  public ZoomTool(final XRootDiagram diagram) {
    final Scene scene = diagram.getScene();
    final Procedure1<ZoomEvent> _function = new Procedure1<ZoomEvent>() {
        public void apply(final ZoomEvent it) {
          double _scaleX = diagram.getScaleX();
          double _sceneX = it.getSceneX();
          double _sceneY = it.getSceneY();
          Point2D _sceneToLocal = diagram.sceneToLocal(_sceneX, _sceneY);
          ZoomContext _zoomContext = new ZoomContext(_scaleX, _sceneToLocal);
          ZoomTool.this.zoomContext = _zoomContext;
        }
      };
    scene.setOnZoomStarted(new EventHandler<ZoomEvent>() {
        public void handle(ZoomEvent event) {
          _function.apply(event);
        }
    });
    final Procedure1<ZoomEvent> _function_1 = new Procedure1<ZoomEvent>() {
        public void apply(final ZoomEvent it) {
          double _totalZoomFactor = it.getTotalZoomFactor();
          double _initialScale = ZoomTool.this.zoomContext.getInitialScale();
          final double scale = (_totalZoomFactor * _initialScale);
          boolean _greaterThan = (scale > 0);
          if (_greaterThan) {
            diagram.setScaleX(scale);
            diagram.setScaleY(scale);
            Point2D _initialDiagramPos = ZoomTool.this.zoomContext.getInitialDiagramPos();
            final Point2D pivotInScene = diagram.localToScene(_initialDiagramPos);
            double _translateX = diagram.getTranslateX();
            double _sceneX = it.getSceneX();
            double _plus = (_translateX + _sceneX);
            double _x = pivotInScene.getX();
            double _minus = (_plus - _x);
            diagram.setTranslateX(_minus);
            double _translateY = diagram.getTranslateY();
            double _sceneY = it.getSceneY();
            double _plus_1 = (_translateY + _sceneY);
            double _y = pivotInScene.getY();
            double _minus_1 = (_plus_1 - _y);
            diagram.setTranslateY(_minus_1);
          }
        }
      };
    final EventHandler<ZoomEvent> zoomHandler = new EventHandler<ZoomEvent>() {
        public void handle(ZoomEvent event) {
          _function_1.apply(event);
        }
    };
    scene.setOnZoom(zoomHandler);
    scene.setOnZoomFinished(zoomHandler);
    final Procedure1<ScrollEvent> _function_2 = new Procedure1<ScrollEvent>() {
        public void apply(final ScrollEvent it) {
          double _translateX = diagram.getTranslateX();
          double _deltaX = it.getDeltaX();
          double _plus = (_translateX + _deltaX);
          diagram.setTranslateX(_plus);
          double _translateY = diagram.getTranslateY();
          double _deltaY = it.getDeltaY();
          double _plus_1 = (_translateY + _deltaY);
          diagram.setTranslateY(_plus_1);
        }
      };
    final EventHandler<ScrollEvent> scrollHandler = new EventHandler<ScrollEvent>() {
        public void handle(ScrollEvent event) {
          _function_2.apply(event);
        }
    };
    scene.setOnScrollStarted(scrollHandler);
    scene.setOnScroll(scrollHandler);
    scene.setOnScrollFinished(scrollHandler);
  }
}
