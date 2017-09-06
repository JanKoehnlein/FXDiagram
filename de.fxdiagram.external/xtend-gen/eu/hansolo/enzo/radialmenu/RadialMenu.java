/**
 * Copyright (c) 2013 by Gerrit Grunwald
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.hansolo.enzo.radialmenu;

import com.google.common.base.Objects;
import eu.hansolo.enzo.radialmenu.MenuItem;
import eu.hansolo.enzo.radialmenu.Options;
import eu.hansolo.enzo.radialmenu.SymbolCanvas;
import eu.hansolo.enzo.radialmenu.SymbolType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.Exceptions;

/**
 * Created with IntelliJ IDEA. User: hansolo Date: 21.09.12 Time: 13:25 To
 * change this template use File | Settings | File Templates.
 */
@SuppressWarnings("all")
public class RadialMenu extends Pane {
  public enum State {
    OPENED,
    
    CLOSED;
  }
  
  public static class ItemEvent extends Event {
    public final static EventType<RadialMenu.ItemEvent> ITEM_SELECTED = new EventType(Event.ANY, "itemSelected");
    
    private MenuItem item;
    
    public ItemEvent(final MenuItem ITEM, final Object SOURCE, final EventTarget TARGET, final EventType<RadialMenu.ItemEvent> EVENT_TYPE) {
      super(SOURCE, TARGET, EVENT_TYPE);
      this.item = ITEM;
    }
    
    public final MenuItem getItem() {
      return this.item;
    }
  }
  
  public static class MenuEvent extends Event {
    public final static EventType<RadialMenu.MenuEvent> MENU_OPEN_STARTED = new EventType(Event.ANY, "menuOpenStarted");
    
    public final static EventType<RadialMenu.MenuEvent> MENU_OPEN_FINISHED = new EventType(Event.ANY, "menuOpenFinished");
    
    public final static EventType<RadialMenu.MenuEvent> MENU_CLOSE_STARTED = new EventType(Event.ANY, "menuCloseStarted");
    
    public final static EventType<RadialMenu.MenuEvent> MENU_CLOSE_FINISHED = new EventType(Event.ANY, "menuCloseFinished");
    
    public MenuEvent(final Object SOURCE, final EventTarget TARGET, final EventType<RadialMenu.MenuEvent> EVENT_TYPE) {
      super(SOURCE, TARGET, EVENT_TYPE);
    }
  }
  
  private ObservableMap<Parent, MenuItem> items;
  
  private RadialMenu.State defaultState;
  
  private ObjectProperty<RadialMenu.State> state;
  
  private double degrees;
  
  private int positions;
  
  private Timeline[] openTimeLines;
  
  private Timeline[] closeTimeLines;
  
  private Options options;
  
  private Group button;
  
  private Circle mainMenuMouseCatcher;
  
  private boolean isDirty;
  
  private EventHandler<MouseEvent> mouseHandler;
  
  public RadialMenu(final Options OPTIONS, final MenuItem... ITEMS) {
    this(OPTIONS, Arrays.<MenuItem>asList(ITEMS));
  }
  
