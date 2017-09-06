package de.fxdiagram.eclipse.example;

import de.fxdiagram.examples.Demo;
import de.fxdiagram.swtfx.SwtToFXGestureConverter;
import javafx.embed.swt.FXCanvas;
import javafx.scene.Scene;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("all")
public class SwtDemo {
  private static Scene createScene() {
    return new Demo().createScene();
  }
  
  public static void main(final String[] args) {
    final Display display = new Display();
    final Shell shell = new Shell(display);
    FillLayout _fillLayout = new FillLayout();
    shell.setLayout(_fillLayout);
    final FXCanvas canvas = new FXCanvas(shell, SWT.NONE);
    final SwtToFXGestureConverter gestureConverter = new SwtToFXGestureConverter(canvas);
    final Scene scene = SwtDemo.createScene();
    canvas.setScene(scene);
    shell.open();
    while ((!shell.isDisposed())) {
      boolean _readAndDispatch = display.readAndDispatch();
      boolean _not = (!_readAndDispatch);
      if (_not) {
        display.sleep();
      }
    }
    gestureConverter.dispose();
    display.dispose();
  }
}
