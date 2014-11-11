package de.fxdiagram.xtext.glue.shapes;

import de.fxdiagram.xtext.glue.mapping.ConnectionMapping;
import java.util.List;
import javafx.geometry.Side;

@SuppressWarnings("all")
public interface INodeWithLazyMappings {
  public abstract List<Side> getButtonSides(final ConnectionMapping<?> mapping);
}
