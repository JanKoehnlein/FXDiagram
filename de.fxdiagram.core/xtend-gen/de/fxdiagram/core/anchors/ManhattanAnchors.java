package de.fxdiagram.core.anchors;

import javafx.geometry.Point2D;
import javafx.geometry.Side;

@SuppressWarnings("all")
public interface ManhattanAnchors {
  public abstract Point2D getManhattanAnchor(final double x, final double y, final Side side);
  
  public abstract Point2D getDefaultAnchor(final Side side);
  
  public abstract Point2D getDefaultSnapAnchor(final Side side);
}
