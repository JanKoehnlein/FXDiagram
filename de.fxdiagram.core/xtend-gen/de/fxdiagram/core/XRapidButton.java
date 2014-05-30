package de.fxdiagram.core;

import com.google.common.base.Objects;
import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButtonAction;
import de.fxdiagram.core.extensions.CoreExtensions;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
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
  public static class Placer extends ObjectBinding<Point2D> {
    private XRapidButton button;
    
    private double xPos;
    
    private double yPos;
    
    public Placer(final XRapidButton button, final double xPos, final double yPos) {
      this.button = button;
      this.xPos = xPos;
      this.yPos = yPos;
      this.activate();
    }
    
    public void activate() {
      final XNode node = this.button.host;
      DoubleProperty _layoutXProperty = node.layoutXProperty();
      DoubleProperty _layoutYProperty = node.layoutYProperty();
      DoubleProperty _scaleXProperty = node.scaleXProperty();
      DoubleProperty _scaleYProperty = node.scaleYProperty();
      ReadOnlyObjectProperty<Bounds> _layoutBoundsProperty = node.layoutBoundsProperty();
      this.bind(_layoutXProperty, _layoutYProperty, _scaleXProperty, _scaleYProperty, _layoutBoundsProperty);
    }
    
    protected Point2D computeValue() {
      Point2D _xblockexpression = null;
      {
        final XNode node = this.button.host;
        Bounds _layoutBounds = node.getLayoutBounds();
        final Bounds boundsInDiagram = CoreExtensions.localToDiagram(node, _layoutBounds);
        Point2D _xifexpression = null;
        boolean _notEquals = (!Objects.equal(boundsInDiagram, null));
        if (_notEquals) {
          Point2D _xblockexpression_1 = null;
          {
            final Bounds buttonBounds = this.button.getBoundsInLocal();
            double _width = boundsInDiagram.getWidth();
            double _width_1 = buttonBounds.getWidth();
            double _multiply = (2 * _width_1);
            final double totalWidth = (_width + _multiply);
            double _height = boundsInDiagram.getHeight();
            double _height_1 = buttonBounds.getHeight();
            double _multiply_1 = (2 * _height_1);
            final double totalHeight = (_height + _multiply_1);
            double _minX = boundsInDiagram.getMinX();
            double _width_2 = buttonBounds.getWidth();
            double _multiply_2 = (1.5 * _width_2);
            double _minus = (_minX - _multiply_2);
            double _minX_1 = buttonBounds.getMinX();
            double _minus_1 = (_minus - _minX_1);
            double _plus = (_minus_1 + (this.xPos * totalWidth));
            double _minY = boundsInDiagram.getMinY();
            double _height_2 = buttonBounds.getHeight();
            double _multiply_3 = (1.5 * _height_2);
            double _minus_2 = (_minY - _multiply_3);
            double _minY_1 = buttonBounds.getMinY();
            double _minus_3 = (_minus_2 - _minY_1);
            double _plus_1 = (_minus_3 + (this.yPos * totalHeight));
            final Point2D position = new Point2D(_plus, _plus_1);
            _xblockexpression_1 = position;
          }
          _xifexpression = _xblockexpression_1;
        } else {
          _xifexpression = null;
        }
        _xblockexpression = _xifexpression;
      }
      return _xblockexpression;
    }
    
    public double getXPos() {
      return this.xPos;
    }
    
    public double getYPos() {
      return this.yPos;
    }
  }
  
  private XNode host;
  
  private XRapidButton.Placer placer;
  
  private XRapidButtonAction action;
  
  private Timeline timeline;
  
  public XRapidButton(final XNode host, final double xPos, final double yPos, final Image image, final XRapidButtonAction action) {
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
    XRapidButton.Placer _placer = new XRapidButton.Placer(this, xPos, yPos);
    this.placer = _placer;
  }
  
  public XRapidButton(final XNode host, final double xPos, final double yPos, final Node image, final XRapidButtonAction action) {
    this.host = host;
    this.action = action;
    ObservableList<Node> _children = this.getChildren();
    _children.add(image);
    XRapidButton.Placer _placer = new XRapidButton.Placer(this, xPos, yPos);
    this.placer = _placer;
  }
  
  public void activate() {
    boolean _isActive = this.getIsActive();
    boolean _not = (!_isActive);
    if (_not) {
      this.doActivate();
    }
    this.isActiveProperty.set(true);
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
        XRapidButton.this.setOpacity(0);
        XRapidButton.this.setVisible(false);
        XRapidButton.this.action.perform(XRapidButton.this);
        it.consume();
      }
    };
    this.setOnMousePressed(_function_2);
    this.placer.activate();
    final ChangeListener<Point2D> _function_3 = new ChangeListener<Point2D>() {
      public void changed(final ObservableValue<? extends Point2D> element, final Point2D oldVal, final Point2D newVal) {
        boolean _notEquals = (!Objects.equal(newVal, null));
        if (_notEquals) {
          double _x = newVal.getX();
          XRapidButton.this.setLayoutX(_x);
          double _y = newVal.getY();
          XRapidButton.this.setLayoutY(_y);
        }
      }
    };
    this.placer.addListener(_function_3);
    Point2D _value = this.placer.getValue();
    double _x = _value.getX();
    this.setLayoutX(_x);
    Point2D _value_1 = this.placer.getValue();
    double _y = _value_1.getY();
    this.setLayoutY(_y);
    Node _node = this.host.getNode();
    final EventHandler<MouseEvent> _function_4 = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        XRapidButton.this.show();
      }
    };
    _node.<MouseEvent>addEventHandler(MouseEvent.MOUSE_ENTERED, _function_4);
    Node _node_1 = this.host.getNode();
    final EventHandler<MouseEvent> _function_5 = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        XRapidButton.this.fade();
      }
    };
    _node_1.<MouseEvent>addEventHandler(MouseEvent.MOUSE_EXITED, _function_5);
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
  
  public XRapidButton.Placer getPlacer() {
    return this.placer;
  }
  
  public void show() {
    boolean _isEnabled = this.action.isEnabled(this);
    if (_isEnabled) {
      Timeline _timeline = this.getTimeline();
      _timeline.stop();
      this.setVisible(true);
      this.setOpacity(1.0);
    }
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
      _xblockexpression = this.timeline;
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
      _xblockexpression = Pos.valueOf(_plus_1);
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
