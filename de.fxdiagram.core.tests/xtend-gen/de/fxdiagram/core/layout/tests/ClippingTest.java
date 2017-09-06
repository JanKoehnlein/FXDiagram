package de.fxdiagram.core.layout.tests;

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
    Assert.assertEquals(2, rect.getBoundsInLocal().getMinX(), ClippingTest.EPS);
    Assert.assertEquals(3, rect.getBoundsInLocal().getMinY(), ClippingTest.EPS);
    Assert.assertEquals(1, rect.getBoundsInLocal().getWidth(), ClippingTest.EPS);
    Assert.assertEquals(1, rect.getBoundsInLocal().getHeight(), ClippingTest.EPS);
  }
}
