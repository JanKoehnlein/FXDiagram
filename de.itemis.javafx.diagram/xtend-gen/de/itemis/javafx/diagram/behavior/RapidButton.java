package de.itemis.javafx.diagram.behavior;

import de.itemis.javafx.diagram.Activateable;
import de.itemis.javafx.diagram.XNode;
import de.itemis.javafx.diagram.behavior.Placer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure3;

@SuppressWarnings("all")
public class RapidButton extends ImageView implements Activateable {
  private XNode host;
  
  private Placer placer;
  
  private Procedure1<? super RapidButton> action;
  
  private Timeline timeline;
  
  public RapidButton(final XNode host, final double xPos, final double yPos, final String file, final Procedure1<? super RapidButton> action) {
    this.host = host;
    this.action = action;
    Image _image = new Image(file);
    this.setImage(_image);
    Placer _placer = new Placer(this, xPos, yPos);
    this.placer = _placer;
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
      boolean _equals = ObjectExtensions.operator_equals(this.timeline, null);
      if (_equals) {
        Timeline _timeline = new Timeline();
        final Procedure1<Timeline> _function = new Procedure1<Timeline>() {
            public void apply(final Timeline it) {
              it.setAutoReverse(true);
              ObservableList<KeyFrame> _keyFrames = it.getKeyFrames();
              Duration _millis = Duration.millis(500);
              DoubleProperty _opacityProperty = RapidButton.this.opacityProperty();
              KeyValue _keyValue = new KeyValue(_opacityProperty, Double.valueOf(1.0));
              KeyFrame _keyFrame = new KeyFrame(_millis, _keyValue);
              _keyFrames.add(_keyFrame);
              ObservableList<KeyFrame> _keyFrames_1 = it.getKeyFrames();
              Duration _millis_1 = Duration.millis(1000);
              DoubleProperty _opacityProperty_1 = RapidButton.this.opacityProperty();
              KeyValue _keyValue_1 = new KeyValue(_opacityProperty_1, Double.valueOf(0.0));
              KeyFrame _keyFrame_1 = new KeyFrame(_millis_1, _keyValue_1);
              _keyFrames_1.add(_keyFrame_1);
              ObservableList<KeyFrame> _keyFrames_2 = it.getKeyFrames();
              Duration _millis_2 = Duration.millis(1000);
              BooleanProperty _visibleProperty = RapidButton.this.visibleProperty();
              KeyValue _keyValue_2 = new KeyValue(_visibleProperty, Boolean.valueOf(false));
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
  
  public void activate() {
    this.setVisible(false);
    final Procedure1<MouseEvent> _function = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          RapidButton.this.show();
        }
      };
    this.setOnMouseEntered(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function.apply(arg0);
        }
    });
    final Procedure1<MouseEvent> _function_1 = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          RapidButton.this.fade();
        }
      };
    this.setOnMouseExited(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function_1.apply(arg0);
        }
    });
    final Procedure1<MouseEvent> _function_2 = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          RapidButton.this.action.apply(RapidButton.this);
        }
      };
    this.setOnMousePressed(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function_2.apply(arg0);
        }
    });
    this.placer.activate();
    Point2D _value = this.placer.getValue();
    this.setPosition(_value);
    final Procedure3<ObservableValue<? extends Point2D>,Point2D,Point2D> _function_3 = new Procedure3<ObservableValue<? extends Point2D>,Point2D,Point2D>() {
        public void apply(final ObservableValue<? extends Point2D> element, final Point2D oldVal, final Point2D newVal) {
          RapidButton.this.setPosition(newVal);
        }
      };
    final ChangeListener<Point2D> listener = new ChangeListener<Point2D>() {
        public void changed(ObservableValue<? extends Point2D> arg0,Point2D arg1,Point2D arg2) {
          _function_3.apply(arg0,arg1,arg2);
        }
    };
    this.placer.addListener(listener);
  }
}
