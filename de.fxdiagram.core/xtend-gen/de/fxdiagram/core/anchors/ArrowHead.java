package de.fxdiagram.core.anchors;

import com.google.common.base.Objects;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import de.fxdiagram.core.extensions.Point2DExtensions;
import de.fxdiagram.core.extensions.TransformExtensions;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

@SuppressWarnings("all")
public abstract class ArrowHead extends Parent {
  private Node node;
  
  private XConnection connection;
  
  private boolean isSource;
  
  public ArrowHead(final XConnection connection, final Node node, final boolean isSource) {
    this.connection = connection;
    this.node = node;
    ObservableList<Node> _children = this.getChildren();
    _children.add(node);
    this.isSource = isSource;
    if (isSource) {
      connection.setSourceArrowHead(this);
    } else {
      connection.setTargetArrowHead(this);
    }
  }
  
  public double getLineCut() {
    double _strokeWidth = this.connection.getStrokeWidth();
    return _strokeWidth;
  }
  
  public XConnection getConnection() {
    return this.connection;
  }
  
  public Node getNode() {
    return this.node;
  }
  
  public Point2D correctAnchor(final double x, final double y, final Point2D anchorOnOutline) {
    Point2D _xblockexpression = null;
    {
      boolean _and = false;
      boolean _notEquals = (!Objects.equal(anchorOnOutline, null));
      if (!_notEquals) {
        _and = false;
      } else {
        double _lineCut = this.getLineCut();
        boolean _greaterThan = (_lineCut > 0);
        _and = (_notEquals && _greaterThan);
      }
      if (_and) {
        double _x = anchorOnOutline.getX();
        double _minus = (_x - x);
        double _y = anchorOnOutline.getY();
        double _minus_1 = (_y - y);
        Point2D _point2D = new Point2D(_minus, _minus_1);
        final Point2D direction = _point2D;
        double _norm = Point2DExtensions.norm(direction);
        boolean _greaterThan_1 = (_norm > NumberExpressionExtensions.EPSILON);
        if (_greaterThan_1) {
          double _lineCut_1 = this.getLineCut();
          double _norm_1 = Point2DExtensions.norm(direction);
          double _divide = (_lineCut_1 / _norm_1);
          final Point2D correction = Point2DExtensions.operator_multiply(direction, _divide);
          return Point2DExtensions.operator_minus(anchorOnOutline, correction);
        }
      }
      _xblockexpression = (anchorOnOutline);
    }
    return _xblockexpression;
  }
  
  public void place() {
    int _xifexpression = (int) 0;
    if (this.isSource) {
      _xifexpression = 0;
    } else {
      _xifexpression = 1;
    }
    final int t = _xifexpression;
    Affine _affine = new Affine();
    final Affine trafo = _affine;
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
    final Point2D derivative = this.connection.derivativeAt(t);
    double _y = derivative.getY();
    double _x = derivative.getX();
    double _atan2 = Math.atan2(_y, _x);
    double _degrees = Math.toDegrees(_atan2);
    int _xifexpression_1 = (int) 0;
    if (this.isSource) {
      _xifexpression_1 = 180;
    } else {
      _xifexpression_1 = 0;
    }
    final double angle = (_degrees + _xifexpression_1);
    TransformExtensions.rotate(trafo, angle);
    final Point2D pos = this.connection.at(t);
    double _x_1 = pos.getX();
    double _y_1 = pos.getY();
    TransformExtensions.translate(trafo, _x_1, _y_1);
    ObservableList<Transform> _transforms = this.getTransforms();
    _transforms.setAll(trafo);
  }
}
