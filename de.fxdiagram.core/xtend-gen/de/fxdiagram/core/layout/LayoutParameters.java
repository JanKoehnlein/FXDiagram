package de.fxdiagram.core.layout;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.layout.LayoutType;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.core.model.XModelProvider;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

@ModelNode({ "type", "connectionKind" })
@SuppressWarnings("all")
public class LayoutParameters implements XModelProvider {
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(typeProperty, LayoutType.class);
    modelElement.addProperty(connectionKindProperty, XConnection.Kind.class);
  }
  
  public void postLoad() {
    
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
  
  private SimpleObjectProperty<XConnection.Kind> connectionKindProperty = new SimpleObjectProperty<XConnection.Kind>(this, "connectionKind",_initConnectionKind());
  
  private static final XConnection.Kind _initConnectionKind() {
    return XConnection.Kind.CUBIC_CURVE;
  }
  
  public XConnection.Kind getConnectionKind() {
    return this.connectionKindProperty.get();
  }
  
  public void setConnectionKind(final XConnection.Kind connectionKind) {
    this.connectionKindProperty.set(connectionKind);
  }
  
  public ObjectProperty<XConnection.Kind> connectionKindProperty() {
    return this.connectionKindProperty;
  }
}
