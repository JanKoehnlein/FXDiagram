package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot
import de.fxdiagram.core.behavior.Behavior
import de.fxdiagram.core.behavior.CloseBehavior
import de.fxdiagram.core.behavior.NavigationBehavior
import de.fxdiagram.core.behavior.OpenBehavior

import static extension de.fxdiagram.core.tools.actions.BehaviorProvider.*

class NavigateNextAction implements DiagramAction {
	override perform(XRoot root) {
		root.triggerBehavior(NavigationBehavior, [next])
	}
}

class NavigatePreviousAction implements DiagramAction {
	override perform(XRoot root) {
		root.triggerBehavior(NavigationBehavior, [previous])
	}
}

class CloseAction implements DiagramAction {
	override perform(XRoot root) {
		root.triggerBehavior(CloseBehavior, [ close; true ])
	}
}

class OpenAction implements DiagramAction {
	override perform(XRoot root) {
		val selectedNodes = root.diagram.nodes.filter[selected]
		if(selectedNodes.size == 1)
			selectedNodes.head.getBehavior(OpenBehavior)?.open
	}
}

class BehaviorProvider {
	static def <T extends Behavior> void triggerBehavior(XRoot root, Class<T> type, (T)=>boolean exec) {
		val selectedNodes = root.diagram.nodes.filter[selected]
		if(selectedNodes.size == 1) {
			val behavior = selectedNodes.head.getBehavior(type)
			if(behavior != null) {
				if(exec.apply(behavior))
					return
			}
		}
		val behavior = root.diagram.getBehavior(type)
		if(behavior != null) 
			exec.apply(behavior)
	}
}