  public RadialMenu(final Options OPTIONS, final List<MenuItem> ITEMS) {
    this.options = OPTIONS;
    this.items = FXCollections.<Parent, MenuItem>observableHashMap();
    SimpleObjectProperty<RadialMenu.State> _simpleObjectProperty = new SimpleObjectProperty<RadialMenu.State>(RadialMenu.State.CLOSED);
    this.state = _simpleObjectProperty;
    this.degrees = Math.max(Math.min(360, this.options.getDegrees()), 0);
    int _xifexpression = (int) 0;
    int _compare = Double.compare(this.degrees, 360.0);
    boolean _tripleEquals = (_compare == 0);
    if (_tripleEquals) {
      _xifexpression = ITEMS.size();
    } else {
      int _size = ITEMS.size();
      _xifexpression = (_size - 1);
    }
    this.positions = _xifexpression;
    this.openTimeLines = new Timeline[ITEMS.size()];
    this.closeTimeLines = new Timeline[ITEMS.size()];
    Group _group = new Group();
    this.button = _group;
    this.isDirty = true;
    final EventHandler<MouseEvent> _function = (MouseEvent EVENT) -> {
      final Object SOURCE = EVENT.getSource();
      EventType<? extends MouseEvent> _eventType = EVENT.getEventType();
      boolean _tripleEquals_1 = (MouseEvent.MOUSE_CLICKED == _eventType);
      if (_tripleEquals_1) {
        boolean _equals = EVENT.getSource().equals(this.mainMenuMouseCatcher);
        if (_equals) {
          RadialMenu.State _state = this.getState();
          boolean _tripleEquals_2 = (RadialMenu.State.CLOSED == _state);
          if (_tripleEquals_2) {
            this.open();
          } else {
            this.close();
          }
        }
      } else {
        EventType<? extends MouseEvent> _eventType_1 = EVENT.getEventType();
        boolean _tripleEquals_3 = (MouseEvent.MOUSE_PRESSED == _eventType_1);
        if (_tripleEquals_3) {
          boolean _equals_1 = SOURCE.equals(this.mainMenuMouseCatcher);
          if (_equals_1) {
            this.mainMenuMouseCatcher.setFill(Color.rgb(0, 0, 0, 0.5));
          } else {
            this.select(this.items.get(SOURCE));
            MenuItem _get = this.items.get(SOURCE);
            RadialMenu.ItemEvent _itemEvent = new RadialMenu.ItemEvent(_get, this, null, RadialMenu.ItemEvent.ITEM_SELECTED);
            this.fireItemEvent(_itemEvent);
            EVENT.consume();
          }
        } else {
          EventType<? extends MouseEvent> _eventType_2 = EVENT.getEventType();
          boolean _tripleEquals_4 = (MouseEvent.MOUSE_RELEASED == _eventType_2);
          if (_tripleEquals_4) {
            boolean _equals_2 = EVENT.getSource().equals(this.mainMenuMouseCatcher);
            if (_equals_2) {
              this.mainMenuMouseCatcher.setFill(Color.TRANSPARENT);
            }
          } else {
            EventType<? extends MouseEvent> _eventType_3 = EVENT.getEventType();
            boolean _tripleEquals_5 = (MouseEvent.MOUSE_ENTERED == _eventType_3);
            if (_tripleEquals_5) {
              boolean _equals_3 = EVENT.getSource().equals(this.mainMenuMouseCatcher);
              if (_equals_3) {
              } else {
              }
            }
          }
        }
      }
    };
    this.mouseHandler = _function;
    this.initMenuItems(ITEMS);
    this.initGraphics();
    this.registerListeners();
  }
  
  private void initMenuItems(final List<MenuItem> ITEMS) {
    int _size = ITEMS.size();
    Map<Parent, MenuItem> itemMap = new HashMap<Parent, MenuItem>(_size);
    final DropShadow SHADOW = new DropShadow();
    double _buttonSize = this.options.getButtonSize();
    double _multiply = (0.1590909091 * _buttonSize);
    SHADOW.setRadius(_multiply);
    SHADOW.setColor(Color.rgb(0, 0, 0, 0.6));
    SHADOW.setBlurType(BlurType.TWO_PASS_BOX);
    for (int i = 0; (i < ITEMS.size()); i++) {
      {
        MenuItem item = ITEMS.get(i);
        final StackPane NODE = new StackPane();
        NODE.getChildren().add(this.createItemShape(item, SHADOW));
        if (((SymbolType.NONE == item.getSymbol()) && item.getThumbnailImageName().isEmpty())) {
          String _string = Integer.toString(i);
          Text text = new Text(_string);
          double _size_1 = item.getSize();
          double _multiply_1 = (_size_1 * 0.5);
          text.setFont(Font.font("Verdana", FontWeight.BOLD, _multiply_1));
          text.setFill(item.getForegroundColor());
          NODE.getChildren().add(text);
        } else {
          boolean _isEmpty = item.getThumbnailImageName().isEmpty();
          boolean _not = (!_isEmpty);
          if (_not) {
            try {
              NODE.getChildren().add(this.createCanvasThumbnail(item));
            } catch (final Throwable _t) {
              if (_t instanceof IllegalArgumentException) {
                final IllegalArgumentException exception = (IllegalArgumentException)_t;
                String _string_1 = Integer.toString(i);
                Text text_1 = new Text(_string_1);
                double _size_2 = item.getSize();
                double _multiply_2 = (_size_2 * 0.5);
                text_1.setFont(Font.font("Verdana", FontWeight.BOLD, _multiply_2));
                text_1.setFill(item.getForegroundColor());
                NODE.getChildren().add(text_1);
              } else {
                throw Exceptions.sneakyThrow(_t);
              }
            }
          } else {
            SymbolType _symbol = item.getSymbol();
            double _size_3 = item.getSize();
            double _multiply_3 = (0.7 * _size_3);
            Canvas symbol = SymbolCanvas.getSymbol(_symbol, _multiply_3, Color.WHITE);
            NODE.getChildren().add(symbol);
          }
        }
        double _size_4 = item.getSize();
        double _multiply_4 = (_size_4 * 0.5);
        Circle itemMouseCatcher = new Circle(_multiply_4);
        itemMouseCatcher.setFill(Color.TRANSPARENT);
        itemMouseCatcher.<MouseEvent>addEventFilter(MouseEvent.MOUSE_CLICKED, this.mouseHandler);
        NODE.getChildren().add(itemMouseCatcher);
        NODE.setOpacity(0.0);
        double _offset = this.options.getOffset();
        double _plus = (((this.degrees / this.positions) * i) + _offset);
        double degree = (_plus % 360);
        double _cos = Math.cos(Math.toRadians(degree));
        double _sin = Math.sin(Math.toRadians(degree));
        Point2D position = new Point2D(_cos, _sin);
        double _x = position.getX();
        double _radius = this.options.getRadius();
        double _multiply_5 = (_x * _radius);
        double x = Math.round(_multiply_5);
        double _y = position.getY();
        double _radius_1 = this.options.getRadius();
        double _multiply_6 = (_y * _radius_1);
        double y = Math.round(_multiply_6);
        int _size_5 = ITEMS.size();
        int _divide = (200 / _size_5);
        double delay = (_divide * i);
        {
          final Timeline[] _wrVal_openTimeLines = this.openTimeLines;
          final int _wrIndx_openTimeLines = i;
          _wrVal_openTimeLines[_wrIndx_openTimeLines] = this.createItemOpenTimeLine(NODE, x, y, delay);
        }
        {
          final Timeline[] _wrVal_closeTimeLines = this.closeTimeLines;
          final int _wrIndx_closeTimeLines = i;
          _wrVal_closeTimeLines[_wrIndx_closeTimeLines] = this.createItemCloseTimeLine(NODE, x, y, delay);
        }
        NODE.setOnMousePressed(this.mouseHandler);
        NODE.setOnMouseReleased(this.mouseHandler);
        itemMap.put(NODE, item);
        String _tooltip = item.getTooltip();
        Tooltip _tooltip_1 = new Tooltip(_tooltip);
        Tooltip.install(NODE, _tooltip_1);
      }
    }
    this.items.putAll(itemMap);
  }
  
