package de.fxdiagram.core;

import com.google.common.base.Objects;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.extensions.CoreExtensions;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;

@SuppressWarnings("all")
public class Placer extends ObjectBinding<Point2D> {
  private XRapidButton button;
  
  private double xPos;
  
  private double yPos;
  
  public Placer(final XRapidButton button, final double xPos, final double yPos) {
    this.button = button;
    this.xPos = xPos;
    this.yPos = yPos;
    this.activate();
  }
  
  public void activate() {
    final XNode node = this.button.getHost();
    DoubleProperty _layoutXProperty = node.layoutXProperty();
    DoubleProperty _layoutYProperty = node.layoutYProperty();
    DoubleProperty _scaleXProperty = node.scaleXProperty();
    DoubleProperty _scaleYProperty = node.scaleYProperty();
    ReadOnlyObjectProperty<Bounds> _layoutBoundsProperty = node.layoutBoundsProperty();
    this.bind(_layoutXProperty, _layoutYProperty, _scaleXProperty, _scaleYProperty, _layoutBoundsProperty);
  }
  
  protected Point2D computeValue() {
    Point2D _xblockexpression = null;
    {
      final XNode node = this.button.getHost();
      Bounds _layoutBounds = node.getLayoutBounds();
      final Bounds boundsInDiagram = CoreExtensions.localToDiagram(node, _layoutBounds);
      Point2D _xifexpression = null;
      boolean _notEquals = (!Objects.equal(boundsInDiagram, null));
      if (_notEquals) {
        Point2D _xblockexpression_1 = null;
        {
          final Bounds buttonBounds = this.button.getBoundsInLocal();
          double _width = boundsInDiagram.getWidth();
          double _width_1 = buttonBounds.getWidth();
          double _multiply = (2 * _width_1);
          final double totalWidth = (_width + _multiply);
          double _height = boundsInDiagram.getHeight();
          double _height_1 = buttonBounds.getHeight();
          double _multiply_1 = (2 * _height_1);
          final double totalHeight = (_height + _multiply_1);
          double _minX = boundsInDiagram.getMinX();
          double _width_2 = buttonBounds.getWidth();
          double _multiply_2 = (1.5 * _width_2);
          double _minus = (_minX - _multiply_2);
          double _minX_1 = buttonBounds.getMinX();
          double _minus_1 = (_minus - _minX_1);
          double _plus = (_minus_1 + (this.xPos * totalWidth));
          double _minY = boundsInDiagram.getMinY();
          double _height_2 = buttonBounds.getHeight();
          double _multiply_3 = (1.5 * _height_2);
          double _minus_2 = (_minY - _multiply_3);
          double _minY_1 = buttonBounds.getMinY();
          double _minus_3 = (_minus_2 - _minY_1);
          double _plus_1 = (_minus_3 + (this.yPos * totalHeight));
          final Point2D position = new Point2D(_plus, _plus_1);
          _xblockexpression_1 = position;
        }
        _xifexpression = _xblockexpression_1;
      } else {
        _xifexpression = null;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public double getXPos() {
    return this.xPos;
  }
  
  public double getYPos() {
    return this.yPos;
  }
}
