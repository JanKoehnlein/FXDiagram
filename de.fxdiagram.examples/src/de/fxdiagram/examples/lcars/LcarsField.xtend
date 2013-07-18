package de.fxdiagram.examples.lcars

import de.fxdiagram.core.tools.chooser.CoverFlowChooser
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.layout.FlowPane
import javafx.scene.text.Text

import static de.fxdiagram.examples.lcars.LcarsExtensions.*

import static extension de.fxdiagram.core.Extensions.*
import javafx.concurrent.Task
import javafx.application.Platform

class LcarsField extends Parent {

	FlowPane flowPane

	new(LcarsNode host, String name, String value) {
		children += flowPane = new FlowPane => [
			prefWrapLength = 150
			children += new Text => [
				text = name + ": "
				font = lcarsFont(12)
				fill = FLESH
			]
			var currentWord = ''
			for (c : value.toCharArray) {
				currentWord = currentWord + c
				if (splitAt(c)) {
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
				val textNodes = flowPane.children.filter(Text)
				textNodes.forEach[fill = RED]
				new LcarsQueryTask(host, name, value).run
			]
			onMouseReleased = [
				val textNodes = flowPane.children.filter(Text)
				textNodes.head.fill = FLESH
				textNodes.tail.forEach[fill = ORANGE]
			]
		]
	}

	protected def splitAt(char c) {
		Character.isWhitespace(c)
	}

}

class LcarsQueryTask extends Task<Void> {
	
	LcarsNode host
	String fieldName
	String fieldValue
	
	new(LcarsNode host, String fieldName, String fieldValue) {
		this.host = host
		this.fieldName = fieldName
		this.fieldValue = fieldValue
	}
	
	override protected call() throws Exception {
		val siblings = LcarsAccess.get.query(fieldName, fieldValue)
		val chooser = new CoverFlowChooser(host, Pos.BOTTOM_CENTER)
		chooser += siblings.filter[get('_id').toString != host.dbId].map[
			new LcarsNode(it) => [
				width = host.width
				height = host.height
			]]
		Platform.runLater [|
			host.rootDiagram.currentTool = chooser
		]
		null
	}
	
}
