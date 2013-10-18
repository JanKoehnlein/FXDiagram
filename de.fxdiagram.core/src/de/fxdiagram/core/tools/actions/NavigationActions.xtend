package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot
import de.fxdiagram.core.behavior.CloseBehavior
import de.fxdiagram.core.behavior.NavigationBehavior
import de.fxdiagram.core.behavior.OpenBehavior

class NavigateNextAction implements DiagramAction {
	override perform(XRoot root) {
		root.diagram.getBehavior(NavigationBehavior)?.next
	}
}

class NavigatePreviousAction implements DiagramAction {
	override perform(XRoot root) {
		root.diagram.getBehavior(NavigationBehavior)?.previous
	}
}

class CloseAction implements DiagramAction {
	override perform(XRoot root) {
		val selectedNodes = root.diagram.nodes.filter[selected]
		if(selectedNodes.size == 1) {
			val closeBehavior = selectedNodes.head.getBehavior(CloseBehavior)
			if(closeBehavior != null) {
				closeBehavior.close
				return
			}
		}
		root.diagram.getBehavior(CloseBehavior)?.close
	}
}

class OpenAction implements DiagramAction {
	override perform(XRoot root) {
		val selectedNodes = root.diagram.nodes.filter[selected]
		if(selectedNodes.size == 1)
			selectedNodes.head.getBehavior(OpenBehavior)?.open
	}
}