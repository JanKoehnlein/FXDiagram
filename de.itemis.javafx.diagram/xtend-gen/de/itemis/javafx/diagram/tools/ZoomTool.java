package de.itemis.javafx.diagram.tools;

import de.itemis.javafx.diagram.Diagram;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.ZoomEvent;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ZoomTool {
  private double initialScale;
  
  public ZoomTool(final Diagram diagram) {
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
    scene.setOnZoom(new EventHandler<ZoomEvent>() {
        public void handle(ZoomEvent arg0) {
          _function_1.apply(arg0);
        }
    });
    final Procedure1<ZoomEvent> _function_2 = new Procedure1<ZoomEvent>() {
        public void apply(final ZoomEvent it) {
          double _totalZoomFactor = it.getTotalZoomFactor();
          double _multiply = (_totalZoomFactor * ZoomTool.this.initialScale);
          rootPane.setScaleX(_multiply);
          double _totalZoomFactor_1 = it.getTotalZoomFactor();
          double _multiply_1 = (_totalZoomFactor_1 * ZoomTool.this.initialScale);
          rootPane.setScaleY(_multiply_1);
        }
      };
    scene.setOnZoomFinished(new EventHandler<ZoomEvent>() {
        public void handle(ZoomEvent arg0) {
          _function_2.apply(arg0);
        }
    });
  }
}
