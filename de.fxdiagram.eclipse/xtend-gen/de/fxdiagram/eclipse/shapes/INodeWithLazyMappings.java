package de.fxdiagram.eclipse.shapes;

import de.fxdiagram.eclipse.mapping.ConnectionMapping;
import java.util.List;
import javafx.geometry.Side;

@SuppressWarnings("all")
public interface INodeWithLazyMappings {
  public abstract List<Side> getButtonSides(final ConnectionMapping<?> mapping);
}
