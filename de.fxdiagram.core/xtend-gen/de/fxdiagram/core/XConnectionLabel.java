package de.fxdiagram.core;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.extensions.TransformExtensions;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class XConnectionLabel extends XShape {
  private Effect selectionEffect;
  
  public XConnectionLabel(final XConnection connection) {
    this.setConnection(connection);
    ObservableList<XConnectionLabel> _labels = connection.getLabels();
    _labels.add(this);
    Text _text = new Text();
    final Procedure1<Text> _function = new Procedure1<Text>() {
      public void apply(final Text it) {
        it.setTextOrigin(VPos.TOP);
        Font _font = it.getFont();
        String _family = _font.getFamily();
        Font _font_1 = it.getFont();
        double _size = _font_1.getSize();
        double _multiply = (_size * 0.9);
        Font _font_2 = Font.font(_family, _multiply);
        it.setFont(_font_2);
        ObjectProperty<Paint> _fillProperty = it.fillProperty();
        ObjectProperty<Paint> _strokeProperty = connection.strokeProperty();
        _fillProperty.bind(_strokeProperty);
      }
    };
    Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
    this.setText(_doubleArrow);
    Text _text_1 = this.getText();
    this.setNode(_text_1);
    DropShadow _dropShadow = new DropShadow();
    this.selectionEffect = _dropShadow;
  }
  
  public void doActivate() {
    MoveBehavior<XConnectionLabel> _moveBehavior = new MoveBehavior<XConnectionLabel>(this);
    this.addBehavior(_moveBehavior);
  }
  
  public void selectionFeedback(final boolean isSelected) {
    if (isSelected) {
      this.setEffect(this.selectionEffect);
      this.setScaleX(1.05);
      this.setScaleY(1.05);
      XConnection _connection = this.getConnection();
      _connection.setSelected(true);
    } else {
      this.setEffect(null);
      this.setScaleX(1.0);
      this.setScaleY(1.0);
    }
  }
  
  public void place(final List<XControlPoint> list) {
    XConnection _connection = this.getConnection();
    double _position = this.getPosition();
    final Point2D center = _connection.at(_position);
    XConnection _connection_1 = this.getConnection();
    double _position_1 = this.getPosition();
    final Point2D derivative = _connection_1.derivativeAt(_position_1);
    double _y = derivative.getY();
    double _x = derivative.getX();
    double angle = Math.atan2(_y, _x);
    Bounds _boundsInLocal = this.getBoundsInLocal();
    double _width = _boundsInLocal.getWidth();
    double _minus = (-_width);
    final double labelDx = (_minus / 2);
    int labelDy = 1;
    double _abs = Math.abs(angle);
    double _divide = (Math.PI / 2);
    boolean _greaterThan = (_abs > _divide);
    if (_greaterThan) {
      boolean _lessThan = (angle < 0);
      if (_lessThan) {
        double _plus = (angle + Math.PI);
        angle = _plus;
      } else {
        boolean _greaterThan_1 = (angle > 0);
        if (_greaterThan_1) {
          double _minus_1 = (angle - Math.PI);
          angle = _minus_1;
        }
      }
    }
    Affine _affine = new Affine();
    final Affine transform = _affine;
    TransformExtensions.translate(transform, labelDx, labelDy);
    double _degrees = Math.toDegrees(angle);
    TransformExtensions.rotate(transform, _degrees);
    double _tx = transform.getTx();
    double _x_1 = center.getX();
    double _plus_1 = (_tx + _x_1);
    this.setLayoutX(_plus_1);
    double _ty = transform.getTy();
    double _y_1 = center.getY();
    double _plus_2 = (_ty + _y_1);
    this.setLayoutY(_plus_2);
    transform.setTx(0);
    transform.setTy(0);
    ObservableList<Transform> _transforms = this.getTransforms();
    _transforms.setAll(transform);
  }
  
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
  
  private SimpleObjectProperty<Text> textProperty = new SimpleObjectProperty<Text>(this, "text");
  
  public Text getText() {
    return this.textProperty.get();
  }
  
  public void setText(final Text text) {
    this.textProperty.set(text);
  }
  
  public ObjectProperty<Text> textProperty() {
    return this.textProperty;
  }
  
  private SimpleDoubleProperty positionProperty = new SimpleDoubleProperty(this, "position",_initPosition());
  
  private static final double _initPosition() {
    return 0.5;
  }
  
  public double getPosition() {
    return this.positionProperty.get();
  }
  
  public void setPosition(final double position) {
    this.positionProperty.set(position);
  }
  
  public DoubleProperty positionProperty() {
    return this.positionProperty;
  }
}
