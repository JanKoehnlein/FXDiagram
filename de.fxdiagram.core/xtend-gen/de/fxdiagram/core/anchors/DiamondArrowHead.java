package de.fxdiagram.core.anchors;

import com.google.common.collect.Lists;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.anchors.ArrowHead;
import java.util.Collections;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class DiamondArrowHead extends ArrowHead {
  private double width;
  
  public DiamondArrowHead(final XConnection connection, final double width, final double height, final Property<Paint> strokeProperty, final Property<Paint> fillProperty, final boolean isSource) {
    super(connection, new Function0<Node>() {
      public Node apply() {
        Polygon _polygon = new Polygon();
        final Procedure1<Polygon> _function = new Procedure1<Polygon>() {
          public void apply(final Polygon it) {
            ObservableList<Double> _points = it.getPoints();
            _points.setAll(
              Collections.<Double>unmodifiableList(Lists.<Double>newArrayList(Double.valueOf(0.0), Double.valueOf(0.0), Double.valueOf((0.5 * width)), Double.valueOf(((-0.5) * height)), Double.valueOf(width), Double.valueOf(0.0), Double.valueOf((0.5 * width)), Double.valueOf((0.5 * height)))));
            ObjectProperty<Paint> _fillProperty = it.fillProperty();
            _fillProperty.bindBidirectional(fillProperty);
            ObjectProperty<Paint> _strokeProperty = it.strokeProperty();
            _strokeProperty.bind(strokeProperty);
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
  
  public DiamondArrowHead(final XConnection connection, final boolean isSource) {
    this(connection, 10, 10, connection.strokeProperty(), connection.strokeProperty(), isSource);
  }
  
  public double getLineCut() {
    XConnection _connection = this.getConnection();
    double _strokeWidth = _connection.getStrokeWidth();
    double _plus = (this.width + _strokeWidth);
    return _plus;
  }
}
