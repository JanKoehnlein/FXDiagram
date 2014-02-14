package de.fxdiagram.examples

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionKind
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.lib.simple.AddRapidButtonBehavior
import de.fxdiagram.lib.simple.LevelOfDetailDiagramNode
import de.fxdiagram.lib.simple.OpenableDiagramNode
import de.fxdiagram.lib.simple.SimpleNode
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.annotations.properties.FxProperty

@ModelNode(#['nodes', 'connections', 'parentDiagram', 'nameSuffix'])
class LazyExampleDiagram extends XDiagram {
	
	@FxProperty String nameSuffix
	
	new(String nameSuffix) {
		this.nameSuffix = nameSuffix	
	}
	
	override doActivate() {
		if(nodes.empty) {
			contentsInitializer = [
				val simple = newSimpleNode(nameSuffix)
				val openable = newOpenableDiagramNode(nameSuffix)
				val levelOfDetail = newEmbeddedDiagramNode(nameSuffix) 
				nodes += simple => [
					layoutX = 75
					layoutY = 50
				]
				nodes += openable => [
					layoutX = 350
					layoutY = 150
				]
				nodes += levelOfDetail => [
					layoutX = 50
					layoutY = 300
				]
				connections += new XConnection(simple, openable) => [
					new XConnectionLabel(it) => [
						text.text = 'polyline'
					]
				]
				connections += new XConnection(openable, levelOfDetail) => [
					kind = XConnectionKind.QUAD_CURVE
					new XConnectionLabel(it) => [
						text.text = 'quadratic'
					]
				]
				connections += new XConnection(simple, levelOfDetail) => [
					kind = XConnectionKind.CUBIC_CURVE
					new XConnectionLabel(it) => [
						text.text = 'cubic'
					]
				]
			]
		} else {
			nodes.forEach[
				if(!nameSuffix.empty || !(node instanceof SimpleNode))
					addRapidButtons(nameSuffix)
			]
		}
		super.doActivate()
	}
	
	protected def void addRapidButtons(XNode node, String nameSuffix) {
		node.addBehavior(new AddRapidButtonBehavior(node) => [
			choiceInitializer = [
				for(i: 5..20) 
					addChoice(newSimpleNode(' ' + i))
				addChoice(newSimpleNode(nameSuffix))
				addChoice(newOpenableDiagramNode(nameSuffix))
				addChoice(newEmbeddedDiagramNode(nameSuffix))
				for(i: 1..4) 
					addChoice(newSimpleNode(' ' + i))
				addChoice(newSimpleNode(nameSuffix))
			]
		])
	}
		
	def newSimpleNode(String nameSuffix) {
		new SimpleNode('Node' + nameSuffix) => [
			if(!nameSuffix.empty)
				addRapidButtons(nameSuffix)
		]
	}
	
	def newOpenableDiagramNode(String nameSuffix) {
		new OpenableDiagramNode('Nested' + nameSuffix) => [
			innerDiagram = new LazyExampleDiagram(nameSuffix + " (n)")
			addRapidButtons(nameSuffix)
		]
	}
	
	def newEmbeddedDiagramNode(String nameSuffix) {
		new LevelOfDetailDiagramNode('Embedded' + nameSuffix) => [
			innerDiagram = new LazyExampleDiagram(nameSuffix + " (e)")
			addRapidButtons(nameSuffix)
		]
	}
	
}