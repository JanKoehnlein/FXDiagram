package de.fxdiagram.eclipse;

import de.fxdiagram.examples.Demo;
import javafx.embed.swt.FXCanvas;
import javafx.scene.Scene;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

@SuppressWarnings("all")
public class FXDiagramViewPart extends ViewPart {
  private FXCanvas canvas;
  
  private /* SwtToFXGestureConverter */Object gestureConverter;
  
  public void createPartControl(final Composite parent) {
    throw new Error("Unresolved compilation problems:"
      + "\nSwtToFXGestureConverter cannot be resolved.");
  }
  
  public void dispose() {
    throw new Error("Unresolved compilation problems:"
      + "\ndispose cannot be resolved");
  }
  
  protected Scene createFxScene() {
    Demo _demo = new Demo();
    return _demo.createScene();
  }
  
  public void setFocus() {
    this.canvas.setFocus();
    this.setFxFocus();
  }
  
  protected void setFxFocus() {
  }
}
