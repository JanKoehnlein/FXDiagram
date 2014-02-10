package de.fxdiagram.core.anchors;

import com.google.common.collect.Lists;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.anchors.ArrowHead;
import de.fxdiagram.core.model.ModelElement;
import de.fxdiagram.core.model.ModelLoad;
import de.fxdiagram.core.model.XModelProvider;
import java.util.Collections;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeType;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode({ "connection", "isSource", "width", "height", "stroke" })
@SuppressWarnings("all")
public class LineArrowHead extends ArrowHead implements XModelProvider {
  public LineArrowHead(final XConnection connection, final double width, final double height, final Property<Paint> strokeProperty, final boolean isSource) {
    this.setConnection(connection);
    this.setIsSource(isSource);
    this.setWidth(width);
    this.setHeight(height);
    this.strokeProperty.bind(strokeProperty);
    this.initialize();
  }
  
  public LineArrowHead(final XConnection connection, final boolean isSource) {
    this(connection, 7, 10, connection.strokeProperty(), isSource);
  }
  
  public void initialize() {
    Group _group = new Group();
    final Procedure1<Group> _function = new Procedure1<Group>() {
      public void apply(final Group it) {
        ObservableList<Node> _children = it.getChildren();
        Polyline _polyline = new Polyline();
        final Procedure1<Polyline> _function = new Procedure1<Polyline>() {
          public void apply(final Polyline it) {
            ObservableList<Double> _points = it.getPoints();
            double _height = LineArrowHead.this.getHeight();
            double _multiply = ((-0.5) * _height);
            double _width = LineArrowHead.this.getWidth();
            double _height_1 = LineArrowHead.this.getHeight();
            double _multiply_1 = (0.5 * _height_1);
            _points.setAll(Collections.<Double>unmodifiableList(Lists.<Double>newArrayList(Double.valueOf(0.0), Double.valueOf(_multiply), Double.valueOf(_width), Double.valueOf(0.0), Double.valueOf(0.0), Double.valueOf(_multiply_1))));
            ObjectProperty<Paint> _strokeProperty = it.strokeProperty();
            _strokeProperty.bind(LineArrowHead.this.strokeProperty);
            DoubleProperty _strokeWidthProperty = it.strokeWidthProperty();
            XConnection _connection = LineArrowHead.this.getConnection();
            DoubleProperty _strokeWidthProperty_1 = _connection.strokeWidthProperty();
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
            double _width = LineArrowHead.this.getWidth();
            XConnection _connection = LineArrowHead.this.getConnection();
            double _strokeWidth = _connection.getStrokeWidth();
            double _minus = (_width - _strokeWidth);
            _points.setAll(Collections.<Double>unmodifiableList(Lists.<Double>newArrayList(Double.valueOf(0.0), Double.valueOf(0.0), Double.valueOf(_minus), Double.valueOf(0.0))));
            ObjectProperty<Paint> _strokeProperty = it.strokeProperty();
            _strokeProperty.bind(LineArrowHead.this.strokeProperty);
            DoubleProperty _strokeWidthProperty = it.strokeWidthProperty();
            XConnection _connection_1 = LineArrowHead.this.getConnection();
            DoubleProperty _strokeWidthProperty_1 = _connection_1.strokeWidthProperty();
            _strokeWidthProperty.bind(_strokeWidthProperty_1);
            it.setStrokeType(StrokeType.CENTERED);
          }
        };
        Polyline _doubleArrow_1 = ObjectExtensions.<Polyline>operator_doubleArrow(_polyline_1, _function_1);
        _children_1.add(_doubleArrow_1);
      }
    };
    Group _doubleArrow = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
    this.setNode(_doubleArrow);
    super.initialize();
  }
  
  public double getLineCut() {
    double _width = this.getWidth();
    XConnection _connection = this.getConnection();
    double _strokeWidth = _connection.getStrokeWidth();
    return (_width + _strokeWidth);
  }
  
  /**
   * Automatically generated by @ModelNode. Used in model deserialization.
   */
  public LineArrowHead(final ModelLoad modelLoad) {
  }
  
  public void populate(final ModelElement modelElement) {
    modelElement.addProperty(connectionProperty(), XConnection.class);
    modelElement.addProperty(isSourceProperty(), Boolean.class);
    modelElement.addProperty(widthProperty, Double.class);
    modelElement.addProperty(heightProperty, Double.class);
    modelElement.addProperty(strokeProperty, Paint.class);
  }
  
  private SimpleDoubleProperty widthProperty = new SimpleDoubleProperty(this, "width");
  
  public double getWidth() {
    return this.widthProperty.get();
  }
  
  public void setWidth(final double width) {
    this.widthProperty.set(width);
  }
  
  public DoubleProperty widthProperty() {
    return this.widthProperty;
  }
  
  private SimpleDoubleProperty heightProperty = new SimpleDoubleProperty(this, "height");
  
  public double getHeight() {
    return this.heightProperty.get();
  }
  
  public void setHeight(final double height) {
    this.heightProperty.set(height);
  }
  
  public DoubleProperty heightProperty() {
    return this.heightProperty;
  }
  
  private SimpleObjectProperty<Paint> strokeProperty = new SimpleObjectProperty<Paint>(this, "stroke");
  
  public Paint getStroke() {
    return this.strokeProperty.get();
  }
  
  public void setStroke(final Paint stroke) {
    this.strokeProperty.set(stroke);
  }
  
  public ObjectProperty<Paint> strokeProperty() {
    return this.strokeProperty;
  }
}