  private void initGraphics() {
    this.getChildren().setAll(this.items.keySet());
    this.getChildren().add(this.button);
  }
  
  private void registerListeners() {
    final ChangeListener<Number> _function = (ObservableValue<? extends Number> ov, Number oldWidth, Number newWidth) -> {
      double _doubleValue = oldWidth.doubleValue();
      double _doubleValue_1 = newWidth.doubleValue();
      boolean _tripleNotEquals = (_doubleValue != _doubleValue_1);
      if (_tripleNotEquals) {
        this.isDirty = true;
      }
    };
    this.widthProperty().addListener(_function);
    final ChangeListener<Number> _function_1 = (ObservableValue<? extends Number> ov, Number oldHeight, Number newHeight) -> {
      double _doubleValue = oldHeight.doubleValue();
      double _doubleValue_1 = newHeight.doubleValue();
      boolean _tripleNotEquals = (_doubleValue != _doubleValue_1);
      if (_tripleNotEquals) {
        this.isDirty = true;
      }
    };
    this.heightProperty().addListener(_function_1);
  }
  
  @Override
  public void layoutChildren() {
    super.layoutChildren();
    if (this.isDirty) {
      this.resize();
      this.isDirty = false;
    }
  }
  
  public final RadialMenu.State getState() {
    RadialMenu.State _xifexpression = null;
    if ((null == this.state)) {
      _xifexpression = this.defaultState;
    } else {
      _xifexpression = this.state.get();
    }
    return _xifexpression;
  }
  
  private final void setState(final RadialMenu.State STATE) {
    if ((null == this.state)) {
      this.defaultState = STATE;
    } else {
      this.state.set(STATE);
    }
  }
  
  public final ReadOnlyObjectProperty<RadialMenu.State> stateProperty() {
    if ((null == this.state)) {
      SimpleObjectProperty<RadialMenu.State> _simpleObjectProperty = new SimpleObjectProperty<RadialMenu.State>(this, "state", this.defaultState);
      this.state = _simpleObjectProperty;
    }
    return this.state;
  }
  
  public MenuItem getItem(final int INDEX) {
    if (((INDEX < 0) || (INDEX > this.items.size()))) {
      throw new IndexOutOfBoundsException();
    }
    return this.getItems().get(INDEX);
  }
  
  public void addItem(final MenuItem ITEM) {
    Collection<MenuItem> _values = this.items.values();
    List<MenuItem> tmpItems = ((List<MenuItem>) _values);
    tmpItems.add(ITEM);
    this.initMenuItems(tmpItems);
    this.initGraphics();
  }
  
  public void removeItem(final MenuItem ITEM) {
    boolean _contains = this.items.values().contains(ITEM);
    boolean _not = (!_contains);
    if (_not) {
      return;
    }
    Collection<MenuItem> _values = this.items.values();
    List<MenuItem> tmpItems = ((List<MenuItem>) _values);
    tmpItems.remove(ITEM);
    this.initMenuItems(tmpItems);
    this.initGraphics();
  }
  
