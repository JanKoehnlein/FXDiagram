package de.fxdiagram.core.anchors;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.anchors.ArrowHead;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class TriangleArrowHead extends ArrowHead {
  private double width;
  
  public TriangleArrowHead(final XConnection connection, final double width, final double height, final Paint fill, final boolean isSource) {
    super(connection, new Function0<Node>() {
      public Node apply() {
        Polygon _polygon = new Polygon();
        final Procedure1<Polygon> _function = new Procedure1<Polygon>() {
          public void apply(final Polygon it) {
            ObservableList<Double> _points = it.getPoints();
            double _minus = (-0.5);
            double _multiply = (_minus * height);
            double _multiply_1 = (0.5 * height);
            _points.setAll(new Double[] { Double.valueOf(0.0), Double.valueOf(_multiply), Double.valueOf(width), Double.valueOf(0.0), Double.valueOf(0.0), Double.valueOf(_multiply_1) });
            it.setFill(fill);
            ObjectProperty<Paint> _strokeProperty = it.strokeProperty();
            ObjectProperty<Paint> _strokeProperty_1 = connection.strokeProperty();
            _strokeProperty.bind(_strokeProperty_1);
            DoubleProperty _strokeWidthProperty = it.strokeWidthProperty();
            DoubleProperty _strokeWidthProperty_1 = connection.strokeWidthProperty();
            _strokeWidthProperty.bind(_strokeWidthProperty_1);
            it.setStrokeType(StrokeType.CENTERED);
          }
        };
        Polygon _doubleArrow = ObjectExtensions.<Polygon>operator_doubleArrow(_polygon, _function);
        return _doubleArrow;
      }
    }.apply(), isSource);
    this.width = width;
  }
  
  public TriangleArrowHead(final XConnection connection, final boolean isSource) {
    this(connection, 5, 10, new Function0<Paint>() {
      public Paint apply() {
        Paint _stroke = connection.getStroke();
        return _stroke;
      }
    }.apply(), isSource);
  }
  
  public double getLineCut() {
    XConnection _connection = this.getConnection();
    double _strokeWidth = _connection.getStrokeWidth();
    double _plus = (this.width + _strokeWidth);
    return _plus;
  }
}
