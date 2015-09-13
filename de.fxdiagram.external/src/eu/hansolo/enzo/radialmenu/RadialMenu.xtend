/*
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
package eu.hansolo.enzo.radialmenu

import eu.hansolo.enzo.radialmenu.RadialMenu.ItemEvent
import eu.hansolo.enzo.radialmenu.RadialMenu.MenuEvent
import java.util.ArrayList
import java.util.Arrays
import java.util.HashMap
import java.util.List
import java.util.Map
import javafx.animation.FadeTransition
import javafx.animation.Interpolator
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.ParallelTransition
import javafx.animation.ScaleTransition
import javafx.animation.Timeline
import javafx.animation.Transition
import javafx.beans.property.ObjectProperty
import javafx.beans.property.ObjectPropertyBase
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ObservableMap
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.event.EventHandler
import javafx.event.EventTarget
import javafx.event.EventType
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.Parent
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Tooltip
import javafx.scene.effect.BlurType
import javafx.scene.effect.DropShadow
import javafx.scene.effect.Effect
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.StrokeType
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import javafx.util.Duration

/** 
 * Created with IntelliJ IDEA. User: hansolo Date: 21.09.12 Time: 13:25 To
 * change this template use File | Settings | File Templates.
 */
@SuppressWarnings("all") class RadialMenu extends Pane {
	public static enum State {
		OPENED,
		CLOSED
	}

	ObservableMap<Parent, MenuItem> items
	State defaultState
	ObjectProperty<State> state
	double degrees
	int positions
	Timeline[] openTimeLines
	Timeline[] closeTimeLines
	Options options
	Group button
	// private Group cross;
	Circle mainMenuMouseCatcher
	boolean isDirty
	EventHandler<MouseEvent> mouseHandler

	// ******************** Constructors **************************************
	new(Options OPTIONS, MenuItem... ITEMS) {
		this(OPTIONS, Arrays.asList(ITEMS))
	}

	new(Options OPTIONS, List<MenuItem> ITEMS) {
		options = OPTIONS
		items = FXCollections.observableHashMap()
		state = new SimpleObjectProperty<State>(State.CLOSED)
		degrees = Math.max(Math.min(360, options.getDegrees()), 0)
		positions = if(Double.compare(degrees, 360.0) === 0) ITEMS.size() else ITEMS.size() - 1
		openTimeLines = newArrayOfSize(ITEMS.size())
		closeTimeLines = newArrayOfSize(ITEMS.size())
		button = new Group() // cross = new Group();
		isDirty = true
		mouseHandler = [ MouseEvent EVENT |
			val Object SOURCE = EVENT.getSource()
			if (MouseEvent.MOUSE_CLICKED === EVENT.getEventType()) {
				if (EVENT.getSource().equals(mainMenuMouseCatcher)) {
					if (State.CLOSED === getState()) {
						open()
					} else {
						close()
					}
				}

			} else if (MouseEvent.MOUSE_PRESSED === EVENT.getEventType()) {
				if (SOURCE.equals(mainMenuMouseCatcher)) {
					mainMenuMouseCatcher.setFill(Color.rgb(0, 0, 0, 0.5))
				} else {
					select(items.get(SOURCE))
					fireItemEvent(new ItemEvent(items.get(SOURCE), this, null, ItemEvent.ITEM_SELECTED))
					EVENT.consume()
				}
			} else if (MouseEvent.MOUSE_RELEASED === EVENT.getEventType()) {
				if (EVENT.getSource().equals(mainMenuMouseCatcher)) {
					mainMenuMouseCatcher.setFill(Color.TRANSPARENT)
				}

			} else if (MouseEvent.MOUSE_ENTERED === EVENT.getEventType()) {
				if (EVENT.getSource().equals(mainMenuMouseCatcher)) {
				} else {
				}
			}
		] // initMainButton();
		initMenuItems(ITEMS)
		initGraphics()
		registerListeners()
	}

