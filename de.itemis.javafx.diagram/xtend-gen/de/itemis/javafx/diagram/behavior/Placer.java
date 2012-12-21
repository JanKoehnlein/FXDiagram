package de.itemis.javafx.diagram.behavior;

import de.itemis.javafx.diagram.Activateable;
import de.itemis.javafx.diagram.Extensions;
import de.itemis.javafx.diagram.XNode;
import de.itemis.javafx.diagram.behavior.RapidButton;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;

@SuppressWarnings("all")
public class Placer extends ObjectBinding<Point2D> implements Activateable {
  private Extensions _extensions;
  
  private RapidButton button;
  
  private double xPos;
  
  private double yPos;
  
  public Placer(final RapidButton button, final double xPos, final double yPos) {
    this.button = button;
    this.xPos = xPos;
    this.yPos = yPos;
  }
  
  public void activate() {
    final XNode node = this.button.getHost();
    DoubleProperty _translateXProperty = node.translateXProperty();
    DoubleProperty _translateYProperty = node.translateYProperty();
    ReadOnlyObjectProperty<Bounds> _boundsInLocalProperty = node.boundsInLocalProperty();
    this.bind(_translateXProperty, _translateYProperty, _boundsInLocalProperty);
  }
  
  protected Point2D computeValue() {
    Point2D _xblockexpression = null;
    {
      XNode _host = this.button.getHost();
      final Node node = _host.getNode();
      double _translateX = node.getTranslateX();
      double _translateY = node.getTranslateY();
      final Point2D absPosition = this._extensions.localToRoot(node, _translateX, _translateY);
      Bounds _boundsInLocal = node.getBoundsInLocal();
      final Bounds absBounds = this._extensions.localToRoot(node, _boundsInLocal);
      double _width = absBounds.getWidth();
      Bounds _layoutBounds = this.button.getLayoutBounds();
      double _width_1 = _layoutBounds.getWidth();
      double _multiply = (2 * _width_1);
      final double totalWidth = (_width + _multiply);
      double _height = absBounds.getHeight();
      Bounds _layoutBounds_1 = this.button.getLayoutBounds();
      double _height_1 = _layoutBounds_1.getHeight();
      double _multiply_1 = (2 * _height_1);
      final double totalHeight = (_height + _multiply_1);
      double _x = absPosition.getX();
      Bounds _layoutBounds_2 = this.button.getLayoutBounds();
      double _width_2 = _layoutBounds_2.getWidth();
      double _multiply_2 = (1.5 * _width_2);
      double _minus = (_x - _multiply_2);
      double _multiply_3 = (this.xPos * totalWidth);
      double _plus = (_minus + _multiply_3);
      double _y = absPosition.getY();
      Bounds _layoutBounds_3 = this.button.getLayoutBounds();
      double _height_2 = _layoutBounds_3.getHeight();
      double _multiply_4 = (1.5 * _height_2);
      double _minus_1 = (_y - _multiply_4);
      double _multiply_5 = (this.yPos * totalHeight);
      double _plus_1 = (_minus_1 + _multiply_5);
      Point2D _point2D = new Point2D(_plus, _plus_1);
      _xblockexpression = (_point2D);
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
