package de.fxdiagram.core.anchors;

import javafx.geometry.Point2D;

@SuppressWarnings("all")
public interface Anchors {
  public abstract Point2D getAnchor(final double x, final double y);
}
