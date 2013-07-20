package de.fxdiagram.core.tools;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.XAbstractDiagram;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.core.binding.StringExpressionExtensions;
import de.fxdiagram.core.tools.XDiagramTool;
import de.fxdiagram.core.tools.XNodeChooserTransition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.transform.Transform;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public abstract class AbstractXNodeChooser implements XDiagramTool {
  private DoubleProperty currentPositionProperty = new Function0<DoubleProperty>() {
    public DoubleProperty apply() {
      SimpleDoubleProperty _simpleDoubleProperty = new SimpleDoubleProperty(0.0);
      return _simpleDoubleProperty;
    }
  }.apply();
  
  private final ArrayList<XNode> visibleNodes = new Function0<ArrayList<XNode>>() {
    public ArrayList<XNode> apply() {
      ArrayList<XNode> _newArrayList = CollectionLiterals.<XNode>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  private XNode host;
  
  private Group group = new Function0<Group>() {
    public Group apply() {
      Group _group = new Group();
      return _group;
    }
  }.apply();
  
  private final LinkedHashMap<String,XNode> nodeMap = new Function0<LinkedHashMap<String,XNode>>() {
    public LinkedHashMap<String,XNode> apply() {
      LinkedHashMap<String,XNode> _newLinkedHashMap = CollectionLiterals.<String, XNode>newLinkedHashMap();
      return _newLinkedHashMap;
    }
  }.apply();
  
  private ChangeListener<Number> positionListener;
  
  protected XNodeChooserTransition spinToPosition;
  
  private EventHandler<SwipeEvent> swipeHandler;
  
  private EventHandler<ScrollEvent> scrollHandler;
  
  private EventHandler<KeyEvent> keyHandler;
  
  private ChangeListener<String> filterChangeListener;
  
  private Pos layoutPosition;
  
  private Button plusButton;
  
  private Button minusButton;
  
  public AbstractXNodeChooser(final XNode host, final Pos layoutPosition, final boolean hasButtons) {
    this.host = host;
    this.layoutPosition = layoutPosition;
    final ChangeListener<Number> _function = new ChangeListener<Number>() {
        public void changed(final ObservableValue<? extends Number> element, final Number oldValue, final Number newValue) {
          final double newVal = newValue.doubleValue();
          ArrayList<XNode> _nodes = AbstractXNodeChooser.this.getNodes();
          int _size = _nodes.size();
          double _modulo = (newVal % _size);
          AbstractXNodeChooser.this.setInterpolatedPosition(_modulo);
        }
      };
    this.positionListener = _function;
    XNodeChooserTransition _xNodeChooserTransition = new XNodeChooserTransition(this);
    this.spinToPosition = _xNodeChooserTransition;
    final EventHandler<SwipeEvent> _function_1 = new EventHandler<SwipeEvent>() {
        public void handle(final SwipeEvent it) {
          int _switchResult = (int) 0;
          EventType<? extends Event> _eventType = it.getEventType();
          final EventType<? extends Event> getEventType = _eventType;
          boolean _matched = false;
          if (!_matched) {
            if (Objects.equal(getEventType,SwipeEvent.SWIPE_DOWN)) {
              _matched=true;
              int _minus = (-1);
              _switchResult = _minus;
            }
          }
          if (!_matched) {
            if (Objects.equal(getEventType,SwipeEvent.SWIPE_RIGHT)) {
              _matched=true;
              int _minus_1 = (-1);
              _switchResult = _minus_1;
            }
          }
          if (!_matched) {
            _switchResult = 1;
          }
          final int direction = _switchResult;
          int _multiply = (direction * 10);
          AbstractXNodeChooser.this.spinToPosition.setTargetPositionDelta(_multiply);
        }
      };
    this.swipeHandler = _function_1;
    final EventHandler<ScrollEvent> _function_2 = new EventHandler<ScrollEvent>() {
        public void handle(final ScrollEvent it) {
          EventType<? extends Event> _eventType = it.getEventType();
          boolean _equals = Objects.equal(_eventType, ScrollEvent.SCROLL_FINISHED);
          if (_equals) {
            double _currentPosition = AbstractXNodeChooser.this.getCurrentPosition();
            double _plus = (_currentPosition + 0.5);
            AbstractXNodeChooser.this.spinToPosition.setTargetPosition(((int) _plus));
          } else {
            double _currentPosition_1 = AbstractXNodeChooser.this.getCurrentPosition();
            double _deltaX = it.getDeltaX();
            double _deltaY = it.getDeltaY();
            double _plus_1 = (_deltaX + _deltaY);
            double _divide = (_plus_1 / 100);
            double _minus = (_currentPosition_1 - _divide);
            AbstractXNodeChooser.this.setCurrentPosition(_minus);
          }
        }
      };
    this.scrollHandler = _function_2;
    final EventHandler<KeyEvent> _function_3 = new EventHandler<KeyEvent>() {
        public void handle(final KeyEvent it) {
          KeyCode _code = it.getCode();
          final KeyCode getCode = _code;
          boolean _matched = false;
          if (!_matched) {
            if (Objects.equal(getCode,KeyCode.CANCEL)) {
              _matched=true;
              AbstractXNodeChooser.this.cancel();
            }
          }
          if (!_matched) {
            if (Objects.equal(getCode,KeyCode.ESCAPE)) {
              _matched=true;
              AbstractXNodeChooser.this.cancel();
            }
          }
          if (!_matched) {
            if (Objects.equal(getCode,KeyCode.UP)) {
              _matched=true;
              AbstractXNodeChooser.this.spinToPosition.setTargetPositionDelta(1);
            }
          }
          if (!_matched) {
            if (Objects.equal(getCode,KeyCode.LEFT)) {
              _matched=true;
              int _minus = (-1);
              AbstractXNodeChooser.this.spinToPosition.setTargetPositionDelta(_minus);
            }
          }
          if (!_matched) {
            if (Objects.equal(getCode,KeyCode.DOWN)) {
              _matched=true;
              int _minus_1 = (-1);
              AbstractXNodeChooser.this.spinToPosition.setTargetPositionDelta(_minus_1);
            }
          }
          if (!_matched) {
            if (Objects.equal(getCode,KeyCode.RIGHT)) {
              _matched=true;
              AbstractXNodeChooser.this.spinToPosition.setTargetPositionDelta(1);
            }
          }
          if (!_matched) {
            if (Objects.equal(getCode,KeyCode.ENTER)) {
              _matched=true;
              XNode _currentNode = AbstractXNodeChooser.this.getCurrentNode();
              AbstractXNodeChooser.this.nodeChosen(_currentNode);
              XRootDiagram _rootDiagram = Extensions.getRootDiagram(host);
              _rootDiagram.restoreDefaultTool();
            }
          }
          if (!_matched) {
            if (Objects.equal(getCode,KeyCode.BACK_SPACE)) {
              _matched=true;
              final String oldFilter = AbstractXNodeChooser.this.getFilterString();
              boolean _isEmpty = oldFilter.isEmpty();
              boolean _not = (!_isEmpty);
              if (_not) {
                int _length = oldFilter.length();
                int _minus_2 = (_length - 1);
                String _substring = oldFilter.substring(0, _minus_2);
                AbstractXNodeChooser.this.setFilterString(_substring);
              }
            }
          }
          if (!_matched) {
            String _filterString = AbstractXNodeChooser.this.getFilterString();
            String _text = it.getText();
            String _plus = (_filterString + _text);
            AbstractXNodeChooser.this.setFilterString(_plus);
          }
        }
      };
    this.keyHandler = _function_3;
    final ChangeListener<String> _function_4 = new ChangeListener<String>() {
        public void changed(final ObservableValue<? extends String> property, final String oldValue, final String newValue) {
          AbstractXNodeChooser.this.calculateVisibleNodes();
        }
      };
    this.filterChangeListener = _function_4;
    if (hasButtons) {
      boolean _and = false;
      HPos _hpos = layoutPosition.getHpos();
      boolean _notEquals = (!Objects.equal(_hpos, HPos.CENTER));
      if (!_notEquals) {
        _and = false;
      } else {
        HPos _hpos_1 = layoutPosition.getHpos();
        boolean _notEquals_1 = (!Objects.equal(_hpos_1, null));
        _and = (_notEquals && _notEquals_1);
      }
      final boolean isVertical = _and;
      Button _button = new Button();
      final Procedure1<Button> _function_5 = new Procedure1<Button>() {
          public void apply(final Button it) {
            String _xifexpression = null;
            if (isVertical) {
              _xifexpression = "button-down";
            } else {
              _xifexpression = "button-right";
            }
            it.setId(_xifexpression);
            String _id = it.getId();
            it.setText(_id);
            final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
                public void handle(final ActionEvent it) {
                  int _minus = (-1);
                  AbstractXNodeChooser.this.spinToPosition.setTargetPositionDelta(_minus);
                }
              };
            it.setOnAction(_function);
            it.setFocusTraversable(false);
          }
        };
      Button _doubleArrow = ObjectExtensions.<Button>operator_doubleArrow(_button, _function_5);
      this.minusButton = _doubleArrow;
      Button _button_1 = new Button();
      final Procedure1<Button> _function_6 = new Procedure1<Button>() {
          public void apply(final Button it) {
            String _xifexpression = null;
            if (isVertical) {
              _xifexpression = "button-up";
            } else {
              _xifexpression = "button-left";
            }
            it.setId(_xifexpression);
            String _id = it.getId();
            it.setText(_id);
            final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
                public void handle(final ActionEvent it) {
                  AbstractXNodeChooser.this.spinToPosition.setTargetPositionDelta(1);
                }
              };
            it.setOnAction(_function);
            it.setFocusTraversable(false);
          }
        };
      Button _doubleArrow_1 = ObjectExtensions.<Button>operator_doubleArrow(_button_1, _function_6);
      this.plusButton = _doubleArrow_1;
    }
    Label _label = new Label();
    final Procedure1<Label> _function_7 = new Procedure1<Label>() {
        public void apply(final Label it) {
          StringProperty _textProperty = it.textProperty();
          StringExpression _plus = StringExpressionExtensions.operator_plus("Filter: ", AbstractXNodeChooser.this.filterStringProperty);
          StringExpression _plus_1 = StringExpressionExtensions.operator_plus(_plus, "");
          _textProperty.bind(_plus_1);
        }
      };
    Label _doubleArrow_2 = ObjectExtensions.<Label>operator_doubleArrow(_label, _function_7);
    this.setFilterLabel(_doubleArrow_2);
  }
  
  public boolean operator_add(final XNode node) {
    boolean _xifexpression = false;
    String _key = node.getKey();
    boolean _containsKey = this.nodeMap.containsKey(_key);
    boolean _not = (!_containsKey);
    if (_not) {
      boolean _xblockexpression = false;
      {
        String _key_1 = node.getKey();
        this.nodeMap.put(_key_1, node);
        node.layout();
        this.calculateVisibleNodes();
        ObservableList<Node> _children = this.group.getChildren();
        _children.add(node);
        _xblockexpression = (true);
      }
      _xifexpression = _xblockexpression;
    } else {
      _xifexpression = false;
    }
    return _xifexpression;
  }
  
  public Boolean operator_add(final Iterable<? extends XNode> nodes) {
    final Function1<XNode,Boolean> _function = new Function1<XNode,Boolean>() {
        public Boolean apply(final XNode it) {
          boolean _add = AbstractXNodeChooser.this.operator_add(it);
          return Boolean.valueOf(_add);
        }
      };
    Iterable<Boolean> _map = IterableExtensions.map(nodes, _function);
    final Function2<Boolean,Boolean,Boolean> _function_1 = new Function2<Boolean,Boolean,Boolean>() {
        public Boolean apply(final Boolean a, final Boolean b) {
          boolean _or = false;
          if ((a).booleanValue()) {
            _or = true;
          } else {
            _or = ((a).booleanValue() || (b).booleanValue());
          }
          return Boolean.valueOf(_or);
        }
      };
    Boolean _reduce = IterableExtensions.<Boolean>reduce(_map, _function_1);
    return _reduce;
  }
  
  public boolean operator_remove(final XNode node) {
    boolean _xifexpression = false;
    String _key = node.getKey();
    XNode _remove = this.nodeMap.remove(_key);
    boolean _notEquals = (!Objects.equal(_remove, null));
    if (_notEquals) {
      boolean _xblockexpression = false;
      {
        ObservableList<Node> _children = this.group.getChildren();
        _children.add(node);
        this.visibleNodes.remove(node);
        this.calculateVisibleNodes();
        _xblockexpression = (true);
      }
      _xifexpression = _xblockexpression;
    } else {
      _xifexpression = false;
    }
    return _xifexpression;
  }
  
  public Boolean operator_remove(final Iterable<XNode> nodes) {
    final Function1<XNode,Boolean> _function = new Function1<XNode,Boolean>() {
        public Boolean apply(final XNode it) {
          boolean _remove = AbstractXNodeChooser.this.operator_remove(it);
          return Boolean.valueOf(_remove);
        }
      };
    Iterable<Boolean> _map = IterableExtensions.<XNode, Boolean>map(nodes, _function);
    final Function2<Boolean,Boolean,Boolean> _function_1 = new Function2<Boolean,Boolean,Boolean>() {
        public Boolean apply(final Boolean a, final Boolean b) {
          boolean _or = false;
          if ((a).booleanValue()) {
            _or = true;
          } else {
            _or = ((a).booleanValue() || (b).booleanValue());
          }
          return Boolean.valueOf(_or);
        }
      };
    Boolean _reduce = IterableExtensions.<Boolean>reduce(_map, _function_1);
    return _reduce;
  }
  
  public boolean activate() {
    boolean _xblockexpression = false;
    {
      boolean _or = false;
      boolean _isActive = this.getIsActive();
      if (_isActive) {
        _or = true;
      } else {
        ArrayList<XNode> _nodes = this.getNodes();
        boolean _isEmpty = _nodes.isEmpty();
        _or = (_isActive || _isEmpty);
      }
      if (_or) {
        return false;
      }
      this.isActiveProperty.set(true);
      XAbstractDiagram _diagram = this.getDiagram();
      Group _buttonLayer = _diagram.getButtonLayer();
      ObservableList<Node> _children = _buttonLayer.getChildren();
      _children.add(this.group);
      boolean _notEquals = (!Objects.equal(this.minusButton, null));
      if (_notEquals) {
        XAbstractDiagram _diagram_1 = this.getDiagram();
        Group _buttonLayer_1 = _diagram_1.getButtonLayer();
        ObservableList<Node> _children_1 = _buttonLayer_1.getChildren();
        _children_1.add(this.plusButton);
        XAbstractDiagram _diagram_2 = this.getDiagram();
        Group _buttonLayer_2 = _diagram_2.getButtonLayer();
        ObservableList<Node> _children_2 = _buttonLayer_2.getChildren();
        _children_2.add(this.minusButton);
        final ChangeListener<Bounds> _function = new ChangeListener<Bounds>() {
            public void changed(final ObservableValue<? extends Bounds> prop, final Bounds oldVal, final Bounds newVal) {
              AbstractXNodeChooser.this.relocateButtons(AbstractXNodeChooser.this.minusButton, AbstractXNodeChooser.this.plusButton);
            }
          };
        final ChangeListener<Bounds> relocateButtons_0 = _function;
        final ChangeListener<Number> _function_1 = new ChangeListener<Number>() {
            public void changed(final ObservableValue<? extends Number> prop, final Number oldVal, final Number newVal) {
              AbstractXNodeChooser.this.relocateButtons(AbstractXNodeChooser.this.minusButton, AbstractXNodeChooser.this.plusButton);
            }
          };
        final ChangeListener<Number> relocateButtons_1 = _function_1;
        ReadOnlyObjectProperty<Bounds> _layoutBoundsProperty = this.minusButton.layoutBoundsProperty();
        _layoutBoundsProperty.addListener(relocateButtons_0);
        ReadOnlyObjectProperty<Bounds> _layoutBoundsProperty_1 = this.plusButton.layoutBoundsProperty();
        _layoutBoundsProperty_1.addListener(relocateButtons_0);
        ReadOnlyObjectProperty<Bounds> _layoutBoundsProperty_2 = this.group.layoutBoundsProperty();
        _layoutBoundsProperty_2.addListener(relocateButtons_0);
        DoubleProperty _layoutXProperty = this.group.layoutXProperty();
        _layoutXProperty.addListener(relocateButtons_1);
        DoubleProperty _layoutYProperty = this.group.layoutYProperty();
        _layoutYProperty.addListener(relocateButtons_1);
      }
      this.setCurrentPosition(0);
      ArrayList<XNode> _nodes_1 = this.getNodes();
      int _size = _nodes_1.size();
      boolean _equals = (_size == 1);
      if (_equals) {
        ArrayList<XNode> _nodes_2 = this.getNodes();
        XNode _head = IterableExtensions.<XNode>head(_nodes_2);
        this.nodeChosen(_head);
        return false;
      }
      this.setBlurDiagram(true);
      ArrayList<XNode> _nodes_3 = this.getNodes();
      int _size_1 = _nodes_3.size();
      boolean _notEquals_1 = (_size_1 != 0);
      if (_notEquals_1) {
        this.setInterpolatedPosition(0);
      }
      ArrayList<XNode> _nodes_4 = this.getNodes();
      final Procedure1<XNode> _function_2 = new Procedure1<XNode>() {
          public void apply(final XNode node) {
            final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
                public void handle(final MouseEvent it) {
                  int _clickCount = it.getClickCount();
                  final int getClickCount = _clickCount;
                  boolean _matched = false;
                  if (!_matched) {
                    if (Objects.equal(getClickCount,1)) {
                      _matched=true;
                      ArrayList<XNode> _nodes = AbstractXNodeChooser.this.getNodes();
                      List<XNode> _list = IterableExtensions.<XNode>toList(_nodes);
                      int _indexOf = _list.indexOf(node);
                      AbstractXNodeChooser.this.spinToPosition.setTargetPosition(_indexOf);
                    }
                  }
                  if (!_matched) {
                    if (Objects.equal(getClickCount,2)) {
                      _matched=true;
                      XNode _currentNode = AbstractXNodeChooser.this.getCurrentNode();
                      AbstractXNodeChooser.this.nodeChosen(_currentNode);
                      XRootDiagram _rootDiagram = Extensions.getRootDiagram(AbstractXNodeChooser.this.host);
                      _rootDiagram.restoreDefaultTool();
                    }
                  }
                }
              };
            node.setOnMouseClicked(_function);
          }
        };
      IterableExtensions.<XNode>forEach(_nodes_4, _function_2);
      XAbstractDiagram _diagram_3 = this.getDiagram();
      Scene _scene = _diagram_3.getScene();
      _scene.<SwipeEvent>addEventHandler(SwipeEvent.ANY, this.swipeHandler);
      XAbstractDiagram _diagram_4 = this.getDiagram();
      Scene _scene_1 = _diagram_4.getScene();
      _scene_1.<ScrollEvent>addEventHandler(ScrollEvent.ANY, this.scrollHandler);
      XAbstractDiagram _diagram_5 = this.getDiagram();
      Scene _scene_2 = _diagram_5.getScene();
      _scene_2.<KeyEvent>addEventHandler(KeyEvent.KEY_PRESSED, this.keyHandler);
      this.currentPositionProperty.addListener(this.positionListener);
      this.filterStringProperty.addListener(this.filterChangeListener);
      XRootDiagram _rootDiagram = Extensions.getRootDiagram(this.host);
      XRoot _root = _rootDiagram.getRoot();
      ObservableList<Node> _children_3 = _root.getChildren();
      Label _filterLabel = this.getFilterLabel();
      _children_3.add(_filterLabel);
      Label _filterLabel_1 = this.getFilterLabel();
      _filterLabel_1.toFront();
      _xblockexpression = (true);
    }
    return _xblockexpression;
  }
  
  public boolean deactivate() {
    boolean _xblockexpression = false;
    {
      boolean _isActive = this.getIsActive();
      boolean _not = (!_isActive);
      if (_not) {
        return false;
      }
      XRootDiagram _rootDiagram = Extensions.getRootDiagram(this.host);
      XRoot _root = _rootDiagram.getRoot();
      ObservableList<Node> _children = _root.getChildren();
      Label _filterLabel = this.getFilterLabel();
      _children.remove(_filterLabel);
      this.isActiveProperty.set(false);
      XAbstractDiagram _diagram = this.getDiagram();
      Scene _scene = _diagram.getScene();
      _scene.<KeyEvent>removeEventHandler(KeyEvent.KEY_PRESSED, this.keyHandler);
      XAbstractDiagram _diagram_1 = this.getDiagram();
      Scene _scene_1 = _diagram_1.getScene();
      _scene_1.<ScrollEvent>removeEventHandler(ScrollEvent.ANY, this.scrollHandler);
      XAbstractDiagram _diagram_2 = this.getDiagram();
      Scene _scene_2 = _diagram_2.getScene();
      _scene_2.<SwipeEvent>removeEventHandler(SwipeEvent.ANY, this.swipeHandler);
      this.spinToPosition.stop();
      this.setBlurDiagram(false);
      boolean _notEquals = (!Objects.equal(this.minusButton, null));
      if (_notEquals) {
        XAbstractDiagram _diagram_3 = this.getDiagram();
        Group _buttonLayer = _diagram_3.getButtonLayer();
        ObservableList<Node> _children_1 = _buttonLayer.getChildren();
        _children_1.remove(this.minusButton);
        XAbstractDiagram _diagram_4 = this.getDiagram();
        Group _buttonLayer_1 = _diagram_4.getButtonLayer();
        ObservableList<Node> _children_2 = _buttonLayer_1.getChildren();
        _children_2.remove(this.plusButton);
      }
      XAbstractDiagram _diagram_5 = this.getDiagram();
      Group _buttonLayer_2 = _diagram_5.getButtonLayer();
      ObservableList<Node> _children_3 = _buttonLayer_2.getChildren();
      _children_3.remove(this.group);
      _xblockexpression = (true);
    }
    return _xblockexpression;
  }
  
  protected void nodeChosen(final XNode choice) {
    boolean _notEquals = (!Objects.equal(choice, null));
    if (_notEquals) {
      ArrayList<XNode> _nodes = this.getNodes();
      final Procedure1<XNode> _function = new Procedure1<XNode>() {
          public void apply(final XNode it) {
            it.setOnMouseClicked(null);
          }
        };
      IterableExtensions.<XNode>forEach(_nodes, _function);
      choice.setEffect(null);
      Point2D center = Extensions.localToDiagram(this.group, 0, 0);
      ObservableList<Transform> _transforms = choice.getTransforms();
      _transforms.clear();
      ObservableList<Node> _children = this.group.getChildren();
      _children.remove(choice);
      XAbstractDiagram _diagram = this.getDiagram();
      _diagram.addNode(choice);
      choice.layout();
      final Bounds bounds = choice.getLayoutBounds();
      double _x = center.getX();
      double _width = bounds.getWidth();
      double _multiply = (0.5 * _width);
      double _minus = (_x - _multiply);
      choice.setLayoutX(_minus);
      double _y = center.getY();
      double _height = bounds.getHeight();
      double _multiply_1 = (0.5 * _height);
      double _minus_1 = (_y - _multiply_1);
      choice.setLayoutY(_minus_1);
      XConnection _xConnection = new XConnection(this.host, choice);
      final XConnection connection = _xConnection;
      XAbstractDiagram _diagram_1 = this.getDiagram();
      _diagram_1.addConnection(connection);
      choice.toFront();
      connection.toFront();
    }
  }
  
  protected ParallelTransition setBlurDiagram(final boolean isBlur) {
    ParallelTransition _parallelTransition = new ParallelTransition();
    final Procedure1<ParallelTransition> _function = new Procedure1<ParallelTransition>() {
        public void apply(final ParallelTransition it) {
          XRootDiagram _rootDiagram = Extensions.getRootDiagram(AbstractXNodeChooser.this.host);
          Group _nodeLayer = _rootDiagram.getNodeLayer();
          XRootDiagram _rootDiagram_1 = Extensions.getRootDiagram(AbstractXNodeChooser.this.host);
          Group _connectionLayer = _rootDiagram_1.getConnectionLayer();
          for (final Group layer : Collections.<Group>unmodifiableList(Lists.<Group>newArrayList(_nodeLayer, _connectionLayer))) {
            ObservableList<Animation> _children = it.getChildren();
            FadeTransition _fadeTransition = new FadeTransition();
            final Procedure1<FadeTransition> _function = new Procedure1<FadeTransition>() {
                public void apply(final FadeTransition it) {
                  it.setNode(layer);
                  double _xifexpression = (double) 0;
                  if (isBlur) {
                    _xifexpression = 0.3;
                  } else {
                    _xifexpression = 1;
                  }
                  it.setToValue(_xifexpression);
                  Duration _millis = Duration.millis(300);
                  it.setDuration(_millis);
                  it.play();
                }
              };
            FadeTransition _doubleArrow = ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition, _function);
            _children.add(_doubleArrow);
          }
        }
      };
    ParallelTransition _doubleArrow = ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function);
    return _doubleArrow;
  }
  
  protected void cancel() {
    XRootDiagram _rootDiagram = Extensions.getRootDiagram(this.host);
    _rootDiagram.restoreDefaultTool();
  }
  
  protected abstract void setInterpolatedPosition(final double interpolatedPosition);
  
  public double getCurrentPosition() {
    double _xblockexpression = (double) 0;
    {
      double _get = this.currentPositionProperty.get();
      ArrayList<XNode> _nodes = this.getNodes();
      int _size = _nodes.size();
      double result = (_get % _size);
      boolean _lessThan = (result < 0);
      if (_lessThan) {
        ArrayList<XNode> _nodes_1 = this.getNodes();
        int _size_1 = _nodes_1.size();
        double _divide = (result / _size_1);
        int _plus = (((int) _divide) + 1);
        ArrayList<XNode> _nodes_2 = this.getNodes();
        int _size_2 = _nodes_2.size();
        int _multiply = (_plus * _size_2);
        double _plus_1 = (result + _multiply);
        result = _plus_1;
      }
      _xblockexpression = (result);
    }
    return _xblockexpression;
  }
  
  public void setCurrentPosition(final double currentPosition) {
    this.currentPositionProperty.set(currentPosition);
  }
  
  public ArrayList<XNode> getNodes() {
    return this.visibleNodes;
  }
  
  public XNode getCurrentNode() {
    XNode _xblockexpression = null;
    {
      double _currentPosition = this.getCurrentPosition();
      double _plus = (_currentPosition + 0.5);
      int currentPosition = ((int) _plus);
      ArrayList<XNode> _nodes = this.getNodes();
      XNode _get = _nodes.get(currentPosition);
      _xblockexpression = (_get);
    }
    return _xblockexpression;
  }
  
  public XAbstractDiagram getDiagram() {
    XAbstractDiagram _diagram = Extensions.getDiagram(this.host);
    return _diagram;
  }
  
  protected Group getGroup() {
    return this.group;
  }
  
  protected void calculateVisibleNodes() {
    int currentVisibleIndex = 0;
    XNode currentVisibleNode = IterableExtensions.<XNode>head(this.visibleNodes);
    int mapIndex = 0;
    double maxWidth = 0.0;
    double maxHeight = 0.0;
    Set<Entry<String,XNode>> _entrySet = this.nodeMap.entrySet();
    for (final Entry<String,XNode> entry : _entrySet) {
      {
        String _key = entry.getKey();
        String _filterString = this.getFilterString();
        boolean _contains = _key.contains(_filterString);
        if (_contains) {
          XNode _value = entry.getValue();
          boolean _notEquals = (!Objects.equal(currentVisibleNode, _value));
          if (_notEquals) {
            XNode _value_1 = entry.getValue();
            this.visibleNodes.add(currentVisibleIndex, _value_1);
          }
          XNode _value_2 = entry.getValue();
          final Bounds layoutBounds = _value_2.getLayoutBounds();
          double _width = layoutBounds.getWidth();
          double _max = Math.max(maxWidth, _width);
          maxWidth = _max;
          double _height = layoutBounds.getHeight();
          double _max_1 = Math.max(maxHeight, _height);
          maxHeight = _max_1;
          int _plus = (currentVisibleIndex + 1);
          currentVisibleIndex = _plus;
          XNode _xifexpression = null;
          int _size = this.visibleNodes.size();
          boolean _lessThan = (currentVisibleIndex < _size);
          if (_lessThan) {
            XNode _get = this.visibleNodes.get(currentVisibleIndex);
            _xifexpression = _get;
          } else {
            _xifexpression = null;
          }
          currentVisibleNode = _xifexpression;
        } else {
          XNode _value_3 = entry.getValue();
          boolean _equals = Objects.equal(currentVisibleNode, _value_3);
          if (_equals) {
            this.visibleNodes.remove(currentVisibleIndex);
            currentVisibleNode.setVisible(false);
            XNode _xifexpression_1 = null;
            int _size_1 = this.visibleNodes.size();
            boolean _lessThan_1 = (currentVisibleIndex < _size_1);
            if (_lessThan_1) {
              XNode _get_1 = this.visibleNodes.get(currentVisibleIndex);
              _xifexpression_1 = _get_1;
            } else {
              _xifexpression_1 = null;
            }
            currentVisibleNode = _xifexpression_1;
          }
        }
        int _plus_1 = (mapIndex + 1);
        mapIndex = _plus_1;
      }
    }
    double _switchResult = (double) 0;
    HPos _hpos = this.layoutPosition.getHpos();
    final HPos _switchValue = _hpos;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HPos.LEFT)) {
        _matched=true;
        double _layoutX = this.host.getLayoutX();
        double _layoutDistance = this.getLayoutDistance();
        double _minus = (_layoutX - _layoutDistance);
        double _multiply = (0.5 * maxWidth);
        double _minus_1 = (_minus - _multiply);
        _switchResult = _minus_1;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HPos.RIGHT)) {
        _matched=true;
        double _layoutX_1 = this.host.getLayoutX();
        Bounds _layoutBounds = this.host.getLayoutBounds();
        double _width = _layoutBounds.getWidth();
        double _plus = (_layoutX_1 + _width);
        double _layoutDistance_1 = this.getLayoutDistance();
        double _plus_1 = (_plus + _layoutDistance_1);
        double _multiply_1 = (0.5 * maxWidth);
        double _plus_2 = (_plus_1 + _multiply_1);
        _switchResult = _plus_2;
      }
    }
    if (!_matched) {
      double _layoutX_2 = this.host.getLayoutX();
      Bounds _layoutBounds_1 = this.host.getLayoutBounds();
      double _width_1 = _layoutBounds_1.getWidth();
      double _multiply_2 = (0.5 * _width_1);
      double _plus_3 = (_layoutX_2 + _multiply_2);
      _switchResult = _plus_3;
    }
    this.group.setLayoutX(_switchResult);
    double _switchResult_1 = (double) 0;
    VPos _vpos = this.layoutPosition.getVpos();
    final VPos _switchValue_1 = _vpos;
    boolean _matched_1 = false;
    if (!_matched_1) {
      if (Objects.equal(_switchValue_1,VPos.TOP)) {
        _matched_1=true;
        double _layoutY = this.host.getLayoutY();
        double _layoutDistance_2 = this.getLayoutDistance();
        double _minus_2 = (_layoutY - _layoutDistance_2);
        double _multiply_3 = (0.5 * maxHeight);
        double _minus_3 = (_minus_2 - _multiply_3);
        _switchResult_1 = _minus_3;
      }
    }
    if (!_matched_1) {
      if (Objects.equal(_switchValue_1,VPos.BOTTOM)) {
        _matched_1=true;
        double _layoutY_1 = this.host.getLayoutY();
        Bounds _layoutBounds_2 = this.host.getLayoutBounds();
        double _height = _layoutBounds_2.getHeight();
        double _plus_4 = (_layoutY_1 + _height);
        double _layoutDistance_3 = this.getLayoutDistance();
        double _plus_5 = (_plus_4 + _layoutDistance_3);
        double _multiply_4 = (0.5 * maxHeight);
        double _plus_6 = (_plus_5 + _multiply_4);
        _switchResult_1 = _plus_6;
      }
    }
    if (!_matched_1) {
      double _layoutY_2 = this.host.getLayoutY();
      Bounds _layoutBounds_3 = this.host.getLayoutBounds();
      double _height_1 = _layoutBounds_3.getHeight();
      double _multiply_5 = (0.5 * _height_1);
      double _plus_7 = (_layoutY_2 + _multiply_5);
      _switchResult_1 = _plus_7;
    }
    this.group.setLayoutY(_switchResult_1);
    double _currentPosition = this.getCurrentPosition();
    this.setInterpolatedPosition(_currentPosition);
    this.spinToPosition.resetTargetPosition();
  }
  
  public void relocateButtons(final Button minusButton, final Button plusButton) {
  }
  
  private ReadOnlyBooleanWrapper isActiveProperty = new ReadOnlyBooleanWrapper(this, "isActive",_initIsActive());
  
  private static final boolean _initIsActive() {
    return false;
  }
  
  public boolean getIsActive() {
    return this.isActiveProperty.get();
    
  }
  
  public ReadOnlyBooleanProperty isActiveProperty() {
    return this.isActiveProperty.getReadOnlyProperty();
    
  }
  
  private SimpleObjectProperty<Label> filterLabelProperty = new SimpleObjectProperty<Label>(this, "filterLabel");
  
  public Label getFilterLabel() {
    return this.filterLabelProperty.get();
    
  }
  
  public void setFilterLabel(final Label filterLabel) {
    this.filterLabelProperty.set(filterLabel);
    
  }
  
  public ObjectProperty<Label> filterLabelProperty() {
    return this.filterLabelProperty;
    
  }
  
  private SimpleStringProperty filterStringProperty = new SimpleStringProperty(this, "filterString",_initFilterString());
  
  private static final String _initFilterString() {
    return "";
  }
  
  public String getFilterString() {
    return this.filterStringProperty.get();
    
  }
  
  public void setFilterString(final String filterString) {
    this.filterStringProperty.set(filterString);
    
  }
  
  public StringProperty filterStringProperty() {
    return this.filterStringProperty;
    
  }
  
  private SimpleDoubleProperty layoutDistanceProperty = new SimpleDoubleProperty(this, "layoutDistance",_initLayoutDistance());
  
  private static final double _initLayoutDistance() {
    return 40;
  }
  
  public double getLayoutDistance() {
    return this.layoutDistanceProperty.get();
    
  }
  
  public void setLayoutDistance(final double layoutDistance) {
    this.layoutDistanceProperty.set(layoutDistance);
    
  }
  
  public DoubleProperty layoutDistanceProperty() {
    return this.layoutDistanceProperty;
    
  }
}
