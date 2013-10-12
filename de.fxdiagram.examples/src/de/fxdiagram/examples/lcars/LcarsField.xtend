package de.fxdiagram.examples.lcars

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.anchors.TriangleArrowHead
import de.fxdiagram.core.tools.actions.LayoutAction
import de.fxdiagram.lib.tools.CoverFlowChooser
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.application.Platform
import javafx.concurrent.Service
import javafx.concurrent.Task
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.input.MouseButton
import javafx.scene.layout.FlowPane
import javafx.scene.text.Text

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.examples.lcars.LcarsExtensions.*
import static extension javafx.util.Duration.*
import de.fxdiagram.core.tools.ChooserConnectionProvider

class LcarsField extends Parent {

	FlowPane flowPane
	
	LcarsNode node
	
	new(LcarsNode node, String name, String value) {
		this.node = node
		val ChooserConnectionProvider connectionProvider = [ 
			host,  choice, choiceInfo | 
			new XConnection(host, choice) => [
				sourceArrowHead = new TriangleArrowHead(it, true)
				new XConnectionLabel(it) => [
					text.text = name.replace('_', ' ')
				]
			]
		]
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
				if(button != MouseButton.PRIMARY) {
					val Service<Void> service = [|
						new LcarsQueryTask(this, name, value, connectionProvider)
					]
					service.start
				}
			]
			onMouseReleased = [
				if(button == MouseButton.PRIMARY) {
					val newConnections  = diagram.nodes
						.filter(LcarsNode)
						.filter[
							it != node && data.get(name)==value 
							&& !outgoingConnections.exists[target == node && label?.text?.text == name]
							&& !incomingConnections.exists[source == node && label?.text?.text == name]
						]
						.map[connectionProvider.getConnection(node, it, null)]
						.toList
					diagram.connections += newConnections
					if(!newConnections.empty)
						new LayoutAction().perform(root)
					resetColors
				}
			]
		]
	}
	
	def getLcarsNode() {
		node
	}

	protected def getAllTextNodes() {
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
						cycleDuration.add(15.millis), 
						new KeyValue(textNode.textProperty, textNode.text.substring(0, index + 1))
					)			
				}
				textNode.text = ''
			}
		]
		timeline
	}
	
	def resetColors() {
		allTextNodes.head.fill = FLESH
		allTextNodes.tail.forEach[fill = ORANGE]
	}
}

class LcarsQueryTask extends Task<Void> {
	
	LcarsField host
	String fieldName
	String fieldValue
	ChooserConnectionProvider connectionProvider
	
	new(LcarsField host, String fieldName, String fieldValue, ChooserConnectionProvider connectionProvider) {
		this.host = host
		this.fieldName = fieldName
		this.fieldValue = fieldValue
		this.connectionProvider = connectionProvider
	}
	
	override protected call() throws Exception {
		val siblings = host.lcarsDiagram.lcarsAccess.query(fieldName, fieldValue)
		val lcarsNode = host.lcarsNode
		val chooser = new CoverFlowChooser(lcarsNode, Pos.BOTTOM_CENTER)
		chooser.connectionProvider = connectionProvider
		siblings
			.filter[get('_id').toString != lcarsNode.dbId]
			.forEach[
				chooser.addChoice(new LcarsNode(it) => [
					width = lcarsNode.width
					height = lcarsNode.height
				])
			]
		Platform.runLater [|
			host.root.currentTool = chooser
			host.resetColors
		]
		null
	}
	
}
