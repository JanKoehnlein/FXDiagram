package de.fxdiagram.core.anchors;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import de.fxdiagram.core.extensions.Point2DExtensions;
import de.fxdiagram.core.extensions.TransformExtensions;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.core.model.XModelProvider;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Affine;

/**
 * Arrow head for the start or end of an {@link XConnection}.
 * 
 * In order to avoid ugly overlapping with the connections curve, the curve ends
 * {@link #getLineCut()} units before the anchor point, but will have the same
 * tangent as calculated for the exact anchor point.
 * 
 * For the node's coordiante system consider that it is the target arrow head of
 * a connection comming from (-infinity, 0) ending in (0,0).
 */
@ModelNode({ "connection", "isSource", "width", "height", "stroke" })
@Logging
@SuppressWarnings("all")
public abstract class ArrowHead extends Parent implements XModelProvider {
  private Node node;
  
  public ArrowHead(final XConnection connection, final double width, final double height, final Paint stroke, final boolean isSource) {
    this.setConnection(connection);
    this.setIsSource(isSource);
    this.setWidth(width);
    this.setHeight(height);
    boolean _notEquals = (!Objects.equal(stroke, null));
    if (_notEquals) {
      this.strokeProperty.set(stroke);
    }
  }
  
  public void initializeGraphics() {
    Node _node = this.getNode();
    boolean _equals = Objects.equal(_node, null);
    if (_equals) {
      ArrowHead.LOG.severe("Node is null");
    }
  }
  
  public Node getNode() {
    Node _xblockexpression = null;
    {
      Paint _stroke = this.getStroke();
      boolean _equals = Objects.equal(_stroke, null);
      if (_equals) {
        this.strokeProperty.bind(this.getConnection().strokeProperty());
      }
      boolean _equals_1 = Objects.equal(this.node, null);
      if (_equals_1) {
        this.node = this.createNode();
        ObservableList<Node> _children = this.getChildren();
        _children.add(this.node);
        this.setIsSource(this.getIsSource());
        boolean _isSource = this.getIsSource();
        if (_isSource) {
          XConnection _connection = this.getConnection();
          _connection.setSourceArrowHead(this);
        } else {
          XConnection _connection_1 = this.getConnection();
          _connection_1.setTargetArrowHead(this);
        }
      }
      _xblockexpression = this.node;
    }
    return _xblockexpression;
  }
  
  public abstract Node createNode();
  
  public double getLineCut() {
    return this.getConnection().getStrokeWidth();
  }
  
  public Point2D correctAnchor(final double x, final double y, final Point2D anchorOnOutline) {
    Point2D _xblockexpression = null;
    {
      if (((!Objects.equal(anchorOnOutline, null)) && (this.getLineCut() > 0))) {
        double _x = anchorOnOutline.getX();
        double _minus = (_x - x);
        double _y = anchorOnOutline.getY();
        double _minus_1 = (_y - y);
        final Point2D direction = new Point2D(_minus, _minus_1);
        double _norm = Point2DExtensions.norm(direction);
        boolean _greaterThan = (_norm > NumberExpressionExtensions.EPSILON);
        if (_greaterThan) {
          double _lineCut = this.getLineCut();
          double _norm_1 = Point2DExtensions.norm(direction);
          double _divide = (_lineCut / _norm_1);
          final Point2D correction = Point2DExtensions.operator_multiply(direction, _divide);
          return Point2DExtensions.operator_minus(anchorOnOutline, correction);
        }
      }
      _xblockexpression = anchorOnOutline;
    }
    return _xblockexpression;
  }
  
  public void place() {
    int _xifexpression = (int) 0;
    boolean _isSource = this.getIsSource();
    if (_isSource) {
      _xifexpression = 0;
    } else {
      _xifexpression = 1;
    }
    final int t = _xifexpression;
    final Affine trafo = new Affine();
    final Bounds headBounds = this.getBoundsInLocal();
    double _width = headBounds.getWidth();
    double _minus = (-_width);
    double _minX = headBounds.getMinX();
    double _minus_1 = (_minus - _minX);
    double _lineCut = this.getLineCut();
    double _plus = (_minus_1 + _lineCut);
    double _height = headBounds.getHeight();
    double _multiply = ((-0.5) * _height);
    double _minY = headBounds.getMinY();
    double _minus_2 = (_multiply - _minY);
    TransformExtensions.translate(trafo, _plus, _minus_2);
    final Point2D derivative = this.getConnection().derivativeAt(t);
    double _degrees = Math.toDegrees(Math.atan2(derivative.getY(), derivative.getX()));
    int _xifexpression_1 = (int) 0;
    boolean _isSource_1 = this.getIsSource();
    if (_isSource_1) {
      _xifexpression_1 = 180;
    } else {
      _xifexpression_1 = 0;
    }
    final double angle = (_degrees + _xifexpression_1);
    TransformExtensions.rotate(trafo, angle);
    final Point2D pos = this.getConnection().at(t);
    TransformExtensions.translate(trafo, pos.getX(), pos.getY());
    this.getTransforms().setAll(trafo);
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public ArrowHead() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(connectionProperty, XConnection.class);
    modelElement.addProperty(isSourceProperty, Boolean.class);
    modelElement.addProperty(widthProperty, Double.class);
    modelElement.addProperty(heightProperty, Double.class);
    modelElement.addProperty(strokeProperty, Paint.class);
  }
  
  public void postLoad() {
    
  }
  
  public String toString() {
    return ToString.toString(this);
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.anchors.ArrowHead");
    ;
  
  private SimpleObjectProperty<XConnection> connectionProperty = new SimpleObjectProperty<XConnection>(this, "connection");
  
  public XConnection getConnection() {
    return this.connectionProperty.get();
  }
  
  public void setConnection(final XConnection connection) {
    this.connectionProperty.set(connection);
  }
  
  public ObjectProperty<XConnection> connectionProperty() {
    return this.connectionProperty;
  }
  
  private SimpleBooleanProperty isSourceProperty = new SimpleBooleanProperty(this, "isSource");
  
  public boolean getIsSource() {
    return this.isSourceProperty.get();
  }
  
  public void setIsSource(final boolean isSource) {
    this.isSourceProperty.set(isSource);
  }
  
  public BooleanProperty isSourceProperty() {
    return this.isSourceProperty;
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
