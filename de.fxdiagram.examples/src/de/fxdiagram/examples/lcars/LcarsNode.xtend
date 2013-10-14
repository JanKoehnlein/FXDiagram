package de.fxdiagram.examples.lcars

import com.mongodb.DBObject
import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.XNode
import de.fxdiagram.core.services.ImageCache
import de.fxdiagram.lib.nodes.RectangleBorderPane
import java.util.List
import java.util.Map
import javafx.animation.Timeline
import javafx.beans.value.ChangeListener
import javafx.geometry.Bounds
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.geometry.Rectangle2D
import javafx.geometry.VPos
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text

import static de.fxdiagram.examples.lcars.LcarsExtensions.*

import static extension de.fxdiagram.core.extensions.DoubleExpressionExtensions.*
import static extension de.fxdiagram.core.extensions.TooltipExtensions.*
import static extension javafx.scene.layout.VBox.*

@Logging
class LcarsNode extends XNode {

	static val PAGE_STRUCTURE = #{ 
		'person' -> #['gender', 'species', 'born', 'status', 'died', 'marital_status'],
		'profession' -> #['occupation', 'affiliation', 'rank', 'serial_number'],
		'family' -> #['spouses', 'children', 'mother', 'father', 'siblings', 'other_relatives']
	}
	
	static val pageOrder = #[
		'person', 'profession', 'family', 'other'
	]
	
	extension NameShortener = new NameShortener()
	
	DBObject data 
	String name
	String dbId

	Map<String,List<LcarsField>> pages

	List<String> imageUrls = newArrayList
	String currentImageUrl
	@FxProperty double imageRatio = 0.7

	VBox vbox
	HBox infoBox
	Text nameField
	Pane infoTextBox
	ImageView imageView
	ChangeListener<Bounds> nameShortener
	
	new(DBObject data) {
		this.data = data
		this.name = data.get("name").toString
		this.dbId = data.get('_id').toString
		imageUrls = (data.get("images") as List<DBObject>).map[get('url').toString]
		vbox = new VBox
		node = new RectangleBorderPane => [
			backgroundRadius = 0
			borderRadius = 0
			backgroundPaint = Color.BLACK
			borderPaint = Color.BLACK 
			children += vbox => [
				spacing = 2
				fillWidth = true
				children += new HBox => [
					vgrow = Priority.ALWAYS
					children += createBox(DARKBLUE) => [
						alignment = Pos.TOP_LEFT
						HBox.setHgrow(it, Priority.ALWAYS)
					]
					nameField = new Text 
					children += nameField => [
						text = name
						fill = FLESH
						font = lcarsFont(28)
						HBox.setMargin(it, new Insets(0, 0, 0, 5))
					]
				]
				children += createBox(VIOLET) => [
					vgrow = Priority.ALWAYS
				]
				clip = new Rectangle => [
					x = 0
					y = 0
					arcHeight = 20
					arcWidth = 20
					widthProperty.bind(vbox.widthProperty + 20)
					heightProperty.bind(vbox.heightProperty)
				]
				StackPane.setMargin(it, new Insets(5, 5, 5, 5))
			]
			
			children += new RectangleBorderPane => [
				backgroundPaint = Color.BLACK
				backgroundRadius = 8
				borderPaint = Color.BLACK
				borderRadius = 8
				StackPane.setMargin(it, new Insets(35, -3, 10, 25))
				infoBox = new HBox
				children += infoBox => [
					spacing = 5
					children += infoTextBox = new VBox => [
					]
					children += imageView = new ImageView => [
						fitWidthProperty.bind(widthProperty - 20)
						fitHeightProperty.bind(heightProperty - 50)
						preserveRatio = true
					]
					showImage(imageUrls.last)
					StackPane.setMargin(it, new Insets(5, 6, 5, 5))
				]
				clip = new Rectangle => [
					widthProperty.bind(vbox.widthProperty - 15)
					heightProperty.bind(vbox.heightProperty)
				]
			]
			tooltip = '''
				Click on a property to connect with equivalents,
				Right-click to add new equivalents.'''
		]
		key = name
		nameShortener = [ 
			property, oldValue, newValue | 
			while(nameField.boundsInLocal.width > newValue.width) 
				nameField.text = shortenName(nameField.text)
		]
		infoBox.boundsInLocalProperty.addListener(nameShortener) 
	}

	protected def createBox(Color color) {
		new RectangleBorderPane => [
			borderPaint = color
			backgroundPaint = color
			borderRadius = 0
			backgroundRadius = 0
			setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE)
			alignment = Pos.CENTER_LEFT
		]
	}

	protected def createButtonText(String buttonText, VPos alignment) {
		new Text => [
			font = lcarsFont(4)
			text = buttonText
			fill = Color.BLACK
			textOrigin = VPos.TOP
			switch alignment {
				case VPos.TOP: StackPane.setMargin(it, new Insets(0,0,5,3))
				case VPos.BOTTOM: StackPane.setMargin(it, new Insets(5,0,0,3))
				default: StackPane.setMargin(it, new Insets(1,0,1,3))
			}
		]
	}

	protected def createPages(DBObject data) {
		val pages = <String, List<LcarsField>> newLinkedHashMap
		val handledKeys = newHashSet
		handledKeys.addAll('name', '_id', 'images')
		for(pageTitle: PAGE_STRUCTURE.keySet) {
			val page = data.createPageText(PAGE_STRUCTURE.get(pageTitle))
			if(!page.empty) 
				pages.put(pageTitle, page)
			handledKeys.addAll(PAGE_STRUCTURE.get(pageTitle))
		}
		val otherKeys = data.keySet.filter[!handledKeys.contains(it)]
		if(!otherKeys.empty)
			pages.put('other', data.createPageText(otherKeys))
		pages
	}
	
	protected def createPageText(DBObject data, Iterable<String> keys) {
		val fields = newArrayList		
		for(key: keys) {
			if(data.containsField(key)) 
				fields += new LcarsField(this, key, data.get(key).toString)
		}
		fields	
	}

	def void showImage(String imageUrl) {
		val imageCache = ImageCache.get
		val image = imageCache.getImage(imageUrl, [|
			(data.get('images') as List<DBObject>)
				.filter[get('url').toString == imageUrl]
				.head.get('data') as byte[] 
		])
		if(image != null) {	
			currentImageUrl = imageUrl
			imageView => [
				it.image = image
				val ratio = image.width / image.height
				if (ratio < imageRatio) {
					val newHeight = image.width / imageRatio
					viewport = new Rectangle2D(
						0,
						0.5 * (image.height - newHeight),
						image.width,
						newHeight
					)
				}
				else {
					val newWidth = imageRatio * image.height
					viewport = new Rectangle2D(
						0.5 * (image.width - newWidth),
						0,
						newWidth,
						image.height
					)
				}
			] 
		}
	}
	
	def showPage(String page) {
		val fields = pages.get(page)
		infoTextBox.children.clear
		infoTextBox.children += fields
		val timeline = new Timeline
		fields.forEach[addAnimation(timeline)]
		timeline.play
	}
	
	override doActivate() {
		super.doActivate()
		infoBox.boundsInLocalProperty.removeListener(nameShortener) 
		nameField.text = name
		pages = data.createPages
		vbox => [
			val lastStripe = children.last
			children -= lastStripe
			for(pageTitle: pageOrder.filter[pages.containsKey(it)]) {
				val box = createBox(FLESH) 
				children += box => [
					vgrow = Priority.SOMETIMES
					children += createButtonText(
						pageTitle, 
						switch pageOrder.indexOf(pageTitle) {
							case 0: VPos.BOTTOM
							case pages.keySet.size-1: VPos.TOP
							default: VPos.CENTER
						})
					onMousePressed = [
						showPage(pageTitle)
						invertColors(box)
					]
					onMouseReleased = [
						invertColors(box)
					]
				]
			}
			if(imageUrls.size > 1) {
				val previous_box = createBox(RED)
				children += previous_box => [
					vgrow = Priority.SOMETIMES
					children += createButtonText("previous pic", VPos.BOTTOM)
					onMousePressed = [
						showImage(imageUrls.get((imageUrls.indexOf(currentImageUrl) + 1) % imageUrls.size))
						invertColors(previous_box)
					]
					onMouseReleased = [
						invertColors(previous_box)
					]
				]
				val next_box = createBox(RED)
				children += next_box => [
					vgrow = Priority.SOMETIMES
					children += createButtonText("next pic", VPos.TOP)
					onMousePressed = [
						showImage(imageUrls.get((imageUrls.indexOf(currentImageUrl) + imageUrls.size - 1) % imageUrls.size))
						invertColors(next_box)
					]
					onMouseReleased = [
						invertColors(next_box)
					]
				]
			} else {
				children += createBox(RED) => [
					vgrow = Priority.ALWAYS
				]
			}
			children += lastStripe
		]
		showPage(pages.keySet.iterator.next)
		infoBox.boundsInLocalProperty.addListener(nameShortener) 
	}
	
	protected def invertColors(RectangleBorderPane box) {
		val text = box.children.head as Text
		val textColor = text.fill
		text.fill = box.backgroundPaint
		box.backgroundPaint = textColor
		box.borderPaint = textColor
	}
	
	def getDbId() {
		dbId
	}
	
	def getData() {
		data
	}
	
	override selectionFeedback(boolean isSelected) {
		(outgoingConnections + incomingConnections).forEach[toFront]
	}
}
