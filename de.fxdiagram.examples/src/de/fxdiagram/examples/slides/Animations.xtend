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
import javafx.animation.RotateTransition
import javafx.animation.Interpolator
import javafx.animation.PathTransition
import javafx.scene.shape.Path
import javafx.scene.shape.CubicCurveTo
import javafx.scene.shape.MoveTo
import javafx.util.Duration

/**
 * Some fancy animations for elements on a slide.
 */
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
	
	static def orbit(Node creature, double radiusX, double radiusY, Duration cycleTime, double initialAngle) {
		new ParallelTransition => [
			children += new RotateTransition => [
				node = creature
				fromAngle = initialAngle
				toAngle = initialAngle + 360
				axis = new Point3D(0,1,0)
				cycleCount = -1
				duration = 5.seconds
				delay = random * 5.seconds
				interpolator = Interpolator.LINEAR
			]
			children += new PathTransition => [
				node = creature
				path = new Path => [
					elements += new MoveTo(-radiusX, 0)
					elements += new CubicCurveTo() => [
						controlX1 = -radiusX
						controlY1 = 0.5 * radiusY 
						controlX2 = - 0.5 * radiusX
						controlY2 = radiusY
						x = 0 
						y = radiusY
					]
					elements += new CubicCurveTo() => [
						controlX1 = 0.5 * radiusX
						controlY1 = radiusY
						controlX2 = radiusX
						controlY2 = 0.5 * radiusY 
						x = radiusX
						y = 0 
					]
					elements += new CubicCurveTo() => [
						controlX1 = radiusX
						controlY1 = - 0.5 * radiusY
						controlX2 = 0.5 * radiusX
						controlY2 = -radiusY 
						x = 0
						y = -radiusY 
					]
					elements += new CubicCurveTo() => [
						controlX1 = -0.5 * radiusX
						controlY1 = -radiusY
						controlX2 = -radiusX
						controlY2 = - 0.5 *radiusY 
						x = -radiusX
						y = 0 
					]
				]
				duration = cycleTime
				delay = cycleTime * initialAngle / 360.0
				interpolator = Interpolator.LINEAR
				cycleCount = -1 
			]
			cycleCount = -1
			play
		]
	}

	static def spin(Node creature) {
		new RotateTransition => [
			node = creature
			fromAngle = 0
			toAngle = 360
			axis = new Point3D(0.7,0.8,1)
			cycleCount = -1
			duration = 7.seconds
			interpolator = Interpolator.LINEAR
			play
		]
	}

	public static def warpOut(Node creature, double stepSize) {
		val stepDuration = random * 300.millis + 600.millis
		new SequentialTransition => [
			children += new ParallelTransition => [
				children +=	new ScaleTransition => [
					node = creature
					fromX = 1
					toX = 1.8
					fromY = 1
					toY = 0.3
					duration = 0.8 * stepDuration
					delay = 0.2 * stepDuration
				]
				children += new TranslateTransition => [
					node = creature
					byX = signum(stepSize) * 0.8 * creature.prefWidth(-1) * cos(creature.rotate.toRadians)
					byY = signum(stepSize) * 0.8 * creature.prefWidth(-1) * sin(creature.rotate.toRadians)
					duration = 0.8 * stepDuration
					delay = 0.2 * stepDuration
				]
			]
			children += new TranslateTransition => [
				node = creature
				byX = stepSize * cos(creature.rotate.toRadians)
				byY = stepSize * sin(creature.rotate.toRadians)
				duration = 0.2 * stepDuration
				delay = 0.2 * stepDuration
			]
			children += new ScaleTransition => [
				toX = 1
				toY = 1
				duration = 0.seconds
			]
		]
	}
	
	protected static def warpIn(Node creature, double stepSize) {
		val stepDuration = random * 300.millis + 600.millis
		new SequentialTransition => [
			children += new ScaleTransition => [
				fromX = 1.8
				toX = 1
				fromY = 0.3
				toY = 1
				duration = 0.seconds
			]
			children += new TranslateTransition => [
				node = creature
				fromX = creature.layoutX - stepSize * cos(creature.rotate.toRadians)
				fromY = creature.layoutY - stepSize * sin(creature.rotate.toRadians)
				byX = stepSize * cos(creature.rotate.toRadians)
				byY = stepSize * sin(creature.rotate.toRadians)
				duration = 0.2 * stepDuration
				delay = 0.2 * stepDuration
			]
			children += new ParallelTransition => [
				children +=	new ScaleTransition => [
					node = creature
					fromX = 1.8
					toX = 1
					fromY = 0.6
					toY = 1
					duration = 0.8 * stepDuration
				]
				children += new TranslateTransition => [
					node = creature
					byX = signum(stepSize) * 0.8 * creature.prefWidth(-1) * cos(creature.rotate.toRadians)
					byY = signum(stepSize) * 0.8 * creature.prefWidth(-1) * sin(creature.rotate.toRadians)
					duration = 0.8 * stepDuration
				]
			]
		]
	}
}

