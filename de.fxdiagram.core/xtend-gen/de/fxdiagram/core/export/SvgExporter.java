package de.fxdiagram.core.export;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.export.ShapeConverterExtensions;
import de.fxdiagram.core.export.SvgExportable;
import de.fxdiagram.core.export.SvgLink;
import de.fxdiagram.core.export.SvgLinkProvider;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javax.imageio.ImageIO;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

/**
 * Exports a given diagram to SVG.
 * 
 * SVG was chosen because it can be converted to a number of different other scalable
 * vector formats with third-party open-source tools and because it is easy to
 * generate and inspect as text.
 * 
 * The class will produce scalable graphics for all {@link Shape}s and {@link Text}
 * elements. For {@link ImageView} and {@link MediaView}, the respective content will be
 * copied without transformation, and thus be as scalable as the original. Custom SVG
 * export can be provided by implementing {@link SvgExportable}. For all remaining nodes,
 * snapshots will be taken and embedded as images (i.e. not scalable).
 * 
 * In Java 7, the CSS styling of nodes was not inspectable. This is why we are currently
 * still bound to this strategy. By fully migrating to Java 8, we should extend that.
 */
@Logging
@SuppressWarnings("all")
public class SvgExporter {
  private final static double PT_TO_PX = (2.0 / 3.0);
  
  private int currentID;
  
  private int imageCounter;
  
  private List<String> defs;
  
  private File baseDir;
  
  private SvgLinkProvider linkProvider;
  
  private Map<Node, SvgLink> linkCache = CollectionLiterals.<Node, SvgLink>newHashMap();
  
