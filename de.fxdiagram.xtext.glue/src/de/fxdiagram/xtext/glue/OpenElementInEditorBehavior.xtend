package de.fxdiagram.xtext.glue

import de.fxdiagram.core.XShape
import de.fxdiagram.core.behavior.AbstractHostBehavior
import de.fxdiagram.core.behavior.OpenBehavior
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor
import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode

class OpenElementInEditorBehavior extends AbstractHostBehavior<XShape> implements OpenBehavior {
	
	new(XShape host) {
		super(host)
	}
	
	override getBehaviorKey() {
		OpenBehavior
	}
	
	override protected doActivate() {
		host.onMouseClicked = [
			if (clickCount == 2)
				open()
		]
	}
	
	override open() {
		domainObject.revealInEditor
	}
	
	private def getDomainObject() {
		switch it: host {
			XNode: domainObject
			XConnection: domainObject
			default: null
		} as XtextDomainObjectDescriptor<?>
	}
}