  public List<MenuItem> getItems() {
    int _size = this.items.size();
    List<MenuItem> tmpList = new ArrayList<MenuItem>(_size);
    Collection<MenuItem> _values = this.items.values();
    for (final MenuItem item : _values) {
      tmpList.add(item);
    }
    return tmpList;
  }
  
  public void open() {
    boolean _buttonHideOnSelect = this.options.getButtonHideOnSelect();
    boolean _not = (!_buttonHideOnSelect);
    if (_not) {
      this.show();
    }
    RadialMenu.State _state = this.getState();
    boolean _tripleEquals = (RadialMenu.State.OPENED == _state);
    if (_tripleEquals) {
      return;
    }
    this.setState(RadialMenu.State.OPENED);
    this.button.setOpacity(1.0);
    Timeline _xblockexpression = null;
    {
      int _length = this.openTimeLines.length;
      final int _rdIndx_openTimeLines = (_length - 1);
      _xblockexpression = this.openTimeLines[_rdIndx_openTimeLines];
    }
    final EventHandler<ActionEvent> _function = (ActionEvent actionEvent) -> {
      RadialMenu.MenuEvent _menuEvent = new RadialMenu.MenuEvent(this, null, RadialMenu.MenuEvent.MENU_OPEN_FINISHED);
      this.fireMenuEvent(_menuEvent);
    };
    _xblockexpression.setOnFinished(_function);
    for (int i = 0; (i < this.openTimeLines.length); i++) {
      Timeline _xblockexpression_1 = null;
      {
        final int _rdIndx_openTimeLines = i;
        _xblockexpression_1 = this.openTimeLines[_rdIndx_openTimeLines];
      }
      _xblockexpression_1.play();
    }
    RadialMenu.MenuEvent _menuEvent = new RadialMenu.MenuEvent(this, null, RadialMenu.MenuEvent.MENU_OPEN_STARTED);
    this.fireMenuEvent(_menuEvent);
  }
  
  public void close() {
    RadialMenu.State _state = this.getState();
    boolean _tripleEquals = (RadialMenu.State.CLOSED == _state);
    if (_tripleEquals) {
      return;
    }
    this.setState(RadialMenu.State.CLOSED);
    Timeline _xblockexpression = null;
    {
      int _length = this.closeTimeLines.length;
      final int _rdIndx_closeTimeLines = (_length - 1);
      _xblockexpression = this.closeTimeLines[_rdIndx_closeTimeLines];
    }
    final EventHandler<ActionEvent> _function = (ActionEvent actionEvent) -> {
      FadeTransition buttonFadeOut = new FadeTransition();
      buttonFadeOut.setNode(this.button);
      buttonFadeOut.setDuration(Duration.millis(100));
      buttonFadeOut.setToValue(this.options.getButtonAlpha());
      buttonFadeOut.play();
      RadialMenu.MenuEvent _menuEvent = new RadialMenu.MenuEvent(this, null, RadialMenu.MenuEvent.MENU_CLOSE_FINISHED);
      this.fireMenuEvent(_menuEvent);
    };
    _xblockexpression.setOnFinished(_function);
    for (int i = 0; (i < this.closeTimeLines.length); i++) {
      Timeline _xblockexpression_1 = null;
      {
        final int _rdIndx_closeTimeLines = i;
        _xblockexpression_1 = this.closeTimeLines[_rdIndx_closeTimeLines];
      }
      _xblockexpression_1.play();
    }
    RadialMenu.MenuEvent _menuEvent = new RadialMenu.MenuEvent(this, null, RadialMenu.MenuEvent.MENU_CLOSE_STARTED);
    this.fireMenuEvent(_menuEvent);
  }
  
  public void show() {
    if ((this.options.getButtonHideOnSelect() && (this.button.getOpacity() > 0))) {
      return;
    }
    if ((this.options.getButtonHideOnSelect() || (this.button.getOpacity() == 0))) {
      this.button.setScaleX(1.0);
      this.button.setScaleY(1.0);
      this.button.setRotate(0);
      FadeTransition buttonFadeIn = new FadeTransition();
      buttonFadeIn.setNode(this.button);
      buttonFadeIn.setDuration(Duration.millis(200));
      buttonFadeIn.setToValue(this.options.getButtonAlpha());
      buttonFadeIn.play();
    }
    Set<Parent> _keySet = this.items.keySet();
    for (final Parent node : _keySet) {
      {
        node.setScaleX(1.0);
        node.setScaleY(1.0);
        node.setTranslateX(0);
        node.setTranslateY(0);
        node.setRotate(0);
      }
    }
  }
  
