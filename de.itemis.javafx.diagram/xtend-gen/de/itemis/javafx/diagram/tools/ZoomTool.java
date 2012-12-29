package de.itemis.javafx.diagram.tools;

import de.itemis.javafx.diagram.XRootDiagram;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ZoomTool {
  private double initialScale;
  
  public ZoomTool(final XRootDiagram diagram) {
    final Scene scene = diagram.getScene();
    final Procedure1<ZoomEvent> _function = new Procedure1<ZoomEvent>() {
        public void apply(final ZoomEvent it) {
          double _scaleX = diagram.getScaleX();
          ZoomTool.this.initialScale = _scaleX;
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
          double _multiply = (_totalZoomFactor * ZoomTool.this.initialScale);
          diagram.setScaleX(_multiply);
          double _totalZoomFactor_1 = it.getTotalZoomFactor();
          double _multiply_1 = (_totalZoomFactor_1 * ZoomTool.this.initialScale);
          diagram.setScaleY(_multiply_1);
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
