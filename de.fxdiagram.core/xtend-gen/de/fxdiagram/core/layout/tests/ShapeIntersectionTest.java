package de.fxdiagram.core.layout.tests;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.Collections;
import javafx.collections.ObservableList;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.junit.Test;

@SuppressWarnings("all")
public class ShapeIntersectionTest {
  @Test
  public void testCirclePolyline() {
    Circle _circle = new Circle();
    final Procedure1<Circle> _function = new Procedure1<Circle>() {
      public void apply(final Circle it) {
        it.setRadius(1);
        it.setFill(null);
      }
    };
    final Circle circle = ObjectExtensions.<Circle>operator_doubleArrow(_circle, _function);
    Polyline _polyline = new Polyline();
    final Procedure1<Polyline> _function_1 = new Procedure1<Polyline>() {
      public void apply(final Polyline it) {
        ObservableList<Double> _points = it.getPoints();
        double _minus = (-1.0);
        double _minus_1 = (-1.0);
        Iterables.<Double>addAll(_points, Collections.<Double>unmodifiableList(Lists.<Double>newArrayList(Double.valueOf(_minus), Double.valueOf(_minus_1), Double.valueOf(1.0), Double.valueOf(1.0))));
      }
    };
    final Polyline line = ObjectExtensions.<Polyline>operator_doubleArrow(_polyline, _function_1);
    final Shape intersect = Shape.intersect(circle, line);
    InputOutput.<Shape>println(intersect);
  }
}
