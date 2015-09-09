package de.fxdiagram.core.behavior

import de.fxdiagram.core.XShape
import de.fxdiagram.core.command.AnimationCommand

interface DirtyStateBehavior extends Behavior {
	def DirtyState getDirtyState()
	
	def void showDirtyState(DirtyState state)
	
	def void update(UpdateAcceptor acceptor) 
}

interface UpdateAcceptor {
	def void delete(XShape shape)
	def void add(XShape shape)
	def void morph(AnimationCommand command)
}

abstract class AbstractDirtyStateBehavior<T extends XShape> extends AbstractHostBehavior<T> implements DirtyStateBehavior {
	
	DirtyState shownState = DirtyState.CLEAN
	
	new(T host) {
		super(host)
	}
	
	override getBehaviorKey() {
		DirtyStateBehavior
	}
	
	override protected doActivate() {
		showDirtyState(dirtyState)
	}
	
	override showDirtyState(DirtyState state) {
		feedback(false)
		shownState = state
		feedback(true)
	}
	
	protected def void feedback(boolean show) {
		switch shownState {
			case CLEAN: cleanFeedback(show)
			case DIRTY: dirtyFeedback(show)
			case DANGLING: danglingFeedback(show)
		}
	}
	
	protected def void cleanFeedback(boolean isClean) {}
	
	protected def void dirtyFeedback(boolean isDirty) {}
	
	protected def void danglingFeedback(boolean isDangling) {
		if(isDangling)
			host.opacity = 0.2
		else
			host.opacity = 1
	}
}

enum DirtyState {
	CLEAN, DIRTY, DANGLING
} 

