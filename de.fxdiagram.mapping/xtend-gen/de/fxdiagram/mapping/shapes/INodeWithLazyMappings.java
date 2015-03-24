package de.fxdiagram.mapping.shapes;

import de.fxdiagram.mapping.ConnectionMapping;
import java.util.List;
import javafx.geometry.Side;

@SuppressWarnings("all")
public interface INodeWithLazyMappings {
  public abstract List<Side> getButtonSides(final ConnectionMapping<?> mapping);
}
