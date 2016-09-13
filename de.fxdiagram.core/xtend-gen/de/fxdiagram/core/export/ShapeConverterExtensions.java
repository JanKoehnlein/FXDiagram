package de.fxdiagram.core.export;

import com.google.common.base.Objects;
import java.util.Arrays;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;
import javafx.scene.shape.VLineTo;
import javafx.scene.text.Text;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * Copied from the <a href="https://github.com/JFXtras">JFXtras</a> project and converted to Xtend.
 */
@SuppressWarnings("all")
public class ShapeConverterExtensions {
  private final static double KAPPA = 0.5522847498307935;
  
  public static SVGPath toSvgPath(final Shape shape) {
    SVGPath _sVGPath = new SVGPath();
    final Procedure1<SVGPath> _function = (SVGPath it) -> {
      String _svgString = ShapeConverterExtensions.toSvgString(shape);
      it.setContent(_svgString);
    };
    return ObjectExtensions.<SVGPath>operator_doubleArrow(_sVGPath, _function);
  }
  
  public static String toSvgString(final Shape shape) {
    String _internalToSvgString = ShapeConverterExtensions.internalToSvgString(shape);
    String _replaceAll = _internalToSvgString.replaceAll("\\s+", " ");
    return _replaceAll.trim();
  }
  
  protected static String _internalToSvgString(final SVGPath svgPath) {
    return svgPath.getContent();
  }
  
  protected static String _internalToSvgString(final Text text) {
    String _xblockexpression = null;
    {
      Rectangle _rectangle = new Rectangle(0, 0);
      Shape _subtract = Shape.subtract(text, _rectangle);
      final Path path = ((Path) _subtract);
      _xblockexpression = ShapeConverterExtensions.toSvgString(path);
    }
    return _xblockexpression;
  }
  
