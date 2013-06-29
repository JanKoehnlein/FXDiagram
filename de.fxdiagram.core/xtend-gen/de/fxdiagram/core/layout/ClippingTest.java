package de.fxdiagram.core.layout;

import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("all")
public class ClippingTest {
  private final static double EPS = 1E-6;
  
  @Test
  public void boundsInLocalConsiderClipping() {
    Rectangle _rectangle = new Rectangle();
    final Procedure1<Rectangle> _function = new Procedure1<Rectangle>() {
        public void apply(final Rectangle it) {
          it.setX(1);
          it.setY(2);
          it.setWidth(3);
          it.setHeight(4);
          Rectangle _rectangle = new Rectangle();
          final Procedure1<Rectangle> _function = new Procedure1<Rectangle>() {
              public void apply(final Rectangle it) {
                it.setX(2);
                it.setY(3);
                it.setWidth(1);
                it.setHeight(1);
              }
            };
          Rectangle _doubleArrow = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function);
          it.setClip(_doubleArrow);
        }
      };
    final Rectangle rect = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function);
    Bounds _boundsInLocal = rect.getBoundsInLocal();
    double _minX = _boundsInLocal.getMinX();
    Assert.assertEquals(2, _minX, ClippingTest.EPS);
    Bounds _boundsInLocal_1 = rect.getBoundsInLocal();
    double _minY = _boundsInLocal_1.getMinY();
    Assert.assertEquals(3, _minY, ClippingTest.EPS);
    Bounds _boundsInLocal_2 = rect.getBoundsInLocal();
    double _width = _boundsInLocal_2.getWidth();
    Assert.assertEquals(1, _width, ClippingTest.EPS);
    Bounds _boundsInLocal_3 = rect.getBoundsInLocal();
    double _height = _boundsInLocal_3.getHeight();
    Assert.assertEquals(1, _height, ClippingTest.EPS);
  }
}
