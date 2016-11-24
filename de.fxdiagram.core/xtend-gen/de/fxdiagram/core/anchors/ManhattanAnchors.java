package de.fxdiagram.core.anchors;

import javafx.geometry.Point2D;
import javafx.geometry.Side;

@SuppressWarnings("all")
public interface ManhattanAnchors {
  public abstract Point2D getManhattanAnchor(final double x, final double y, final Side side);
}
