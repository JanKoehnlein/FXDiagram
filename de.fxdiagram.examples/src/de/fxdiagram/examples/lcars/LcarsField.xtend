package de.fxdiagram.examples.lcars

import de.fxdiagram.lib.tools.CoverFlowChooser
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.application.Platform
import javafx.concurrent.Service
import javafx.concurrent.Task
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.layout.FlowPane
import javafx.scene.text.Text

import static extension de.fxdiagram.examples.lcars.LcarsExtensions.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension javafx.util.Duration.*

class LcarsField extends Parent {

	FlowPane flowPane
	
	LcarsNode node

	new(LcarsNode node, String name, String value) {
		this.node = node
		children += flowPane = new FlowPane => [
			prefWrapLength = 150
			children += new Text => [
				text = name.replace('_', ' ') + ": "
				font = lcarsFont(12)
				fill = FLESH
			]
			var currentWord = ''
			for (c : value.toCharArray) {
				currentWord = currentWord + c
				if (c.isSplitHere) {
					children += new Text(currentWord) => [
						font = lcarsFont(12)
						fill = ORANGE
					]
					currentWord = ''
				}
			}
			if (!currentWord.empty) {
				children += new Text(currentWord) => [
					font = lcarsFont(12)
					fill = ORANGE
				]
			}
			onMousePressed = [
				allTextNodes.forEach[fill = RED]
				val Service<Void> service = [|
					new LcarsQueryTask(this, name, value)
				]
				service.start
			]
			onMouseReleased = [
			]
		]
	}
	
	def getLcarsNode() {
		node
	}

	def getAllTextNodes() {
		flowPane.children.filter(Text)
	}

	protected def isSplitHere(char c) {
		Character.isWhitespace(c)
	}
	
	def addAnimation(Timeline timeline) {
		timeline => [
			for(textNode: allTextNodes) {
				for(index: 0..<textNode.text.length) {
					keyFrames += new KeyFrame(
						cycleDuration.add(20.millis), 
						new KeyValue(textNode.textProperty, textNode.text.substring(0, index + 1))
					)			
				}
				textNode.text = ''
			}
		]
		timeline
	}
}

class LcarsQueryTask extends Task<Void> {
	
	LcarsField host
	String fieldName
	String fieldValue
	
	new(LcarsField host, String fieldName, String fieldValue) {
		this.host = host
		this.fieldName = fieldName
		this.fieldValue = fieldValue
	}
	
	override protected call() throws Exception {
		val siblings = host.lcarsDiagram.lcarsAccess.query(fieldName, fieldValue)
		val lcarsNode = host.lcarsNode
		val chooser = new CoverFlowChooser(lcarsNode, Pos.BOTTOM_CENTER)
		chooser += siblings
			.filter[get('_id').toString != lcarsNode.dbId]
			.map[
				new LcarsNode(it) => [
					width = lcarsNode.width
					height = lcarsNode.height
				]
			]
		Platform.runLater [|
			host.root.currentTool = chooser
			host.allTextNodes.head.fill = FLESH
			host.allTextNodes.tail.forEach[fill = ORANGE]
		]
		null
	}
	
}
