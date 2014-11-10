package de.fxdiagram.core.tools;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.HeadsUpDisplay;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.command.AddRemoveCommand;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.StringExpressionExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.tools.ChooserTransition;
import de.fxdiagram.core.tools.XDiagramTool;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
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
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.scene.transform.Transform;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public abstract class AbstractBaseChooser implements XDiagramTool {
  private DoubleProperty currentPositionProperty = new SimpleDoubleProperty(0.0);
  
  private final ArrayList<XNode> visibleNodes = CollectionLiterals.<XNode>newArrayList();
  
  private Group group = new Group();
  
  private final LinkedHashMap<String, XNode> nodeMap = CollectionLiterals.<String, XNode>newLinkedHashMap();
  
  private final HashMap<XNode, DomainObjectDescriptor> node2choiceInfo = CollectionLiterals.<XNode, DomainObjectDescriptor>newHashMap();
  
  private ChangeListener<Number> positionListener;
  
  protected ChooserTransition spinToPosition;
  
  private EventHandler<SwipeEvent> swipeHandler;
  
  private EventHandler<ScrollEvent> scrollHandler;
  
  private EventHandler<KeyEvent> keyHandler;
  
  private ChangeListener<String> filterChangeListener;
  
  protected Side layoutPosition;
  
  private Node plusButton;
  
  private Node minusButton;
  
  public AbstractBaseChooser(final Side layoutPosition, final boolean hasButtons) {
    this.layoutPosition = layoutPosition;
    final ChangeListener<Number> _function = new ChangeListener<Number>() {
      public void changed(final ObservableValue<? extends Number> element, final Number oldValue, final Number newValue) {
        final double newVal = newValue.doubleValue();
        ArrayList<XNode> _nodes = AbstractBaseChooser.this.getNodes();
        int _size = _nodes.size();
        double _modulo = (newVal % _size);
        AbstractBaseChooser.this.setInterpolatedPosition(_modulo);
      }
    };
    this.positionListener = _function;
    ChooserTransition _chooserTransition = new ChooserTransition(this);
    this.spinToPosition = _chooserTransition;
    final EventHandler<SwipeEvent> _function_1 = new EventHandler<SwipeEvent>() {
      public void handle(final SwipeEvent it) {
        int _switchResult = (int) 0;
        EventType<SwipeEvent> _eventType = it.getEventType();
        boolean _matched = false;
        if (!_matched) {
          if (Objects.equal(_eventType, SwipeEvent.SWIPE_DOWN)) {
            _matched=true;
            _switchResult = (-1);
          }
        }
        if (!_matched) {
          if (Objects.equal(_eventType, SwipeEvent.SWIPE_RIGHT)) {
            _matched=true;
            _switchResult = (-1);
          }
        }
        if (!_matched) {
          _switchResult = 1;
        }
        final int direction = _switchResult;
        AbstractBaseChooser.this.spinToPosition.setTargetPositionDelta((direction * 10));
      }
    };
    this.swipeHandler = _function_1;
    final EventHandler<ScrollEvent> _function_2 = new EventHandler<ScrollEvent>() {
      public void handle(final ScrollEvent it) {
        boolean _isShortcutDown = it.isShortcutDown();
        boolean _not = (!_isShortcutDown);
        if (_not) {
          EventType<ScrollEvent> _eventType = it.getEventType();
          boolean _equals = Objects.equal(_eventType, ScrollEvent.SCROLL_FINISHED);
          if (_equals) {
            double _currentPosition = AbstractBaseChooser.this.getCurrentPosition();
            double _plus = (_currentPosition + 0.5);
            AbstractBaseChooser.this.spinToPosition.setTargetPosition(((int) _plus));
          } else {
            double _currentPosition_1 = AbstractBaseChooser.this.getCurrentPosition();
            double _deltaX = it.getDeltaX();
            double _deltaY = it.getDeltaY();
            double _plus_1 = (_deltaX + _deltaY);
            double _divide = (_plus_1 / 100);
            double _minus = (_currentPosition_1 - _divide);
            AbstractBaseChooser.this.setCurrentPosition(_minus);
          }
        }
      }
    };
    this.scrollHandler = _function_2;
    final EventHandler<KeyEvent> _function_3 = new EventHandler<KeyEvent>() {
      public void handle(final KeyEvent it) {
        KeyCode _code = it.getCode();
        if (_code != null) {
          switch (_code) {
            case CANCEL:
              AbstractBaseChooser.this.cancel();
              break;
            case ESCAPE:
              AbstractBaseChooser.this.cancel();
              break;
            case UP:
              AbstractBaseChooser.this.spinToPosition.setTargetPositionDelta(1);
              break;
            case LEFT:
              AbstractBaseChooser.this.spinToPosition.setTargetPositionDelta((-1));
              break;
            case DOWN:
              AbstractBaseChooser.this.spinToPosition.setTargetPositionDelta((-1));
              break;
            case RIGHT:
              AbstractBaseChooser.this.spinToPosition.setTargetPositionDelta(1);
              break;
            case ENTER:
              XNode _currentNode = AbstractBaseChooser.this.getCurrentNode();
              AbstractBaseChooser.this.nodeChosen(_currentNode);
              XRoot _root = AbstractBaseChooser.this.getRoot();
              _root.restoreDefaultTool();
              break;
            case BACK_SPACE:
              final String oldFilter = AbstractBaseChooser.this.getFilterString();
              boolean _isEmpty = oldFilter.isEmpty();
              boolean _not = (!_isEmpty);
              if (_not) {
                int _length = oldFilter.length();
                int _minus = (_length - 1);
                String _substring = oldFilter.substring(0, _minus);
                AbstractBaseChooser.this.setFilterString(_substring);
              }
              break;
            default:
              String _filterString = AbstractBaseChooser.this.getFilterString();
              String _text = it.getText();
              String _plus = (_filterString + _text);
              AbstractBaseChooser.this.setFilterString(_plus);
              break;
          }
        } else {
          String _filterString = AbstractBaseChooser.this.getFilterString();
          String _text = it.getText();
          String _plus = (_filterString + _text);
          AbstractBaseChooser.this.setFilterString(_plus);
        }
      }
    };
    this.keyHandler = _function_3;
    final ChangeListener<String> _function_4 = new ChangeListener<String>() {
      public void changed(final ObservableValue<? extends String> property, final String oldValue, final String newValue) {
        AbstractBaseChooser.this.calculateVisibleNodes();
      }
    };
    this.filterChangeListener = _function_4;
    if (hasButtons) {
      SVGPath _xifexpression = null;
      boolean _isVertical = layoutPosition.isVertical();
      if (_isVertical) {
        _xifexpression = ButtonExtensions.getArrowButton(Side.BOTTOM, "previous");
      } else {
        _xifexpression = ButtonExtensions.getArrowButton(Side.RIGHT, "previous");
      }
      final Procedure1<SVGPath> _function_5 = new Procedure1<SVGPath>() {
        public void apply(final SVGPath it) {
          final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
            public void handle(final MouseEvent it) {
              AbstractBaseChooser.this.spinToPosition.setTargetPositionDelta((-1));
            }
          };
          it.setOnMouseClicked(_function);
        }
      };
      SVGPath _doubleArrow = ObjectExtensions.<SVGPath>operator_doubleArrow(_xifexpression, _function_5);
      this.minusButton = _doubleArrow;
      SVGPath _xifexpression_1 = null;
      boolean _isVertical_1 = layoutPosition.isVertical();
      if (_isVertical_1) {
        _xifexpression_1 = ButtonExtensions.getArrowButton(Side.TOP, "next");
      } else {
        _xifexpression_1 = ButtonExtensions.getArrowButton(Side.LEFT, "next");
      }
      final Procedure1<SVGPath> _function_6 = new Procedure1<SVGPath>() {
        public void apply(final SVGPath it) {
          final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
            public void handle(final MouseEvent it) {
              AbstractBaseChooser.this.spinToPosition.setTargetPositionDelta(1);
            }
          };
          it.setOnMouseClicked(_function);
        }
      };
      SVGPath _doubleArrow_1 = ObjectExtensions.<SVGPath>operator_doubleArrow(_xifexpression_1, _function_6);
      this.plusButton = _doubleArrow_1;
    }
    Label _label = new Label();
    final Procedure1<Label> _function_7 = new Procedure1<Label>() {
      public void apply(final Label it) {
        StringProperty _textProperty = it.textProperty();
        StringExpression _plus = StringExpressionExtensions.operator_plus("Filter: ", AbstractBaseChooser.this.filterStringProperty);
        StringExpression _plus_1 = StringExpressionExtensions.operator_plus(_plus, "");
        _textProperty.bind(_plus_1);
      }
    };
    Label _doubleArrow_2 = ObjectExtensions.<Label>operator_doubleArrow(_label, _function_7);
    this.setFilterLabel(_doubleArrow_2);
  }
  
  public abstract XRoot getRoot();
  
  public abstract XDiagram getDiagram();
  
  public abstract Point2D getPosition();
  
  public boolean addChoice(final XNode node) {
    DomainObjectDescriptor _domainObject = node.getDomainObject();
    return this.addChoice(node, _domainObject);
  }
  
  public boolean addChoice(final XNode node, final DomainObjectDescriptor choiceInfo) {
    boolean _xifexpression = false;
    String _name = node.getName();
    boolean _containsKey = this.nodeMap.containsKey(_name);
    boolean _not = (!_containsKey);
    if (_not) {
      boolean _xblockexpression = false;
      {
        String _name_1 = node.getName();
        this.nodeMap.put(_name_1, node);
        node.initializeGraphics();
        node.autosize();
        node.layout();
        this.calculateVisibleNodes();
        ObservableList<Node> _children = this.group.getChildren();
        _children.add(node);
        boolean _notEquals = (!Objects.equal(choiceInfo, null));
        if (_notEquals) {
          this.node2choiceInfo.put(node, choiceInfo);
        }
        _xblockexpression = true;
      }
      _xifexpression = _xblockexpression;
    } else {
      _xifexpression = false;
    }
    return _xifexpression;
  }
  
  protected DomainObjectDescriptor getChoiceInfo(final XNode choice) {
    return this.node2choiceInfo.get(choice);
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
        _or = _isEmpty;
      }
      if (_or) {
        return false;
      }
      this.isActiveProperty.set(true);
      XDiagram _diagram = this.getDiagram();
      Group _buttonLayer = _diagram.getButtonLayer();
      ObservableList<Node> _children = _buttonLayer.getChildren();
      _children.add(this.group);
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
      boolean _notEquals = (_size_1 != 0);
      if (_notEquals) {
        this.setInterpolatedPosition(0);
      }
      ArrayList<XNode> _nodes_4 = this.getNodes();
      final Consumer<XNode> _function = new Consumer<XNode>() {
        public void accept(final XNode node) {
          final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
            public void handle(final MouseEvent it) {
              int _clickCount = it.getClickCount();
              switch (_clickCount) {
                case 1:
                  ArrayList<XNode> _nodes = AbstractBaseChooser.this.getNodes();
                  List<XNode> _list = IterableExtensions.<XNode>toList(_nodes);
                  int _indexOf = _list.indexOf(node);
                  AbstractBaseChooser.this.spinToPosition.setTargetPosition(_indexOf);
                  break;
                case 2:
                  XNode _currentNode = AbstractBaseChooser.this.getCurrentNode();
                  AbstractBaseChooser.this.nodeChosen(_currentNode);
                  XRoot _root = AbstractBaseChooser.this.getRoot();
                  _root.restoreDefaultTool();
                  break;
              }
            }
          };
          node.setOnMouseClicked(_function);
        }
      };
      _nodes_4.forEach(_function);
      XDiagram _diagram_1 = this.getDiagram();
      Scene _scene = _diagram_1.getScene();
      _scene.<SwipeEvent>addEventHandler(SwipeEvent.ANY, this.swipeHandler);
      XDiagram _diagram_2 = this.getDiagram();
      Scene _scene_1 = _diagram_2.getScene();
      _scene_1.<ScrollEvent>addEventHandler(ScrollEvent.ANY, this.scrollHandler);
      XDiagram _diagram_3 = this.getDiagram();
      Scene _scene_2 = _diagram_3.getScene();
      _scene_2.<KeyEvent>addEventHandler(KeyEvent.KEY_PRESSED, this.keyHandler);
      this.currentPositionProperty.addListener(this.positionListener);
      this.filterStringProperty.addListener(this.filterChangeListener);
      XRoot _root = this.getRoot();
      HeadsUpDisplay _headsUpDisplay = _root.getHeadsUpDisplay();
      Label _filterLabel = this.getFilterLabel();
      _headsUpDisplay.add(_filterLabel, Pos.BOTTOM_LEFT);
      Label _filterLabel_1 = this.getFilterLabel();
      final Procedure1<Label> _function_1 = new Procedure1<Label>() {
        public void apply(final Label it) {
          XDiagram _diagram = AbstractBaseChooser.this.getDiagram();
          Paint _foregroundPaint = _diagram.getForegroundPaint();
          it.setTextFill(_foregroundPaint);
          it.toFront();
        }
      };
      ObjectExtensions.<Label>operator_doubleArrow(_filterLabel_1, _function_1);
      boolean _notEquals_1 = (!Objects.equal(this.minusButton, null));
      if (_notEquals_1) {
        XDiagram _diagram_4 = this.getDiagram();
        Group _buttonLayer_1 = _diagram_4.getButtonLayer();
        ObservableList<Node> _children_1 = _buttonLayer_1.getChildren();
        _children_1.add(this.plusButton);
        XDiagram _diagram_5 = this.getDiagram();
        Group _buttonLayer_2 = _diagram_5.getButtonLayer();
        ObservableList<Node> _children_2 = _buttonLayer_2.getChildren();
        _children_2.add(this.minusButton);
        final ChangeListener<Bounds> _function_2 = new ChangeListener<Bounds>() {
          public void changed(final ObservableValue<? extends Bounds> prop, final Bounds oldVal, final Bounds newVal) {
            AbstractBaseChooser.this.relocateButtons(AbstractBaseChooser.this.minusButton, AbstractBaseChooser.this.plusButton);
          }
        };
        final ChangeListener<Bounds> relocateButtons_0 = _function_2;
        final ChangeListener<Number> _function_3 = new ChangeListener<Number>() {
          public void changed(final ObservableValue<? extends Number> prop, final Number oldVal, final Number newVal) {
            AbstractBaseChooser.this.relocateButtons(AbstractBaseChooser.this.minusButton, AbstractBaseChooser.this.plusButton);
          }
        };
        final ChangeListener<Number> relocateButtons_1 = _function_3;
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
        this.relocateButtons(this.minusButton, this.plusButton);
      }
      _xblockexpression = true;
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
      XRoot _root = this.getRoot();
      HeadsUpDisplay _headsUpDisplay = _root.getHeadsUpDisplay();
      ObservableList<Node> _children = _headsUpDisplay.getChildren();
      Label _filterLabel = this.getFilterLabel();
      _children.remove(_filterLabel);
      this.isActiveProperty.set(false);
      XDiagram _diagram = this.getDiagram();
      Scene _scene = _diagram.getScene();
      _scene.<KeyEvent>removeEventHandler(KeyEvent.KEY_PRESSED, this.keyHandler);
      XDiagram _diagram_1 = this.getDiagram();
      Scene _scene_1 = _diagram_1.getScene();
      _scene_1.<ScrollEvent>removeEventHandler(ScrollEvent.ANY, this.scrollHandler);
      XDiagram _diagram_2 = this.getDiagram();
      Scene _scene_2 = _diagram_2.getScene();
      _scene_2.<SwipeEvent>removeEventHandler(SwipeEvent.ANY, this.swipeHandler);
      this.spinToPosition.stop();
      this.setBlurDiagram(false);
      boolean _notEquals = (!Objects.equal(this.minusButton, null));
      if (_notEquals) {
        XDiagram _diagram_3 = this.getDiagram();
        Group _buttonLayer = _diagram_3.getButtonLayer();
        ObservableList<Node> _children_1 = _buttonLayer.getChildren();
        _children_1.remove(this.minusButton);
        XDiagram _diagram_4 = this.getDiagram();
        Group _buttonLayer_1 = _diagram_4.getButtonLayer();
        ObservableList<Node> _children_2 = _buttonLayer_1.getChildren();
        _children_2.remove(this.plusButton);
      }
      XDiagram _diagram_5 = this.getDiagram();
      Group _buttonLayer_2 = _diagram_5.getButtonLayer();
      ObservableList<Node> _children_3 = _buttonLayer_2.getChildren();
      _children_3.remove(this.group);
      _xblockexpression = true;
    }
    return _xblockexpression;
  }
  
  protected void nodeChosen(final XNode choice) {
    boolean _notEquals = (!Objects.equal(choice, null));
    if (_notEquals) {
      ArrayList<XNode> _nodes = this.getNodes();
      final Consumer<XNode> _function = new Consumer<XNode>() {
        public void accept(final XNode it) {
          it.setOnMouseClicked(null);
        }
      };
      _nodes.forEach(_function);
      ObservableList<Node> _children = this.group.getChildren();
      _children.remove(choice);
      final ArrayList<XShape> shapesToAdd = CollectionLiterals.<XShape>newArrayList();
      XDiagram _diagram = this.getDiagram();
      ObservableList<XNode> _nodes_1 = _diagram.getNodes();
      final Function1<XNode, Boolean> _function_1 = new Function1<XNode, Boolean>() {
        public Boolean apply(final XNode it) {
          DomainObjectDescriptor _domainObject = it.getDomainObject();
          DomainObjectDescriptor _domainObject_1 = choice.getDomainObject();
          return Boolean.valueOf(Objects.equal(_domainObject, _domainObject_1));
        }
      };
      XNode existingChoice = IterableExtensions.<XNode>findFirst(_nodes_1, _function_1);
      boolean _equals = Objects.equal(existingChoice, null);
      if (_equals) {
        existingChoice = choice;
        final Bounds unlayoutedBounds = choice.getLayoutBounds();
        choice.setEffect(null);
        Point2D center = CoreExtensions.localToDiagram(this.group, 0, 0);
        ObservableList<Transform> _transforms = choice.getTransforms();
        _transforms.clear();
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
        final Side layoutPosition = this.layoutPosition;
        if (layoutPosition != null) {
          switch (layoutPosition) {
            case LEFT:
              double _layoutX = choice.getLayoutX();
              double _width_1 = bounds.getWidth();
              double _width_2 = unlayoutedBounds.getWidth();
              double _minus_2 = (_width_1 - _width_2);
              double _multiply_2 = (0.5 * _minus_2);
              double _minus_3 = (_layoutX - _multiply_2);
              choice.setLayoutX(_minus_3);
              break;
            case RIGHT:
              double _layoutX_1 = choice.getLayoutX();
              double _width_3 = bounds.getWidth();
              double _width_4 = unlayoutedBounds.getWidth();
              double _minus_4 = (_width_3 - _width_4);
              double _multiply_3 = (0.5 * _minus_4);
              double _plus = (_layoutX_1 + _multiply_3);
              choice.setLayoutX(_plus);
              break;
            case TOP:
              double _layoutY = choice.getLayoutY();
              double _height_1 = bounds.getHeight();
              double _height_2 = unlayoutedBounds.getHeight();
              double _minus_5 = (_height_1 - _height_2);
              double _multiply_4 = (0.5 * _minus_5);
              double _minus_6 = (_layoutY - _multiply_4);
              choice.setLayoutY(_minus_6);
              break;
            case BOTTOM:
              double _layoutY_1 = choice.getLayoutY();
              double _height_3 = bounds.getHeight();
              double _height_4 = unlayoutedBounds.getHeight();
              double _minus_7 = (_height_3 - _height_4);
              double _multiply_5 = (0.5 * _minus_7);
              double _plus_1 = (_layoutY_1 + _multiply_5);
              choice.setLayoutY(_plus_1);
              break;
            default:
              break;
          }
        }
        choice.setPlacementHint(this.layoutPosition);
        shapesToAdd.add(choice);
      }
      Iterable<? extends XShape> _additionalShapesToAdd = this.getAdditionalShapesToAdd(existingChoice);
      Iterables.<XShape>addAll(shapesToAdd, _additionalShapesToAdd);
      XRoot _root = this.getRoot();
      CommandStack _commandStack = _root.getCommandStack();
      XDiagram _diagram_1 = this.getDiagram();
      AddRemoveCommand _newAddCommand = AddRemoveCommand.newAddCommand(_diagram_1, ((XShape[])Conversions.unwrapArray(shapesToAdd, XShape.class)));
      _commandStack.execute(_newAddCommand);
    }
  }
  
  protected Iterable<? extends XShape> getAdditionalShapesToAdd(final XNode choice) {
    return Collections.<XShape>unmodifiableList(CollectionLiterals.<XShape>newArrayList());
  }
  
  protected ParallelTransition setBlurDiagram(final boolean isBlur) {
    ParallelTransition _parallelTransition = new ParallelTransition();
    final Procedure1<ParallelTransition> _function = new Procedure1<ParallelTransition>() {
      public void apply(final ParallelTransition it) {
        XRoot _root = AbstractBaseChooser.this.getRoot();
        XDiagram _diagram = _root.getDiagram();
        Group _nodeLayer = _diagram.getNodeLayer();
        XRoot _root_1 = AbstractBaseChooser.this.getRoot();
        XDiagram _diagram_1 = _root_1.getDiagram();
        Group _connectionLayer = _diagram_1.getConnectionLayer();
        for (final Group layer : Collections.<Group>unmodifiableList(CollectionLiterals.<Group>newArrayList(_nodeLayer, _connectionLayer))) {
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
    return ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function);
  }
  
  protected void cancel() {
    XRoot _root = this.getRoot();
    _root.restoreDefaultTool();
  }
  
  protected void setInterpolatedPosition(final double interpolatedPosition) {
    this.doSetInterpolatedPosition(interpolatedPosition);
  }
  
  protected abstract void doSetInterpolatedPosition(final double interpolatedPosition);
  
  public double getCurrentPosition() {
    double _xblockexpression = (double) 0;
    {
      double _get = this.currentPositionProperty.get();
      ArrayList<XNode> _nodes = this.getNodes();
      int _size = _nodes.size();
      double result = (_get % _size);
      if ((result < 0)) {
        ArrayList<XNode> _nodes_1 = this.getNodes();
        int _size_1 = _nodes_1.size();
        int _multiply = ((((int) (result / this.getNodes().size())) + 1) * _size_1);
        double _plus = (result + _multiply);
        result = _plus;
      }
      _xblockexpression = result;
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
      _xblockexpression = _nodes.get(currentPosition);
    }
    return _xblockexpression;
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
    Set<Map.Entry<String, XNode>> _entrySet = this.nodeMap.entrySet();
    for (final Map.Entry<String, XNode> entry : _entrySet) {
      {
        XNode _value = entry.getValue();
        boolean _matchesFilter = this.matchesFilter(_value);
        if (_matchesFilter) {
          XNode _value_1 = entry.getValue();
          boolean _notEquals = (!Objects.equal(currentVisibleNode, _value_1));
          if (_notEquals) {
            XNode _value_2 = entry.getValue();
            this.visibleNodes.add(currentVisibleIndex, _value_2);
          }
          XNode _value_3 = entry.getValue();
          final Bounds layoutBounds = _value_3.getLayoutBounds();
          double _width = layoutBounds.getWidth();
          double _max = Math.max(maxWidth, _width);
          maxWidth = _max;
          double _height = layoutBounds.getHeight();
          double _max_1 = Math.max(maxHeight, _height);
          maxHeight = _max_1;
          currentVisibleIndex = (currentVisibleIndex + 1);
          XNode _xifexpression = null;
          int _size = this.visibleNodes.size();
          boolean _lessThan = (currentVisibleIndex < _size);
          if (_lessThan) {
            _xifexpression = this.visibleNodes.get(currentVisibleIndex);
          } else {
            _xifexpression = null;
          }
          currentVisibleNode = _xifexpression;
        } else {
          XNode _value_4 = entry.getValue();
          boolean _equals = Objects.equal(currentVisibleNode, _value_4);
          if (_equals) {
            this.visibleNodes.remove(currentVisibleIndex);
            currentVisibleNode.setVisible(false);
            XNode _xifexpression_1 = null;
            int _size_1 = this.visibleNodes.size();
            boolean _lessThan_1 = (currentVisibleIndex < _size_1);
            if (_lessThan_1) {
              _xifexpression_1 = this.visibleNodes.get(currentVisibleIndex);
            } else {
              _xifexpression_1 = null;
            }
            currentVisibleNode = _xifexpression_1;
          }
        }
        mapIndex = (mapIndex + 1);
      }
    }
    this.resizeGroup(this.group, maxWidth, maxHeight);
    double _currentPosition = this.getCurrentPosition();
    this.setInterpolatedPosition(_currentPosition);
    this.spinToPosition.resetTargetPosition();
  }
  
  protected void resizeGroup(final Group group, final double maxWidth, final double maxHeight) {
    double _switchResult = (double) 0;
    final Side layoutPosition = this.layoutPosition;
    if (layoutPosition != null) {
      switch (layoutPosition) {
        case LEFT:
          Point2D _position = this.getPosition();
          double _x = _position.getX();
          _switchResult = (_x - maxWidth);
          break;
        case RIGHT:
          Point2D _position_1 = this.getPosition();
          _switchResult = _position_1.getX();
          break;
        default:
          Point2D _position_2 = this.getPosition();
          double _x_1 = _position_2.getX();
          _switchResult = (_x_1 + (0.5 * maxWidth));
          break;
      }
    } else {
      Point2D _position_2 = this.getPosition();
      double _x_1 = _position_2.getX();
      _switchResult = (_x_1 + (0.5 * maxWidth));
    }
    group.setLayoutX(_switchResult);
    double _switchResult_1 = (double) 0;
    final Side layoutPosition_1 = this.layoutPosition;
    if (layoutPosition_1 != null) {
      switch (layoutPosition_1) {
        case TOP:
          Point2D _position_3 = this.getPosition();
          double _y = _position_3.getY();
          _switchResult_1 = (_y - maxHeight);
          break;
        case BOTTOM:
          Point2D _position_4 = this.getPosition();
          _switchResult_1 = _position_4.getY();
          break;
        default:
          Point2D _position_5 = this.getPosition();
          double _y_1 = _position_5.getY();
          _switchResult_1 = (_y_1 + (0.5 * maxWidth));
          break;
      }
    } else {
      Point2D _position_5 = this.getPosition();
      double _y_1 = _position_5.getY();
      _switchResult_1 = (_y_1 + (0.5 * maxWidth));
    }
    group.setLayoutY(_switchResult_1);
  }
  
  protected boolean matchesFilter(final XNode node) {
    boolean _xifexpression = false;
    String _filterString = this.getFilterString();
    String _lowerCase = _filterString.toLowerCase();
    String _filterString_1 = this.getFilterString();
    boolean _equals = Objects.equal(_lowerCase, _filterString_1);
    if (_equals) {
      String _name = node.getName();
      String _lowerCase_1 = _name.toLowerCase();
      String _filterString_2 = this.getFilterString();
      _xifexpression = _lowerCase_1.contains(_filterString_2);
    } else {
      String _name_1 = node.getName();
      String _filterString_3 = this.getFilterString();
      _xifexpression = _name_1.contains(_filterString_3);
    }
    return _xifexpression;
  }
  
  public void relocateButtons(final Node minusButton, final Node plusButton) {
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
}
