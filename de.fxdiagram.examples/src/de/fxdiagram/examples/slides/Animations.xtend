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
import javafx.scene.transform.Rotate

import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import static extension java.lang.Math.*

class Animations {
	
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
	
	def static flicker(Shape creature, Color fromColor, Color toColor) {
		new FillTransition => [
			shape = creature 
			fromValue = fromColor
			toValue = toColor
			duration = 100.millis
			cycleCount = -1
			delay = random * 2000.millis
			play
		]
	}
	
	def static breathe(Shape creature, Color fromColor, Color toColor) {
		new SequentialTransition => [
			children += new ParallelTransition => [
				children += new ScaleTransition => [
					fromX = 1
					toX = 1.15
					fromY = 1
					toY = 1.1
					node = creature
					duration = 1800.millis
					delay = 250.millis
				]
				children += new FillTransition => [
					shape = creature 
					fromValue = toColor
					toValue = fromColor
					duration = 1800.millis
					delay = 250.millis
					play
				]
			]
			children += new ParallelTransition => [
				children += new ScaleTransition => [
					fromX = 1.15
					toX = 1
					fromY = 1.1
					toY = 1
					node = creature
					duration = 2500.millis
					delay = 300.millis
				]
				children += new FillTransition => [
					shape = creature 
					fromValue = fromColor
					toValue = toColor
					duration = 2500.millis
					delay = 300.millis
					play
				]
			]
			delay = random * 4000.millis
			cycleCount = -1
			play
		]
	}
	
	def static crawl(Node creature) {
		val stepSize = 20 + 10 * random
		val numSteps = (2 + 4 * random) as int
		new SequentialTransition => [
		 	children += crawlOneWay(creature, stepSize, numSteps)
		 	children += crawlOneWay(creature, -stepSize, numSteps) => [
		 		delay = random * 1.seconds
		 	]
		 	cycleCount = -1
		 	delay = random * 4.seconds
			play
		]
	}
	
	def static crawlOneWay(Node creature, double stepSize, int numSteps) {
		val stepDuration = random * 300.millis + 600.millis
		new SequentialTransition => [
			for(i: 1..numSteps) {
				children += new ParallelTransition => [
					children +=	new ScaleTransition => [
						node = creature
						fromX = 1
						toX = 1.2
						fromY = 1
						toY = 0.9
						duration = 0.8 * stepDuration
						delay = 0.2 * stepDuration
					]
					children += new TranslateTransition => [
						node = creature
						byX = stepSize * cos(creature.rotate.toRadians)
						byY = stepSize * sin(creature.rotate.toRadians)
						duration = 0.8 * stepDuration
						delay = 0.2 * stepDuration
					]
				]
				children += new ParallelTransition => [
					children += new ScaleTransition => [
						node = creature
						fromX = 1.2
						toX = 1
						fromY = 0.9
						toY = 1
						duration = 0.6 * stepDuration
						delay = 0.4 * stepDuration
					]
					children += new TranslateTransition => [
						node = creature
						byX = stepSize * cos(creature.rotate.toRadians)
						byY = stepSize * sin(creature.rotate.toRadians)
						duration = 0.6 * stepDuration
						delay = 0.4 * stepDuration
					]
				]
			}
		]
	}
}

