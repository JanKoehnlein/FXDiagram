package de.fxdiagram.examples.slides

import javafx.animation.FillTransition
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.ParallelTransition
import javafx.animation.ScaleTransition
import javafx.animation.SequentialTransition
import javafx.animation.Timeline
import javafx.animation.TranslateTransition
import javafx.geometry.Point3D
import javafx.scene.Node
import javafx.scene.paint.Color
import javafx.scene.shape.Shape
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import javafx.scene.transform.Rotate

import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import static extension java.lang.Math.*

class Styles {
	
	static def createText(String text, int fontSize) {
		createText(text, 'Gill Sans', fontSize)
	}
	
	static def createJungleText(String text, int fontSize) {
		createText(text, 'Chalkduster', fontSize)
	}
	
	static def createText(String text, String fontName, int fontSize) {
		new Text => [
			it.text = text.trim
			it.textAlignment = TextAlignment.CENTER
			it.font = new Font(fontName, fontSize)
			it.fill = jungleGreen()  
		]
	}
	
	def static jungleGreen() {
		Color.rgb(224, 237, 214)
	}
	
	def static jungleDarkGreen() {
		Color.rgb(161, 171, 74)
	}
	
	def static dangle(Node creature) {
		val transform = new Rotate => [
			axis = new Point3D(0,0,1)
		]
		creature.transforms.add(transform) 
		new Timeline => [
			for(i: 0..10) 
				keyFrames += new KeyFrame(i * 130.millis, new KeyValue(transform.angleProperty, interpolateAngle(i/10.0)))
			cycleCount = -1
			autoReverse = true
			delay = random * 1000.millis
			play
		]
	}
	
	protected static def interpolateAngle(double alpha) {
		90 + 10 * sin(alpha*PI - PI/2)
	}
	
	def static flicker(Shape creature) {
		new FillTransition => [
			shape = creature 
			fromValue = jungleDarkGreen
			toValue = Color.rgb(107, 114, 51)
			duration = 300.millis
			cycleCount = -1
			delay = random * 2000.millis
			play
		]
	}
	
	def static breathe(Node creature) {
		new SequentialTransition => [
			children += new ScaleTransition => [
				fromX = 1
				toX = 1.15
				fromY = 1
				toY = 1.1
				node = creature
				duration = 1000.millis
				delay = 200.millis
			]
			children += new ScaleTransition => [
				fromX = 1.15
				toX = 1
				fromY = 1.1
				toY = 1
				node = creature
				duration = 1500.millis
				delay = 300.millis
			]
			delay = random * 1000.millis
			cycleCount = -1
			play
		]
	}
	
	def static crawl(Node creature) {
		val stepSize = 20 + 10 * random
		new SequentialTransition => [
		 	children += crawlOneWay(creature, stepSize)
		 	children += crawlOneWay(creature, -stepSize)
		 	cycleCount = -1
		 	delay = random * 4.seconds
			play
		]
	}
	
	def static crawlOneWay(Node creature, double stepSize) {
		new SequentialTransition => [
			for(i: 1..6) {
				children += new ParallelTransition => [
					children +=	new ScaleTransition => [
						node = creature
						fromX = 1
						toX = 1.2
						fromY = 1
						toY = 0.9
						duration = 400.millis
						delay = 100.millis
					]
					children += new TranslateTransition => [
						node = creature
						byX = stepSize * cos(creature.rotate.toRadians)
						byY = stepSize * sin(creature.rotate.toRadians)
						duration = 400.millis
						delay = 100.millis
					]
				]
				children += new ParallelTransition => [
					children += new ScaleTransition => [
						node = creature
						fromX = 1.2
						toX = 1
						fromY = 0.9
						toY = 1
						duration = 300.millis
						delay = 200.millis
					]
					children += new TranslateTransition => [
						node = creature
						byX = stepSize * cos(creature.rotate.toRadians)
						byY = stepSize * sin(creature.rotate.toRadians)
						duration = 300.millis
						delay = 200.millis
					]
				]
			}
		]
	}

}

