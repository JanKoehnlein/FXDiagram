package de.fxdiagram.core.layout.tests;

import java.util.ArrayList;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.shape.Rectangle;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("all")
public class RectangleBoundsTest {
  @Test
  public void testBoundInLocalEvent() {
    final ArrayList<Object> result = CollectionLiterals.<Object>newArrayList();
    Rectangle _rectangle = new Rectangle();
    final Procedure1<Rectangle> _function = (Rectangle it) -> {
      final InvalidationListener _function_1 = (Observable it_1) -> {
        Object _object = new Object();
        result.add(_object);
      };
      it.boundsInLocalProperty().addListener(_function_1);
    };
    final Rectangle r = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function);
    r.setWidth(7);
    Assert.assertFalse(result.isEmpty());
  }
}
