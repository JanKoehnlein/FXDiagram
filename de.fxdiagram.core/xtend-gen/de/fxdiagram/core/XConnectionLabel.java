package de.fxdiagram.core;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.extensions.TransformExtensions;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.XModelProvider;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * A label on an {@link XConnection}.
 * 
 * The {@link text} property denotes the actual text.
 * 
 * The label placed next to the connection the given {@link position} which is in between
 * 0 (start of the connection) and 1 (end of the connection). It is also rotated such that
 * it is always tangeltial to the curve of the connection at the given position and upside
 * up.
 */
@ModelNode({ "connection", "text" })
@SuppressWarnings("all")
public class XConnectionLabel extends XShape implements XModelProvider {
  private Effect selectionEffect = new DropShadow();
  
  public XConnectionLabel(final XConnection connection) {
    this.setConnection(connection);
  }
  
  public boolean setConnection(final XConnection connection) {
    boolean _xblockexpression = false;
    {
      XConnection _connection = this.getConnection();
      boolean _notEquals = (!Objects.equal(_connection, null));
      if (_notEquals) {
        throw new IllegalStateException("Cannot reset the connection on a label");
      }
      this.connectionProperty.set(connection);
      ObservableList<XConnectionLabel> _labels = connection.getLabels();
      _xblockexpression = _labels.add(this);
    }
    return _xblockexpression;
  }
  
  @Override
  protected Node createNode() {
    Text _text = this.getText();
    final Procedure1<Text> _function = (Text it) -> {
      it.setTextOrigin(VPos.TOP);
      Font _font = it.getFont();
      String _family = _font.getFamily();
      Font _font_1 = it.getFont();
      double _size = _font_1.getSize();
      double _multiply = (_size * 0.9);
      Font _font_2 = Font.font(_family, _multiply);
      it.setFont(_font_2);
    };
    return ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
  }
  
  @Override
  public void doActivate() {
    Text _text = this.getText();
    ObjectProperty<Paint> _fillProperty = _text.fillProperty();
    XConnection _connection = this.getConnection();
    ObjectProperty<Paint> _strokeProperty = _connection.strokeProperty();
    _fillProperty.bind(_strokeProperty);
    MoveBehavior<XConnectionLabel> _moveBehavior = new MoveBehavior<XConnectionLabel>(this);
    this.addBehavior(_moveBehavior);
  }
  
  @Override
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
  
  public void place(final boolean force) {
    final MoveBehavior moveBehavior = this.<MoveBehavior>getBehavior(MoveBehavior.class);
    boolean _and = false;
    boolean _isManuallyPlaced = moveBehavior.getIsManuallyPlaced();
    if (!_isManuallyPlaced) {
      _and = false;
    } else {
      _and = (!force);
    }
    if (_and) {
      return;
    }
    if (force) {
      moveBehavior.setIsManuallyPlaced(false);
    }
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
    boolean _greaterThan = (_abs > (Math.PI / 2));
    if (_greaterThan) {
      if ((angle < 0)) {
        angle = (angle + Math.PI);
      } else {
        if ((angle > 0)) {
          angle = (angle - Math.PI);
        }
      }
    }
    final Affine transform = new Affine();
    TransformExtensions.translate(transform, labelDx, labelDy);
    double _degrees = Math.toDegrees(angle);
    TransformExtensions.rotate(transform, _degrees);
    double _tx = transform.getTx();
    double _x_1 = center.getX();
    double _plus = (_tx + _x_1);
    this.setLayoutX(_plus);
    double _ty = transform.getTy();
    double _y_1 = center.getY();
    double _plus_1 = (_ty + _y_1);
    this.setLayoutY(_plus_1);
    transform.setTx(0);
    transform.setTy(0);
    ObservableList<Transform> _transforms = this.getTransforms();
    _transforms.setAll(transform);
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public XConnectionLabel() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(connectionProperty, XConnection.class);
    modelElement.addProperty(textProperty, Text.class);
  }
  
  private ReadOnlyObjectWrapper<XConnection> connectionProperty = new ReadOnlyObjectWrapper<XConnection>(this, "connection");
  
  public XConnection getConnection() {
    return this.connectionProperty.get();
  }
  
  public ReadOnlyObjectProperty<XConnection> connectionProperty() {
    return this.connectionProperty.getReadOnlyProperty();
  }
  
  private SimpleObjectProperty<Text> textProperty = new SimpleObjectProperty<Text>(this, "text",_initText());
  
  private static final Text _initText() {
    Text _text = new Text();
    return _text;
  }
  
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
