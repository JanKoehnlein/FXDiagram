package de.fxdiagram.core.layout.tests;

import java.util.ArrayList;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Bounds;
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
    final Procedure1<Rectangle> _function = new Procedure1<Rectangle>() {
      public void apply(final Rectangle it) {
        ReadOnlyObjectProperty<Bounds> _boundsInLocalProperty = it.boundsInLocalProperty();
        final InvalidationListener _function = new InvalidationListener() {
          public void invalidated(final Observable it) {
            Object _object = new Object();
            result.add(_object);
          }
        };
        _boundsInLocalProperty.addListener(_function);
      }
    };
    final Rectangle r = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function);
    r.setWidth(7);
    boolean _isEmpty = result.isEmpty();
    Assert.assertFalse(_isEmpty);
  }
}
