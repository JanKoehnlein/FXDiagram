package de.fxdiagram.lib.nodes;

import de.fxdiagram.core.css.JavaToCss;
import de.fxdiagram.core.export.SvgExportable;
import de.fxdiagram.core.export.SvgExporter;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.transform.Transform;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Extension;

@SuppressWarnings("all")
public class RectangleBorderPane extends StackPane implements SvgExportable {
  public RectangleBorderPane() {
    this.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    final ChangeListener<Number> _function = new ChangeListener<Number>() {
        public void changed(final ObservableValue<? extends Number> prop, final Number oldVal, final Number newVal) {
          RectangleBorderPane.this.updateStyle();
        }
      };
    this.borderWidthProperty.addListener(_function);
    final ChangeListener<Number> _function_1 = new ChangeListener<Number>() {
        public void changed(final ObservableValue<? extends Number> prop, final Number oldVal, final Number newVal) {
          RectangleBorderPane.this.updateStyle();
        }
      };
    this.borderRadiusProperty.addListener(_function_1);
    final ChangeListener<Insets> _function_2 = new ChangeListener<Insets>() {
        public void changed(final ObservableValue<? extends Insets> prop, final Insets oldVal, final Insets newVal) {
          RectangleBorderPane.this.updateStyle();
        }
      };
    this.borderInsetsProperty.addListener(_function_2);
    final ChangeListener<Paint> _function_3 = new ChangeListener<Paint>() {
        public void changed(final ObservableValue<? extends Paint> prop, final Paint oldVal, final Paint newVal) {
          RectangleBorderPane.this.updateStyle();
        }
      };
    this.borderPaintProperty.addListener(_function_3);
    final ChangeListener<Paint> _function_4 = new ChangeListener<Paint>() {
        public void changed(final ObservableValue<? extends Paint> prop, final Paint oldVal, final Paint newVal) {
          RectangleBorderPane.this.updateStyle();
        }
      };
    this.backgroundPaintProperty.addListener(_function_4);
    final ChangeListener<Number> _function_5 = new ChangeListener<Number>() {
        public void changed(final ObservableValue<? extends Number> prop, final Number oldVal, final Number newVal) {
          RectangleBorderPane.this.updateStyle();
        }
      };
    this.backgroundRadiusProperty.addListener(_function_5);
    this.updateStyle();
  }
  
