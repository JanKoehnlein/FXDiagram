package de.itemis.javafx.diagram.export;

import com.google.common.base.Objects;
import de.itemis.javafx.diagram.XRootDiagram;
import de.itemis.javafx.diagram.export.ShapeConverterExtensions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class SvgExporter {
  private int currentID;
  
  private List<String> defs;
  
  public CharSequence toSvg(final XRootDiagram diagram) {
    CharSequence _xblockexpression = null;
    {
      ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList();
      this.defs = _newArrayList;
      this.currentID = 0;
      final Bounds bounds = diagram.getBoundsInLocal();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("<?xml version=\"1.0\" standalone=\"no\"?>");
      _builder.newLine();
      _builder.append("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" ");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("\"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">");
      _builder.newLine();
      _builder.append("<svg viewBox=\"");
      double _minX = bounds.getMinX();
      _builder.append(_minX, "");
      _builder.append(" ");
      double _minY = bounds.getMinY();
      _builder.append(_minY, "");
      _builder.append(" ");
      double _width = bounds.getWidth();
      _builder.append(_width, "");
      _builder.append(" ");
      double _height = bounds.getHeight();
      _builder.append(_height, "");
      _builder.append("\"");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">");
      _builder.newLine();
      {
        ObservableList<Node> _childrenUnmodifiable = diagram.getChildrenUnmodifiable();
        final Function1<Node,Boolean> _function = new Function1<Node,Boolean>() {
            public Boolean apply(final Node it) {
              boolean _isVisible = it.isVisible();
              return Boolean.valueOf(_isVisible);
            }
          };
        Iterable<Node> _filter = IterableExtensions.<Node>filter(_childrenUnmodifiable, _function);
        for(final Node child : _filter) {
          _builder.append("\t");
          CharSequence _svgElement = this.toSvgElement(child);
          _builder.append(_svgElement, "	");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.append("<defs>");
      _builder.newLine();
      {
        for(final String defElement : this.defs) {
          _builder.append("\t\t");
          _builder.append(defElement, "		");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.append("</defs>");
      _builder.newLine();
      _builder.append("</svg>");
      _builder.newLine();
      _xblockexpression = (_builder);
    }
    return _xblockexpression;
  }
  
  protected CharSequence _toSvgElement(final Text it) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<!-- ");
    Class<? extends Text> _class = it.getClass();
    String _name = _class.getName();
    _builder.append(_name, "");
    _builder.append(" -->");
    _builder.newLineIfNotEmpty();
    _builder.append("<path d=\"");
    String _svgString = this.toSvgString(it);
    _builder.append(_svgString, "");
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("fill=\"");
    Paint _fill = it.getFill();
    CharSequence _svgString_1 = this.toSvgString(_fill);
    _builder.append(_svgString_1, "	");
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    double _opacity = it.getOpacity();
    CharSequence _svgAttribute = this.toSvgAttribute(Double.valueOf(_opacity), "opacity", Double.valueOf(1.0));
    _builder.append(_svgAttribute, "	");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    double _strokeDashOffset = it.getStrokeDashOffset();
    CharSequence _svgAttribute_1 = this.toSvgAttribute(Double.valueOf(_strokeDashOffset), "stroke-dahsoffset", "0.0", "em");
    _builder.append(_svgAttribute_1, "	");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    StrokeLineCap _strokeLineCap = it.getStrokeLineCap();
    CharSequence _svgAttribute_2 = this.toSvgAttribute(_strokeLineCap, "stroke-linecap", "butt");
    _builder.append(_svgAttribute_2, "	");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    StrokeLineJoin _strokeLineJoin = it.getStrokeLineJoin();
    CharSequence _svgAttribute_3 = this.toSvgAttribute(_strokeLineJoin, "stroke-linejoin", "miter");
    _builder.append(_svgAttribute_3, "	");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    double _strokeMiterLimit = it.getStrokeMiterLimit();
    CharSequence _svgAttribute_4 = this.toSvgAttribute(Double.valueOf(_strokeMiterLimit), "stroke-miterLimit", Double.valueOf(4.0));
    _builder.append(_svgAttribute_4, "	");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("stroke-width=\"0.0\"");
    _builder.newLine();
    _builder.append("/>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _toSvgElement(final Shape it) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<!-- ");
    Class<? extends Shape> _class = it.getClass();
    String _name = _class.getName();
    _builder.append(_name, "");
    _builder.append(" -->");
    _builder.newLineIfNotEmpty();
    _builder.append("<path d=\"");
    String _svgString = this.toSvgString(it);
    _builder.append(_svgString, "");
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    Transform _localToSceneTransform = it.getLocalToSceneTransform();
    CharSequence _svgString_1 = this.toSvgString(_localToSceneTransform);
    _builder.append(_svgString_1, "	");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("fill=\"");
    Paint _fill = it.getFill();
    CharSequence _svgString_2 = this.toSvgString(_fill);
    _builder.append(_svgString_2, "	");
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    double _opacity = it.getOpacity();
    CharSequence _svgAttribute = this.toSvgAttribute(Double.valueOf(_opacity), "opacity", Double.valueOf(1.0));
    _builder.append(_svgAttribute, "	");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("stroke=\"");
    Paint _stroke = it.getStroke();
    CharSequence _svgString_3 = this.toSvgString(_stroke);
    _builder.append(_svgString_3, "	");
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    double _strokeDashOffset = it.getStrokeDashOffset();
    CharSequence _svgAttribute_1 = this.toSvgAttribute(Double.valueOf(_strokeDashOffset), "stroke-dahsoffset", "0.0", "em");
    _builder.append(_svgAttribute_1, "	");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    StrokeLineCap _strokeLineCap = it.getStrokeLineCap();
    CharSequence _svgAttribute_2 = this.toSvgAttribute(_strokeLineCap, "stroke-linecap", "butt");
    _builder.append(_svgAttribute_2, "	");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    StrokeLineJoin _strokeLineJoin = it.getStrokeLineJoin();
    CharSequence _svgAttribute_3 = this.toSvgAttribute(_strokeLineJoin, "stroke-linejoin", "miter");
    _builder.append(_svgAttribute_3, "	");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    double _strokeMiterLimit = it.getStrokeMiterLimit();
    CharSequence _svgAttribute_4 = this.toSvgAttribute(Double.valueOf(_strokeMiterLimit), "stroke-miterLimit", Double.valueOf(4.0));
    _builder.append(_svgAttribute_4, "	");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    double _strokeWidth = it.getStrokeWidth();
    CharSequence _svgAttribute_5 = this.toSvgAttribute(Double.valueOf(_strokeWidth), "stroke-width", Double.valueOf(1.0));
    _builder.append(_svgAttribute_5, "	");
    _builder.newLineIfNotEmpty();
    _builder.append("/>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _toSvgElement(final Parent it) {
    StringConcatenation _builder = new StringConcatenation();
    {
      ObservableList<Node> _childrenUnmodifiable = it.getChildrenUnmodifiable();
      final Function1<Node,Boolean> _function = new Function1<Node,Boolean>() {
          public Boolean apply(final Node it) {
            boolean _isVisible = it.isVisible();
            return Boolean.valueOf(_isVisible);
          }
        };
      Iterable<Node> _filter = IterableExtensions.<Node>filter(_childrenUnmodifiable, _function);
      boolean _isEmpty = IterableExtensions.isEmpty(_filter);
      boolean _not = (!_isEmpty);
      if (_not) {
        {
          ObservableList<Node> _childrenUnmodifiable_1 = it.getChildrenUnmodifiable();
          final Function1<Node,Boolean> _function_1 = new Function1<Node,Boolean>() {
              public Boolean apply(final Node it) {
                boolean _isVisible = it.isVisible();
                return Boolean.valueOf(_isVisible);
              }
            };
          Iterable<Node> _filter_1 = IterableExtensions.<Node>filter(_childrenUnmodifiable_1, _function_1);
          for(final Node child : _filter_1) {
            CharSequence _svgElement = this.toSvgElement(child);
            _builder.append(_svgElement, "");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder;
  }
  
  protected CharSequence _toSvgElement(final Node node) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<!-- ");
    Class<? extends Node> _class = node.getClass();
    String _name = _class.getName();
    _builder.append(_name, "");
    _builder.append(" not yet implemented -->");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence toSvgAttribute(final Object value, final String name, final Object defaultValue) {
    CharSequence _svgAttribute = this.toSvgAttribute(value, name, defaultValue, "");
    return _svgAttribute;
  }
  
  protected CharSequence toSvgAttribute(final Object value, final String name, final Object defaultValue, final String unit) {
    CharSequence _xifexpression = null;
    String _svgString = this.toSvgString(value);
    String _string = defaultValue.toString();
    boolean _notEquals = (!Objects.equal(_svgString, _string));
    if (_notEquals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(name, "");
      _builder.append("=\"");
      String _svgString_1 = this.toSvgString(value);
      _builder.append(_svgString_1, "");
      _builder.append(unit, "");
      _builder.append("\" ");
      _xifexpression = _builder;
    } else {
      _xifexpression = "";
    }
    return _xifexpression;
  }
  
  protected String toSvgString(final Shape shape) {
    String _svgString = ShapeConverterExtensions.toSvgString(shape);
    return _svgString;
  }
  
  protected CharSequence toSvgString(final Transform it) {
    StringConcatenation _builder = new StringConcatenation();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("matrix(");
    double _mxx = it.getMxx();
    _builder_1.append(_mxx, "");
    _builder_1.append(",");
    double _myx = it.getMyx();
    _builder_1.append(_myx, "");
    _builder_1.append(",");
    double _mxy = it.getMxy();
    _builder_1.append(_mxy, "");
    _builder_1.append(",");
    double _myy = it.getMyy();
    _builder_1.append(_myy, "");
    _builder_1.append(",");
    double _tx = it.getTx();
    _builder_1.append(_tx, "");
    _builder_1.append(",");
    double _ty = it.getTy();
    _builder_1.append(_ty, "");
    _builder_1.append(")");
    CharSequence _svgAttribute = this.toSvgAttribute(_builder_1, "transform", "matrix(1.0,0.0,0.0,1.0,0.0,0.0)");
    _builder.append(_svgAttribute, "");
    return _builder;
  }
  
  protected CharSequence toSvgString(final Paint paint) {
    CharSequence _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (paint instanceof Color) {
        final Color _color = (Color)paint;
        _matched=true;
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("rgb(");
        double _red = _color.getRed();
        double _multiply = (255 * _red);
        _builder.append(((int) _multiply), "");
        _builder.append(",");
        double _green = _color.getGreen();
        double _multiply_1 = (255 * _green);
        _builder.append(((int) _multiply_1), "");
        _builder.append(",");
        double _blue = _color.getBlue();
        double _multiply_2 = (255 * _blue);
        _builder.append(((int) _multiply_2), "");
        _builder.append(")");
        _switchResult = _builder;
      }
    }
    if (!_matched) {
      if (paint instanceof LinearGradient) {
        final LinearGradient _linearGradient = (LinearGradient)paint;
        _matched=true;
        CharSequence _xblockexpression = null;
        {
          int _plus = (this.currentID + 1);
          this.currentID = _plus;
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("<linearGradient id=\"Gradient");
          _builder.append(this.currentID, "");
          _builder.append("\"");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("gradientUnits=\"");
          {
            boolean _isProportional = _linearGradient.isProportional();
            if (_isProportional) {
              _builder.append("objectBoundingBox");
            } else {
              _builder.append("userSpaceOnUse");
            }
          }
          _builder.append("\"");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          double _startX = _linearGradient.getStartX();
          CharSequence _svgAttribute = this.toSvgAttribute(Double.valueOf(_startX), "x1", Double.valueOf(0.0));
          _builder.append(_svgAttribute, "	");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          double _startY = _linearGradient.getStartY();
          CharSequence _svgAttribute_1 = this.toSvgAttribute(Double.valueOf(_startY), "y1", Double.valueOf(0.0));
          _builder.append(_svgAttribute_1, "	");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          double _endX = _linearGradient.getEndX();
          CharSequence _svgAttribute_2 = this.toSvgAttribute(Double.valueOf(_endX), "x2", Double.valueOf(1.0));
          _builder.append(_svgAttribute_2, "	");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          double _endY = _linearGradient.getEndY();
          CharSequence _svgAttribute_3 = this.toSvgAttribute(Double.valueOf(_endY), "y2", Double.valueOf(1.0));
          _builder.append(_svgAttribute_3, "	");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          CycleMethod _cycleMethod = _linearGradient.getCycleMethod();
          CharSequence _svgAttribute_4 = this.toSvgAttribute(_cycleMethod, "spreadMethod", "pad");
          _builder.append(_svgAttribute_4, "	");
          _builder.append(">");
          _builder.newLineIfNotEmpty();
          {
            List<Stop> _stops = _linearGradient.getStops();
            for(final Stop stop : _stops) {
              _builder.append("\t");
              _builder.append("<stop offset=\"");
              double _offset = stop.getOffset();
              double _multiply = (_offset * 100);
              _builder.append(_multiply, "	");
              _builder.append("%\" stop-color=\"");
              Color _color = stop.getColor();
              CharSequence _svgString = this.toSvgString(_color);
              _builder.append(_svgString, "	");
              _builder.append("\" />");
              _builder.newLineIfNotEmpty();
            }
          }
          _builder.append("</linearGradient>");
          _builder.newLine();
          this.defs.add(_builder.toString());
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("url(#Gradient");
          _builder_1.append(this.currentID, "");
          _builder_1.append(")");
          _xblockexpression = (_builder_1);
        }
        _switchResult = _xblockexpression;
      }
    }
    if (!_matched) {
      _switchResult = "gray";
    }
    return _switchResult;
  }
  
  protected String toSvgString(final CycleMethod it) {
    String _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(it,CycleMethod.NO_CYCLE)) {
        _matched=true;
        _switchResult = "pad";
      }
    }
    if (!_matched) {
      if (Objects.equal(it,CycleMethod.REFLECT)) {
        _matched=true;
        _switchResult = "reflect";
      }
    }
    if (!_matched) {
      if (Objects.equal(it,CycleMethod.REPEAT)) {
        _matched=true;
        _switchResult = "repeat";
      }
    }
    return _switchResult;
  }
  
  protected String toSvgString(final Object it) {
    String _string = it.toString();
    String _lowerCase = _string.toLowerCase();
    return _lowerCase;
  }
  
  protected CharSequence toSvgElement(final Node it) {
    if (it instanceof Text) {
      return _toSvgElement((Text)it);
    } else if (it instanceof Parent) {
      return _toSvgElement((Parent)it);
    } else if (it instanceof Shape) {
      return _toSvgElement((Shape)it);
    } else if (it != null) {
      return _toSvgElement(it);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(it).toString());
    }
  }
}
