package eu.hansolo.enzo.radialmenu;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

@SuppressWarnings("all")
public class Options {
  private SimpleDoubleProperty degreesProperty = new SimpleDoubleProperty(this, "degrees",_initDegrees());
  
  private static final double _initDegrees() {
    return 360;
  }
  
  public double getDegrees() {
    return this.degreesProperty.get();
  }
  
  public void setDegrees(final double degrees) {
    this.degreesProperty.set(degrees);
  }
  
  public DoubleProperty degreesProperty() {
    return this.degreesProperty;
  }
  
  private SimpleDoubleProperty offsetProperty = new SimpleDoubleProperty(this, "offset",_initOffset());
  
  private static final double _initOffset() {
    return (-90);
  }
  
  public double getOffset() {
    return this.offsetProperty.get();
  }
  
  public void setOffset(final double offset) {
    this.offsetProperty.set(offset);
  }
  
  public DoubleProperty offsetProperty() {
    return this.offsetProperty;
  }
  
  private SimpleDoubleProperty radiusProperty = new SimpleDoubleProperty(this, "radius",_initRadius());
  
  private static final double _initRadius() {
    return 100;
  }
  
  public double getRadius() {
    return this.radiusProperty.get();
  }
  
  public void setRadius(final double radius) {
    this.radiusProperty.set(radius);
  }
  
  public DoubleProperty radiusProperty() {
    return this.radiusProperty;
  }
  
  private SimpleDoubleProperty buttonSizeProperty = new SimpleDoubleProperty(this, "buttonSize",_initButtonSize());
  
  private static final double _initButtonSize() {
    return 44;
  }
  
  public double getButtonSize() {
    return this.buttonSizeProperty.get();
  }
  
  public void setButtonSize(final double buttonSize) {
    this.buttonSizeProperty.set(buttonSize);
  }
  
  public DoubleProperty buttonSizeProperty() {
    return this.buttonSizeProperty;
  }
  
  private SimpleObjectProperty<Color> buttonInnerColorProperty = new SimpleObjectProperty<Color>(this, "buttonInnerColor",_initButtonInnerColor());
  
  private static final Color _initButtonInnerColor() {
    Color _hsb = Color.hsb(0, 0.1, 0.1);
    return _hsb;
  }
  
  public Color getButtonInnerColor() {
    return this.buttonInnerColorProperty.get();
  }
  
  public void setButtonInnerColor(final Color buttonInnerColor) {
    this.buttonInnerColorProperty.set(buttonInnerColor);
  }
  
  public ObjectProperty<Color> buttonInnerColorProperty() {
    return this.buttonInnerColorProperty;
  }
  
  private SimpleObjectProperty<Color> buttonFrameColorProperty = new SimpleObjectProperty<Color>(this, "buttonFrameColor",_initButtonFrameColor());
  
  private static final Color _initButtonFrameColor() {
    return Color.WHITE;
  }
  
  public Color getButtonFrameColor() {
    return this.buttonFrameColorProperty.get();
  }
  
  public void setButtonFrameColor(final Color buttonFrameColor) {
    this.buttonFrameColorProperty.set(buttonFrameColor);
  }
  
  public ObjectProperty<Color> buttonFrameColorProperty() {
    return this.buttonFrameColorProperty;
  }
  
  private SimpleObjectProperty<Color> buttonForegroundColorProperty = new SimpleObjectProperty<Color>(this, "buttonForegroundColor",_initButtonForegroundColor());
  
  private static final Color _initButtonForegroundColor() {
    return Color.WHITE;
  }
  
  public Color getButtonForegroundColor() {
    return this.buttonForegroundColorProperty.get();
  }
  
  public void setButtonForegroundColor(final Color buttonForegroundColor) {
    this.buttonForegroundColorProperty.set(buttonForegroundColor);
  }
  
  public ObjectProperty<Color> buttonForegroundColorProperty() {
    return this.buttonForegroundColorProperty;
  }
  
  private SimpleDoubleProperty buttonAlphaProperty = new SimpleDoubleProperty(this, "buttonAlpha",_initButtonAlpha());
  
  private static final double _initButtonAlpha() {
    return 0.5;
  }
  
  public double getButtonAlpha() {
    return this.buttonAlphaProperty.get();
  }
  
  public void setButtonAlpha(final double buttonAlpha) {
    this.buttonAlphaProperty.set(buttonAlpha);
  }
  
  public DoubleProperty buttonAlphaProperty() {
    return this.buttonAlphaProperty;
  }
  
  private SimpleBooleanProperty buttonVisibleProperty = new SimpleBooleanProperty(this, "buttonVisible",_initButtonVisible());
  
  private static final boolean _initButtonVisible() {
    return true;
  }
  
  public boolean getButtonVisible() {
    return this.buttonVisibleProperty.get();
  }
  
  public void setButtonVisible(final boolean buttonVisible) {
    this.buttonVisibleProperty.set(buttonVisible);
  }
  
  public BooleanProperty buttonVisibleProperty() {
    return this.buttonVisibleProperty;
  }
  
  private SimpleBooleanProperty buttonHideOnSelectProperty = new SimpleBooleanProperty(this, "buttonHideOnSelect",_initButtonHideOnSelect());
  
  private static final boolean _initButtonHideOnSelect() {
    return true;
  }
  
  public boolean getButtonHideOnSelect() {
    return this.buttonHideOnSelectProperty.get();
  }
  
  public void setButtonHideOnSelect(final boolean buttonHideOnSelect) {
    this.buttonHideOnSelectProperty.set(buttonHideOnSelect);
  }
  
  public BooleanProperty buttonHideOnSelectProperty() {
    return this.buttonHideOnSelectProperty;
  }
  
  private SimpleBooleanProperty tooltipsEnabledProperty = new SimpleBooleanProperty(this, "tooltipsEnabled",_initTooltipsEnabled());
  
  private static final boolean _initTooltipsEnabled() {
    return true;
  }
  
  public boolean getTooltipsEnabled() {
    return this.tooltipsEnabledProperty.get();
  }
  
  public void setTooltipsEnabled(final boolean tooltipsEnabled) {
    this.tooltipsEnabledProperty.set(tooltipsEnabled);
  }
  
  public BooleanProperty tooltipsEnabledProperty() {
    return this.tooltipsEnabledProperty;
  }
}