  protected void updateStyle() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("-fx-border-color: ");
    Paint _borderPaint = this.getBorderPaint();
    CharSequence _css = JavaToCss.toCss(_borderPaint);
    _builder.append(_css, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.append("-fx-border-width: ");
    double _borderWidth = this.getBorderWidth();
    _builder.append(_borderWidth, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.append("-fx-border-radius: ");
    double _borderRadius = this.getBorderRadius();
    _builder.append(_borderRadius, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.append("-fx-background-color: ");
    Paint _backgroundPaint = this.getBackgroundPaint();
    CharSequence _css_1 = JavaToCss.toCss(_backgroundPaint);
    _builder.append(_css_1, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.append("-fx-background-radius: ");
    double _backgroundRadius = this.getBackgroundRadius();
    _builder.append(_backgroundRadius, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.append("-fx-background-insets: ");
    Insets _borderInsets = this.getBorderInsets();
    CharSequence _css_2 = JavaToCss.toCss(_borderInsets);
    _builder.append(_css_2, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    this.setStyle(_builder.toString());
  }
  
  public CharSequence toSvgElement(@Extension final SvgExporter exporter) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<!-- ");
    Class<? extends RectangleBorderPane> _class = this.getClass();
    String _name = _class.getName();
    _builder.append(_name, "");
    _builder.append(" -->");
    _builder.newLineIfNotEmpty();
    _builder.append("<rect");
    _builder.newLine();
    _builder.append("\t");
    Transform _localToSceneTransform = this.getLocalToSceneTransform();
    CharSequence _svgString = exporter.toSvgString(_localToSceneTransform);
    _builder.append(_svgString, "	");
    _builder.append(" ");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("width=\"");
    double _width = this.getWidth();
    _builder.append(_width, "	");
    _builder.append("\" height=\"");
    double _height = this.getHeight();
    _builder.append(_height, "	");
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("rx=\"");
    double _borderRadius = this.getBorderRadius();
    _builder.append(_borderRadius, "	");
    _builder.append("\" ry=\"");
    double _borderRadius_1 = this.getBorderRadius();
    _builder.append(_borderRadius_1, "	");
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("fill=\"");
    Paint _backgroundPaint = this.getBackgroundPaint();
    CharSequence _svgString_1 = exporter.toSvgString(_backgroundPaint);
    _builder.append(_svgString_1, "	");
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("stroke=\"");
    Paint _borderPaint = this.getBorderPaint();
    CharSequence _svgString_2 = exporter.toSvgString(_borderPaint);
    _builder.append(_svgString_2, "	");
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("strokeWidth=\"");
    double _borderWidth = this.getBorderWidth();
    _builder.append(_borderWidth, "	");
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    double _opacity = this.getOpacity();
    CharSequence _svgAttribute = exporter.toSvgAttribute(Double.valueOf(_opacity), "opacity", Double.valueOf(1.0));
    _builder.append(_svgAttribute, "	");
    _builder.newLineIfNotEmpty();
    _builder.append("/>");
    _builder.newLine();
    CharSequence _parentToSvgElement = exporter.parentToSvgElement(this);
    _builder.append(_parentToSvgElement, "");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  private SimpleDoubleProperty borderWidthProperty = new SimpleDoubleProperty(this, "borderWidth",_initBorderWidth());
  
  private static final double _initBorderWidth() {
    return 1.2;
  }
  
  public double getBorderWidth() {
    return this.borderWidthProperty.get();
    
  }
  
  public void setBorderWidth(final double borderWidth) {
    this.borderWidthProperty.set(borderWidth);
    
  }
  
  public DoubleProperty borderWidthProperty() {
    return this.borderWidthProperty;
    
  }
  
  private SimpleDoubleProperty borderRadiusProperty = new SimpleDoubleProperty(this, "borderRadius",_initBorderRadius());
  
  private static final double _initBorderRadius() {
    return 12.0;
  }
  
  public double getBorderRadius() {
    return this.borderRadiusProperty.get();
    
  }
  
  public void setBorderRadius(final double borderRadius) {
    this.borderRadiusProperty.set(borderRadius);
    
  }
  
  public DoubleProperty borderRadiusProperty() {
    return this.borderRadiusProperty;
    
  }
  
  private SimpleObjectProperty<Insets> borderInsetsProperty = new SimpleObjectProperty<Insets>(this, "borderInsets",_initBorderInsets());
  
  private static final Insets _initBorderInsets() {
    Insets _insets = new Insets(1, 1, 1, 1);
    return _insets;
  }
  
  public Insets getBorderInsets() {
    return this.borderInsetsProperty.get();
    
  }
  
  public void setBorderInsets(final Insets borderInsets) {
    this.borderInsetsProperty.set(borderInsets);
    
  }
  
  public ObjectProperty<Insets> borderInsetsProperty() {
    return this.borderInsetsProperty;
    
  }
  
  private SimpleObjectProperty<Paint> borderPaintProperty = new SimpleObjectProperty<Paint>(this, "borderPaint",_initBorderPaint());
  
  private static final Paint _initBorderPaint() {
    return Color.GRAY;
  }
  
  public Paint getBorderPaint() {
    return this.borderPaintProperty.get();
    
  }
  
  public void setBorderPaint(final Paint borderPaint) {
    this.borderPaintProperty.set(borderPaint);
    
  }
  
  public ObjectProperty<Paint> borderPaintProperty() {
    return this.borderPaintProperty;
    
  }
  
  private SimpleObjectProperty<Paint> backgroundPaintProperty = new SimpleObjectProperty<Paint>(this, "backgroundPaint",_initBackgroundPaint());
  
  private static final Paint _initBackgroundPaint() {
    Color _gray = Color.gray(0.6);
    Stop _stop = new Stop(0, _gray);
    Color _gray_1 = Color.gray(0.9);
    Stop _stop_1 = new Stop(1, _gray_1);
    LinearGradient _linearGradient = new LinearGradient(
      0, 0, 1, 1, 
      true, CycleMethod.NO_CYCLE, 
      new Stop[] { _stop, _stop_1 });
    return _linearGradient;
  }
  
  public Paint getBackgroundPaint() {
    return this.backgroundPaintProperty.get();
    
  }
  
  public void setBackgroundPaint(final Paint backgroundPaint) {
    this.backgroundPaintProperty.set(backgroundPaint);
    
  }
  
  public ObjectProperty<Paint> backgroundPaintProperty() {
    return this.backgroundPaintProperty;
    
  }
  
  private SimpleDoubleProperty backgroundRadiusProperty = new SimpleDoubleProperty(this, "backgroundRadius",_initBackgroundRadius());
  
  private static final double _initBackgroundRadius() {
    return 12.0;
  }
  
  public double getBackgroundRadius() {
    return this.backgroundRadiusProperty.get();
    
  }
  
  public void setBackgroundRadius(final double backgroundRadius) {
    this.backgroundRadiusProperty.set(backgroundRadius);
    
  }
  
  public DoubleProperty backgroundRadiusProperty() {
    return this.backgroundRadiusProperty;
    
  }
}