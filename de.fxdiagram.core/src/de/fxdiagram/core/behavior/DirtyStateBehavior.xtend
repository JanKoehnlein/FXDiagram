package de.fxdiagram.core.behavior

import de.fxdiagram.core.XShape
import javafx.animation.Animation
import javafx.animation.RotateTransition
import javafx.animation.SequentialTransition

import static extension de.fxdiagram.core.extensions.DurationExtensions.*

interface DirtyStateBehavior extends Behavior {
	def DirtyState getDirtyState()
	
	def void showDirtyState(DirtyState state)
}

abstract class AbstractDirtyStateBehavior<T extends XShape> extends AbstractHostBehavior<T> implements DirtyStateBehavior {
	
	Animation modifiedAnimation
	
	new(T host) {
		super(host)
		modifiedAnimation = new SequentialTransition => [
			children += new RotateTransition => [
				fromAngle = -5
				toAngle = 5
				duration = 170.millis
				cycleCount = Animation.INDEFINITE
				autoReverse = true 
				node = host
			]
		]
	}
	
	override getBehaviorKey() {
		DirtyStateBehavior
	}
	
	override protected doActivate() {
		showDirtyState(dirtyState)
	}
	
	override showDirtyState(DirtyState state) {
		switch state {
			case CLEAN: showAsClean
			case DIRTY: showAsDirty
			case DANGLING: showAsDangling
		}
	}
	
	protected def void showAsClean() {
		host.opacity = 1
		modifiedAnimation.stop
		host.rotate = 0
	}
	
	protected def void showAsDirty() {
		host.opacity = 1
		modifiedAnimation.play
	}
	
	protected def void showAsDangling() {
		host.node.opacity = 0.5
		modifiedAnimation.stop
		host.rotate = 0
	}
}

enum DirtyState {
	CLEAN, DIRTY, DANGLING
} 

