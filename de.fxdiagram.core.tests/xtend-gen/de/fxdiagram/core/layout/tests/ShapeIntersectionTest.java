package de.fxdiagram.core.layout.tests;

import com.google.common.collect.Iterables;
import java.util.Collections;
import javafx.collections.ObservableList;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.junit.Test;

@SuppressWarnings("all")
public class ShapeIntersectionTest {
  @Test
  public void testCirclePolyline() {
    Circle _circle = new Circle();
    final Procedure1<Circle> _function = (Circle it) -> {
      it.setRadius(1);
      it.setFill(null);
    };
    final Circle circle = ObjectExtensions.<Circle>operator_doubleArrow(_circle, _function);
    Polyline _polyline = new Polyline();
    final Procedure1<Polyline> _function_1 = (Polyline it) -> {
      ObservableList<Double> _points = it.getPoints();
      Iterables.<Double>addAll(_points, Collections.<Double>unmodifiableList(CollectionLiterals.<Double>newArrayList(Double.valueOf((-1.0)), Double.valueOf((-1.0)), Double.valueOf(1.0), Double.valueOf(1.0))));
    };
    final Polyline line = ObjectExtensions.<Polyline>operator_doubleArrow(_polyline, _function_1);
    final Shape intersect = Shape.intersect(circle, line);
    InputOutput.<Shape>println(intersect);
  }
}
