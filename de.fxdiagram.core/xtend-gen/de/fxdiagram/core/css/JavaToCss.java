package de.fxdiagram.core.css;

import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class JavaToCss {
  public static CharSequence toCss(final Paint paint) {
    CharSequence _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (paint instanceof Color) {
        _matched=true;
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("rgb(");
        double _red = ((Color)paint).getRed();
        double _multiply = (255 * _red);
        _builder.append(((int) _multiply), "");
        _builder.append(", ");
        double _green = ((Color)paint).getGreen();
        double _multiply_1 = (255 * _green);
        _builder.append(((int) _multiply_1), "");
        _builder.append(", ");
        double _blue = ((Color)paint).getBlue();
        double _multiply_2 = (255 * _blue);
        _builder.append(((int) _multiply_2), "");
        _builder.append(")");
        _switchResult = _builder;
      }
    }
    if (!_matched) {
      if (paint instanceof LinearGradient) {
        _matched=true;
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("linear-gradient(");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("from ");
        double _startX = ((LinearGradient)paint).getStartX();
        double _multiply = (_startX * 100);
        _builder.append(_multiply, "\t");
        _builder.append("% ");
        double _startY = ((LinearGradient)paint).getStartY();
        double _multiply_1 = (_startY * 100);
        _builder.append(_multiply_1, "\t");
        _builder.append("% ");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("to ");
        double _endX = ((LinearGradient)paint).getEndX();
        double _multiply_2 = (_endX * 100);
        _builder.append(_multiply_2, "\t");
        _builder.append("% ");
        double _endY = ((LinearGradient)paint).getEndY();
        double _multiply_3 = (_endY * 100);
        _builder.append(_multiply_3, "\t");
        _builder.append("%, ");
        _builder.newLineIfNotEmpty();
        {
          List<Stop> _stops = ((LinearGradient)paint).getStops();
          boolean _hasElements = false;
          for(final Stop stop : _stops) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(", ", "\t");
            }
            _builder.append("\t");
            Color _color = stop.getColor();
            CharSequence _css = JavaToCss.toCss(_color);
            _builder.append(_css, "\t");
            _builder.append(" ");
            double _offset = stop.getOffset();
            double _multiply_4 = (_offset * 100);
            _builder.append(_multiply_4, "\t");
            _builder.append("%");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append(")");
        _builder.newLine();
        _switchResult = _builder;
      }
    }
    if (!_matched) {
      _switchResult = "gray";
    }
    return _switchResult;
  }
  
  public static CharSequence toCss(final Insets it) {
    StringConcatenation _builder = new StringConcatenation();
    double _top = it.getTop();
    _builder.append(_top, "");
    _builder.append(" ");
    double _right = it.getRight();
    _builder.append(_right, "");
    _builder.append(" ");
    double _bottom = it.getBottom();
    _builder.append(_bottom, "");
    _builder.append(" ");
    double _left = it.getLeft();
    _builder.append(_left, "");
    return _builder;
  }
}
