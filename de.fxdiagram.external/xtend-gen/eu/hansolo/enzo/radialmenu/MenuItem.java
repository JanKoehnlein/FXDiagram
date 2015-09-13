package eu.hansolo.enzo.radialmenu;

import eu.hansolo.enzo.radialmenu.SymbolType;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

@SuppressWarnings("all")
public class MenuItem {
  private SimpleStringProperty tooltipProperty = new SimpleStringProperty(this, "tooltip",_initTooltip());
  
  private static final String _initTooltip() {
    return "";
  }
  
  public String getTooltip() {
    return this.tooltipProperty.get();
  }
  
  public void setTooltip(final String tooltip) {
    this.tooltipProperty.set(tooltip);
  }
  
  public StringProperty tooltipProperty() {
    return this.tooltipProperty;
  }
  
  private SimpleDoubleProperty sizeProperty = new SimpleDoubleProperty(this, "size",_initSize());
  
  private static final double _initSize() {
    return 32;
  }
  
  public double getSize() {
    return this.sizeProperty.get();
  }
  
  public void setSize(final double size) {
    this.sizeProperty.set(size);
  }
  
  public DoubleProperty sizeProperty() {
    return this.sizeProperty;
  }
  
  private SimpleObjectProperty<Color> innerColorProperty = new SimpleObjectProperty<Color>(this, "innerColor",_initInnerColor());
  
  private static final Color _initInnerColor() {
    Color _rgb = Color.rgb(68, 64, 60);
    return _rgb;
  }
  
  public Color getInnerColor() {
    return this.innerColorProperty.get();
  }
  
  public void setInnerColor(final Color innerColor) {
    this.innerColorProperty.set(innerColor);
  }
  
  public ObjectProperty<Color> innerColorProperty() {
    return this.innerColorProperty;
  }
  
  private SimpleObjectProperty<Color> frameColorProperty = new SimpleObjectProperty<Color>(this, "frameColor",_initFrameColor());
  
  private static final Color _initFrameColor() {
    return Color.WHITE;
  }
  
  public Color getFrameColor() {
    return this.frameColorProperty.get();
  }
  
  public void setFrameColor(final Color frameColor) {
    this.frameColorProperty.set(frameColor);
  }
  
  public ObjectProperty<Color> frameColorProperty() {
    return this.frameColorProperty;
  }
  
  private SimpleObjectProperty<Color> foregroundColorProperty = new SimpleObjectProperty<Color>(this, "foregroundColor",_initForegroundColor());
  
  private static final Color _initForegroundColor() {
    return Color.WHITE;
  }
  
  public Color getForegroundColor() {
    return this.foregroundColorProperty.get();
  }
  
  public void setForegroundColor(final Color foregroundColor) {
    this.foregroundColorProperty.set(foregroundColor);
  }
  
  public ObjectProperty<Color> foregroundColorProperty() {
    return this.foregroundColorProperty;
  }
  
  private SimpleObjectProperty<SymbolType> symbolProperty = new SimpleObjectProperty<SymbolType>(this, "symbol",_initSymbol());
  
  private static final SymbolType _initSymbol() {
    return SymbolType.NONE;
  }
  
  public SymbolType getSymbol() {
    return this.symbolProperty.get();
  }
  
  public void setSymbol(final SymbolType symbol) {
    this.symbolProperty.set(symbol);
  }
  
  public ObjectProperty<SymbolType> symbolProperty() {
    return this.symbolProperty;
  }
  
  private SimpleStringProperty thumbnailImageNameProperty = new SimpleStringProperty(this, "thumbnailImageName",_initThumbnailImageName());
  
  private static final String _initThumbnailImageName() {
    return "";
  }
  
  public String getThumbnailImageName() {
    return this.thumbnailImageNameProperty.get();
  }
  
  public void setThumbnailImageName(final String thumbnailImageName) {
    this.thumbnailImageNameProperty.set(thumbnailImageName);
  }
  
  public StringProperty thumbnailImageNameProperty() {
    return this.thumbnailImageNameProperty;
  }
}
