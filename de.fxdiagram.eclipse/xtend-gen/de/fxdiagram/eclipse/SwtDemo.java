package de.fxdiagram.eclipse;

import de.fxdiagram.examples.Demo;
import javafx.scene.Scene;

@SuppressWarnings("all")
public class SwtDemo {
  private static Scene createScene() {
    Demo _demo = new Demo();
    return _demo.createScene();
  }
  
  public static void main(final String[] args) {
    throw new Error("Unresolved compilation problems:"
      + "\nSwtToFXGestureConverter cannot be resolved."
      + "\ndispose cannot be resolved");
  }
}
