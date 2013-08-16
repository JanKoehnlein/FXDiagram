package de.fxdiagram.lib.nodes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Parent;

@SuppressWarnings("all")
public class RectangleNode extends Parent {
  private SimpleStringProperty nameProperty = new SimpleStringProperty(this, "name");
  
  public String getName() {
    return this.nameProperty.get();
  }
  
  public void setName(final String name) {
    this.nameProperty.set(name);
  }
  
  public StringProperty nameProperty() {
    return this.nameProperty;
  }
}
