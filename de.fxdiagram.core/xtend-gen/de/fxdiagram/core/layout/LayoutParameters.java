package de.fxdiagram.core.layout;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.layout.LayoutType;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.core.model.XModelProvider;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

@ModelNode({ "type", "useSplines" })
@SuppressWarnings("all")
public class LayoutParameters implements XModelProvider {
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(typeProperty, LayoutType.class);
    modelElement.addProperty(useSplinesProperty, Boolean.class);
  }
  
  public String toString() {
    return ToString.toString(this);
  }
  
  private SimpleObjectProperty<LayoutType> typeProperty = new SimpleObjectProperty<LayoutType>(this, "type",_initType());
  
  private static final LayoutType _initType() {
    return LayoutType.DOT;
  }
  
  public LayoutType getType() {
    return this.typeProperty.get();
  }
  
  public void setType(final LayoutType type) {
    this.typeProperty.set(type);
  }
  
  public ObjectProperty<LayoutType> typeProperty() {
    return this.typeProperty;
  }
  
  private SimpleBooleanProperty useSplinesProperty = new SimpleBooleanProperty(this, "useSplines",_initUseSplines());
  
  private static final boolean _initUseSplines() {
    return true;
  }
  
  public boolean getUseSplines() {
    return this.useSplinesProperty.get();
  }
  
  public void setUseSplines(final boolean useSplines) {
    this.useSplinesProperty.set(useSplines);
  }
  
  public BooleanProperty useSplinesProperty() {
    return this.useSplinesProperty;
  }
}