  protected static String _internalToSvgString(final Line line) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("M ");
    double _startX = line.getStartX();
    _builder.append(_startX, "");
    _builder.append(" ");
    double _startY = line.getStartY();
    _builder.append(_startY, "");
    _builder.append(" L ");
    double _endX = line.getEndX();
    _builder.append(_endX, "");
    _builder.append(" ");
    double _endY = line.getEndY();
    _builder.append(_endY, "");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  protected static String _internalToSvgString(final Arc arc) {
    String _xblockexpression = null;
    {
      final double centerX = arc.getCenterX();
      final double centerY = arc.getCenterY();
      final double radiusX = arc.getRadiusX();
      final double radiusY = arc.getRadiusY();
      double _startAngle = arc.getStartAngle();
      final double startAngle = Math.toRadians(_startAngle);
      final double length = arc.getLength();
      double _length = arc.getLength();
      double _plus = (_length + startAngle);
      final double alpha = Math.toRadians(_plus);
      final double phiOffset = Math.toRadians((-90));
      double _cos = Math.cos(phiOffset);
      double _multiply = (_cos * radiusX);
      double _cos_1 = Math.cos(startAngle);
      double _multiply_1 = (_multiply * _cos_1);
      double _plus_1 = (centerX + _multiply_1);
      double _sin = Math.sin((-phiOffset));
      double _multiply_2 = (_sin * radiusY);
      double _sin_1 = Math.sin(startAngle);
      double _multiply_3 = (_multiply_2 * _sin_1);
      final double startX = (_plus_1 + _multiply_3);
      double _sin_2 = Math.sin(phiOffset);
      double _multiply_4 = (_sin_2 * radiusX);
      double _cos_2 = Math.cos(startAngle);
      double _multiply_5 = (_multiply_4 * _cos_2);
      double _plus_2 = (centerY + _multiply_5);
      double _cos_3 = Math.cos(phiOffset);
      double _multiply_6 = (_cos_3 * radiusY);
      double _sin_3 = Math.sin(startAngle);
      double _multiply_7 = (_multiply_6 * _sin_3);
      final double startY = (_plus_2 + _multiply_7);
      double _cos_4 = Math.cos(phiOffset);
      double _multiply_8 = (_cos_4 * radiusX);
      double _cos_5 = Math.cos(alpha);
      double _multiply_9 = (_multiply_8 * _cos_5);
      double _plus_3 = (centerX + _multiply_9);
      double _sin_4 = Math.sin((-phiOffset));
      double _multiply_10 = (_sin_4 * radiusY);
      double _sin_5 = Math.sin(alpha);
      double _multiply_11 = (_multiply_10 * _sin_5);
      final double endX = (_plus_3 + _multiply_11);
      double _sin_6 = Math.sin(phiOffset);
      double _multiply_12 = (_sin_6 * radiusX);
      double _cos_6 = Math.cos(alpha);
      double _multiply_13 = (_multiply_12 * _cos_6);
      double _plus_4 = (centerY + _multiply_13);
      double _cos_7 = Math.cos(phiOffset);
      double _multiply_14 = (_cos_7 * radiusY);
      double _sin_7 = Math.sin(alpha);
      double _multiply_15 = (_multiply_14 * _sin_7);
      final double endY = (_plus_4 + _multiply_15);
      final int xAxisRot = 0;
      int _xifexpression = (int) 0;
      if ((length > 180)) {
        _xifexpression = 1;
      } else {
        _xifexpression = 0;
      }
      final int largeArc = _xifexpression;
      int _xifexpression_1 = (int) 0;
      if ((length > 0)) {
        _xifexpression_1 = 1;
      } else {
        _xifexpression_1 = 0;
      }
      final int sweep = _xifexpression_1;
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("M ");
      _builder.append(centerX, "");
      _builder.append(" ");
      _builder.append(centerY, "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      {
        ArcType _type = arc.getType();
        boolean _equals = Objects.equal(ArcType.ROUND, _type);
        if (_equals) {
          _builder.append("h ");
          _builder.append((startX - centerX), "");
          _builder.append(" v ");
          _builder.append((startY - centerY), "");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("A ");
      _builder.append(radiusX, "");
      _builder.append(" ");
      _builder.append(radiusY, "");
      _builder.append(" ");
      _builder.append(xAxisRot, "");
      _builder.append(" ");
      _builder.append(largeArc, "");
      _builder.append(" ");
      _builder.append(sweep, "");
      _builder.append(" ");
      _builder.append(endX, "");
      _builder.append(" ");
      _builder.append(endY, "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      {
        if ((Objects.equal(ArcType.CHORD, arc.getType()) || Objects.equal(ArcType.ROUND, arc.getType()))) {
          _builder.append("Z");
          _builder.newLine();
        }
      }
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  protected static String _internalToSvgString(final QuadCurve quadCurve) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("M ");
    double _startX = quadCurve.getStartX();
    _builder.append(_startX, "");
    _builder.append(" ");
    double _startY = quadCurve.getStartY();
    _builder.append(_startY, "");
    _builder.append(" ");
    _builder.newLineIfNotEmpty();
    _builder.append("Q ");
    double _controlX = quadCurve.getControlX();
    _builder.append(_controlX, "");
    _builder.append(" ");
    double _controlY = quadCurve.getControlY();
    _builder.append(_controlY, "");
    _builder.append(" ");
    _builder.newLineIfNotEmpty();
    double _endX = quadCurve.getEndX();
    _builder.append(_endX, "");
    _builder.append(" ");
    double _endY = quadCurve.getEndY();
    _builder.append(_endY, "");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  protected static String _internalToSvgString(final CubicCurve cubicCurve) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("M ");
    double _startX = cubicCurve.getStartX();
    _builder.append(_startX, "");
    _builder.append(" ");
    double _startY = cubicCurve.getStartY();
    _builder.append(_startY, "");
    _builder.append(" ");
    _builder.newLineIfNotEmpty();
    _builder.append("C ");
    double _controlX1 = cubicCurve.getControlX1();
    _builder.append(_controlX1, "");
    _builder.append(" ");
    double _controlY1 = cubicCurve.getControlY1();
    _builder.append(_controlY1, "");
    _builder.append(" ");
    _builder.newLineIfNotEmpty();
    double _controlX2 = cubicCurve.getControlX2();
    _builder.append(_controlX2, "");
    _builder.append(" ");
    double _controlY2 = cubicCurve.getControlY2();
    _builder.append(_controlY2, "");
    _builder.append(" ");
    _builder.newLineIfNotEmpty();
    double _endX = cubicCurve.getEndX();
    _builder.append(_endX, "");
    _builder.append(" ");
    double _endY = cubicCurve.getEndY();
    _builder.append(_endY, "");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  protected static String _internalToSvgString(final Rectangle rectangle) {
    String _xblockexpression = null;
    {
      final Bounds bounds = rectangle.getBoundsInLocal();
      String _xifexpression = null;
      if ((((Double.valueOf(rectangle.getArcWidth()).compareTo(Double.valueOf(0.0))) == 0) && ((Double.valueOf(rectangle.getArcHeight()).compareTo(Double.valueOf(0.0))) == 0))) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("M ");
        double _minX = bounds.getMinX();
        _builder.append(_minX, "");
        _builder.append(" ");
        double _minY = bounds.getMinY();
        _builder.append(_minY, "");
        _builder.append(" ");
        _builder.newLineIfNotEmpty();
        _builder.append("H ");
        double _maxX = bounds.getMaxX();
        _builder.append(_maxX, "");
        _builder.append(" V ");
        double _maxY = bounds.getMaxY();
        _builder.append(_maxY, "");
        _builder.append(" ");
        _builder.newLineIfNotEmpty();
        _builder.append("H ");
        double _minX_1 = bounds.getMinX();
        _builder.append(_minX_1, "");
        _builder.append(" V ");
        double _minY_1 = bounds.getMinY();
        _builder.append(_minY_1, "");
        _builder.append(" Z");
        _builder.newLineIfNotEmpty();
        _xifexpression = _builder.toString();
      } else {
        String _xblockexpression_1 = null;
        {
          final double x = bounds.getMinX();
          final double y = bounds.getMinY();
          final double width = bounds.getWidth();
          final double height = bounds.getHeight();
          final double arcWidth = rectangle.getArcWidth();
          final double arcHeight = rectangle.getArcHeight();
          final double r = (x + width);
          final double b = (y + height);
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("M ");
          _builder_1.append((x + arcWidth), "");
          _builder_1.append(" ");
          _builder_1.append(y, "");
          _builder_1.append(" ");
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("L ");
          _builder_1.append((r - arcWidth), "");
          _builder_1.append(" ");
          _builder_1.append(y, "");
          _builder_1.append(" Q ");
          _builder_1.append(r, "");
          _builder_1.append(" ");
          _builder_1.append(y, "");
          _builder_1.append(" ");
          _builder_1.append(r, "");
          _builder_1.append(" ");
          _builder_1.append((y + arcHeight), "");
          _builder_1.append(" ");
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("L ");
          _builder_1.append(r, "");
          _builder_1.append(" ");
          _builder_1.append(((y + height) - arcHeight), "");
          _builder_1.append(" Q ");
          _builder_1.append(r, "");
          _builder_1.append(" ");
          _builder_1.append(b, "");
          _builder_1.append(" ");
          _builder_1.append((r - arcWidth), "");
          _builder_1.append(" ");
          _builder_1.append(b, "");
          _builder_1.append(" ");
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("L ");
          _builder_1.append((x + arcWidth), "");
          _builder_1.append(" ");
          _builder_1.append(b, "");
          _builder_1.append(" Q ");
          _builder_1.append(x, "");
          _builder_1.append(" ");
          _builder_1.append(b, "");
          _builder_1.append(" ");
          _builder_1.append(x, "");
          _builder_1.append(" ");
          _builder_1.append((b - arcHeight), "");
          _builder_1.append(" ");
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("L ");
          _builder_1.append(x, "");
          _builder_1.append(" ");
          _builder_1.append((y + arcHeight), "");
          _builder_1.append(" Q ");
          _builder_1.append(x, "");
          _builder_1.append(" ");
          _builder_1.append(y, "");
          _builder_1.append(" ");
          _builder_1.append((x + arcWidth), "");
          _builder_1.append(" ");
          _builder_1.append(y, "");
          _builder_1.append(" Z");
          _builder_1.newLineIfNotEmpty();
          _xblockexpression_1 = _builder_1.toString();
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  protected static String _internalToSvgString(final Circle circle) {
    String _xblockexpression = null;
    {
      final double centerX = circle.getCenterX();
      final double centerY = circle.getCenterY();
      final double radius = circle.getRadius();
      final double controlDistance = (radius * ShapeConverterExtensions.KAPPA);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("M ");
      _builder.append(centerX, "");
      _builder.append(" ");
      _builder.append((centerY - radius), "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      _builder.append("C ");
      _builder.append((centerX + controlDistance), "");
      _builder.append(" ");
      _builder.append((centerY - radius), "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      _builder.append((centerX + radius), "");
      _builder.append(" ");
      _builder.append((centerY - controlDistance), "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      _builder.append((centerX + radius), "");
      _builder.append(" ");
      _builder.append(centerY, "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      _builder.append("C ");
      _builder.append((centerX + radius), "");
      _builder.append(" ");
      _builder.append((centerY + controlDistance), "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      _builder.append((centerX + controlDistance), "");
      _builder.append(" ");
      _builder.append((centerY + radius), "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      _builder.append(centerX, "");
      _builder.append(" ");
      _builder.append((centerY + radius), "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      _builder.append("C ");
      _builder.append((centerX - controlDistance), "");
      _builder.append(" ");
      _builder.append((centerY + radius), "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      _builder.append((centerX - radius), "");
      _builder.append(" ");
      _builder.append((centerY + controlDistance), "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      _builder.append((centerX - radius), "");
      _builder.append(" ");
      _builder.append(centerY, "");
      _builder.append("  ");
      _builder.newLineIfNotEmpty();
      _builder.append("C ");
      _builder.append((centerX - radius), "");
      _builder.append(" ");
      _builder.append((centerY - controlDistance), "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      _builder.append((centerX - controlDistance), "");
      _builder.append(" ");
      _builder.append((centerY - radius), "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      _builder.append(centerX, "");
      _builder.append(" ");
      _builder.append((centerY - radius), "");
      _builder.append(" Z");
      _builder.newLineIfNotEmpty();
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  protected static String _internalToSvgString(final Ellipse ellipse) {
    String _xblockexpression = null;
    {
      double _xifexpression = (double) 0;
      double _centerX = ellipse.getCenterX();
      boolean _equals = (_centerX == 0);
      if (_equals) {
        _xifexpression = ellipse.getRadiusX();
      } else {
        _xifexpression = ellipse.getCenterX();
      }
      final double centerX = _xifexpression;
      double _xifexpression_1 = (double) 0;
      double _centerY = ellipse.getCenterY();
      boolean _equals_1 = (_centerY == 0);
      if (_equals_1) {
        _xifexpression_1 = ellipse.getRadiusY();
      } else {
        _xifexpression_1 = ellipse.getCenterY();
      }
      final double centerY = _xifexpression_1;
      final double radiusX = ellipse.getRadiusX();
      final double radiusY = ellipse.getRadiusY();
      final double controlDistanceX = (radiusX * ShapeConverterExtensions.KAPPA);
      final double controlDistanceY = (radiusY * ShapeConverterExtensions.KAPPA);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("M ");
      _builder.append(centerX, "");
      _builder.append(" ");
      _builder.append((centerY - radiusY), "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      _builder.append("C ");
      _builder.append((centerX + controlDistanceX), "");
      _builder.append(" ");
      _builder.append((centerY - radiusY), "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      _builder.append((centerX + radiusX), "");
      _builder.append(" ");
      _builder.append((centerY - controlDistanceY), "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      _builder.append((centerX + radiusX), "");
      _builder.append(" ");
      _builder.append(centerY, "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      _builder.append("C ");
      _builder.append((centerX + radiusX), "");
      _builder.append(" ");
      _builder.append((centerY + controlDistanceY), "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      _builder.append((centerX + controlDistanceX), "");
      _builder.append(" ");
      _builder.append((centerY + radiusY), "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      _builder.append(centerX, "");
      _builder.append(" ");
      _builder.append((centerY + radiusY), "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      _builder.append("C ");
      _builder.append((centerX - controlDistanceX), "");
      _builder.append(" ");
      _builder.append((centerY + radiusY), "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      _builder.append((centerX - radiusX), "");
      _builder.append(" ");
      _builder.append((centerY + controlDistanceY), "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      _builder.append((centerX - radiusX), "");
      _builder.append(" ");
      _builder.append(centerY, "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      _builder.append("C ");
      _builder.append((centerX - radiusX), "");
      _builder.append(" ");
      _builder.append((centerY - controlDistanceY), "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      _builder.append((centerX - controlDistanceX), "");
      _builder.append(" ");
      _builder.append((centerY - radiusY), "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      _builder.append(centerX, "");
      _builder.append(" ");
      _builder.append((centerY - radiusY), "");
      _builder.append(" Z");
      _builder.newLineIfNotEmpty();
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  protected static String _internalToSvgString(final Path path) {
    String _xblockexpression = null;
    {
      final StringBuilder it = new StringBuilder();
      ObservableList<PathElement> _elements = path.getElements();
      for (final PathElement element : _elements) {
        boolean _matched = false;
        if (element instanceof MoveTo) {
          _matched=true;
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("M ");
          double _x = ((MoveTo)element).getX();
          _builder.append(_x, "");
          _builder.append(" ");
          double _y = ((MoveTo)element).getY();
          _builder.append(_y, "");
          _builder.append(" ");
          it.append(_builder);
        }
        if (!_matched) {
          if (element instanceof LineTo) {
            _matched=true;
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("L ");
            double _x = ((LineTo)element).getX();
            _builder.append(_x, "");
            _builder.append(" ");
            double _y = ((LineTo)element).getY();
            _builder.append(_y, "");
            _builder.append(" ");
            it.append(_builder);
          }
        }
        if (!_matched) {
          if (element instanceof CubicCurveTo) {
            _matched=true;
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("C ");
            double _controlX1 = ((CubicCurveTo)element).getControlX1();
            _builder.append(_controlX1, "");
            _builder.append(" ");
            double _controlY1 = ((CubicCurveTo)element).getControlY1();
            _builder.append(_controlY1, "");
            _builder.append(" ");
            double _controlX2 = ((CubicCurveTo)element).getControlX2();
            _builder.append(_controlX2, "");
            _builder.append(" ");
            double _controlY2 = ((CubicCurveTo)element).getControlY2();
            _builder.append(_controlY2, "");
            _builder.append(" ");
            double _x = ((CubicCurveTo)element).getX();
            _builder.append(_x, "");
            _builder.append(" ");
            double _y = ((CubicCurveTo)element).getY();
            _builder.append(_y, "");
            _builder.append(" ");
            it.append(_builder);
          }
        }
        if (!_matched) {
          if (element instanceof QuadCurveTo) {
            _matched=true;
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("Q ");
            double _controlX = ((QuadCurveTo)element).getControlX();
            _builder.append(_controlX, "");
            _builder.append(" ");
            double _controlY = ((QuadCurveTo)element).getControlY();
            _builder.append(_controlY, "");
            _builder.append(" ");
            double _x = ((QuadCurveTo)element).getX();
            _builder.append(_x, "");
            _builder.append(" ");
            double _y = ((QuadCurveTo)element).getY();
            _builder.append(_y, "");
            _builder.append(" ");
            it.append(_builder);
          }
        }
        if (!_matched) {
          if (element instanceof ArcTo) {
            _matched=true;
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("A ");
            double _x = ((ArcTo)element).getX();
            _builder.append(_x, "");
            _builder.append(" ");
            double _y = ((ArcTo)element).getY();
            _builder.append(_y, "");
            _builder.append(" ");
            double _radiusX = ((ArcTo)element).getRadiusX();
            _builder.append(_radiusX, "");
            _builder.append(" ");
            double _radiusY = ((ArcTo)element).getRadiusY();
            _builder.append(_radiusY, "");
            _builder.append(" ");
            it.append(_builder);
          }
        }
        if (!_matched) {
          if (element instanceof HLineTo) {
            _matched=true;
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("H ");
            double _x = ((HLineTo)element).getX();
            _builder.append(_x, "");
            _builder.append(" ");
            it.append(_builder);
          }
        }
        if (!_matched) {
          if (element instanceof VLineTo) {
            _matched=true;
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("V ");
            double _y = ((VLineTo)element).getY();
            _builder.append(_y, "");
            _builder.append(" ");
            it.append(_builder);
          }
        }
        if (!_matched) {
          if (element instanceof ClosePath) {
            _matched=true;
            it.append("Z");
          }
        }
      }
      _xblockexpression = it.toString();
    }
    return _xblockexpression;
  }
  
  protected static String _internalToSvgString(final Polygon polygon) {
    ObservableList<Double> _points = polygon.getPoints();
    String _pointsToSvgString = ShapeConverterExtensions.pointsToSvgString(_points);
    return (_pointsToSvgString + "Z");
  }
  
  protected static String _internalToSvgString(final Polyline polyline) {
    ObservableList<Double> _points = polyline.getPoints();
    return ShapeConverterExtensions.pointsToSvgString(_points);
  }
  
  protected static String pointsToSvgString(final List<Double> points) {
    String _xblockexpression = null;
    {
      final StringBuilder it = new StringBuilder();
      final int size = points.size();
      if (((size % 2) == 0)) {
        final List<Double> coordinates = points;
        int i = 0;
        while ((i < size)) {
          {
            StringConcatenation _builder = new StringConcatenation();
            {
              if ((i == 0)) {
                _builder.append("M");
                _builder.newLine();
              } else {
                _builder.append("L");
                _builder.newLine();
              }
            }
            Double _get = coordinates.get(i);
            _builder.append(_get, "");
            _builder.append(" ");
            Double _get_1 = coordinates.get((i + 1));
            _builder.append(_get_1, "");
            _builder.append(" ");
            _builder.newLineIfNotEmpty();
            it.append(_builder);
            i = (i + 2);
          }
        }
      }
      _xblockexpression = it.toString();
    }
    return _xblockexpression;
  }
  
  protected static String internalToSvgString(final Shape arc) {
    if (arc instanceof Arc) {
      return _internalToSvgString((Arc)arc);
    } else if (arc instanceof Circle) {
      return _internalToSvgString((Circle)arc);
    } else if (arc instanceof CubicCurve) {
      return _internalToSvgString((CubicCurve)arc);
    } else if (arc instanceof Ellipse) {
      return _internalToSvgString((Ellipse)arc);
    } else if (arc instanceof Line) {
      return _internalToSvgString((Line)arc);
    } else if (arc instanceof Path) {
      return _internalToSvgString((Path)arc);
    } else if (arc instanceof Polygon) {
      return _internalToSvgString((Polygon)arc);
    } else if (arc instanceof Polyline) {
      return _internalToSvgString((Polyline)arc);
    } else if (arc instanceof QuadCurve) {
      return _internalToSvgString((QuadCurve)arc);
    } else if (arc instanceof Rectangle) {
      return _internalToSvgString((Rectangle)arc);
    } else if (arc instanceof SVGPath) {
      return _internalToSvgString((SVGPath)arc);
    } else if (arc instanceof Text) {
      return _internalToSvgString((Text)arc);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(arc).toString());
    }
  }
}
