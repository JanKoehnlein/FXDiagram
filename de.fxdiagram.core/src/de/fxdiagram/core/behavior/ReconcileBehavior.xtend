package de.fxdiagram.core.behavior

import de.fxdiagram.core.XShape
import de.fxdiagram.core.command.AnimationCommand
import javafx.animation.Animation
import javafx.animation.FadeTransition
import javafx.animation.SequentialTransition

import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import javafx.scene.Node
import org.eclipse.xtend.lib.annotations.Accessors

/**
 * A behvaior to compare the shown state of a shape with its current domain model.
 */
interface ReconcileBehavior extends Behavior {
	def DirtyState getDirtyState()
	
	def void showDirtyState(DirtyState dirtyState)

	def void hideDirtyState()
	
	def void reconcile(UpdateAcceptor acceptor)
	 
	interface UpdateAcceptor {
		def void delete(XShape shape)
		def void add(XShape shape)
		def void morph(AnimationCommand command)
	}
}


abstract class AbstractReconcileBehavior<T extends Node> extends AbstractHostBehavior<T> implements ReconcileBehavior {
	
	@Accessors(PUBLIC_GETTER)
	DirtyState shownState = DirtyState.CLEAN
	
	Animation dirtyAnimation
	
	new(T host) {
		super(host)
	}
	
	override getBehaviorKey() {
		ReconcileBehavior
	}
	
	override protected doActivate() {
		dirtyAnimation = new SequentialTransition => [
			children += new FadeTransition => [
				node = host
				duration = 300.millis
				fromValue = 1
				toValue = 0.2
			]
			children += new FadeTransition => [
				node = host
				duration = 300.millis
				fromValue = 0.2
				toValue = 1
			]
			cycleCount = Animation.INDEFINITE
		]
		showDirtyState(dirtyState)
	}
	
	override showDirtyState(DirtyState state) {
		feedback(false)
		shownState = state
		feedback(true)
	}
	
	override hideDirtyState() {
		feedback(false)
	}
	
	protected def void feedback(boolean show) {
		switch shownState {
			case CLEAN: cleanFeedback(show)
			case DIRTY: dirtyFeedback(show)
			case DANGLING: danglingFeedback(show)
		}
	}
	
	protected def void cleanFeedback(boolean isClean) {}
	
	protected def void dirtyFeedback(boolean isDirty) {
		if(isDirty) {
			dirtyAnimation.play
		} else {
			dirtyAnimation.stop
			host.opacity = 1			
		}
	}
	
	protected def void danglingFeedback(boolean isDangling) {
		if(isDangling)
			host.opacity = 0.2
		else
			host.opacity = 1
	}
}

/**
 * Describes whether a shape is in sync with its domain model.
 */
enum DirtyState {
	CLEAN,    // shape is in sync with domain model
	DIRTY,    // shape is out of sync but can be reconciled automatically  
	DANGLING  // shape is out of sync and cannot be reconciled
} 

