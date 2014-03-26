package de.fxdiagram.eclipse;

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
    Demo _demo = new Demo();
    return _demo.createScene();
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
    boolean _isDisposed = shell.isDisposed();
    boolean _not = (!_isDisposed);
    boolean _while = _not;
    while (_while) {
      boolean _readAndDispatch = display.readAndDispatch();
      boolean _not_1 = (!_readAndDispatch);
      if (_not_1) {
        display.sleep();
      }
      boolean _isDisposed_1 = shell.isDisposed();
      boolean _not_2 = (!_isDisposed_1);
      _while = _not_2;
    }
    gestureConverter.dispose();
    display.dispose();
  }
}
