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
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * JavaFX's {@link Tooltip} affects gesture events in an unpredictable way.
 * This tooltip is a lightweight version not using a pop-up.
 */
@SuppressWarnings("all")
public class SoftTooltip {
  private StringProperty textProperty;
  
  private Node host;
  
  private XRoot root;
  
  private Node tooltip;
  
  private TooltipTimer timer;
  
  private boolean isHideOnTrigger = false;
  
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
    TooltipTimer _tooltipTimer = new TooltipTimer(this);
    this.timer = _tooltipTimer;
  }
  
  public void install(final Node host) {
    final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        EventType<? extends Event> _eventType = it.getEventType();
        boolean _matched = false;
        if (!_matched) {
          if (Objects.equal(_eventType, MouseEvent.MOUSE_ENTERED_TARGET)) {
            _matched=true;
            SoftTooltip.this.isHideOnTrigger = false;
            double _sceneX = it.getSceneX();
            double _sceneY = it.getSceneY();
            SoftTooltip.this.setReferencePosition(_sceneX, _sceneY);
            if (SoftTooltip.this.timer!=null) {
              SoftTooltip.this.timer.restart();
            }
          }
        }
        if (!_matched) {
          if (Objects.equal(_eventType, MouseEvent.MOUSE_EXITED_TARGET)) {
            _matched=true;
          }
        }
        if (!_matched) {
          if (Objects.equal(_eventType, MouseEvent.MOUSE_ENTERED)) {
            _matched=true;
            SoftTooltip.this.isHideOnTrigger = false;
            double _sceneX_1 = it.getSceneX();
            double _sceneY_1 = it.getSceneY();
            SoftTooltip.this.setReferencePosition(_sceneX_1, _sceneY_1);
            if (SoftTooltip.this.timer!=null) {
              SoftTooltip.this.timer.restart();
            }
          }
        }
        if (!_matched) {
          if (Objects.equal(_eventType, MouseEvent.MOUSE_MOVED)) {
            _matched=true;
            double _sceneX_2 = it.getSceneX();
            double _sceneY_2 = it.getSceneY();
            SoftTooltip.this.setReferencePosition(_sceneX_2, _sceneY_2);
            if (SoftTooltip.this.timer!=null) {
              SoftTooltip.this.timer.restart();
            }
          }
        }
        if (!_matched) {
          {
            SoftTooltip.this.isHideOnTrigger = true;
            if (SoftTooltip.this.timer!=null) {
              SoftTooltip.this.timer.restart();
            }
          }
        }
      }
    };
    host.<MouseEvent>addEventHandler(MouseEvent.ANY, _function);
  }
  
  public String getText() {
    return this.textProperty.get();
  }
  
  public void setText(final String text) {
    this.textProperty.set(text);
  }
  
  public boolean isShowing() {
    return this.isShowing;
  }
  
  public Node setReferencePosition(final double positionX, final double positionY) {
    return this.setPosition((positionX + 16), (positionY - 32));
  }
  
  public Node setPosition(final double positionX, final double positionY) {
    final Procedure1<Node> _function = new Procedure1<Node>() {
      public void apply(final Node it) {
        it.setLayoutX(positionX);
        it.setLayoutY(positionY);
      }
    };
    return ObjectExtensions.<Node>operator_doubleArrow(
      this.tooltip, _function);
  }
  
  public boolean show(final double positionX, final double positionY) {
    boolean _xblockexpression = false;
    {
      this.setReferencePosition(positionX, positionY);
      _xblockexpression = this.show();
    }
    return _xblockexpression;
  }
  
  public boolean trigger() {
    boolean _xifexpression = false;
    if (this.isHideOnTrigger) {
      _xifexpression = this.hide();
    } else {
      _xifexpression = this.show();
    }
    return _xifexpression;
  }
  
  public boolean show() {
    boolean _xblockexpression = false;
    {
      if ((!this.isShowing)) {
        XRoot _root = CoreExtensions.getRoot(this.host);
        this.root = _root;
        HeadsUpDisplay _headsUpDisplay = null;
        if (this.root!=null) {
          _headsUpDisplay=this.root.getHeadsUpDisplay();
        }
        ObservableList<Node> _children = null;
        if (_headsUpDisplay!=null) {
          _children=_headsUpDisplay.getChildren();
        }
        if (_children!=null) {
          _children.add(this.tooltip);
        }
      }
      _xblockexpression = this.isShowing = true;
    }
    return _xblockexpression;
  }
  
  public boolean hide() {
    boolean _xblockexpression = false;
    {
      if (this.isShowing) {
        HeadsUpDisplay _headsUpDisplay = null;
        if (this.root!=null) {
          _headsUpDisplay=this.root.getHeadsUpDisplay();
        }
        ObservableList<Node> _children = null;
        if (_headsUpDisplay!=null) {
          _children=_headsUpDisplay.getChildren();
        }
        if (_children!=null) {
          _children.remove(this.tooltip);
        }
      }
      _xblockexpression = this.isShowing = false;
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
