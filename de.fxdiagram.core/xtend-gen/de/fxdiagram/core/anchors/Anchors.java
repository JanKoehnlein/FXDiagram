package de.fxdiagram.core.anchors;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XNode;
import javafx.geometry.Point2D;

/**
 * Calculates the endpoint of an {@link XConnection} on a {@link XNode}.
 */
@SuppressWarnings("all")
public interface Anchors {
  public abstract Point2D getAnchor(final double x, final double y);
}