  public CharSequence toSvg(final XDiagram diagram, final File baseDir, final SvgLinkProvider linkProvider) {
    CharSequence _xblockexpression = null;
    {
      this.baseDir = baseDir;
      this.linkProvider = linkProvider;
      ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList();
      this.defs = _newArrayList;
      this.currentID = 0;
      final Bounds bounds = diagram.getBoundsInParent();
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
      _builder.append("xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.1\">");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("<rect x=\"");
      double _minX_1 = bounds.getMinX();
      _builder.append(_minX_1, "\t");
      _builder.append("\" y=\"");
      double _minY_1 = bounds.getMinY();
      _builder.append(_minY_1, "\t");
      _builder.append("\" width=\"100%\" height =\"100%\" fill=\"");
      Paint _backgroundPaint = diagram.getBackgroundPaint();
      CharSequence _svgString = this.toSvgString(_backgroundPaint);
      _builder.append(_svgString, "\t");
      _builder.append("\"/>");
      _builder.newLineIfNotEmpty();
      {
        ObservableList<Node> _childrenUnmodifiable = diagram.getChildrenUnmodifiable();
        final Function1<Node, Boolean> _function = (Node it) -> {
          return Boolean.valueOf(it.isVisible());
        };
        Iterable<Node> _filter = IterableExtensions.<Node>filter(_childrenUnmodifiable, _function);
        for(final Node child : _filter) {
          _builder.append("\t");
          CharSequence _svgElement = this.toSvgElement(child);
          _builder.append(_svgElement, "\t");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.append("<defs>");
      _builder.newLine();
      {
        for(final String defElement : this.defs) {
          _builder.append("\t\t");
          _builder.append(defElement, "\t\t");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.append("</defs>");
      _builder.newLine();
      _builder.append("</svg>");
      _builder.newLine();
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  public CharSequence toSvg(final XDiagram diagram, final File baseDir) {
    return this.toSvg(diagram, baseDir, null);
  }
  
  public CharSequence toSvgElement(final Node o) {
    CharSequence _xblockexpression = null;
    {
      final SvgLink link = this.doGetSvgLink(o);
      CharSequence _xifexpression = null;
      boolean _and = false;
      boolean _notEquals = (!Objects.equal(link, null));
      if (!_notEquals) {
        _and = false;
      } else {
        boolean _notEquals_1 = (!Objects.equal(link, SvgLink.NONE));
        _and = _notEquals_1;
      }
      if (_and) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("<a xlink:href=\"");
        String _href = link.getHref();
        _builder.append(_href, "");
        _builder.append("\"");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        String _title = link.getTitle();
        CharSequence _svgAttribute = this.toSvgAttribute(_title, "xlink:title", null);
        _builder.append(_svgAttribute, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        String _targetFrame = link.getTargetFrame();
        CharSequence _svgAttribute_1 = this.toSvgAttribute(_targetFrame, "target", null);
        _builder.append(_svgAttribute_1, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        {
          boolean _isOpenInNewWindow = link.isOpenInNewWindow();
          if (_isOpenInNewWindow) {
            _builder.append("xlink:show=\"new\"");
          }
        }
        _builder.append(">");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        CharSequence _svgElement2 = this.toSvgElement2(o);
        _builder.append(_svgElement2, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("</a>");
        _builder.newLine();
        _xifexpression = _builder;
      } else {
        _xifexpression = this.toSvgElement2(o);
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  protected CharSequence toSvgElement2(final Node o) {
    CharSequence _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (o instanceof SvgExportable) {
        _matched=true;
        _switchResult = ((SvgExportable)o).toSvgElement(this);
      }
    }
    if (!_matched) {
      if (o instanceof Text) {
        _matched=true;
        _switchResult = this.textToSvgElement(((Text)o));
      }
    }
    if (!_matched) {
      if (o instanceof Shape) {
        _matched=true;
        _switchResult = this.shapeToSvgElement(((Shape)o));
      }
    }
    if (!_matched) {
      if (o instanceof ImageView) {
        _matched=true;
        _switchResult = this.imageToSvgElement(((ImageView)o));
      }
    }
    if (!_matched) {
      if (o instanceof MediaView) {
        _matched=true;
        _switchResult = this.snapshotToSvgElement(o);
      }
    }
    if (!_matched) {
      if (o instanceof Parent) {
        _matched=true;
        _switchResult = this.parentToSvgElement(((Parent)o), "");
      }
    }
    if (!_matched) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("<!-- ");
      Class<? extends Node> _class = o.getClass();
      String _name = _class.getName();
      _builder.append(_name, "");
      _builder.append(" not exportable -->");
      _builder.newLineIfNotEmpty();
      _switchResult = _builder;
    }
    return _switchResult;
  }
  
  public CharSequence textToSvgElement(final Text it) {
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
    Translate _transformForOrigin = this.getTransformForOrigin(it);
    CharSequence _svgString_1 = this.toSvgString(_transformForOrigin);
    _builder.append(_svgString_1, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("fill=\"");
    Paint _fill = it.getFill();
    CharSequence _svgString_2 = this.toSvgString(_fill);
    _builder.append(_svgString_2, "\t");
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    double _opacity = it.getOpacity();
    CharSequence _svgAttribute = this.toSvgAttribute(Double.valueOf(_opacity), "opacity", Double.valueOf(1.0));
    _builder.append(_svgAttribute, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    double _strokeDashOffset = it.getStrokeDashOffset();
    CharSequence _svgAttribute_1 = this.toSvgAttribute(Double.valueOf(_strokeDashOffset), "stroke-dahsoffset", "0.0", "em");
    _builder.append(_svgAttribute_1, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    StrokeLineCap _strokeLineCap = it.getStrokeLineCap();
    CharSequence _svgAttribute_2 = this.toSvgAttribute(_strokeLineCap, "stroke-linecap", "butt");
    _builder.append(_svgAttribute_2, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    StrokeLineJoin _strokeLineJoin = it.getStrokeLineJoin();
    CharSequence _svgAttribute_3 = this.toSvgAttribute(_strokeLineJoin, "stroke-linejoin", "miter");
    _builder.append(_svgAttribute_3, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    double _strokeMiterLimit = it.getStrokeMiterLimit();
    CharSequence _svgAttribute_4 = this.toSvgAttribute(Double.valueOf(_strokeMiterLimit), "stroke-miterlimit", Double.valueOf(4.0));
    _builder.append(_svgAttribute_4, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("stroke-width=\"0.0\"");
    _builder.newLine();
    _builder.append("/>");
    _builder.newLine();
    return _builder;
  }
  
  protected Translate getTransformForOrigin(final Text it) {
    Translate _xblockexpression = null;
    {
      double _switchResult = (double) 0;
      VPos _textOrigin = it.getTextOrigin();
      if (_textOrigin != null) {
        switch (_textOrigin) {
          case TOP:
            Transform _localToSceneTransform = it.getLocalToSceneTransform();
            double _myy = _localToSceneTransform.getMyy();
            Font _font = it.getFont();
            double _size = _font.getSize();
            double _multiply = (_myy * _size);
            double _multiply_1 = (_multiply * SvgExporter.PT_TO_PX);
            _switchResult = (_multiply_1 / 2);
            break;
          case BOTTOM:
            Transform _localToSceneTransform_1 = it.getLocalToSceneTransform();
            double _myy_1 = _localToSceneTransform_1.getMyy();
            double _minus = (-_myy_1);
            Font _font_1 = it.getFont();
            double _size_1 = _font_1.getSize();
            double _multiply_2 = (_minus * _size_1);
            double _multiply_3 = (_multiply_2 * SvgExporter.PT_TO_PX);
            _switchResult = (_multiply_3 / 2);
            break;
          default:
            _switchResult = 0;
            break;
        }
      } else {
        _switchResult = 0;
      }
      final double deltaY = _switchResult;
      _xblockexpression = new Translate(0, deltaY);
    }
    return _xblockexpression;
  }
  
  public CharSequence shapeToSvgElement(final Shape it) {
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
    _builder.append(_svgString_1, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("fill=\"");
    Paint _fill = it.getFill();
    CharSequence _svgString_2 = this.toSvgString(_fill);
    _builder.append(_svgString_2, "\t");
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    double _opacity = it.getOpacity();
    CharSequence _svgAttribute = this.toSvgAttribute(Double.valueOf(_opacity), "opacity", Double.valueOf(1.0));
    _builder.append(_svgAttribute, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("stroke=\"");
    Paint _stroke = it.getStroke();
    CharSequence _svgString_3 = this.toSvgString(_stroke);
    _builder.append(_svgString_3, "\t");
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    double _strokeDashOffset = it.getStrokeDashOffset();
    CharSequence _svgAttribute_1 = this.toSvgAttribute(Double.valueOf(_strokeDashOffset), "stroke-dahsoffset", "0.0", "em");
    _builder.append(_svgAttribute_1, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    StrokeLineCap _strokeLineCap = it.getStrokeLineCap();
    CharSequence _svgAttribute_2 = this.toSvgAttribute(_strokeLineCap, "stroke-linecap", "butt");
    _builder.append(_svgAttribute_2, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    StrokeLineJoin _strokeLineJoin = it.getStrokeLineJoin();
    CharSequence _svgAttribute_3 = this.toSvgAttribute(_strokeLineJoin, "stroke-linejoin", "miter");
    _builder.append(_svgAttribute_3, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    double _strokeMiterLimit = it.getStrokeMiterLimit();
    CharSequence _svgAttribute_4 = this.toSvgAttribute(Double.valueOf(_strokeMiterLimit), "stroke-miterlimit", Double.valueOf(4.0));
    _builder.append(_svgAttribute_4, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    double _strokeWidth = it.getStrokeWidth();
    CharSequence _svgAttribute_5 = this.toSvgAttribute(Double.valueOf(_strokeWidth), "stroke-width", Double.valueOf(1.0));
    _builder.append(_svgAttribute_5, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("/>");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence imageToSvgElement(final ImageView it) {
    CharSequence _xblockexpression = null;
    {
      Image _image = it.getImage();
      Rectangle2D _viewport = it.getViewport();
      final String fileName = this.saveImageFile(_image, _viewport);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("<!-- ");
      Class<? extends ImageView> _class = it.getClass();
      String _name = _class.getName();
      _builder.append(_name, "");
      _builder.append(" -->");
      _builder.newLineIfNotEmpty();
      _builder.append("<image ");
      Transform _localToSceneTransform = it.getLocalToSceneTransform();
      CharSequence _svgString = this.toSvgString(_localToSceneTransform);
      _builder.append(_svgString, "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("width=\"");
      Bounds _layoutBounds = it.getLayoutBounds();
      double _width = _layoutBounds.getWidth();
      _builder.append(_width, "\t");
      _builder.append("\" ");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("height=\"");
      Bounds _layoutBounds_1 = it.getLayoutBounds();
      double _height = _layoutBounds_1.getHeight();
      _builder.append(_height, "\t");
      _builder.append("\" ");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("xlink:href=\"");
      _builder.append(fileName, "\t");
      _builder.append("\"");
      _builder.newLineIfNotEmpty();
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  public CharSequence snapshotToSvgElement(final Node node) {
    CharSequence _xblockexpression = null;
    {
      Bounds _layoutBounds = node.getLayoutBounds();
      double _width = _layoutBounds.getWidth();
      Bounds _layoutBounds_1 = node.getLayoutBounds();
      double _height = _layoutBounds_1.getHeight();
      final WritableImage image = new WritableImage(((int) _width), ((int) _height));
      node.snapshot(null, image);
      final String fileName = this.saveImageFile(image, null);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("<!-- ");
      Class<? extends Node> _class = node.getClass();
      String _name = _class.getName();
      _builder.append(_name, "");
      _builder.append(" -->");
      _builder.newLineIfNotEmpty();
      _builder.append("<image ");
      Transform _localToSceneTransform = node.getLocalToSceneTransform();
      CharSequence _svgString = this.toSvgString(_localToSceneTransform);
      _builder.append(_svgString, "");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("width=\"");
      Bounds _layoutBounds_2 = node.getLayoutBounds();
      double _width_1 = _layoutBounds_2.getWidth();
      _builder.append(_width_1, "\t");
      _builder.append("\"");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("height=\"");
      Bounds _layoutBounds_3 = node.getLayoutBounds();
      double _height_1 = _layoutBounds_3.getHeight();
      _builder.append(_height_1, "\t");
      _builder.append("\"");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("xlink:href=\"");
      _builder.append(fileName, "\t");
      _builder.append("\"");
      _builder.newLineIfNotEmpty();
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  protected String saveImageFile(final Image image, final Rectangle2D viewport) {
    String _xblockexpression = null;
    {
      int _nextImageNumber = this.nextImageNumber();
      String _plus = ("image" + Integer.valueOf(_nextImageNumber));
      final String fileName = (_plus + ".png");
      try {
        final BufferedImage buffered = SwingFXUtils.fromFXImage(image, null);
        BufferedImage _xifexpression = null;
        boolean _equals = Objects.equal(viewport, null);
        if (_equals) {
          _xifexpression = buffered;
        } else {
          double _minX = viewport.getMinX();
          double _minY = viewport.getMinY();
          double _width = viewport.getWidth();
          double _height = viewport.getHeight();
          _xifexpression = buffered.getSubimage(((int) _minX), ((int) _minY), 
            ((int) _width), ((int) _height));
        }
        final BufferedImage visible = _xifexpression;
        File _file = new File(this.baseDir, fileName);
        ImageIO.write(visible, "png", _file);
      } catch (final Throwable _t) {
        if (_t instanceof IOException) {
          final IOException e = (IOException)_t;
          Class<? extends SvgExporter> _class = this.getClass();
          String _name = _class.getName();
          String _plus_1 = ("Error exporting " + _name);
          String _plus_2 = (_plus_1 + " to SVG");
          SvgExporter.LOG.log(Level.SEVERE, _plus_2, e);
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
      _xblockexpression = fileName;
    }
    return _xblockexpression;
  }
  
  protected int nextImageNumber() {
    int _xblockexpression = (int) 0;
    {
      this.imageCounter = (this.imageCounter + 1);
      _xblockexpression = this.imageCounter;
    }
    return _xblockexpression;
  }
  
  public CharSequence parentToSvgElement(final Parent it, final CharSequence ownSvgCode) {
    CharSequence _xblockexpression = null;
    {
      final CharSequence clipPath = this.toSvgClip(it);
      CharSequence _xifexpression = null;
      ObservableList<Node> _childrenUnmodifiable = it.getChildrenUnmodifiable();
      final Function1<Node, Boolean> _function = (Node it_1) -> {
        return Boolean.valueOf(it_1.isVisible());
      };
      Iterable<Node> _filter = IterableExtensions.<Node>filter(_childrenUnmodifiable, _function);
      boolean _isEmpty = IterableExtensions.isEmpty(_filter);
      boolean _not = (!_isEmpty);
      if (_not) {
        StringConcatenation _builder = new StringConcatenation();
        {
          boolean _notEquals = (!Objects.equal(clipPath, null));
          if (_notEquals) {
            _builder.append("<g ");
            _builder.append(clipPath, "");
            _builder.append(">");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("\t");
        _builder.append(ownSvgCode, "\t");
        _builder.newLineIfNotEmpty();
        {
          ObservableList<Node> _childrenUnmodifiable_1 = it.getChildrenUnmodifiable();
          final Function1<Node, Boolean> _function_1 = (Node it_1) -> {
            return Boolean.valueOf(it_1.isVisible());
          };
          Iterable<Node> _filter_1 = IterableExtensions.<Node>filter(_childrenUnmodifiable_1, _function_1);
          for(final Node child : _filter_1) {
            _builder.append("\t");
            CharSequence _svgElement = this.toSvgElement(child);
            _builder.append(_svgElement, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
        {
          boolean _notEquals_1 = (!Objects.equal(clipPath, null));
          if (_notEquals_1) {
            _builder.append("</g>");
            _builder.newLine();
          }
        }
        _xifexpression = _builder;
      } else {
        _xifexpression = ownSvgCode;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  protected SvgLink doGetSvgLink(final Node node) {
    SvgLink _xblockexpression = null;
    {
      final SvgLink link = this.linkCache.get(node);
      SvgLink _xifexpression = null;
      boolean _notEquals = (!Objects.equal(link, null));
      if (_notEquals) {
        _xifexpression = link;
      } else {
        SvgLink _xblockexpression_1 = null;
        {
          SvgLink _elvis = null;
          SvgLink _link = null;
          if (this.linkProvider!=null) {
            _link=this.linkProvider.getLink(node);
          }
          if (_link != null) {
            _elvis = _link;
          } else {
            _elvis = SvgLink.NONE;
          }
          final SvgLink newLink = _elvis;
          this.linkCache.put(node, newLink);
          _xblockexpression_1 = newLink;
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public CharSequence toSvgAttribute(final Object value, final String name, final Object defaultValue) {
    return this.toSvgAttribute(value, name, defaultValue, "");
  }
  
  public CharSequence toSvgAttribute(final Object value, final String name, final Object defaultValue, final String unit) {
    CharSequence _xifexpression = null;
    String _svgString = null;
    if (value!=null) {
      _svgString=this.toSvgString(value);
    }
    String _string = null;
    if (defaultValue!=null) {
      _string=defaultValue.toString();
    }
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
  
  public CharSequence toSvgClip(final Node node) {
    CharSequence _xblockexpression = null;
    {
      final Node clip = node.getClip();
      CharSequence _xifexpression = null;
      if ((clip instanceof Shape)) {
        CharSequence _xblockexpression_1 = null;
        {
          this.currentID = (this.currentID + 1);
          final String clipPathId = ("clipPath" + Integer.valueOf(this.currentID));
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("<clipPath id=\"");
          _builder.append(clipPathId, "");
          _builder.append("\"");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          Transform _localToSceneTransform = node.getLocalToSceneTransform();
          CharSequence _svgString = this.toSvgString(_localToSceneTransform);
          _builder.append(_svgString, "\t");
          _builder.append(">");
          _builder.newLineIfNotEmpty();
          _builder.append(" \t");
          _builder.append("<path d=\"");
          String _svgString_1 = this.toSvgString(((Shape) clip));
          _builder.append(_svgString_1, " \t");
          _builder.append("\"/>");
          _builder.newLineIfNotEmpty();
          _builder.append("</clipPath>");
          _builder.newLine();
          this.defs.add(_builder.toString());
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("clip-path=\"url(#");
          _builder_1.append(clipPathId, "");
          _builder_1.append(")\" ");
          _xblockexpression_1 = _builder_1;
        }
        _xifexpression = _xblockexpression_1;
      } else {
        _xifexpression = null;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public String toSvgString(final Shape shape) {
    return ShapeConverterExtensions.toSvgString(shape);
  }
  
  public CharSequence toSvgString(final Transform it) {
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
  
  public CharSequence toSvgString(final Paint paint) {
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
        _builder.append(",");
        double _green = ((Color)paint).getGreen();
        double _multiply_1 = (255 * _green);
        _builder.append(((int) _multiply_1), "");
        _builder.append(",");
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
        CharSequence _xblockexpression = null;
        {
          this.currentID = (this.currentID + 1);
          final String gradientId = ("Gradient" + Integer.valueOf(this.currentID));
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("<linearGradient id=\"");
          _builder.append(gradientId, "");
          _builder.append("\"");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("gradientUnits=\"");
          {
            boolean _isProportional = ((LinearGradient)paint).isProportional();
            if (_isProportional) {
              _builder.append("objectBoundingBox");
            } else {
              _builder.append("userSpaceOnUse");
            }
          }
          _builder.append("\"");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          double _startX = ((LinearGradient)paint).getStartX();
          CharSequence _svgAttribute = this.toSvgAttribute(Double.valueOf(_startX), "x1", Double.valueOf(0.0));
          _builder.append(_svgAttribute, "\t");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          double _startY = ((LinearGradient)paint).getStartY();
          CharSequence _svgAttribute_1 = this.toSvgAttribute(Double.valueOf(_startY), "y1", Double.valueOf(0.0));
          _builder.append(_svgAttribute_1, "\t");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          double _endX = ((LinearGradient)paint).getEndX();
          CharSequence _svgAttribute_2 = this.toSvgAttribute(Double.valueOf(_endX), "x2", Double.valueOf(1.0));
          _builder.append(_svgAttribute_2, "\t");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          double _endY = ((LinearGradient)paint).getEndY();
          CharSequence _svgAttribute_3 = this.toSvgAttribute(Double.valueOf(_endY), "y2", Double.valueOf(1.0));
          _builder.append(_svgAttribute_3, "\t");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          CycleMethod _cycleMethod = ((LinearGradient)paint).getCycleMethod();
          CharSequence _svgAttribute_4 = this.toSvgAttribute(_cycleMethod, "spreadMethod", "pad");
          _builder.append(_svgAttribute_4, "\t");
          _builder.append(">");
          _builder.newLineIfNotEmpty();
          {
            List<Stop> _stops = ((LinearGradient)paint).getStops();
            for(final Stop stop : _stops) {
              _builder.append("\t");
              _builder.append("<stop offset=\"");
              double _offset = stop.getOffset();
              double _multiply = (_offset * 100);
              _builder.append(_multiply, "\t");
              _builder.append("%\" stop-color=\"");
              Color _color = stop.getColor();
              CharSequence _svgString = this.toSvgString(_color);
              _builder.append(_svgString, "\t");
              _builder.append("\" />");
              _builder.newLineIfNotEmpty();
            }
          }
          _builder.append("</linearGradient>");
          _builder.newLine();
          this.defs.add(_builder.toString());
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("url(#");
          _builder_1.append(gradientId, "");
          _builder_1.append(")");
          _xblockexpression = _builder_1;
        }
        _switchResult = _xblockexpression;
      }
    }
    if (!_matched) {
      _switchResult = "none";
    }
    return _switchResult;
  }
  
  public String toSvgString(final CycleMethod it) {
    String _switchResult = null;
    if (it != null) {
      switch (it) {
        case NO_CYCLE:
          _switchResult = "pad";
          break;
        case REFLECT:
          _switchResult = "reflect";
          break;
        case REPEAT:
          _switchResult = "repeat";
          break;
        default:
          break;
      }
    }
    return _switchResult;
  }
  
  public String toSvgString(final Object it) {
    String _string = it.toString();
    return _string.toLowerCase();
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.export.SvgExporter");
    ;
}
