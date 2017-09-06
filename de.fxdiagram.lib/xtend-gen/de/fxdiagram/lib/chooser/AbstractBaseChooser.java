package de.fxdiagram.lib.chooser;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.command.AddRemoveCommand;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.StringExpressionExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.tools.XDiagramTool;
import de.fxdiagram.core.tools.actions.RevealAction;
import de.fxdiagram.lib.chooser.ChoiceGraphics;
import de.fxdiagram.lib.chooser.ChooserTransition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * Interactive {@link XDiagramTool} to add new nodes and edges the diagram in a user
 * friendly way.
 * 
 * A {@link ChoiceGraphics} describes the visual effect and behavior to show and select
 * the candidates.
 * 
 * Currently there are two concrete subclasses: {@link NodeChooser} to add unrelated
 * nodes, e.g. to initially populate the diagram, and {@link ConnectedNodeChooser} to
 * add nodes connected to a given host node, e.g. for exploration diagrams. To use them
 * you usually
 * <ol>
 * <li>Create an instance with the appropriate {@link ChoiceGraphics},</li>
 * <li>Add choices using {@link #addChoice},</li>
 * <li>Make it the current tool by calling {@link XRoot#setCurrentTool}.</li>
 * </ol>
 */
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
  
  private EventHandler<KeyEvent> keyTypedHandler;
  
  private ChangeListener<String> filterChangeListener;
  
  private final ChoiceGraphics graphics;
  
  private Node plusButton;
  
  private Node minusButton;
  
  public AbstractBaseChooser(final ChoiceGraphics graphics, final boolean isVertical) {
    this.graphics = graphics;
    graphics.setChooser(this);
    final ChangeListener<Number> _function = (ObservableValue<? extends Number> element, Number oldValue, Number newValue) -> {
      final double newVal = newValue.doubleValue();
      int _size = this.getNodes().size();
      double _modulo = (newVal % _size);
      this.setInterpolatedPosition(_modulo);
    };
    this.positionListener = _function;
    ChooserTransition _chooserTransition = new ChooserTransition(this);
    this.spinToPosition = _chooserTransition;
    final EventHandler<SwipeEvent> _function_1 = (SwipeEvent it) -> {
      int _switchResult = (int) 0;
      EventType<SwipeEvent> _eventType = it.getEventType();
      boolean _matched = false;
      if (Objects.equal(_eventType, SwipeEvent.SWIPE_DOWN)) {
        _matched=true;
        _switchResult = (-1);
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
      this.spinToPosition.setTargetPositionDelta((direction * 10));
    };
    this.swipeHandler = _function_1;
    final EventHandler<ScrollEvent> _function_2 = (ScrollEvent it) -> {
      boolean _isShortcutDown = it.isShortcutDown();
      boolean _not = (!_isShortcutDown);
      if (_not) {
        EventType<ScrollEvent> _eventType = it.getEventType();
        boolean _equals = Objects.equal(_eventType, ScrollEvent.SCROLL_FINISHED);
        if (_equals) {
          double _currentPosition = this.getCurrentPosition();
          double _plus = (_currentPosition + 0.5);
          this.spinToPosition.setTargetPosition(((int) _plus));
        } else {
          double _currentPosition_1 = this.getCurrentPosition();
          double _deltaX = it.getDeltaX();
          double _deltaY = it.getDeltaY();
          double _plus_1 = (_deltaX + _deltaY);
          double _divide = (_plus_1 / 100);
          double _minus = (_currentPosition_1 - _divide);
          this.setCurrentPosition(_minus);
        }
      }
    };
    this.scrollHandler = _function_2;
    final EventHandler<KeyEvent> _function_3 = (KeyEvent it) -> {
      KeyCode _code = it.getCode();
      if (_code != null) {
        switch (_code) {
          case CANCEL:
            this.cancel();
            break;
          case ESCAPE:
            this.cancel();
            break;
          case UP:
            this.spinToPosition.setTargetPositionDelta(1);
            break;
          case LEFT:
            this.spinToPosition.setTargetPositionDelta((-1));
            break;
          case DOWN:
            this.spinToPosition.setTargetPositionDelta((-1));
            break;
          case RIGHT:
            this.spinToPosition.setTargetPositionDelta(1);
            break;
          case ENTER:
            this.nodeChosen(this.getCurrentNode());
            this.getRoot().restoreDefaultTool();
            break;
          case BACK_SPACE:
            final String oldFilter = this.getFilterString();
            boolean _isEmpty = oldFilter.isEmpty();
            boolean _not = (!_isEmpty);
            if (_not) {
              int _length = oldFilter.length();
              int _minus = (_length - 1);
              this.setFilterString(oldFilter.substring(0, _minus));
            }
            break;
          default:
            break;
        }
      } else {
      }
    };
    this.keyHandler = _function_3;
    final EventHandler<KeyEvent> _function_4 = (KeyEvent it) -> {
      String _filterString = this.getFilterString();
      final Function1<Character, Boolean> _function_5 = (Character it_1) -> {
        return Boolean.valueOf(((it_1).charValue() > 31));
      };
      Iterable<Character> _filter = IterableExtensions.<Character>filter(((Iterable<Character>)Conversions.doWrapArray(it.getCharacter().toCharArray())), _function_5);
      String _string = new String(((char[])Conversions.unwrapArray(_filter, char.class)));
      String _plus = (_filterString + _string);
      this.setFilterString(_plus);
    };
    this.keyTypedHandler = _function_4;
    final ChangeListener<String> _function_5 = (ObservableValue<? extends String> property, String oldValue, String newValue) -> {
      this.calculateVisibleNodes();
    };
    this.filterChangeListener = _function_5;
    boolean _hasButtons = graphics.hasButtons();
    if (_hasButtons) {
      SVGPath _xifexpression = null;
      if (isVertical) {
        _xifexpression = ButtonExtensions.getArrowButton(Side.BOTTOM, "previous");
      } else {
        _xifexpression = ButtonExtensions.getArrowButton(Side.RIGHT, "previous");
      }
      final Procedure1<SVGPath> _function_6 = (SVGPath it) -> {
        final EventHandler<MouseEvent> _function_7 = (MouseEvent it_1) -> {
          this.spinToPosition.setTargetPositionDelta((-1));
        };
        it.setOnMouseClicked(_function_7);
      };
      SVGPath _doubleArrow = ObjectExtensions.<SVGPath>operator_doubleArrow(_xifexpression, _function_6);
      this.minusButton = _doubleArrow;
      SVGPath _xifexpression_1 = null;
      if (isVertical) {
        _xifexpression_1 = ButtonExtensions.getArrowButton(Side.TOP, "next");
      } else {
        _xifexpression_1 = ButtonExtensions.getArrowButton(Side.LEFT, "next");
      }
      final Procedure1<SVGPath> _function_7 = (SVGPath it) -> {
        final EventHandler<MouseEvent> _function_8 = (MouseEvent it_1) -> {
          this.spinToPosition.setTargetPositionDelta(1);
        };
        it.setOnMouseClicked(_function_8);
      };
      SVGPath _doubleArrow_1 = ObjectExtensions.<SVGPath>operator_doubleArrow(_xifexpression_1, _function_7);
      this.plusButton = _doubleArrow_1;
    }
    Label _label = new Label();
    final Procedure1<Label> _function_8 = (Label it) -> {
      StringProperty _textProperty = it.textProperty();
      StringExpression _plus = StringExpressionExtensions.operator_plus("Filter: ", this.filterStringProperty);
      StringExpression _plus_1 = StringExpressionExtensions.operator_plus(_plus, "");
      _textProperty.bind(_plus_1);
    };
    Label _doubleArrow_2 = ObjectExtensions.<Label>operator_doubleArrow(_label, _function_8);
    this.setFilterLabel(_doubleArrow_2);
  }
  
  public abstract XRoot getRoot();
  
  public abstract XDiagram getDiagram();
  
  public abstract Point2D getPosition();
  
  public boolean addChoice(final XNode node) {
    return this.addChoice(node, node.getDomainObjectDescriptor());
  }
  
  public boolean addChoice(final XNode node, final DomainObjectDescriptor choiceInfo) {
    boolean _xifexpression = false;
    boolean _containsKey = this.nodeMap.containsKey(node.getName());
    boolean _not = (!_containsKey);
    if (_not) {
      boolean _xblockexpression = false;
      {
        this.nodeMap.put(node.getName(), node);
        node.initializeGraphics();
        node.autosize();
        node.layout();
        this.calculateVisibleNodes();
        ObservableList<Node> _children = this.group.getChildren();
        _children.add(node);
        if ((choiceInfo != null)) {
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
  
  @Override
  public boolean activate() {
    boolean _xblockexpression = false;
    {
      if ((this.getIsActive() || this.getNodes().isEmpty())) {
        return false;
      }
      this.isActiveProperty.set(true);
      ObservableList<Node> _children = this.getDiagram().getButtonLayer().getChildren();
      _children.add(this.group);
      this.setCurrentPosition(0);
      int _size = this.getNodes().size();
      boolean _equals = (_size == 1);
      if (_equals) {
        this.nodeChosen(IterableExtensions.<XNode>head(this.getNodes()));
        return false;
      }
      this.setBlurDiagram(true);
      int _size_1 = this.getNodes().size();
      boolean _notEquals = (_size_1 != 0);
      if (_notEquals) {
        this.setInterpolatedPosition(0);
      }
      final Consumer<XNode> _function = (XNode node) -> {
        final EventHandler<MouseEvent> _function_1 = (MouseEvent it) -> {
          int _clickCount = it.getClickCount();
          switch (_clickCount) {
            case 1:
              this.spinToPosition.setTargetPosition(IterableExtensions.<XNode>toList(this.getNodes()).indexOf(node));
              break;
            case 2:
              this.nodeChosen(this.getCurrentNode());
              this.getRoot().restoreDefaultTool();
              it.consume();
              break;
          }
        };
        node.setOnMouseClicked(_function_1);
      };
      this.getNodes().forEach(_function);
      this.getDiagram().getScene().<SwipeEvent>addEventHandler(SwipeEvent.ANY, this.swipeHandler);
      this.getDiagram().getScene().<ScrollEvent>addEventHandler(ScrollEvent.ANY, this.scrollHandler);
      this.getDiagram().getScene().<KeyEvent>addEventHandler(KeyEvent.KEY_PRESSED, this.keyHandler);
      this.getDiagram().getScene().<KeyEvent>addEventHandler(KeyEvent.KEY_TYPED, this.keyTypedHandler);
      this.currentPositionProperty.addListener(this.positionListener);
      this.filterStringProperty.addListener(this.filterChangeListener);
      this.getRoot().getHeadsUpDisplay().add(this.getFilterLabel(), Pos.BOTTOM_LEFT);
      Label _filterLabel = this.getFilterLabel();
      final Procedure1<Label> _function_1 = (Label it) -> {
        it.setTextFill(this.getDiagram().getForegroundPaint());
        it.toFront();
      };
      ObjectExtensions.<Label>operator_doubleArrow(_filterLabel, _function_1);
      if ((this.minusButton != null)) {
        ObservableList<Node> _children_1 = this.getDiagram().getButtonLayer().getChildren();
        _children_1.add(this.plusButton);
        ObservableList<Node> _children_2 = this.getDiagram().getButtonLayer().getChildren();
        _children_2.add(this.minusButton);
        final ChangeListener<Bounds> _function_2 = (ObservableValue<? extends Bounds> prop, Bounds oldVal, Bounds newVal) -> {
          this.graphics.relocateButtons(this.minusButton, this.plusButton);
        };
        final ChangeListener<Bounds> relocateButtons_0 = _function_2;
        final ChangeListener<Number> _function_3 = (ObservableValue<? extends Number> prop, Number oldVal, Number newVal) -> {
          this.graphics.relocateButtons(this.minusButton, this.plusButton);
        };
        final ChangeListener<Number> relocateButtons_1 = _function_3;
        this.minusButton.layoutBoundsProperty().addListener(relocateButtons_0);
        this.plusButton.layoutBoundsProperty().addListener(relocateButtons_0);
        this.group.layoutBoundsProperty().addListener(relocateButtons_0);
        this.group.layoutXProperty().addListener(relocateButtons_1);
        this.group.layoutYProperty().addListener(relocateButtons_1);
        this.graphics.relocateButtons(this.minusButton, this.plusButton);
      }
      ArrayList<XNode> _nodes = this.getNodes();
      new RevealAction(_nodes).perform(this.getRoot());
      _xblockexpression = true;
    }
    return _xblockexpression;
  }
  
  @Override
  public boolean deactivate() {
    boolean _xblockexpression = false;
    {
      boolean _isActive = this.getIsActive();
      boolean _not = (!_isActive);
      if (_not) {
        return false;
      }
      ObservableList<Node> _children = this.getRoot().getHeadsUpDisplay().getChildren();
      Label _filterLabel = this.getFilterLabel();
      _children.remove(_filterLabel);
      this.isActiveProperty.set(false);
      this.getDiagram().getScene().<KeyEvent>removeEventHandler(KeyEvent.KEY_TYPED, this.keyTypedHandler);
      this.getDiagram().getScene().<KeyEvent>removeEventHandler(KeyEvent.KEY_PRESSED, this.keyHandler);
      this.getDiagram().getScene().<ScrollEvent>removeEventHandler(ScrollEvent.ANY, this.scrollHandler);
      this.getDiagram().getScene().<SwipeEvent>removeEventHandler(SwipeEvent.ANY, this.swipeHandler);
      this.spinToPosition.stop();
      this.setBlurDiagram(false);
      if ((this.minusButton != null)) {
        ObservableList<Node> _children_1 = this.getDiagram().getButtonLayer().getChildren();
        _children_1.remove(this.minusButton);
        ObservableList<Node> _children_2 = this.getDiagram().getButtonLayer().getChildren();
        _children_2.remove(this.plusButton);
      }
      ObservableList<Node> _children_3 = this.getDiagram().getButtonLayer().getChildren();
      _children_3.remove(this.group);
      _xblockexpression = true;
    }
    return _xblockexpression;
  }
  
  protected void nodeChosen(final XNode choice) {
    if ((choice != null)) {
      final Consumer<XNode> _function = (XNode it) -> {
        it.setOnMouseClicked(null);
      };
      this.getNodes().forEach(_function);
      this.group.getChildren().remove(choice);
      final ArrayList<XShape> shapesToAdd = CollectionLiterals.<XShape>newArrayList();
      final Function1<XNode, Boolean> _function_1 = (XNode it) -> {
        DomainObjectDescriptor _domainObjectDescriptor = it.getDomainObjectDescriptor();
        DomainObjectDescriptor _domainObjectDescriptor_1 = choice.getDomainObjectDescriptor();
        return Boolean.valueOf(Objects.equal(_domainObjectDescriptor, _domainObjectDescriptor_1));
      };
      XNode existingChoice = IterableExtensions.<XNode>findFirst(this.getDiagram().getNodes(), _function_1);
      if ((existingChoice == null)) {
        existingChoice = choice;
        final Bounds unlayoutedBounds = choice.getLayoutBounds();
        choice.setEffect(null);
        Point2D center = CoreExtensions.localToDiagram(this.group, 0, 0);
        choice.getTransforms().clear();
        choice.autosize();
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
        this.graphics.nodeChosen(choice);
        double _width_1 = bounds.getWidth();
        double _width_2 = unlayoutedBounds.getWidth();
        double _minus_2 = (_width_1 - _width_2);
        double _height_1 = bounds.getHeight();
        double _height_2 = unlayoutedBounds.getHeight();
        double _minus_3 = (_height_1 - _height_2);
        this.adjustNewNode(choice, _minus_2, _minus_3);
        shapesToAdd.add(choice);
      }
      Iterable<? extends XShape> _additionalShapesToAdd = this.getAdditionalShapesToAdd(existingChoice, this.node2choiceInfo.get(choice));
      Iterables.<XShape>addAll(shapesToAdd, _additionalShapesToAdd);
      this.getRoot().getCommandStack().execute(AddRemoveCommand.newAddCommand(this.getDiagram(), ((XShape[])Conversions.unwrapArray(shapesToAdd, XShape.class))));
    }
  }
  
  protected void adjustNewNode(final XNode choice, final double widthDelta, final double heightDelta) {
    double _layoutX = choice.getLayoutX();
    double _minus = (_layoutX - (0.5 * widthDelta));
    choice.setLayoutX(_minus);
    double _layoutY = choice.getLayoutY();
    double _minus_1 = (_layoutY - (0.5 * heightDelta));
    choice.setLayoutY(_minus_1);
  }
  
  protected Iterable<? extends XShape> getAdditionalShapesToAdd(final XNode choice, final DomainObjectDescriptor choiceInfo) {
    return Collections.<XShape>unmodifiableList(CollectionLiterals.<XShape>newArrayList());
  }
  
  protected ParallelTransition setBlurDiagram(final boolean isBlur) {
    ParallelTransition _parallelTransition = new ParallelTransition();
    final Procedure1<ParallelTransition> _function = (ParallelTransition it) -> {
      Group _nodeLayer = this.getDiagram().getNodeLayer();
      Group _connectionLayer = this.getDiagram().getConnectionLayer();
      for (final Group layer : Collections.<Group>unmodifiableList(CollectionLiterals.<Group>newArrayList(_nodeLayer, _connectionLayer))) {
        ObservableList<Animation> _children = it.getChildren();
        FadeTransition _fadeTransition = new FadeTransition();
        final Procedure1<FadeTransition> _function_1 = (FadeTransition it_1) -> {
          it_1.setNode(layer);
          double _xifexpression = (double) 0;
          if (isBlur) {
            _xifexpression = 0.3;
          } else {
            _xifexpression = 1;
          }
          it_1.setToValue(_xifexpression);
          it_1.setDuration(Duration.millis(300));
          it_1.play();
        };
        FadeTransition _doubleArrow = ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition, _function_1);
        _children.add(_doubleArrow);
      }
    };
    return ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function);
  }
  
  protected void cancel() {
    this.getRoot().restoreDefaultTool();
  }
  
  protected void setInterpolatedPosition(final double interpolatedPosition) {
    this.graphics.setInterpolatedPosition(interpolatedPosition);
  }
  
  public double getCurrentPosition() {
    double _xblockexpression = (double) 0;
    {
      double _get = this.currentPositionProperty.get();
      int _size = this.getNodes().size();
      double result = (_get % _size);
      if ((result < 0)) {
        int _size_1 = this.getNodes().size();
        double _divide = (result / _size_1);
        int _plus = (((int) _divide) + 1);
        int _size_2 = this.getNodes().size();
        int _multiply = (_plus * _size_2);
        double _plus_1 = (result + _multiply);
        result = _plus_1;
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
      _xblockexpression = this.getNodes().get(currentPosition);
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
        boolean _matchesFilter = this.matchesFilter(entry.getValue());
        if (_matchesFilter) {
          XNode _value = entry.getValue();
          boolean _notEquals = (!Objects.equal(currentVisibleNode, _value));
          if (_notEquals) {
            this.visibleNodes.add(currentVisibleIndex, entry.getValue());
          }
          final Bounds layoutBounds = entry.getValue().getLayoutBounds();
          maxWidth = Math.max(maxWidth, layoutBounds.getWidth());
          maxHeight = Math.max(maxHeight, layoutBounds.getHeight());
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
          XNode _value_1 = entry.getValue();
          boolean _equals = Objects.equal(currentVisibleNode, _value_1);
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
    this.alignGroup(this.group, maxWidth, maxHeight);
    this.setInterpolatedPosition(this.getCurrentPosition());
    this.spinToPosition.resetTargetPosition();
  }
  
  protected void alignGroup(final Group node, final double maxWidth, final double maxHeight) {
    this.group.setLayoutX(this.getPosition().getX());
    this.group.setLayoutY(this.getPosition().getY());
  }
  
  protected boolean matchesFilter(final XNode node) {
    boolean _xifexpression = false;
    String _lowerCase = this.getFilterString().toLowerCase();
    String _filterString = this.getFilterString();
    boolean _equals = Objects.equal(_lowerCase, _filterString);
    if (_equals) {
      _xifexpression = node.getName().toLowerCase().contains(this.getFilterString());
    } else {
      _xifexpression = node.getName().contains(this.getFilterString());
    }
    return _xifexpression;
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
