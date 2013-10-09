package de.fxdiagram.core;

import com.google.common.base.Objects;
import de.fxdiagram.core.Placer;
import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XNode;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class XRapidButton extends Parent implements XActivatable {
  private XNode host;
  
  private Placer placer;
  
  private Procedure1<? super XRapidButton> action;
  
  private Timeline timeline;
  
  public XRapidButton(final XNode host, final double xPos, final double yPos, final Image image, final Procedure1<? super XRapidButton> action) {
    this.host = host;
    this.action = action;
    ObservableList<Node> _children = this.getChildren();
    ImageView _imageView = new ImageView();
    final Procedure1<ImageView> _function = new Procedure1<ImageView>() {
      public void apply(final ImageView it) {
        it.setImage(image);
      }
    };
    ImageView _doubleArrow = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function);
    _children.add(_doubleArrow);
    Placer _placer = new Placer(this, xPos, yPos);
    this.placer = _placer;
  }
  
  public void activate() {
    boolean _isActive = this.getIsActive();
    boolean _not = (!_isActive);
    if (_not) {
      this.doActivate();
    }
    this.isActiveProperty.set(true);
    Node _node = this.host.getNode();
    final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        XRapidButton.this.show();
      }
    };
    _node.<MouseEvent>addEventHandler(MouseEvent.MOUSE_ENTERED, _function);
    Node _node_1 = this.host.getNode();
    final EventHandler<MouseEvent> _function_1 = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        XRapidButton.this.fade();
      }
    };
    _node_1.<MouseEvent>addEventHandler(MouseEvent.MOUSE_EXITED, _function_1);
  }
  
  public void doActivate() {
    this.setVisible(false);
    final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        XRapidButton.this.show();
      }
    };
    this.setOnMouseEntered(_function);
    final EventHandler<MouseEvent> _function_1 = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        XRapidButton.this.fade();
      }
    };
    this.setOnMouseExited(_function_1);
    final EventHandler<MouseEvent> _function_2 = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        XRapidButton.this.action.apply(XRapidButton.this);
        it.consume();
      }
    };
    this.setOnMousePressed(_function_2);
    this.placer.activate();
    final ChangeListener<Point2D> _function_3 = new ChangeListener<Point2D>() {
      public void changed(final ObservableValue<? extends Point2D> element, final Point2D oldVal, final Point2D newVal) {
        double _x = newVal.getX();
        XRapidButton.this.setLayoutX(_x);
        double _y = newVal.getY();
        XRapidButton.this.setLayoutY(_y);
      }
    };
    this.placer.addListener(_function_3);
    Point2D _value = this.placer.getValue();
    double _x = _value.getX();
    this.setLayoutX(_x);
    Point2D _value_1 = this.placer.getValue();
    double _y = _value_1.getY();
    this.setLayoutY(_y);
  }
  
  protected void setPosition(final Point2D position) {
    double _x = position.getX();
    this.setTranslateX(_x);
    double _y = position.getY();
    this.setTranslateY(_y);
  }
  
  public XNode getHost() {
    return this.host;
  }
  
  public Placer getPlacer() {
    return this.placer;
  }
  
  public void show() {
    Timeline _timeline = this.getTimeline();
    _timeline.stop();
    this.setVisible(true);
    this.setOpacity(1.0);
  }
  
  public void fade() {
    Timeline _timeline = this.getTimeline();
    _timeline.playFromStart();
  }
  
  protected Timeline getTimeline() {
    Timeline _xblockexpression = null;
    {
      boolean _equals = Objects.equal(this.timeline, null);
      if (_equals) {
        Timeline _timeline = new Timeline();
        final Procedure1<Timeline> _function = new Procedure1<Timeline>() {
          public void apply(final Timeline it) {
            it.setAutoReverse(true);
            ObservableList<KeyFrame> _keyFrames = it.getKeyFrames();
            Duration _millis = Duration.millis(500);
            DoubleProperty _opacityProperty = XRapidButton.this.opacityProperty();
            Double _double = new Double(1.0);
            KeyValue _keyValue = new <Number>KeyValue(_opacityProperty, _double);
            KeyFrame _keyFrame = new KeyFrame(_millis, _keyValue);
            _keyFrames.add(_keyFrame);
            ObservableList<KeyFrame> _keyFrames_1 = it.getKeyFrames();
            Duration _millis_1 = Duration.millis(1000);
            DoubleProperty _opacityProperty_1 = XRapidButton.this.opacityProperty();
            KeyValue _keyValue_1 = new <Number>KeyValue(_opacityProperty_1, Double.valueOf(0.0));
            KeyFrame _keyFrame_1 = new KeyFrame(_millis_1, _keyValue_1);
            _keyFrames_1.add(_keyFrame_1);
            ObservableList<KeyFrame> _keyFrames_2 = it.getKeyFrames();
            Duration _millis_2 = Duration.millis(1000);
            BooleanProperty _visibleProperty = XRapidButton.this.visibleProperty();
            KeyValue _keyValue_2 = new <Boolean>KeyValue(_visibleProperty, Boolean.valueOf(false));
            KeyFrame _keyFrame_2 = new KeyFrame(_millis_2, _keyValue_2);
            _keyFrames_2.add(_keyFrame_2);
          }
        };
        Timeline _doubleArrow = ObjectExtensions.<Timeline>operator_doubleArrow(_timeline, _function);
        this.timeline = _doubleArrow;
      }
      _xblockexpression = (this.timeline);
    }
    return _xblockexpression;
  }
  
  public Pos getChooserPosition() {
    Pos _xblockexpression = null;
    {
      HPos _xifexpression = null;
      double _xPos = this.placer.getXPos();
      boolean _lessThan = (_xPos < 0.25);
      if (_lessThan) {
        _xifexpression = HPos.LEFT;
      } else {
        HPos _xifexpression_1 = null;
        double _xPos_1 = this.placer.getXPos();
        boolean _lessThan_1 = (_xPos_1 < 0.75);
        if (_lessThan_1) {
          _xifexpression_1 = HPos.CENTER;
        } else {
          _xifexpression_1 = HPos.RIGHT;
        }
        _xifexpression = _xifexpression_1;
      }
      final HPos hpos = _xifexpression;
      VPos _xifexpression_2 = null;
      double _yPos = this.placer.getYPos();
      boolean _lessThan_2 = (_yPos < 0.25);
      if (_lessThan_2) {
        _xifexpression_2 = VPos.TOP;
      } else {
        VPos _xifexpression_3 = null;
        double _yPos_1 = this.placer.getYPos();
        boolean _lessThan_3 = (_yPos_1 < 0.75);
        if (_lessThan_3) {
          _xifexpression_3 = VPos.CENTER;
        } else {
          _xifexpression_3 = VPos.BOTTOM;
        }
        _xifexpression_2 = _xifexpression_3;
      }
      final VPos vpos = _xifexpression_2;
      String _plus = (vpos + "_");
      String _plus_1 = (_plus + hpos);
      Pos _valueOf = Pos.valueOf(_plus_1);
      _xblockexpression = (_valueOf);
    }
    return _xblockexpression;
  }
  
  private ReadOnlyBooleanWrapper isActiveProperty = new ReadOnlyBooleanWrapper(this, "isActive");
  
  public boolean getIsActive() {
    return this.isActiveProperty.get();
  }
  
  public ReadOnlyBooleanProperty isActiveProperty() {
    return this.isActiveProperty.getReadOnlyProperty();
  }
}
