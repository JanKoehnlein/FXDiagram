package de.fxdiagram.core;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.geometry.TransformExtensions;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.VPos;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class XConnectionLabel extends XShape {
  private MoveBehavior moveBehavior;
  
  private Effect selectionEffect;
  
  public XConnectionLabel(final XConnection connection) {
    this.setConnection(connection);
    connection.setLabel(this);
    Text _text = new Text();
    final Procedure1<Text> _function = new Procedure1<Text>() {
      public void apply(final Text it) {
        it.setTextOrigin(VPos.TOP);
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
    MoveBehavior _moveBehavior = new MoveBehavior(this);
    this.moveBehavior = _moveBehavior;
    this.moveBehavior.activate();
    BooleanProperty _selectedProperty = this.selectedProperty();
    final ChangeListener<Boolean> _function = new ChangeListener<Boolean>() {
      public void changed(final ObservableValue<? extends Boolean> observable, final Boolean oldValue, final Boolean newValue) {
        if ((newValue).booleanValue()) {
          XConnectionLabel.this.setEffect(XConnectionLabel.this.selectionEffect);
          XConnectionLabel.this.setScaleX(1.05);
          XConnectionLabel.this.setScaleY(1.05);
          XConnection _connection = XConnectionLabel.this.getConnection();
          _connection.setSelected(true);
        } else {
          XConnectionLabel.this.setEffect(null);
          XConnectionLabel.this.setScaleX(1.0);
          XConnectionLabel.this.setScaleY(1.0);
        }
      }
    };
    _selectedProperty.addListener(_function);
  }
  
  protected Boolean place(final List<XControlPoint> list) {
    Boolean _xblockexpression = null;
    {
      ObservableList<Transform> _transforms = this.getTransforms();
      _transforms.clear();
      Boolean _xifexpression = null;
      int _size = list.size();
      boolean _equals = (_size == 2);
      if (_equals) {
        boolean _xblockexpression_1 = false;
        {
          XControlPoint _get = list.get(0);
          double _layoutX = _get.getLayoutX();
          XControlPoint _get_1 = list.get(1);
          double _layoutX_1 = _get_1.getLayoutX();
          double _plus = (_layoutX + _layoutX_1);
          final double centerX = (0.5 * _plus);
          XControlPoint _get_2 = list.get(0);
          double _layoutY = _get_2.getLayoutY();
          XControlPoint _get_3 = list.get(1);
          double _layoutY_1 = _get_3.getLayoutY();
          double _plus_1 = (_layoutY + _layoutY_1);
          final double centerY = (0.5 * _plus_1);
          XControlPoint _get_4 = list.get(1);
          double _layoutX_2 = _get_4.getLayoutX();
          XControlPoint _get_5 = list.get(0);
          double _layoutX_3 = _get_5.getLayoutX();
          final double dx = (_layoutX_2 - _layoutX_3);
          XControlPoint _get_6 = list.get(1);
          double _layoutY_2 = _get_6.getLayoutY();
          XControlPoint _get_7 = list.get(0);
          double _layoutY_3 = _get_7.getLayoutY();
          final double dy = (_layoutY_2 - _layoutY_3);
          double angle = Math.atan2(dy, dx);
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
              double _plus_2 = (angle + Math.PI);
              angle = _plus_2;
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
          double _plus_3 = (_tx + centerX);
          this.setLayoutX(_plus_3);
          double _ty = transform.getTy();
          double _plus_4 = (_ty + centerY);
          this.setLayoutY(_plus_4);
          transform.setTx(0);
          transform.setTy(0);
          ObservableList<Transform> _transforms_1 = this.getTransforms();
          boolean _add = _transforms_1.add(transform);
          _xblockexpression_1 = (_add);
        }
        _xifexpression = Boolean.valueOf(_xblockexpression_1);
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  public MoveBehavior getMoveBehavior() {
    return this.moveBehavior;
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
}
