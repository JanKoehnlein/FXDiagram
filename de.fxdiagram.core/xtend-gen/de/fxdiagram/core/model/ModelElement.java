package de.fxdiagram.core.model;

import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;

@SuppressWarnings("all")
public interface ModelElement {
  public abstract List<? extends Property<?>> getProperties();
  
  public abstract List<? extends ListProperty<?>> getListProperties();
  
  public abstract Class<?> getType(final Property<?> property);
  
  public abstract boolean isPrimitive(final Property<?> property);
  
  public abstract Object getNode();
}
