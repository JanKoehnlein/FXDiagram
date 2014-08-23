package de.fxdiagram.core.model;

import com.google.common.base.Objects;
import de.fxdiagram.core.model.ValueAdapter;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class ColorAdapter implements ValueAdapter<Color> {
  private Color color;
  
  public ColorAdapter() {
  }
  
  public ColorAdapter(final Color color) {
    double _red = color.getRed();
    this.setRed(_red);
    double _green = color.getGreen();
    this.setGreen(_green);
    double _blue = color.getBlue();
    this.setBlue(_blue);
    double _opacity = color.getOpacity();
    this.setOpacity(_opacity);
    this.color = color;
  }
  
  public Color getValueObject() {
    double _red = this.getRed();
    double _green = this.getGreen();
    double _blue = this.getBlue();
    double _opacity = this.getOpacity();
    return new Color(_red, _green, _blue, _opacity);
  }
  
  public List<? extends Property<?>> getProperties() {
    return Collections.<SimpleDoubleProperty>unmodifiableList(CollectionLiterals.<SimpleDoubleProperty>newArrayList(this.redProperty, this.greenProperty, this.blueProperty, this.opacityProperty));
  }
  
  public List<? extends ListProperty<?>> getListProperties() {
    return CollectionLiterals.<ListProperty<?>>emptyList();
  }
  
  public Class<?> getType(final Property<?> property) {
    return Double.class;
  }
  
  public boolean isPrimitive(final Property<?> property) {
    return true;
  }
  
  public Object getNode() {
    Color _xblockexpression = null;
    {
      boolean _equals = Objects.equal(this.color, null);
      if (_equals) {
        double _red = this.getRed();
        double _green = this.getGreen();
        double _blue = this.getBlue();
        double _opacity = this.getOpacity();
        Color _color = new Color(_red, _green, _blue, _opacity);
        this.color = _color;
      }
      _xblockexpression = this.color;
    }
    return _xblockexpression;
  }
  
  private SimpleDoubleProperty redProperty = new SimpleDoubleProperty(this, "red");
  
  public double getRed() {
    return this.redProperty.get();
  }
  
  public void setRed(final double red) {
    this.redProperty.set(red);
  }
  
  public DoubleProperty redProperty() {
    return this.redProperty;
  }
  
  private SimpleDoubleProperty greenProperty = new SimpleDoubleProperty(this, "green");
  
  public double getGreen() {
    return this.greenProperty.get();
  }
  
  public void setGreen(final double green) {
    this.greenProperty.set(green);
  }
  
  public DoubleProperty greenProperty() {
    return this.greenProperty;
  }
  
  private SimpleDoubleProperty blueProperty = new SimpleDoubleProperty(this, "blue");
  
  public double getBlue() {
    return this.blueProperty.get();
  }
  
  public void setBlue(final double blue) {
    this.blueProperty.set(blue);
  }
  
  public DoubleProperty blueProperty() {
    return this.blueProperty;
  }
  
  private SimpleDoubleProperty opacityProperty = new SimpleDoubleProperty(this, "opacity");
  
  public double getOpacity() {
    return this.opacityProperty.get();
  }
  
  public void setOpacity(final double opacity) {
    this.opacityProperty.set(opacity);
  }
  
  public DoubleProperty opacityProperty() {
    return this.opacityProperty;
  }
}
