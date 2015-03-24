package de.fxdiagram.eclipse.behavior

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XShape
import de.fxdiagram.core.behavior.AbstractHostBehavior
import de.fxdiagram.core.behavior.OpenBehavior
import de.fxdiagram.mapping.IMappedElementDescriptor
import javafx.scene.input.MouseEvent

/**
 * Opens the domain object of this {@link XShape} in th respective Eclipse editor. 
 */
class OpenElementInEditorBehavior extends AbstractHostBehavior<XShape> implements OpenBehavior {
	
	new(XShape host) {
		super(host)
	}
	
	override getBehaviorKey() {
		OpenElementInEditorBehavior
	}
	
	override protected doActivate() {
		host.addEventHandler(MouseEvent.MOUSE_CLICKED, [
			if (clickCount == 2) {
				open
				consume
			}
		])
	}
	
	override open() {
		domainObject.openInEditor(true)
	}
	
	private def getDomainObject() {
		switch it: host {
			XNode: domainObject
			XConnection: domainObject
			default: null
		} as IMappedElementDescriptor<?>
	}
}