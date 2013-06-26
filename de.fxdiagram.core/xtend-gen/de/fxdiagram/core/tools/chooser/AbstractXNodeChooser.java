package de.fxdiagram.core.tools.chooser;

import com.google.common.base.Objects;
import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.XAbstractDiagram;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.core.binding.StringExpressionExtensions;
import de.fxdiagram.core.tools.XDiagramTool;
import de.fxdiagram.core.tools.chooser.XNodeChooserTransition;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.transform.Transform;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public abstract class AbstractXNodeChooser implements XDiagramTool {
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
  
  private boolean isActive = false;
  
  private DoubleProperty _currentPosition = new Function0<DoubleProperty>() {
    public DoubleProperty apply() {
      SimpleDoubleProperty _simpleDoubleProperty = new SimpleDoubleProperty();
      return _simpleDoubleProperty;
    }
  }.apply();
  
  private ChangeListener<Number> positionListener;
  
  protected XNodeChooserTransition spinToPosition;
  
  private EventHandler<SwipeEvent> swipeHandler;
  
  private EventHandler<ScrollEvent> scrollHandler;
  
  private EventHandler<KeyEvent> keyHandler;
  
  private ChangeListener<String> filterChangeListener;
  
  private final ArrayList<XNode> visibleNodes = new Function0<ArrayList<XNode>>() {
    public ArrayList<XNode> apply() {
      ArrayList<XNode> _newArrayList = CollectionLiterals.<XNode>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  private StringProperty _filterString = new Function0<StringProperty>() {
    public StringProperty apply() {
      SimpleStringProperty _simpleStringProperty = new SimpleStringProperty("");
      return _simpleStringProperty;
    }
  }.apply();
  
  private Label filterLabel;
  
  public AbstractXNodeChooser(final XNode host, final Point2D center) {
    this.host = host;
    double _x = center.getX();
    this.group.setLayoutX(_x);
    double _y = center.getY();
    this.group.setLayoutY(_y);
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
              int _minus = (-1);
              AbstractXNodeChooser.this.spinToPosition.setTargetPositionDelta(_minus);
            }
          }
          if (!_matched) {
            if (Objects.equal(getCode,KeyCode.LEFT)) {
              _matched=true;
              int _minus_1 = (-1);
              AbstractXNodeChooser.this.spinToPosition.setTargetPositionDelta(_minus_1);
            }
          }
          if (!_matched) {
            if (Objects.equal(getCode,KeyCode.DOWN)) {
              _matched=true;
              AbstractXNodeChooser.this.spinToPosition.setTargetPositionDelta(1);
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
              final String oldFilter = AbstractXNodeChooser.this._filterString.get();
              boolean _isEmpty = oldFilter.isEmpty();
              boolean _not = (!_isEmpty);
              if (_not) {
                int _length = oldFilter.length();
                int _minus_2 = (_length - 1);
                String _substring = oldFilter.substring(0, _minus_2);
                AbstractXNodeChooser.this._filterString.set(_substring);
              }
            }
          }
          if (!_matched) {
            String _get = AbstractXNodeChooser.this._filterString.get();
            String _text = it.getText();
            String _plus = (_get + _text);
            AbstractXNodeChooser.this._filterString.set(_plus);
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
  
  public Boolean operator_add(final Iterable<XNode> nodes) {
    final Function1<XNode,Boolean> _function = new Function1<XNode,Boolean>() {
        public Boolean apply(final XNode it) {
          boolean _add = AbstractXNodeChooser.this.operator_add(it);
          return Boolean.valueOf(_add);
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
      if (this.isActive) {
        return false;
      }
      ArrayList<XNode> _nodes = this.getNodes();
      boolean _isEmpty = _nodes.isEmpty();
      if (_isEmpty) {
        return false;
      }
      this.isActive = true;
      XAbstractDiagram _diagram = this.getDiagram();
      Group _nodeLayer = _diagram.getNodeLayer();
      ObservableList<Node> _children = _nodeLayer.getChildren();
      _children.add(this.group);
      this._currentPosition.set(0);
      this.setInterpolatedPosition(0);
      ArrayList<XNode> _nodes_1 = this.getNodes();
      int _size = _nodes_1.size();
      boolean _equals = (_size == 1);
      if (_equals) {
        ArrayList<XNode> _nodes_2 = this.getNodes();
        XNode _head = IterableExtensions.<XNode>head(_nodes_2);
        this.nodeChosen(_head);
        return false;
      }
      ArrayList<XNode> _nodes_3 = this.getNodes();
      final Procedure1<XNode> _function = new Procedure1<XNode>() {
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
      IterableExtensions.<XNode>forEach(_nodes_3, _function);
      XAbstractDiagram _diagram_1 = this.getDiagram();
      Scene _scene = _diagram_1.getScene();
      _scene.<SwipeEvent>addEventHandler(SwipeEvent.ANY, this.swipeHandler);
      XAbstractDiagram _diagram_2 = this.getDiagram();
      Scene _scene_1 = _diagram_2.getScene();
      _scene_1.<ScrollEvent>addEventHandler(ScrollEvent.ANY, this.scrollHandler);
      XAbstractDiagram _diagram_3 = this.getDiagram();
      Scene _scene_2 = _diagram_3.getScene();
      _scene_2.<KeyEvent>addEventHandler(KeyEvent.KEY_PRESSED, this.keyHandler);
      this._currentPosition.addListener(this.positionListener);
      this._filterString.addListener(this.filterChangeListener);
      Label _label = new Label();
      final Procedure1<Label> _function_1 = new Procedure1<Label>() {
          public void apply(final Label it) {
            StringProperty _textProperty = it.textProperty();
            StringExpression _plus = StringExpressionExtensions.operator_plus("Filter: ", AbstractXNodeChooser.this._filterString);
            StringExpression _plus_1 = StringExpressionExtensions.operator_plus(_plus, "");
            _textProperty.bind(_plus_1);
          }
        };
      Label _doubleArrow = ObjectExtensions.<Label>operator_doubleArrow(_label, _function_1);
      this.filterLabel = _doubleArrow;
      XRootDiagram _rootDiagram = Extensions.getRootDiagram(this.host);
      Group _buttonLayer = _rootDiagram.getButtonLayer();
      ObservableList<Node> _children_1 = _buttonLayer.getChildren();
      _children_1.add(this.filterLabel);
      _xblockexpression = (true);
    }
    return _xblockexpression;
  }
  
  public boolean deactivate() {
    boolean _xblockexpression = false;
    {
      boolean _not = (!this.isActive);
      if (_not) {
        return false;
      }
      XRootDiagram _rootDiagram = Extensions.getRootDiagram(this.host);
      Group _buttonLayer = _rootDiagram.getButtonLayer();
      ObservableList<Node> _children = _buttonLayer.getChildren();
      _children.remove(this.filterLabel);
      this.isActive = false;
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
      XAbstractDiagram _diagram_3 = this.getDiagram();
      Group _nodeLayer = _diagram_3.getNodeLayer();
      ObservableList<Node> _children_1 = _nodeLayer.getChildren();
      _children_1.remove(this.group);
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
    }
  }
  
  protected void cancel() {
    XRootDiagram _rootDiagram = Extensions.getRootDiagram(this.host);
    _rootDiagram.restoreDefaultTool();
  }
  
  protected abstract void setInterpolatedPosition(final double interpolatedPosition);
  
  public double getCurrentPosition() {
    double _xblockexpression = (double) 0;
    {
      Double _value = this._currentPosition.getValue();
      ArrayList<XNode> _nodes = this.getNodes();
      int _size = _nodes.size();
      double result = ((_value).doubleValue() % _size);
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
  
  public void setCurrentPosition(final double value) {
    this._currentPosition.set(value);
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
  
  public void calculateVisibleNodes() {
    int currentVisibleIndex = 0;
    XNode currentVisibleNode = IterableExtensions.<XNode>head(this.visibleNodes);
    int mapIndex = 0;
    Set<Entry<String,XNode>> _entrySet = this.nodeMap.entrySet();
    for (final Entry<String,XNode> entry : _entrySet) {
      {
        String _key = entry.getKey();
        String _get = this._filterString.get();
        boolean _contains = _key.contains(_get);
        if (_contains) {
          XNode _value = entry.getValue();
          boolean _notEquals = (!Objects.equal(currentVisibleNode, _value));
          if (_notEquals) {
            XNode _value_1 = entry.getValue();
            this.visibleNodes.add(currentVisibleIndex, _value_1);
          }
          int _plus = (currentVisibleIndex + 1);
          currentVisibleIndex = _plus;
          XNode _xifexpression = null;
          int _size = this.visibleNodes.size();
          boolean _lessThan = (currentVisibleIndex < _size);
          if (_lessThan) {
            XNode _get_1 = this.visibleNodes.get(currentVisibleIndex);
            _xifexpression = _get_1;
          } else {
            _xifexpression = null;
          }
          currentVisibleNode = _xifexpression;
        } else {
          XNode _value_2 = entry.getValue();
          boolean _equals = Objects.equal(currentVisibleNode, _value_2);
          if (_equals) {
            this.visibleNodes.remove(currentVisibleIndex);
            currentVisibleNode.setVisible(false);
            XNode _xifexpression_1 = null;
            int _size_1 = this.visibleNodes.size();
            boolean _lessThan_1 = (currentVisibleIndex < _size_1);
            if (_lessThan_1) {
              XNode _get_2 = this.visibleNodes.get(currentVisibleIndex);
              _xifexpression_1 = _get_2;
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
    double _get = this._currentPosition.get();
    this.setInterpolatedPosition(_get);
    this.spinToPosition.resetTargetPosition();
  }
}