	def private void initMenuItems(List<MenuItem> ITEMS) {
		var Map<Parent, MenuItem> itemMap = new HashMap(ITEMS.size())
		val DropShadow SHADOW = new DropShadow()
		SHADOW.setRadius(0.1590909091 * options.getButtonSize())
		SHADOW.setColor(Color.rgb(0, 0, 0, 0.6))
		SHADOW.setBlurType(BlurType.TWO_PASS_BOX)
		for (var int i = 0; i < ITEMS.size(); i++) {
			var MenuItem item = ITEMS.get(i)
			// Create graphical representation of each menu item
			val StackPane NODE = new StackPane()
			NODE.getChildren().add(createItemShape(item, SHADOW))
			if (SymbolType.NONE === item.getSymbol() && item.getThumbnailImageName().isEmpty()) {
				var Text text = new Text(Integer.toString(i))
				text.setFont(Font.font("Verdana", FontWeight.BOLD, item.getSize() * 0.5))
				text.setFill(item.getForegroundColor())
				NODE.getChildren().add(text)
			} else if (!item.getThumbnailImageName().isEmpty()) {
				try {
					NODE.getChildren().add(createCanvasThumbnail(item))
				} catch (IllegalArgumentException exception) {
					var Text text = new Text(Integer.toString(i))
					text.setFont(Font.font("Verdana", FontWeight.BOLD, item.getSize() * 0.5))
					text.setFill(item.getForegroundColor())
					NODE.getChildren().add(text)
				}

			} else {
				var Canvas symbol = SymbolCanvas.getSymbol(item.getSymbol(), 0.7 * item.getSize(), Color.WHITE)
				NODE.getChildren().add(symbol)
			}
			var Circle itemMouseCatcher = new Circle(item.getSize() * 0.5)
			itemMouseCatcher.setFill(Color.TRANSPARENT)
			itemMouseCatcher.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseHandler)
			NODE.getChildren().add(itemMouseCatcher)
			NODE.setOpacity(0.0) // Add animations for each menu item
			var double degree = (((degrees / positions) * i) + options.getOffset()) % 360
			var Point2D position = new Point2D(Math.cos(Math.toRadians(degree)), Math.sin(Math.toRadians(degree)))
			var double x = Math.round(position.getX() * options.getRadius())
			var double y = Math.round(position.getY() * options.getRadius())
			var double delay = (200 / ITEMS.size()) * i
			{
				val _wrVal_openTimeLines = openTimeLines
				val _wrIndx_openTimeLines = i
				_wrVal_openTimeLines.set(_wrIndx_openTimeLines, createItemOpenTimeLine(NODE, x, y, delay))
			}
			{
				val _wrVal_closeTimeLines = closeTimeLines
				val _wrIndx_closeTimeLines = i
				_wrVal_closeTimeLines.set(_wrIndx_closeTimeLines, createItemCloseTimeLine(NODE, x, y, delay))
			} 
			NODE.setOnMousePressed(mouseHandler)
			NODE.setOnMouseReleased(mouseHandler) // Add items and nodes to map
			itemMap.put(NODE, item)
			Tooltip.install(NODE, new Tooltip(item.getTooltip()))
		}
		items.putAll(itemMap)
	}

	def private void initGraphics() {
		getChildren().setAll(items.keySet())
		getChildren().add(button)
	}

	def private void registerListeners() {
		widthProperty().addListener([ ObservableValue<? extends Number> ov, Number oldWidth, Number newWidth | // TODO Auto-generated method stub
			if(oldWidth.doubleValue() !== newWidth.doubleValue()) isDirty = true
		])
		heightProperty().addListener([ ObservableValue<? extends Number> ov, Number oldHeight, Number newHeight |
			if(oldHeight.doubleValue() !== newHeight.doubleValue()) isDirty = true
		])
	}

	// ******************** Methods *******************************************
	override void layoutChildren() {
		super.layoutChildren()
		if (isDirty) {
			resize()
			isDirty = false
		}
	}

	def final State getState() {
		return if(null === state) defaultState else state.get()
	}

	def private final void setState(State STATE) {
		if (null === state) {
			defaultState = STATE
		} else {
			state.set(STATE)
		}
	}

	def final ReadOnlyObjectProperty<State> stateProperty() {
		if (null === state) {
			state = new SimpleObjectProperty(this, "state", defaultState)
		}
		return state
	}

	def MenuItem getItem(int INDEX) {
		if (INDEX < 0 || INDEX > items.size()) {
			throw new IndexOutOfBoundsException()
		}
		return getItems().get(INDEX)
	}

	def void addItem(MenuItem ITEM) {
		var List<MenuItem> tmpItems = items.values() as List<MenuItem>
		tmpItems.add(ITEM)
		initMenuItems(tmpItems)
		initGraphics()
	}

	def void removeItem(MenuItem ITEM) {
		if (!items.values().contains(ITEM)) {
			return;
		}
		var List<MenuItem> tmpItems = items.values() as List<MenuItem>
		tmpItems.remove(ITEM)
		initMenuItems(tmpItems)
		initGraphics()
	}

	def List<MenuItem> getItems() {
		var List<MenuItem> tmpList = new ArrayList<MenuItem>(items.size())
		for (MenuItem item : items.values()) {
			tmpList.add(item)
		}
		return tmpList
	}

	def void open() {
		if (!options.getButtonHideOnSelect()) {
			show()
		}
		if (State.OPENED === getState()) {
			return;
		}
		setState(State.OPENED)
		button.setOpacity(1.0) 
		{
			val _rdIndx_openTimeLines = openTimeLines.length - 1
			openTimeLines.get(_rdIndx_openTimeLines)
		}.
			setOnFinished([ ActionEvent actionEvent |
				fireMenuEvent(new MenuEvent(this, null, MenuEvent.MENU_OPEN_FINISHED))
			])
		for (var int i = 0; i < openTimeLines.length; i++) {
			{
				val _rdIndx_openTimeLines = i
				openTimeLines.get(_rdIndx_openTimeLines)
			}.play()
		}
		fireMenuEvent(new MenuEvent(this, null, MenuEvent.MENU_OPEN_STARTED))
	}

	def void close() {
		if (State.CLOSED === getState()) {
			return;
		}
		setState(State.CLOSED) 
		{
			val _rdIndx_closeTimeLines = closeTimeLines.length - 1
			closeTimeLines.get(_rdIndx_closeTimeLines)
		}.setOnFinished([ ActionEvent actionEvent |
			var FadeTransition buttonFadeOut = new FadeTransition()
			buttonFadeOut.setNode(button)
			buttonFadeOut.setDuration(Duration.millis(100))
			buttonFadeOut.setToValue(options.getButtonAlpha())
			buttonFadeOut.play()
			fireMenuEvent(new MenuEvent(this, null, MenuEvent.MENU_CLOSE_FINISHED))
		])
		for (var int i = 0; i < closeTimeLines.length; i++) {
			{
				val _rdIndx_closeTimeLines = i
				closeTimeLines.get(_rdIndx_closeTimeLines)
			}.play()
		}
		fireMenuEvent(new MenuEvent(this, null, MenuEvent.MENU_CLOSE_STARTED))
	}

	def void show() {
		if (options.getButtonHideOnSelect() && button.getOpacity() > 0) {
			return;
		}
		if (options.getButtonHideOnSelect() || button.getOpacity() === 0) {
			button.setScaleX(1.0)
			button.setScaleY(1.0) // cross.setRotate(0);
			button.setRotate(0)
			var FadeTransition buttonFadeIn = new FadeTransition()
			buttonFadeIn.setNode(button)
			buttonFadeIn.setDuration(Duration.millis(200))
			buttonFadeIn.setToValue(options.getButtonAlpha())
			buttonFadeIn.play()
		}
		for (Parent node : items.keySet()) {
			node.setScaleX(1.0)
			node.setScaleY(1.0)
			node.setTranslateX(0)
			node.setTranslateY(0)
			node.setRotate(0)
		}

	}

	def void hide() {
		setState(State.CLOSED)
		button.setOpacity(0.0)
		for (Parent node : items.keySet()) {
			node.setOpacity(0)
		}

	}

	def void select(MenuItem SELECTED_ITEM) {
		var List<Transition> transitions = new ArrayList<Transition>(items.size() * 2)
		for (Parent node : items.keySet()) {
			if (items.get(node).equals(SELECTED_ITEM)) {
				// Add enlarge transition to selected item
				var ScaleTransition enlargeItem = new ScaleTransition(Duration.millis(300), node)
				enlargeItem.setToX(5.0)
				enlargeItem.setToY(5.0)
				transitions.add(enlargeItem)
			} else {
				// Add shrink transition to all other items
				var ScaleTransition shrinkItem = new ScaleTransition(Duration.millis(300), node)
				shrinkItem.setToX(0.01)
				shrinkItem.setToY(0.01)
				transitions.add(shrinkItem)
			} // Add fade out transition to every node
			var FadeTransition fadeOutItem = new FadeTransition(Duration.millis(400), node)
			fadeOutItem.setToValue(0.0)
			transitions.add(fadeOutItem)
		}

		var ParallelTransition selectTransition = new ParallelTransition()
		selectTransition.getChildren().addAll(transitions)
		selectTransition.play() // Set menu state back to closed
		close()
	}

	def private Circle createItemShape(MenuItem ITEM, Effect EFFECT) {
		var Circle circle = new Circle(ITEM.getSize() * 0.5)
		circle.setFill(ITEM.getInnerColor())
		circle.setStroke(ITEM.getFrameColor())
		circle.setStrokeWidth(0.09375 * ITEM.getSize())
		circle.setStrokeType(StrokeType.CENTERED)
		circle.setEffect(EFFECT)
		return circle
	}

	def private Canvas createCanvasThumbnail(MenuItem ITEM) {
		val Image THUMBNAIL = new Image(ITEM.getThumbnailImageName())
		val double SIZE = if(THUMBNAIL.getWidth() > THUMBNAIL.getHeight()) THUMBNAIL.getWidth() else THUMBNAIL.
				getHeight()
		val double SCALE = (0.7 * ITEM.getSize()) / SIZE
		var Canvas canvasThumbnail = new Canvas(0.7 * ITEM.getSize(), 0.7 * ITEM.getSize())
		var GraphicsContext ctx = canvasThumbnail.getGraphicsContext2D()
		ctx.scale(SCALE, SCALE)
		ctx.drawImage(THUMBNAIL, 0, 0)
		return canvasThumbnail
	}

	def private Timeline createItemOpenTimeLine(StackPane NODE, double X, double Y, double DELAY) {
		var KeyValue kvX1 = new KeyValue(NODE.translateXProperty(), 0, Interpolator.EASE_OUT)
		var KeyValue kvY1 = new KeyValue(NODE.translateYProperty(), 0, Interpolator.EASE_OUT)
		var KeyValue kvR1 = new KeyValue(NODE.rotateProperty(), 0, Interpolator.EASE_OUT)
		var KeyValue kvO1 = new KeyValue(NODE.opacityProperty(), 0, Interpolator.EASE_OUT)
		var KeyValue kvX2 = new KeyValue(NODE.translateXProperty(), 0.0)
		var KeyValue kvY2 = new KeyValue(NODE.translateYProperty(), 0.0)
		var KeyValue kvSx2 = new KeyValue(NODE.scaleXProperty(), 1)
		var KeyValue kvSy2 = new KeyValue(NODE.scaleYProperty(), 1)
		var KeyValue kvX3 = new KeyValue(NODE.translateXProperty(), 1.1 * X, Interpolator.EASE_IN)
		var KeyValue kvY3 = new KeyValue(NODE.translateYProperty(), 1.1 * Y, Interpolator.EASE_IN)
		var KeyValue kvX4 = new KeyValue(NODE.translateXProperty(), 0.95 * X, Interpolator.EASE_OUT)
		var KeyValue kvY4 = new KeyValue(NODE.translateYProperty(), 0.95 * Y, Interpolator.EASE_OUT)
		var KeyValue kvRO4 = new KeyValue(NODE.rotateProperty(), 360)
		var KeyValue kvO4 = new KeyValue(NODE.opacityProperty(), 1.0, Interpolator.EASE_OUT)
		var KeyValue kvX5 = new KeyValue(NODE.translateXProperty(), X)
		var KeyValue kvY5 = new KeyValue(NODE.translateYProperty(), Y)
		var KeyFrame kfO1 = new KeyFrame(Duration.millis(0), kvX1, kvY1, kvR1, kvO1)
		var KeyFrame kfO2 = new KeyFrame(Duration.millis(50 + DELAY), kvX2, kvY2, kvSx2, kvSy2)
		var KeyFrame kfO3 = new KeyFrame(Duration.millis(250 + DELAY), kvX3, kvY3)
		var KeyFrame kfO4 = new KeyFrame(Duration.millis(400 + DELAY), kvX4, kvY4, kvRO4, kvO4)
		var KeyFrame kfO5 = new KeyFrame(Duration.millis(600 + DELAY), kvX5, kvY5)
		return new Timeline(kfO1, kfO2, kfO3, kfO4, kfO5)
	}

	def private Timeline createItemCloseTimeLine(StackPane NODE, double X, double Y, double DELAY) {
		var KeyValue kvX1 = new KeyValue(NODE.translateXProperty(), 0, Interpolator.EASE_OUT)
		var KeyValue kvY1 = new KeyValue(NODE.translateYProperty(), 0, Interpolator.EASE_OUT)
		var KeyValue kvR1 = new KeyValue(NODE.rotateProperty(), 0, Interpolator.EASE_OUT)
		var KeyValue kvO1 = new KeyValue(NODE.opacityProperty(), 0, Interpolator.EASE_OUT)
		var KeyValue kvX2 = new KeyValue(NODE.translateXProperty(), 0.0)
		var KeyValue kvY2 = new KeyValue(NODE.translateYProperty(), 0.0)
		var KeyValue kvX3 = new KeyValue(NODE.translateXProperty(), 1.1 * X, Interpolator.EASE_IN)
		var KeyValue kvY3 = new KeyValue(NODE.translateYProperty(), 1.1 * Y, Interpolator.EASE_IN)
		var KeyValue kvX4 = new KeyValue(NODE.translateXProperty(), 0.95 * X, Interpolator.EASE_OUT)
		var KeyValue kvY4 = new KeyValue(NODE.translateYProperty(), 0.95 * Y, Interpolator.EASE_OUT)
		var KeyValue kvRC4 = new KeyValue(NODE.rotateProperty(), 720)
		var KeyValue kvO4 = new KeyValue(NODE.opacityProperty(), 1.0, Interpolator.EASE_OUT)
		var KeyValue kvX5 = new KeyValue(NODE.translateXProperty(), X)
		var KeyValue kvY5 = new KeyValue(NODE.translateYProperty(), Y)
		var KeyFrame kfC1 = new KeyFrame(Duration.millis(0), kvX5, kvY5)
		var KeyFrame kfC2 = new KeyFrame(Duration.millis(50 + DELAY), kvX4, kvY4, kvRC4, kvO4)
		var KeyFrame kfC3 = new KeyFrame(Duration.millis(250 + DELAY), kvX3, kvY3)
		var KeyFrame kfC4 = new KeyFrame(Duration.millis(400 + DELAY), kvX2, kvY2)
		var KeyFrame kfC5 = new KeyFrame(Duration.millis(600 + DELAY), kvX1, kvY1, kvR1, kvO1)
		return new Timeline(kfC1, kfC2, kfC3, kfC4, kfC5)
	}

	def private void resize() {
		button.setLayoutX((getPrefWidth()) * 0.5)
		button.setLayoutY((getPrefHeight()) * 0.5)
		for (Parent node : items.keySet()) {
			node.setLayoutX((getPrefWidth() - node.getLayoutBounds().getWidth()) * 0.5)
			node.setLayoutY((getPrefHeight() - node.getLayoutBounds().getHeight()) * 0.5)
		}

	}

	// ******************** Event handling ************************************
	def final ObjectProperty<EventHandler<ItemEvent>> onItemSelectedProperty() {
		return onItemSelected
	}

	def final void setOnItemSelected(EventHandler<ItemEvent> value) {
		onItemSelectedProperty().set(value)
	}

	def final EventHandler<ItemEvent> getOnItemSelected() {
		return onItemSelectedProperty().get()
	}

	ObjectProperty<EventHandler<ItemEvent>> onItemSelected = new ObjectPropertyBase<EventHandler<ItemEvent>>() {
		override Object getBean() {
			return this
		}

		override String getName() {
			return "onItemSelected"
		}
	}

	def void fireItemEvent(ItemEvent EVENT) {
		val EventHandler<ItemEvent> HANDLER = getOnItemSelected()
		if (HANDLER !== null) {
			HANDLER.handle(EVENT)
		}

	}

	def final ObjectProperty<EventHandler<MenuEvent>> onMenuOpenStartedProperty() {
		return onMenuOpenStarted
	}

	def final void setOnMenuOpenStarted(EventHandler<MenuEvent> value) {
		onMenuOpenStartedProperty().set(value)
	}

	def final EventHandler<MenuEvent> getOnMenuOpenStarted() {
		return onMenuOpenStartedProperty().get()
	}

	ObjectProperty<EventHandler<MenuEvent>> onMenuOpenStarted = new ObjectPropertyBase<EventHandler<MenuEvent>>() {
		override Object getBean() {
			return this
		}

		override String getName() {
			return "onMenuOpenStarted"
		}
	}

	def final ObjectProperty<EventHandler<MenuEvent>> onMenuOpenFinishedProperty() {
		return onMenuOpenFinished
	}

	def final void setOnMenuOpenFinished(EventHandler<MenuEvent> value) {
		onMenuOpenFinishedProperty().set(value)
	}

	def final EventHandler<MenuEvent> getOnMenuOpenFinished() {
		return onMenuOpenFinishedProperty().get()
	}

	ObjectProperty<EventHandler<MenuEvent>> onMenuOpenFinished = new ObjectPropertyBase<EventHandler<MenuEvent>>() {
		override Object getBean() {
			return this
		}

		override String getName() {
			return "onMenuOpenFinished"
		}
	}

	def final ObjectProperty<EventHandler<MenuEvent>> onMenuCloseStartedProperty() {
		return onMenuCloseStarted
	}

	def final void setOnMenuCloseStarted(EventHandler<MenuEvent> value) {
		onMenuCloseStartedProperty().set(value)
	}

	def final EventHandler<MenuEvent> getOnMenuCloseStarted() {
		return onMenuCloseStartedProperty().get()
	}

	ObjectProperty<EventHandler<MenuEvent>> onMenuCloseStarted = new ObjectPropertyBase<EventHandler<MenuEvent>>() {
		override Object getBean() {
			return this
		}

		override String getName() {
			return "onMenuCloseStarted"
		}
	}

	def final ObjectProperty<EventHandler<MenuEvent>> onMenuCloseFinishedProperty() {
		return onMenuCloseFinished
	}

	def final void setOnMenuCloseFinished(EventHandler<MenuEvent> value) {
		onMenuCloseFinishedProperty().set(value)
	}

	def final EventHandler<MenuEvent> getOnMenuCloseFinished() {
		return onMenuCloseFinishedProperty().get()
	}

	ObjectProperty<EventHandler<MenuEvent>> onMenuCloseFinished = new ObjectPropertyBase<EventHandler<MenuEvent>>() {
		override Object getBean() {
			return this
		}

		override String getName() {
			return "onMenuCloseFinished"
		}
	}

	def void fireMenuEvent(MenuEvent EVENT) {
		val TYPE = EVENT.getEventType()
		val EventHandler<MenuEvent> HANDLER = switch TYPE {
			case MenuEvent.MENU_OPEN_STARTED:
				getOnMenuOpenStarted()
			case MenuEvent.MENU_OPEN_FINISHED:
				getOnMenuOpenFinished()
			case MenuEvent.MENU_CLOSE_STARTED:
				getOnMenuCloseStarted()
			case MenuEvent.MENU_CLOSE_FINISHED:
				getOnMenuCloseFinished()
			default:
				null
		}
		if (HANDLER !== null) {
			HANDLER.handle(EVENT)
		}

	}

	// ******************** Inner classes *************************************
	static class ItemEvent extends Event {
		public static final EventType<ItemEvent> ITEM_SELECTED = new EventType(ANY, "itemSelected")
		MenuItem item

		// ******************** Constructors **********************************
		new(MenuItem ITEM, Object SOURCE, EventTarget TARGET, EventType<ItemEvent> EVENT_TYPE) {
			super(SOURCE, TARGET, EVENT_TYPE)
			item = ITEM
		}

		// ******************** Methods ***************************************
		def final MenuItem getItem() {
			return item
		}
	// ******************** Constructors **********************************
	}

	static class MenuEvent extends Event {
		public static final EventType<MenuEvent> MENU_OPEN_STARTED = new EventType(ANY, "menuOpenStarted")
		public static final EventType<MenuEvent> MENU_OPEN_FINISHED = new EventType(ANY, "menuOpenFinished")
		public static final EventType<MenuEvent> MENU_CLOSE_STARTED = new EventType(ANY, "menuCloseStarted")
		public static final EventType<MenuEvent> MENU_CLOSE_FINISHED = new EventType(ANY, "menuCloseFinished")

		new(Object SOURCE, EventTarget TARGET, EventType<MenuEvent> EVENT_TYPE) {
			super(SOURCE, TARGET, EVENT_TYPE)
		}

	}
}