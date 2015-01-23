package de.fxdiagram.core.layout.tests;

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
    final Procedure1<Rectangle> _function = (Rectangle it) -> {
      it.setX(1);
      it.setY(2);
      it.setWidth(3);
      it.setHeight(4);
      Rectangle _rectangle_1 = new Rectangle();
      final Procedure1<Rectangle> _function_1 = (Rectangle it_1) -> {
        it_1.setX(2);
        it_1.setY(3);
        it_1.setWidth(1);
        it_1.setHeight(1);
      };
      Rectangle _doubleArrow = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle_1, _function_1);
      it.setClip(_doubleArrow);
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
