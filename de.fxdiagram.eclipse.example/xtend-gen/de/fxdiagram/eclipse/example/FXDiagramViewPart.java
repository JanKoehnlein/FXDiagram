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
    this.canvas.setScene(this.createFxScene());
  }
  
  @Override
  public void dispose() {
    this.gestureConverter.dispose();
    super.dispose();
  }
  
  protected Scene createFxScene() {
    return new Demo().createScene();
  }
  
  @Override
  public void setFocus() {
    this.canvas.setFocus();
    this.setFxFocus();
  }
  
  protected void setFxFocus() {
  }
}
