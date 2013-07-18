package de.fxdiagram.examples.lcars

import com.mongodb.DBObject
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.XNode
import de.fxdiagram.core.export.SvgExporter
import de.fxdiagram.lib.nodes.RectangleBorderPane
import java.util.List
import java.util.Map
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.geometry.Rectangle2D
import javafx.geometry.VPos
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text

import static de.fxdiagram.examples.lcars.LcarsExtensions.*

import static extension de.fxdiagram.core.binding.DoubleExpressionExtensions.*
import static extension javafx.scene.layout.VBox.*

class LcarsNode extends XNode {

	static val PAGE_STRUCTURE = #{ 
		'person' -> #['gender', 'species', 'born', 'status', 'died', 'martial_status'],
		'profession' -> #['occupation', 'affiliation', 'rank', 'serial_number'],
		'family' -> #['spouses', 'children', 'mother', 'father', 'siblings', 'other_relatives']
	}
	
	static val pageOrder = #[
		'person', 'profession', 'family', 'other'
	]
	
	DBObject data 
	String name
	String dbId

	Map<String,List<LcarsField>> pages

	List<String> imageUrls = newArrayList
	String currentImageUrl
	@FxProperty double imageRatio = 0.7

	VBox vbox
	Pane infoTextBox
	ImageView imageView
	
	extension SvgExporter = new SvgExporter
	
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
				children += createBox(DARKBLUE) => [
					vgrow = Priority.ALWAYS
					alignment = Pos.TOP_RIGHT
					children += new Text => [
						text = name
						fill = FLESH
						font = lcarsFont(28)
						HBox.setMargin(it, new Insets(0, 5, 0, 0))
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
				children += new HBox => [
					spacing = 5
					children += infoTextBox = new VBox => [
					]
					children += imageView = new ImageView => [
						fitWidthProperty.bind(widthProperty - 20)
						fitHeightProperty.bind(heightProperty - 50)
						preserveRatio = true
					]
					showImage(imageUrls.last)
					StackPane.setMargin(it, new Insets(5, 8, 5, 5))
				]
				clip = new Rectangle => [
					widthProperty.bind(vbox.widthProperty - 15)
					heightProperty.bind(vbox.heightProperty)
				]
			]
		]
		key = name
	}

	protected def createBox(Color color) {
		new HBox => [
			style = '-fx-background-color: ' + color.toSvgString + ';' 
		]
	}

	protected def createButtonText(String buttonText, VPos alignment) {
		new Text => [
			font = lcarsFont(4)
			text = buttonText
			switch alignment {
				case VPos.TOP: HBox.setMargin(it, new Insets(0,0,5,3))
				case VPos.BOTTOM: HBox.setMargin(it, new Insets(5,0,0,3))
				default: HBox.setMargin(it, new Insets(1,0,1,3))
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

	def showImage(String imageUrl) {
		val imageData = (data.get('images') as List<DBObject>)
			.filter[get('url').toString == imageUrl]
			.head.get('data') as byte[]
		if(imageData != null) {
			val image = LcarsAccess.get().getImage(imageUrl, imageData)
			currentImageUrl = imageUrl
			imageView => [
				it.image = image
				val ratio = image.width / image.height
				if (ratio < imageRatio)
					viewport = new Rectangle2D(
						0,
						0.5 * (image.height - imageRatio / image.width),
						image.width,
						imageRatio / image.width
					)
				else
					viewport = new Rectangle2D(
						0.5 * (image.width - imageRatio * image.height),
						0,
						image.height * imageRatio,
						image.height
					)
			] 
		}
	}
	
	def showPage(String page) {
		val fields = pages.get(page)
		infoTextBox.children.clear
		infoTextBox.children += fields
//		new Timeline => [
//			for(i: 0..fields.length) {
//				keyFrames += new KeyFrame(
//					(i * 100).millis, 
//					new KeyValue(infoTextBox.chtextProperty, text.substring(0, i))
//				)			
//			}
//			play
//		]
	}
	
	override activate() {
		super.activate()
		pages = data.createPages
		vbox => [
			val lastStripe = children.last
			children -= lastStripe
			for(pageTitle: pageOrder.filter[pages.containsKey(it)]) {
				children += createBox(FLESH) => [
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
					]
				]
			}
			if(imageUrls.size > 1) {
				children += createBox(RED) => [
					vgrow = Priority.SOMETIMES
					children += createButtonText("previous pic", VPos.BOTTOM)
					onMousePressed = [
						showImage(imageUrls.get((imageUrls.indexOf(currentImageUrl) + 1) % imageUrls.size))
					]
				]
				children += createBox(RED) => [
					vgrow = Priority.SOMETIMES
					children += createButtonText("next pic", VPos.TOP)
					onMousePressed = [
						showImage(imageUrls.get((imageUrls.indexOf(currentImageUrl) + imageUrls.size - 1) % imageUrls.size))
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
	}
	
	def getDbId() {
		dbId
	}
}