  public void hide() {
    this.setState(RadialMenu.State.CLOSED);
    this.button.setOpacity(0.0);
    Set<Parent> _keySet = this.items.keySet();
    for (final Parent node : _keySet) {
      node.setOpacity(0);
    }
  }
  
  public void select(final MenuItem SELECTED_ITEM) {
    int _size = this.items.size();
    int _multiply = (_size * 2);
    List<Transition> transitions = new ArrayList<Transition>(_multiply);
    Set<Parent> _keySet = this.items.keySet();
    for (final Parent node : _keySet) {
      {
        boolean _equals = this.items.get(node).equals(SELECTED_ITEM);
        if (_equals) {
          Duration _millis = Duration.millis(300);
          ScaleTransition enlargeItem = new ScaleTransition(_millis, node);
          enlargeItem.setToX(5.0);
          enlargeItem.setToY(5.0);
          transitions.add(enlargeItem);
        } else {
          Duration _millis_1 = Duration.millis(300);
          ScaleTransition shrinkItem = new ScaleTransition(_millis_1, node);
          shrinkItem.setToX(0.01);
          shrinkItem.setToY(0.01);
          transitions.add(shrinkItem);
        }
        Duration _millis_2 = Duration.millis(400);
        FadeTransition fadeOutItem = new FadeTransition(_millis_2, node);
        fadeOutItem.setToValue(0.0);
        transitions.add(fadeOutItem);
      }
    }
    ParallelTransition selectTransition = new ParallelTransition();
    selectTransition.getChildren().addAll(transitions);
    selectTransition.play();
    this.close();
  }
  
  private Circle createItemShape(final MenuItem ITEM, final Effect EFFECT) {
    double _size = ITEM.getSize();
    double _multiply = (_size * 0.5);
    Circle circle = new Circle(_multiply);
    circle.setFill(ITEM.getInnerColor());
    circle.setStroke(ITEM.getFrameColor());
    double _size_1 = ITEM.getSize();
    double _multiply_1 = (0.09375 * _size_1);
    circle.setStrokeWidth(_multiply_1);
    circle.setStrokeType(StrokeType.CENTERED);
    circle.setEffect(EFFECT);
    return circle;
  }
  
  private Canvas createCanvasThumbnail(final MenuItem ITEM) {
    String _thumbnailImageName = ITEM.getThumbnailImageName();
    final Image THUMBNAIL = new Image(_thumbnailImageName);
    double _xifexpression = (double) 0;
    double _width = THUMBNAIL.getWidth();
    double _height = THUMBNAIL.getHeight();
    boolean _greaterThan = (_width > _height);
    if (_greaterThan) {
      _xifexpression = THUMBNAIL.getWidth();
    } else {
      _xifexpression = THUMBNAIL.getHeight();
    }
    final double SIZE = _xifexpression;
    double _size = ITEM.getSize();
    double _multiply = (0.7 * _size);
    final double SCALE = (_multiply / SIZE);
    double _size_1 = ITEM.getSize();
    double _multiply_1 = (0.7 * _size_1);
    double _size_2 = ITEM.getSize();
    double _multiply_2 = (0.7 * _size_2);
    Canvas canvasThumbnail = new Canvas(_multiply_1, _multiply_2);
    GraphicsContext ctx = canvasThumbnail.getGraphicsContext2D();
    ctx.scale(SCALE, SCALE);
    ctx.drawImage(THUMBNAIL, 0, 0);
    return canvasThumbnail;
  }
  
