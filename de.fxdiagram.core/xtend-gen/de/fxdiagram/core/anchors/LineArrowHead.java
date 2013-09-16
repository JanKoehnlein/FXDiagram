package de.fxdiagram.core.anchors;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.anchors.ArrowHead;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeType;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class LineArrowHead extends ArrowHead {
  private double width;
  
  public LineArrowHead(final XConnection connection, final double width, final double height, final Paint fill, final boolean isSource) {
    super(connection, new Function0<Node>() {
      public Node apply() {
        Group _group = new Group();
        final Procedure1<Group> _function = new Procedure1<Group>() {
          public void apply(final Group it) {
            ObservableList<Node> _children = it.getChildren();
            Polyline _polyline = new Polyline();
            final Procedure1<Polyline> _function = new Procedure1<Polyline>() {
              public void apply(final Polyline it) {
                ObservableList<Double> _points = it.getPoints();
                double _minus = (-0.5);
                double _multiply = (_minus * height);
                double _multiply_1 = (0.5 * height);
                _points.setAll(new Double[] { Double.valueOf(0.0), Double.valueOf(_multiply), Double.valueOf(width), Double.valueOf(0.0), Double.valueOf(0.0), Double.valueOf(_multiply_1) });
                ObjectProperty<Paint> _strokeProperty = it.strokeProperty();
                ObjectProperty<Paint> _strokeProperty_1 = connection.strokeProperty();
                _strokeProperty.bind(_strokeProperty_1);
                DoubleProperty _strokeWidthProperty = it.strokeWidthProperty();
                DoubleProperty _strokeWidthProperty_1 = connection.strokeWidthProperty();
                _strokeWidthProperty.bind(_strokeWidthProperty_1);
                it.setStrokeType(StrokeType.CENTERED);
              }
            };
            Polyline _doubleArrow = ObjectExtensions.<Polyline>operator_doubleArrow(_polyline, _function);
            _children.add(_doubleArrow);
            ObservableList<Node> _children_1 = it.getChildren();
            Polyline _polyline_1 = new Polyline();
            final Procedure1<Polyline> _function_1 = new Procedure1<Polyline>() {
              public void apply(final Polyline it) {
                ObservableList<Double> _points = it.getPoints();
                double _strokeWidth = connection.getStrokeWidth();
                double _minus = (width - _strokeWidth);
                _points.setAll(new Double[] { Double.valueOf(0.0), Double.valueOf(0.0), Double.valueOf(_minus), Double.valueOf(0.0) });
                ObjectProperty<Paint> _strokeProperty = it.strokeProperty();
                ObjectProperty<Paint> _strokeProperty_1 = connection.strokeProperty();
                _strokeProperty.bind(_strokeProperty_1);
                DoubleProperty _strokeWidthProperty = it.strokeWidthProperty();
                DoubleProperty _strokeWidthProperty_1 = connection.strokeWidthProperty();
                _strokeWidthProperty.bind(_strokeWidthProperty_1);
                it.setStrokeType(StrokeType.CENTERED);
              }
            };
            Polyline _doubleArrow_1 = ObjectExtensions.<Polyline>operator_doubleArrow(_polyline_1, _function_1);
            _children_1.add(_doubleArrow_1);
          }
        };
        Group _doubleArrow = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
        return _doubleArrow;
      }
    }.apply(), isSource);
    this.width = width;
  }
  
  public LineArrowHead(final XConnection connection, final boolean isSource) {
    this(connection, 8, 15, new Function0<Paint>() {
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
