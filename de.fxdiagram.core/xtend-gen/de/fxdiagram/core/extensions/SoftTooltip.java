package de.fxdiagram.core.extensions;

import com.google.common.base.Objects;
import de.fxdiagram.core.HeadsUpDisplay;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.core.extensions.TooltipTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * JavaFX's {@link Tooltip} affectes gesture events in an unpredictable way.
 * This tooltip is a lightweight version not using a pop-up.
 */
@SuppressWarnings("all")
public class SoftTooltip {
  private StringProperty textProperty;
  
  private Node host;
  
  private Node tooltip;
  
  private TooltipTimer timer;
  
  private boolean canceled = false;
  
  private boolean isShowing;
  
  public SoftTooltip(final Node host, final String text) {
    this.host = host;
    StackPane _stackPane = new StackPane();
    final Procedure1<StackPane> _function = new Procedure1<StackPane>() {
      public void apply(final StackPane it) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("-fx-border-color: black;");
        _builder.newLine();
        _builder.append("-fx-border-width: 1;");
        _builder.newLine();
        _builder.append("-fx-background-color: #ffffbb;");
        _builder.newLine();
        it.setStyle(_builder.toString());
        ObservableList<Node> _children = it.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function = new Procedure1<Text>() {
          public void apply(final Text it) {
            it.setText(text);
            StringProperty _textProperty = it.textProperty();
            SoftTooltip.this.textProperty = _textProperty;
            Insets _insets = new Insets(2, 2, 2, 2);
            StackPane.setMargin(it, _insets);
          }
        };
        Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
        _children.add(_doubleArrow);
        it.setMouseTransparent(true);
      }
    };
    StackPane _doubleArrow = ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
    this.tooltip = _doubleArrow;
  }
  
  protected void install(final Node currentHost) {
    final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        EventType<? extends Event> _eventType = it.getEventType();
        final EventType<? extends Event> getEventType = _eventType;
        boolean _matched = false;
        if (!_matched) {
          if (Objects.equal(getEventType,MouseEvent.MOUSE_ENTERED)) {
            _matched=true;
            double _sceneX = it.getSceneX();
            double _sceneY = it.getSceneY();
            SoftTooltip.this.setReferencePosition(_sceneX, _sceneY);
            TooltipTimer _tooltipTimer = new TooltipTimer(SoftTooltip.this);
            SoftTooltip.this.timer = _tooltipTimer;
          }
        }
        if (!_matched) {
          if (Objects.equal(getEventType,MouseEvent.MOUSE_MOVED)) {
            _matched=true;
            double _sceneX_1 = it.getSceneX();
            double _sceneY_1 = it.getSceneY();
            SoftTooltip.this.setReferencePosition(_sceneX_1, _sceneY_1);
            if (SoftTooltip.this.timer!=null) {
              SoftTooltip.this.timer.restart();
            }
          }
        }
        if (!_matched) {
          {
            if (SoftTooltip.this.timer!=null) {
              SoftTooltip.this.timer.stop();
            }
            SoftTooltip.this.timer = null;
            SoftTooltip.this.hide();
            EventType<? extends Event> _eventType_1 = it.getEventType();
            boolean _notEquals = (!Objects.equal(_eventType_1, MouseEvent.MOUSE_EXITED));
            SoftTooltip.this.canceled = _notEquals;
          }
        }
      }
    };
    currentHost.<MouseEvent>addEventHandler(MouseEvent.ANY, _function);
    boolean _matched = false;
    if (!_matched) {
      if (currentHost instanceof Parent) {
        _matched=true;
        ObservableList<Node> _childrenUnmodifiable = ((Parent)currentHost).getChildrenUnmodifiable();
        final Procedure1<Node> _function_1 = new Procedure1<Node>() {
          public void apply(final Node it) {
            SoftTooltip.this.install(it);
          }
        };
        IterableExtensions.<Node>forEach(_childrenUnmodifiable, _function_1);
      }
    }
  }
  
  public String getText() {
    String _get = this.textProperty.get();
    return _get;
  }
  
  public void setText(final String text) {
    this.textProperty.set(text);
  }
  
  public boolean isShowing() {
    return this.isShowing;
  }
  
  public Node setReferencePosition(final double positionX, final double positionY) {
    double _plus = (positionX + 16);
    double _minus = (positionY - 32);
    Node _setPosition = this.setPosition(_plus, _minus);
    return _setPosition;
  }
  
  public Node setPosition(final double positionX, final double positionY) {
    final Procedure1<Node> _function = new Procedure1<Node>() {
      public void apply(final Node it) {
        it.setLayoutX(positionX);
        it.setLayoutY(positionY);
      }
    };
    Node _doubleArrow = ObjectExtensions.<Node>operator_doubleArrow(
      this.tooltip, _function);
    return _doubleArrow;
  }
  
  public boolean show(final double positionX, final double positionY) {
    boolean _xblockexpression = false;
    {
      this.setReferencePosition(positionX, positionY);
      boolean _show = this.show();
      _xblockexpression = (_show);
    }
    return _xblockexpression;
  }
  
  public boolean show() {
    boolean _xblockexpression = false;
    {
      boolean _and = false;
      boolean _notEquals = (!Objects.equal(this.host, null));
      if (!_notEquals) {
        _and = false;
      } else {
        boolean _not = (!this.isShowing);
        _and = (_notEquals && _not);
      }
      if (_and) {
        XRoot _root = CoreExtensions.getRoot(this.host);
        HeadsUpDisplay _headsUpDisplay = _root.getHeadsUpDisplay();
        ObservableList<Node> _children = _headsUpDisplay.getChildren();
        _children.add(this.tooltip);
      }
      boolean _isShowing = this.isShowing = true;
      _xblockexpression = (_isShowing);
    }
    return _xblockexpression;
  }
  
  public boolean hide() {
    boolean _xblockexpression = false;
    {
      boolean _and = false;
      boolean _notEquals = (!Objects.equal(this.host, null));
      if (!_notEquals) {
        _and = false;
      } else {
        _and = (_notEquals && this.isShowing);
      }
      if (_and) {
        XRoot _root = CoreExtensions.getRoot(this.host);
        HeadsUpDisplay _headsUpDisplay = _root.getHeadsUpDisplay();
        ObservableList<Node> _children = _headsUpDisplay.getChildren();
        _children.remove(this.tooltip);
      }
      boolean _isShowing = this.isShowing = false;
      _xblockexpression = (_isShowing);
    }
    return _xblockexpression;
  }
  
  private SimpleObjectProperty<Duration> delayProperty = new SimpleObjectProperty<Duration>(this, "delay",_initDelay());
  
  private static final Duration _initDelay() {
    Duration _millis = DurationExtensions.millis(200);
    return _millis;
  }
  
  public Duration getDelay() {
    return this.delayProperty.get();
  }
  
  public void setDelay(final Duration delay) {
    this.delayProperty.set(delay);
  }
  
  public ObjectProperty<Duration> delayProperty() {
    return this.delayProperty;
  }
}