  private Timeline createItemOpenTimeLine(final StackPane NODE, final double X, final double Y, final double DELAY) {
    DoubleProperty _translateXProperty = NODE.translateXProperty();
    KeyValue kvX1 = new <Number>KeyValue(_translateXProperty, Integer.valueOf(0), Interpolator.EASE_OUT);
    DoubleProperty _translateYProperty = NODE.translateYProperty();
    KeyValue kvY1 = new <Number>KeyValue(_translateYProperty, Integer.valueOf(0), Interpolator.EASE_OUT);
    DoubleProperty _rotateProperty = NODE.rotateProperty();
    KeyValue kvR1 = new <Number>KeyValue(_rotateProperty, Integer.valueOf(0), Interpolator.EASE_OUT);
    DoubleProperty _opacityProperty = NODE.opacityProperty();
    KeyValue kvO1 = new <Number>KeyValue(_opacityProperty, Integer.valueOf(0), Interpolator.EASE_OUT);
    DoubleProperty _translateXProperty_1 = NODE.translateXProperty();
    KeyValue kvX2 = new <Number>KeyValue(_translateXProperty_1, Double.valueOf(0.0));
    DoubleProperty _translateYProperty_1 = NODE.translateYProperty();
    KeyValue kvY2 = new <Number>KeyValue(_translateYProperty_1, Double.valueOf(0.0));
    DoubleProperty _scaleXProperty = NODE.scaleXProperty();
    KeyValue kvSx2 = new <Number>KeyValue(_scaleXProperty, Integer.valueOf(1));
    DoubleProperty _scaleYProperty = NODE.scaleYProperty();
    KeyValue kvSy2 = new <Number>KeyValue(_scaleYProperty, Integer.valueOf(1));
    DoubleProperty _translateXProperty_2 = NODE.translateXProperty();
    KeyValue kvX3 = new <Number>KeyValue(_translateXProperty_2, Double.valueOf((1.1 * X)), Interpolator.EASE_IN);
    DoubleProperty _translateYProperty_2 = NODE.translateYProperty();
    KeyValue kvY3 = new <Number>KeyValue(_translateYProperty_2, Double.valueOf((1.1 * Y)), Interpolator.EASE_IN);
    DoubleProperty _translateXProperty_3 = NODE.translateXProperty();
    KeyValue kvX4 = new <Number>KeyValue(_translateXProperty_3, Double.valueOf((0.95 * X)), Interpolator.EASE_OUT);
    DoubleProperty _translateYProperty_3 = NODE.translateYProperty();
    KeyValue kvY4 = new <Number>KeyValue(_translateYProperty_3, Double.valueOf((0.95 * Y)), Interpolator.EASE_OUT);
    DoubleProperty _rotateProperty_1 = NODE.rotateProperty();
    KeyValue kvRO4 = new <Number>KeyValue(_rotateProperty_1, Integer.valueOf(360));
    DoubleProperty _opacityProperty_1 = NODE.opacityProperty();
    KeyValue kvO4 = new <Number>KeyValue(_opacityProperty_1, Double.valueOf(1.0), Interpolator.EASE_OUT);
    DoubleProperty _translateXProperty_4 = NODE.translateXProperty();
    KeyValue kvX5 = new <Number>KeyValue(_translateXProperty_4, Double.valueOf(X));
    DoubleProperty _translateYProperty_4 = NODE.translateYProperty();
    KeyValue kvY5 = new <Number>KeyValue(_translateYProperty_4, Double.valueOf(Y));
    Duration _millis = Duration.millis(0);
    KeyFrame kfO1 = new KeyFrame(_millis, kvX1, kvY1, kvR1, kvO1);
    Duration _millis_1 = Duration.millis((50 + DELAY));
    KeyFrame kfO2 = new KeyFrame(_millis_1, kvX2, kvY2, kvSx2, kvSy2);
    Duration _millis_2 = Duration.millis((250 + DELAY));
    KeyFrame kfO3 = new KeyFrame(_millis_2, kvX3, kvY3);
    Duration _millis_3 = Duration.millis((400 + DELAY));
    KeyFrame kfO4 = new KeyFrame(_millis_3, kvX4, kvY4, kvRO4, kvO4);
    Duration _millis_4 = Duration.millis((600 + DELAY));
    KeyFrame kfO5 = new KeyFrame(_millis_4, kvX5, kvY5);
    return new Timeline(kfO1, kfO2, kfO3, kfO4, kfO5);
  }
  
