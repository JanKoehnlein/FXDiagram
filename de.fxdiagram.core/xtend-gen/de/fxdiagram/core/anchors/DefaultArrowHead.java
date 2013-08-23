package de.fxdiagram.core.anchors;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.anchors.AbstractArrowHead;
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
public class DefaultArrowHead extends AbstractArrowHead {
  public DefaultArrowHead(final XConnection connection, final boolean isSource) {
    super(connection, new Function0<Node>() {
      public Node apply() {
        Polygon _polygon = new Polygon();
        final Procedure1<Polygon> _function = new Procedure1<Polygon>() {
          public void apply(final Polygon it) {
            ObservableList<Double> _points = it.getPoints();
            double _minus = (-5.0);
            _points.setAll(new Double[] { Double.valueOf(0.0), Double.valueOf(_minus), Double.valueOf(5.0), Double.valueOf(0.0), Double.valueOf(0.0), Double.valueOf(5.0) });
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
  }
  
  public double getLineCut() {
    XConnection _connection = this.getConnection();
    double _strokeWidth = _connection.getStrokeWidth();
    double _plus = (5 + _strokeWidth);
    return _plus;
  }
}
