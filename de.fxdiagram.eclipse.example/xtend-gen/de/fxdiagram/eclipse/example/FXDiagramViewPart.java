package de.fxdiagram.eclipse.example;

import de.fxdiagram.examples.Demo;
import de.fxdiagram.swtfx.SwtToFXGestureConverter;
import javafx.embed.swt.FXCanvas;
import javafx.scene.Scene;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

@SuppressWarnings("all")
public class FXDiagramViewPart extends ViewPart {
  private FXCanvas canvas;
  
  private SwtToFXGestureConverter gestureConverter;
  
  @Override
  public void createPartControl(final Composite parent) {
    FXCanvas _fXCanvas = new FXCanvas(parent, SWT.NONE);
    this.canvas = _fXCanvas;
    SwtToFXGestureConverter _swtToFXGestureConverter = new SwtToFXGestureConverter(this.canvas);
    this.gestureConverter = _swtToFXGestureConverter;
    Scene _createFxScene = this.createFxScene();
    this.canvas.setScene(_createFxScene);
  }
  
  @Override
  public void dispose() {
    this.gestureConverter.dispose();
    super.dispose();
  }
  
  protected Scene createFxScene() {
    Demo _demo = new Demo();
    return _demo.createScene();
  }
  
  @Override
  public void setFocus() {
    this.canvas.setFocus();
    this.setFxFocus();
  }
  
  protected void setFxFocus() {
  }
}