  private Timeline createItemCloseTimeLine(final StackPane NODE, final double X, final double Y, final double DELAY) {
    DoubleProperty _translateXProperty = NODE.translateXProperty();
    KeyValue kvX1 = new <Number>KeyValue(_translateXProperty, Integer.valueOf(0), Interpolator.EASE_OUT);
    DoubleProperty _translateYProperty = NODE.translateYProperty();
    KeyValue kvY1 = new <Number>KeyValue(_translateYProperty, Integer.valueOf(0), Interpolator.EASE_OUT);
    DoubleProperty _rotateProperty = NODE.rotateProperty();
    KeyValue kvR1 = new <Number>KeyValue(_rotateProperty, Integer.valueOf(0), Interpolator.EASE_OUT);
    DoubleProperty _opacityProperty = NODE.opacityProperty();
    KeyValue kvO1 = new <Number>KeyValue(_opacityProperty, Integer.valueOf(0), Interpolator.EASE_OUT);
    DoubleProperty _translateXProperty_1 = NODE.translateXProperty();
    KeyValue kvX2 = new <Number>KeyValue(_translateXProperty_1, Double.valueOf(0.0));
    DoubleProperty _translateYProperty_1 = NODE.translateYProperty();
    KeyValue kvY2 = new <Number>KeyValue(_translateYProperty_1, Double.valueOf(0.0));
    DoubleProperty _translateXProperty_2 = NODE.translateXProperty();
    KeyValue kvX3 = new <Number>KeyValue(_translateXProperty_2, Double.valueOf((1.1 * X)), Interpolator.EASE_IN);
    DoubleProperty _translateYProperty_2 = NODE.translateYProperty();
    KeyValue kvY3 = new <Number>KeyValue(_translateYProperty_2, Double.valueOf((1.1 * Y)), Interpolator.EASE_IN);
    DoubleProperty _translateXProperty_3 = NODE.translateXProperty();
    KeyValue kvX4 = new <Number>KeyValue(_translateXProperty_3, Double.valueOf((0.95 * X)), Interpolator.EASE_OUT);
    DoubleProperty _translateYProperty_3 = NODE.translateYProperty();
    KeyValue kvY4 = new <Number>KeyValue(_translateYProperty_3, Double.valueOf((0.95 * Y)), Interpolator.EASE_OUT);
    DoubleProperty _rotateProperty_1 = NODE.rotateProperty();
    KeyValue kvRC4 = new <Number>KeyValue(_rotateProperty_1, Integer.valueOf(720));
    DoubleProperty _opacityProperty_1 = NODE.opacityProperty();
    KeyValue kvO4 = new <Number>KeyValue(_opacityProperty_1, Double.valueOf(1.0), Interpolator.EASE_OUT);
    DoubleProperty _translateXProperty_4 = NODE.translateXProperty();
    KeyValue kvX5 = new <Number>KeyValue(_translateXProperty_4, Double.valueOf(X));
    DoubleProperty _translateYProperty_4 = NODE.translateYProperty();
    KeyValue kvY5 = new <Number>KeyValue(_translateYProperty_4, Double.valueOf(Y));
    Duration _millis = Duration.millis(0);
    KeyFrame kfC1 = new KeyFrame(_millis, kvX5, kvY5);
    Duration _millis_1 = Duration.millis((50 + DELAY));
    KeyFrame kfC2 = new KeyFrame(_millis_1, kvX4, kvY4, kvRC4, kvO4);
    Duration _millis_2 = Duration.millis((250 + DELAY));
    KeyFrame kfC3 = new KeyFrame(_millis_2, kvX3, kvY3);
    Duration _millis_3 = Duration.millis((400 + DELAY));
    KeyFrame kfC4 = new KeyFrame(_millis_3, kvX2, kvY2);
    Duration _millis_4 = Duration.millis((600 + DELAY));
    KeyFrame kfC5 = new KeyFrame(_millis_4, kvX1, kvY1, kvR1, kvO1);
    return new Timeline(kfC1, kfC2, kfC3, kfC4, kfC5);
  }
  
  private void resize() {
    double _prefWidth = this.getPrefWidth();
    double _multiply = (_prefWidth * 0.5);
    this.button.setLayoutX(_multiply);
    double _prefHeight = this.getPrefHeight();
    double _multiply_1 = (_prefHeight * 0.5);
    this.button.setLayoutY(_multiply_1);
    Set<Parent> _keySet = this.items.keySet();
    for (final Parent node : _keySet) {
      {
        double _prefWidth_1 = this.getPrefWidth();
        double _width = node.getLayoutBounds().getWidth();
        double _minus = (_prefWidth_1 - _width);
        double _multiply_2 = (_minus * 0.5);
        node.setLayoutX(_multiply_2);
        double _prefHeight_1 = this.getPrefHeight();
        double _height = node.getLayoutBounds().getHeight();
        double _minus_1 = (_prefHeight_1 - _height);
        double _multiply_3 = (_minus_1 * 0.5);
        node.setLayoutY(_multiply_3);
      }
    }
  }
  
  public final ObjectProperty<EventHandler<RadialMenu.ItemEvent>> onItemSelectedProperty() {
    return this.onItemSelected;
  }
  
  public final void setOnItemSelected(final EventHandler<RadialMenu.ItemEvent> value) {
    this.onItemSelectedProperty().set(value);
  }
  
  public final EventHandler<RadialMenu.ItemEvent> getOnItemSelected() {
    return this.onItemSelectedProperty().get();
  }
  
  private ObjectProperty<EventHandler<RadialMenu.ItemEvent>> onItemSelected = new ObjectPropertyBase<EventHandler<RadialMenu.ItemEvent>>() {
    @Override
    public Object getBean() {
      return this;
    }
    
    @Override
    public String getName() {
      return "onItemSelected";
    }
  };
  
  public void fireItemEvent(final RadialMenu.ItemEvent EVENT) {
    final EventHandler<RadialMenu.ItemEvent> HANDLER = this.getOnItemSelected();
    if ((HANDLER != null)) {
      HANDLER.handle(EVENT);
    }
  }
  
  public final ObjectProperty<EventHandler<RadialMenu.MenuEvent>> onMenuOpenStartedProperty() {
    return this.onMenuOpenStarted;
  }
  
