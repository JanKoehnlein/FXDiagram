package de.fxdiagram.core;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XLabel;
import de.fxdiagram.core.behavior.ConnectionLabelMoveBehavior;
import de.fxdiagram.core.extensions.TransformExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * A label on an {@link XConnection}.
 * 
 * The {@link text} property denotes the actual text.
 * 
 * The label placed next to the connection the given {@link position} which is in between
 * 0 (start of the connection) and 1 (end of the connection). It is also rotated such that
 * it is always tangential to the curve of the connection at the given position and upside
 * up.
 */
@ModelNode({ "connection", "position" })
@SuppressWarnings("all")
public class XConnectionLabel extends XLabel {
  private Effect selectionEffect = new DropShadow();
  
  public XConnectionLabel(final XConnection connection) {
    this.setConnection(connection);
  }
  
  public XConnectionLabel(final DomainObjectDescriptor domainObjectDescriptor) {
    super(domainObjectDescriptor);
  }
  
  public boolean setConnection(final XConnection connection) {
    boolean _xifexpression = false;
    XConnection _connection = this.getConnection();
    boolean _notEquals = (!Objects.equal(_connection, connection));
    if (_notEquals) {
      boolean _xblockexpression = false;
      {
        XConnection _connection_1 = this.getConnection();
        boolean _notEquals_1 = (!Objects.equal(_connection_1, null));
        if (_notEquals_1) {
          ObservableList<XConnectionLabel> _labels = this.getConnection().getLabels();
          _labels.remove(this);
        }
        this.connectionProperty.set(connection);
        boolean _xifexpression_1 = false;
        if (((!Objects.equal(connection, null)) && (!connection.getLabels().contains(this)))) {
          ObservableList<XConnectionLabel> _labels_1 = connection.getLabels();
          _xifexpression_1 = _labels_1.add(this);
        }
        _xblockexpression = _xifexpression_1;
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }
  
  @Override
  public boolean isSelectable() {
    return this.getIsActive();
  }
  
  @Override
  protected Node createNode() {
    Text _text = this.getText();
    final Procedure1<Text> _function = (Text it) -> {
      it.setTextOrigin(VPos.TOP);
      String _family = it.getFont().getFamily();
      double _size = it.getFont().getSize();
      double _multiply = (_size * 0.9);
      it.setFont(Font.font(_family, _multiply));
    };
    return ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
  }
  
  @Override
  public void selectionFeedback(final boolean isSelected) {
    if (isSelected) {
      this.setEffect(this.selectionEffect);
      XConnection _connection = this.getConnection();
      _connection.setSelected(true);
      this.setScaleX(1.05);
      this.setScaleY(1.05);
      this.toFront();
    } else {
      this.setEffect(null);
      this.setScaleX(1.0);
      this.setScaleY(1.0);
    }
  }
  
  @Override
  public void doActivate() {
    this.getText().fillProperty().bind(this.getConnection().strokeProperty());
    ConnectionLabelMoveBehavior _connectionLabelMoveBehavior = new ConnectionLabelMoveBehavior(this);
    this.addBehavior(_connectionLabelMoveBehavior);
    final ChangeListener<Number> _function = (ObservableValue<? extends Number> p, Number o, Number n) -> {
      this.place(true);
    };
    this.positionProperty.addListener(_function);
  }
  
  public void place(final boolean force) {
    boolean _isActive = this.getConnection().getIsActive();
    boolean _not = (!_isActive);
    if (_not) {
      return;
    }
    if ((this.getManuallyPlaced() && (!force))) {
      return;
    }
    if (force) {
      this.setManuallyPlaced(false);
    }
    final Point2D center = this.getConnection().at(this.getPosition());
    final Point2D derivative = this.getConnection().derivativeAt(this.getPosition());
    double angle = Math.atan2(derivative.getY(), derivative.getX());
    double _width = this.getNode().getBoundsInLocal().getWidth();
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
    TransformExtensions.rotate(transform, Math.toDegrees(angle));
    double _tx = transform.getTx();
    double _x = center.getX();
    double _plus = (_tx + _x);
    this.setLayoutX(_plus);
    double _ty = transform.getTy();
    double _y = center.getY();
    double _plus_1 = (_ty + _y);
    this.setLayoutY(_plus_1);
    transform.setTx(0);
    transform.setTy(0);
    this.getTransforms().setAll(transform);
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public XConnectionLabel() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
    modelElement.addProperty(connectionProperty, XConnection.class);
    modelElement.addProperty(positionProperty, Double.class);
  }
  
  public void postLoad() {
    
  }
  
  public String toString() {
    return ToString.toString(this);
  }
  
  private ReadOnlyObjectWrapper<XConnection> connectionProperty = new ReadOnlyObjectWrapper<XConnection>(this, "connection");
  
  public XConnection getConnection() {
    return this.connectionProperty.get();
  }
  
  public ReadOnlyObjectProperty<XConnection> connectionProperty() {
    return this.connectionProperty.getReadOnlyProperty();
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
