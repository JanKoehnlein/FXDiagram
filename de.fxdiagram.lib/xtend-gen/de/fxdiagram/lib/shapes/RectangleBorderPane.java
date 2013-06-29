package de.fxdiagram.lib.shapes;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class RectangleBorderPane extends StackPane {
  public RectangleBorderPane() {
    this.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("-fx-border-color: gray;");
    _builder.newLine();
    _builder.append("-fx-border-width: 1.2;");
    _builder.newLine();
    _builder.append("-fx-border-radius: 12;");
    _builder.newLine();
    _builder.append("-fx-background-color: linear-gradient(to bottom right, lightgray, darkgray);");
    _builder.newLine();
    _builder.append("-fx-background-radius: 12;");
    _builder.newLine();
    _builder.append("-fx-background-insets: 1 1 1 1;");
    _builder.newLine();
    this.setStyle(_builder.toString());
  }
  
  private final static double DEFAULT_BORDERWIDTH = 1.2;
  
  private SimpleDoubleProperty borderWidthProperty;
  
  public double getBorderWidth() {
    return (this.borderWidthProperty != null)? this.borderWidthProperty.get() : DEFAULT_BORDERWIDTH;
    
  }
  
  public void setBorderWidth(final double borderWidth) {
    this.borderWidthProperty().set(borderWidth);
    
  }
  
  public DoubleProperty borderWidthProperty() {
    if (this.borderWidthProperty == null) { 
    	this.borderWidthProperty = new SimpleDoubleProperty(this, "borderWidth", DEFAULT_BORDERWIDTH);
    }
    return this.borderWidthProperty;
    
  }
  
  private final static double DEFAULT_BORDERRADIUS = 12;
  
  private SimpleDoubleProperty borderRadiusProperty;
  
  public double getBorderRadius() {
    return (this.borderRadiusProperty != null)? this.borderRadiusProperty.get() : DEFAULT_BORDERRADIUS;
    
  }
  
  public void setBorderRadius(final double borderRadius) {
    this.borderRadiusProperty().set(borderRadius);
    
  }
  
  public DoubleProperty borderRadiusProperty() {
    if (this.borderRadiusProperty == null) { 
    	this.borderRadiusProperty = new SimpleDoubleProperty(this, "borderRadius", DEFAULT_BORDERRADIUS);
    }
    return this.borderRadiusProperty;
    
  }
}