  public final void setOnMenuOpenStarted(final EventHandler<RadialMenu.MenuEvent> value) {
    this.onMenuOpenStartedProperty().set(value);
  }
  
  public final EventHandler<RadialMenu.MenuEvent> getOnMenuOpenStarted() {
    return this.onMenuOpenStartedProperty().get();
  }
  
  private ObjectProperty<EventHandler<RadialMenu.MenuEvent>> onMenuOpenStarted = new ObjectPropertyBase<EventHandler<RadialMenu.MenuEvent>>() {
    @Override
    public Object getBean() {
      return this;
    }
    
    @Override
    public String getName() {
      return "onMenuOpenStarted";
    }
  };
  
  public final ObjectProperty<EventHandler<RadialMenu.MenuEvent>> onMenuOpenFinishedProperty() {
    return this.onMenuOpenFinished;
  }
  
  public final void setOnMenuOpenFinished(final EventHandler<RadialMenu.MenuEvent> value) {
    this.onMenuOpenFinishedProperty().set(value);
  }
  
  public final EventHandler<RadialMenu.MenuEvent> getOnMenuOpenFinished() {
    return this.onMenuOpenFinishedProperty().get();
  }
  
  private ObjectProperty<EventHandler<RadialMenu.MenuEvent>> onMenuOpenFinished = new ObjectPropertyBase<EventHandler<RadialMenu.MenuEvent>>() {
    @Override
    public Object getBean() {
      return this;
    }
    
    @Override
    public String getName() {
      return "onMenuOpenFinished";
    }
  };
  
  public final ObjectProperty<EventHandler<RadialMenu.MenuEvent>> onMenuCloseStartedProperty() {
    return this.onMenuCloseStarted;
  }
  
  public final void setOnMenuCloseStarted(final EventHandler<RadialMenu.MenuEvent> value) {
    this.onMenuCloseStartedProperty().set(value);
  }
  
  public final EventHandler<RadialMenu.MenuEvent> getOnMenuCloseStarted() {
    return this.onMenuCloseStartedProperty().get();
  }
  
  private ObjectProperty<EventHandler<RadialMenu.MenuEvent>> onMenuCloseStarted = new ObjectPropertyBase<EventHandler<RadialMenu.MenuEvent>>() {
    @Override
    public Object getBean() {
      return this;
    }
    
    @Override
    public String getName() {
      return "onMenuCloseStarted";
    }
  };
  
  public final ObjectProperty<EventHandler<RadialMenu.MenuEvent>> onMenuCloseFinishedProperty() {
    return this.onMenuCloseFinished;
  }
  
  public final void setOnMenuCloseFinished(final EventHandler<RadialMenu.MenuEvent> value) {
    this.onMenuCloseFinishedProperty().set(value);
  }
  
  public final EventHandler<RadialMenu.MenuEvent> getOnMenuCloseFinished() {
    return this.onMenuCloseFinishedProperty().get();
  }
  
  private ObjectProperty<EventHandler<RadialMenu.MenuEvent>> onMenuCloseFinished = new ObjectPropertyBase<EventHandler<RadialMenu.MenuEvent>>() {
    @Override
    public Object getBean() {
      return this;
    }
    
    @Override
    public String getName() {
      return "onMenuCloseFinished";
    }
  };
  
  public void fireMenuEvent(final RadialMenu.MenuEvent EVENT) {
    final EventType<? extends Event> TYPE = EVENT.getEventType();
    EventHandler<RadialMenu.MenuEvent> _switchResult = null;
    boolean _matched = false;
    if (Objects.equal(TYPE, RadialMenu.MenuEvent.MENU_OPEN_STARTED)) {
      _matched=true;
      _switchResult = this.getOnMenuOpenStarted();
    }
    if (!_matched) {
      if (Objects.equal(TYPE, RadialMenu.MenuEvent.MENU_OPEN_FINISHED)) {
        _matched=true;
        _switchResult = this.getOnMenuOpenFinished();
      }
    }
    if (!_matched) {
      if (Objects.equal(TYPE, RadialMenu.MenuEvent.MENU_CLOSE_STARTED)) {
        _matched=true;
        _switchResult = this.getOnMenuCloseStarted();
      }
    }
    if (!_matched) {
      if (Objects.equal(TYPE, RadialMenu.MenuEvent.MENU_CLOSE_FINISHED)) {
        _matched=true;
        _switchResult = this.getOnMenuCloseFinished();
      }
    }
    if (!_matched) {
      _switchResult = null;
    }
    final EventHandler<RadialMenu.MenuEvent> HANDLER = _switchResult;
    if ((HANDLER != null)) {
      HANDLER.handle(EVENT);
    }
  }
}
