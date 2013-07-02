package de.fxdiagram.core.transform.tests;

import de.fxdiagram.core.export.ShapeConverterExtensions;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("all")
public class ShapeConverterTest {
  @Test
  public void testLine() {
    Line _line = new Line();
    final Procedure1<Line> _function = new Procedure1<Line>() {
        public void apply(final Line it) {
          it.setStartX(1);
          it.setStartY(2);
          it.setEndX(3);
          it.setEndY(4);
        }
      };
    Line _doubleArrow = ObjectExtensions.<Line>operator_doubleArrow(_line, _function);
    this.mustBecome(_doubleArrow, "M 1.0 2.0 L 3.0 4.0");
  }
  
  @Test
  public void testCircle() {
    Circle _circle = new Circle();
    final Procedure1<Circle> _function = new Procedure1<Circle>() {
        public void apply(final Circle it) {
          it.setRadius(1);
          it.setCenterX(2);
          it.setCenterY(2);
        }
      };
    Circle _doubleArrow = ObjectExtensions.<Circle>operator_doubleArrow(_circle, _function);
    this.mustBecome(_doubleArrow, "M 2.0 1.0 C 2.5522847498307932 1.0 3.0 1.4477152501692065 3.0 2.0 C 3.0 2.5522847498307932 2.5522847498307932 3.0 2.0 3.0 C 1.4477152501692065 3.0 1.0 2.5522847498307932 1.0 2.0 C 1.0 1.4477152501692065 1.4477152501692065 1.0 2.0 1.0 Z");
  }
  
  @Test
  public void testArc() {
    Arc _arc = new Arc();
    final Procedure1<Arc> _function = new Procedure1<Arc>() {
        public void apply(final Arc it) {
          it.setRadiusX(1);
          it.setRadiusY(2);
          it.setCenterX(3);
          it.setCenterY(4);
          it.setStartAngle(0);
          it.setLength(90);
        }
      };
    Arc _doubleArrow = ObjectExtensions.<Arc>operator_doubleArrow(_arc, _function);
    this.mustBecome(_doubleArrow, "M 3.0 4.0 A 1.0 2.0 0 0 1 5.0 4.0");
  }
  
  @Test
  public void testQuadCurve() {
    QuadCurve _quadCurve = new QuadCurve();
    final Procedure1<QuadCurve> _function = new Procedure1<QuadCurve>() {
        public void apply(final QuadCurve it) {
          it.setStartX(1);
          it.setStartY(2);
          it.setControlX(3);
          it.setControlY(4);
          it.setEndX(5);
          it.setEndY(6);
        }
      };
    QuadCurve _doubleArrow = ObjectExtensions.<QuadCurve>operator_doubleArrow(_quadCurve, _function);
    this.mustBecome(_doubleArrow, "M 1.0 2.0 Q 3.0 4.0 5.0 6.0");
  }
  
  @Test
  public void testCubicCurve() {
    CubicCurve _cubicCurve = new CubicCurve();
    final Procedure1<CubicCurve> _function = new Procedure1<CubicCurve>() {
        public void apply(final CubicCurve it) {
          it.setStartX(1);
          it.setStartY(2);
          it.setControlX1(3);
          it.setControlY1(4);
          it.setControlX2(5);
          it.setControlY2(6);
          it.setEndX(7);
          it.setEndY(8);
        }
      };
    CubicCurve _doubleArrow = ObjectExtensions.<CubicCurve>operator_doubleArrow(_cubicCurve, _function);
    this.mustBecome(_doubleArrow, "M 1.0 2.0 C 3.0 4.0 5.0 6.0 7.0 8.0");
  }
  
  @Test
  public void testRectangle() {
    Rectangle _rectangle = new Rectangle();
    final Procedure1<Rectangle> _function = new Procedure1<Rectangle>() {
        public void apply(final Rectangle it) {
          it.setX(1);
          it.setY(2);
          it.setWidth(3);
          it.setHeight(4);
        }
      };
    Rectangle _doubleArrow = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function);
    this.mustBecome(_doubleArrow, "M 1.0 2.0 H 4.0 V 6.0 H 1.0 V 2.0 Z");
  }
  
  @Test
  public void testRoundedRectangle() {
    Rectangle _rectangle = new Rectangle();
    final Procedure1<Rectangle> _function = new Procedure1<Rectangle>() {
        public void apply(final Rectangle it) {
          it.setX(1);
          it.setY(2);
          it.setWidth(3);
          it.setHeight(4);
          it.setArcWidth(5);
          it.setArcHeight(6);
        }
      };
    Rectangle _doubleArrow = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function);
    this.mustBecome(_doubleArrow, "M 6.0 2.0 L -1.0 2.0 Q 4.0 2.0 4.0 8.0 L 4.0 0.0 Q 4.0 6.0 -1.0 6.0 L 6.0 6.0 Q 1.0 6.0 1.0 0.0 L 1.0 8.0 Q 1.0 2.0 6.0 2.0 Z");
  }
  
  protected void mustBecome(final Shape shape, final String expectation) {
    String _svgString = ShapeConverterExtensions.toSvgString(shape);
    Assert.assertEquals(expectation, _svgString);
  }
}
