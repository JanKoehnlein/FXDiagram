package de.fxdiagram.xtext.glue.behavior

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XShape
import de.fxdiagram.core.behavior.AbstractHostBehavior
import de.fxdiagram.core.behavior.OpenBehavior
import de.fxdiagram.xtext.glue.mapping.XtextDomainObjectDescriptor
import javafx.scene.input.MouseEvent

class OpenElementInEditorBehavior extends AbstractHostBehavior<XShape> implements OpenBehavior {
	
	new(XShape host) {
		super(host)
	}
	
	override getBehaviorKey() {
		OpenBehavior
	}
	
	override protected doActivate() {
		host.addEventHandler(MouseEvent.MOUSE_CLICKED, [
			if (clickCount == 2)
				open()
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
		} as XtextDomainObjectDescriptor<?>
	}
}