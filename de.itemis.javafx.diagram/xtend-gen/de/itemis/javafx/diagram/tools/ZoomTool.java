package de.itemis.javafx.diagram.tools;

import de.itemis.javafx.diagram.XDiagram;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ZoomTool {
  private double initialScale;
  
  public ZoomTool(final XDiagram diagram) {
    Group _rootPane = diagram.getRootPane();
    final Scene scene = _rootPane.getScene();
    final Group rootPane = diagram.getRootPane();
    final Procedure1<ZoomEvent> _function = new Procedure1<ZoomEvent>() {
        public void apply(final ZoomEvent it) {
          double _scaleX = rootPane.getScaleX();
          ZoomTool.this.initialScale = _scaleX;
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
          double _multiply = (_totalZoomFactor * ZoomTool.this.initialScale);
          rootPane.setScaleX(_multiply);
          double _totalZoomFactor_1 = it.getTotalZoomFactor();
          double _multiply_1 = (_totalZoomFactor_1 * ZoomTool.this.initialScale);
          rootPane.setScaleY(_multiply_1);
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
          double _translateX = rootPane.getTranslateX();
          double _deltaX = it.getDeltaX();
          double _plus = (_translateX + _deltaX);
          rootPane.setTranslateX(_plus);
          double _translateY = rootPane.getTranslateY();
          double _deltaY = it.getDeltaY();
          double _plus_1 = (_translateY + _deltaY);
          rootPane.setTranslateY(_plus_1);
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
  }
}
