package de.fxdiagram.lib.buttons;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.behavior.AbstractHostBehavior;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.core.extensions.InitializingListListener;
import de.fxdiagram.core.extensions.InitializingListener;
import de.fxdiagram.core.extensions.Point2DExtensions;
import de.fxdiagram.lib.buttons.RapidButton;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javafx.animation.FadeTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class RapidButtonBehavior<HOST extends XNode> extends AbstractHostBehavior<HOST> {
  @Accessors
  private double border;
  
  private final Map<Side, Pane> pos2group = Collections.<Side, Pane>unmodifiableMap(CollectionLiterals.<Side, Pane>newHashMap(Pair.<Side, HBox>of(Side.TOP, new HBox()), Pair.<Side, HBox>of(Side.BOTTOM, new HBox()), Pair.<Side, VBox>of(Side.LEFT, new VBox()), Pair.<Side, VBox>of(Side.RIGHT, new VBox())));
  
  private ObservableList<RapidButton> buttonsProperty = FXCollections.<RapidButton>observableArrayList();
  
  private Group allButtons = new Group();
  
  private final FadeTransition fadeTransition = ObjectExtensions.<FadeTransition>operator_doubleArrow(new FadeTransition(), new Procedure1<FadeTransition>() {
    public void apply(final FadeTransition it) {
      it.setNode(RapidButtonBehavior.this.allButtons);
      Duration _millis = DurationExtensions.millis(500);
      it.setDelay(_millis);
      Duration _millis_1 = DurationExtensions.millis(500);
      it.setDuration(_millis_1);
      it.setFromValue(1);
      it.setToValue(0);
      final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
        public void handle(final ActionEvent it) {
          RapidButtonBehavior.this.allButtons.setVisible(false);
        }
      };
      it.setOnFinished(_function);
    }
  });
  
  public RapidButtonBehavior(final HOST host) {
    super(host);
    ObservableList<Node> _children = this.allButtons.getChildren();
    Collection<Pane> _values = this.pos2group.values();
    Iterables.<Node>addAll(_children, _values);
    this.allButtons.setVisible(false);
  }
  
  public boolean add(final RapidButton button) {
    boolean _xblockexpression = false;
    {
      Side _position = button.getPosition();
      final Pane group = this.pos2group.get(_position);
      boolean _equals = Objects.equal(group, null);
      if (_equals) {
        Side _position_1 = button.getPosition();
        String _plus = ("Illegal XRapidButton position " + _position_1);
        throw new IllegalArgumentException(_plus);
      }
      _xblockexpression = this.buttonsProperty.add(button);
    }
    return _xblockexpression;
  }
  
  public boolean remove(final RapidButton button) {
    boolean _xblockexpression = false;
    {
      Side _position = button.getPosition();
      final Pane group = this.pos2group.get(_position);
      boolean _equals = Objects.equal(group, null);
      if (_equals) {
        Side _position_1 = button.getPosition();
        String _plus = ("Illegal XRapidButton position " + _position_1);
        throw new IllegalArgumentException(_plus);
      }
      _xblockexpression = this.buttonsProperty.remove(button);
    }
    return _xblockexpression;
  }
  
  protected void doActivate() {
    HOST _host = this.getHost();
    XDiagram _diagram = CoreExtensions.getDiagram(_host);
    Group _buttonLayer = _diagram.getButtonLayer();
    ObservableList<Node> _children = _buttonLayer.getChildren();
    _children.add(this.allButtons);
    InitializingListListener<RapidButton> _initializingListListener = new InitializingListListener<RapidButton>();
    final Procedure1<InitializingListListener<RapidButton>> _function = new Procedure1<InitializingListListener<RapidButton>>() {
      public void apply(final InitializingListListener<RapidButton> it) {
        final Procedure1<RapidButton> _function = new Procedure1<RapidButton>() {
          public void apply(final RapidButton button) {
            button.activate();
            Side _position = button.getPosition();
            final Pane group = RapidButtonBehavior.this.pos2group.get(_position);
            BooleanProperty _enabledProperty = button.enabledProperty();
            InitializingListener<Boolean> _initializingListener = new InitializingListener<Boolean>();
            final Procedure1<InitializingListener<Boolean>> _function = new Procedure1<InitializingListener<Boolean>>() {
              public void apply(final InitializingListener<Boolean> it) {
                final Procedure1<Boolean> _function = new Procedure1<Boolean>() {
                  public void apply(final Boolean it) {
                    if ((it).booleanValue()) {
                      ObservableList<Node> _children = group.getChildren();
                      _children.add(button);
                    } else {
                      ObservableList<Node> _children_1 = group.getChildren();
                      _children_1.remove(button);
                    }
                    RapidButtonBehavior.this.layout();
                  }
                };
                it.setSet(_function);
              }
            };
            InitializingListener<Boolean> _doubleArrow = ObjectExtensions.<InitializingListener<Boolean>>operator_doubleArrow(_initializingListener, _function);
            CoreExtensions.<Boolean>addInitializingListener(_enabledProperty, _doubleArrow);
          }
        };
        it.setAdd(_function);
      }
    };
    InitializingListListener<RapidButton> _doubleArrow = ObjectExtensions.<InitializingListListener<RapidButton>>operator_doubleArrow(_initializingListListener, _function);
    CoreExtensions.<RapidButton>addInitializingListener(this.buttonsProperty, _doubleArrow);
    this.layout();
    HOST _host_1 = this.getHost();
    Node _node = _host_1.getNode();
    final EventHandler<MouseEvent> _function_1 = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        RapidButtonBehavior.this.show();
      }
    };
    _node.<MouseEvent>addEventHandler(MouseEvent.MOUSE_ENTERED, _function_1);
    HOST _host_2 = this.getHost();
    Node _node_1 = _host_2.getNode();
    final EventHandler<MouseEvent> _function_2 = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        RapidButtonBehavior.this.fade();
      }
    };
    _node_1.<MouseEvent>addEventHandler(MouseEvent.MOUSE_EXITED, _function_2);
    final EventHandler<MouseEvent> _function_3 = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        RapidButtonBehavior.this.allButtons.setVisible(false);
      }
    };
    this.allButtons.setOnMousePressed(_function_3);
    final EventHandler<MouseEvent> _function_4 = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        RapidButtonBehavior.this.show();
      }
    };
    this.allButtons.setOnMouseEntered(_function_4);
    final EventHandler<MouseEvent> _function_5 = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        RapidButtonBehavior.this.fade();
      }
    };
    this.allButtons.setOnMouseExited(_function_5);
    Collection<Pane> _values = this.pos2group.values();
    final Procedure1<Pane> _function_6 = new Procedure1<Pane>() {
      public void apply(final Pane it) {
        ReadOnlyObjectProperty<Bounds> _layoutBoundsProperty = it.layoutBoundsProperty();
        final ChangeListener<Bounds> _function = new ChangeListener<Bounds>() {
          public void changed(final ObservableValue<? extends Bounds> p, final Bounds o, final Bounds n) {
            it.layout();
          }
        };
        _layoutBoundsProperty.addListener(_function);
      }
    };
    IterableExtensions.<Pane>forEach(_values, _function_6);
    HOST _host_3 = this.getHost();
    ReadOnlyObjectProperty<Bounds> _boundsInParentProperty = _host_3.boundsInParentProperty();
    final ChangeListener<Bounds> _function_7 = new ChangeListener<Bounds>() {
      public void changed(final ObservableValue<? extends Bounds> p, final Bounds o, final Bounds n) {
        RapidButtonBehavior.this.layout();
      }
    };
    _boundsInParentProperty.addListener(_function_7);
  }
  
  public Group show() {
    Group _xblockexpression = null;
    {
      this.fadeTransition.stop();
      final Procedure1<Group> _function = new Procedure1<Group>() {
        public void apply(final Group it) {
          it.setVisible(true);
          it.setOpacity(1.0);
        }
      };
      _xblockexpression = ObjectExtensions.<Group>operator_doubleArrow(
        this.allButtons, _function);
    }
    return _xblockexpression;
  }
  
  public void fade() {
    this.fadeTransition.play();
  }
  
  public Class<? extends Behavior> getBehaviorKey() {
    return this.getClass();
  }
  
  public void layout() {
    HOST _host = this.getHost();
    HOST _host_1 = this.getHost();
    Bounds _layoutBounds = _host_1.getLayoutBounds();
    final Bounds hostBounds = CoreExtensions.localToDiagram(_host, _layoutBounds);
    final Point2D hostCenter = BoundsExtensions.center(hostBounds);
    Set<Map.Entry<Side, Pane>> _entrySet = this.pos2group.entrySet();
    for (final Map.Entry<Side, Pane> pos2group : _entrySet) {
      {
        final Side pos = pos2group.getKey();
        final Pane group = pos2group.getValue();
        group.layout();
        final Bounds groupBounds = group.getBoundsInLocal();
        Point2D _center = BoundsExtensions.center(groupBounds);
        final Point2D centered = Point2DExtensions.operator_minus(hostCenter, _center);
        final Procedure1<Pane> _function = new Procedure1<Pane>() {
          public void apply(final Pane it) {
            double _x = centered.getX();
            double _switchResult = (double) 0;
            if (pos != null) {
              switch (pos) {
                case LEFT:
                  double _width = hostBounds.getWidth();
                  double _multiply = ((-0.5) * _width);
                  double _minus = (_multiply - RapidButtonBehavior.this.border);
                  double _width_1 = groupBounds.getWidth();
                  _switchResult = (_minus - _width_1);
                  break;
                case RIGHT:
                  double _width_2 = hostBounds.getWidth();
                  double _multiply_1 = (0.5 * _width_2);
                  double _plus = (_multiply_1 + RapidButtonBehavior.this.border);
                  double _width_3 = groupBounds.getWidth();
                  _switchResult = (_plus + _width_3);
                  break;
                default:
                  _switchResult = 0;
                  break;
              }
            } else {
              _switchResult = 0;
            }
            double _plus_1 = (_x + _switchResult);
            it.setLayoutX(_plus_1);
            double _y = centered.getY();
            double _switchResult_1 = (double) 0;
            if (pos != null) {
              switch (pos) {
                case TOP:
                  double _height = hostBounds.getHeight();
                  double _multiply_2 = ((-0.5) * _height);
                  double _minus_1 = (_multiply_2 - RapidButtonBehavior.this.border);
                  double _height_1 = groupBounds.getHeight();
                  _switchResult_1 = (_minus_1 - _height_1);
                  break;
                case BOTTOM:
                  double _height_2 = hostBounds.getHeight();
                  double _multiply_3 = (0.5 * _height_2);
                  double _plus_2 = (_multiply_3 + RapidButtonBehavior.this.border);
                  double _height_3 = groupBounds.getHeight();
                  _switchResult_1 = (_plus_2 + _height_3);
                  break;
                default:
                  _switchResult_1 = 0;
                  break;
              }
            } else {
              _switchResult_1 = 0;
            }
            double _plus_3 = (_y + _switchResult_1);
            it.setLayoutY(_plus_3);
          }
        };
        ObjectExtensions.<Pane>operator_doubleArrow(group, _function);
      }
    }
  }
  
  @Pure
  public double getBorder() {
    return this.border;
  }
  
  public void setBorder(final double border) {
    this.border = border;
  }
}
